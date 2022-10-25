package it.keybiz.cdp.innova.service.sync;

import feign.Response;
import it.keybiz.cdp.innova.client.cognistreamer.CognistreamerClient;
import it.keybiz.cdp.innova.client.cognistreamer.models.BaseResponseModelList;
import it.keybiz.cdp.innova.config.ApplicationProperties;
import it.keybiz.cdp.innova.config.CognistreamerProperties;
import it.keybiz.cdp.innova.domain.SynchronizableEntity;
import it.keybiz.cdp.innova.event.ImageFetchedEvent;
import it.keybiz.cdp.innova.repository.SynchronizableEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
abstract class AbstractSynchronizator<ApiEntity, Entity extends SynchronizableEntity, Parent extends SynchronizableEntity> implements Synchronizator {
    private final ApplicationEventPublisher eventPublisher;

    private final ApplicationProperties applicationProperties;

    final CognistreamerProperties cognistreamerProperties;

    final CognistreamerClient cognistreamerClient;

    final SynchronizableEntityRepository<Parent, UUID> parentRepository;

    final SynchronizableEntityRepository<Entity, UUID> repository;

    boolean fetchImages;

    boolean loadTest;

    /**
     * Imposta il flag "toDelete" a true su tutti i modelli,
     * alla fine del processo verranno rimossi quelli che hanno ancora il flag a true
     */
    private void prepareDatabase() {
        log.info("Preparing database, setting all {} flag 'toDelete' to true", getEntityName());
        repository.setAllToDeleteTrue();
        log.info("Database prepared.");
    }

    /**
     * Rimuove tutti i modelli con flag "toDelete" a true
     */
    @Override
    @Transactional
    public void cleanupDatabase() {
        repository.deleteAllByToDeleteIsTrue();
    }

    /**
     * Recupera i dati dalle API
     *
     * @param parentId  eventuale ID del modello di riferimento per chiamare le APIs
     * @param offset    offset della chiamata
     * @param batchSize dimesione dei risultati
     * @return Lista di modelli recuperati tramite API
     */
    abstract BaseResponseModelList<ApiEntity> fetchData(UUID parentId, int offset, int batchSize);

    /**
     * Eventuale filtro per i dati che non è possibile filtrare tramite API
     *
     * @param responseData Lista di modelli recuperati dalle API
     * @return Lista di domain entities filtrate
     */
    List<ApiEntity> filterData(List<ApiEntity> responseData) {
        log.trace("Skipping filterData(), not implemented.");
        return responseData;
    }

    /**
     * Se dovesse servire processare ulteriormente i dati appena recuperati PRIMA della conversione in entity, implementare questo metodo
     *
     * @param responseData Lista di modelli recuperati dalle API
     * @return Lista di domain entities processare
     */
    List<ApiEntity> beforeConvert(List<ApiEntity> responseData) {
        log.trace("Skipping beforeConvert(), not implemented.");
        return responseData;
    }

    /**
     * Converte i modelli recuperati dalle API in domain entities
     *
     * @param responseData Lista di modelli recuperati dalle API
     * @return Lista di domain entities convertiti
     */
    abstract List<Entity> convertData(List<ApiEntity> responseData);

    /**
     * Se dovesse servire processare ulteriormente i dati appena recuperati PRIMA del salvataggio, implementare questo metodo
     *
     * @param parentId Eventuale ID del domain entity "padre"
     * @param entity   Domain entity relativa ai dati appena recuperati
     */
    void beforePersist(UUID parentId, Entity entity) {
        log.trace("Skipping beforePersist(), not implemented.");
    }

    /**
     * Se dovesse servire processare ulteriormente i dati appena recuperati DOPO il salvataggio, implementare questo metodo
     *
     * @param entity Domain entity relativa ai dati appena recuperati
     */
    @SuppressWarnings("unused")
    void afterPersist(Entity entity) {
        log.trace("Skipping afterPersist(), not implemented.");
    }

    @Override
    @Transactional
    public void sync() {
        this.prepareDatabase();

        if (parentRepository != null) {
            try (Stream<UUID> parentIds = parentRepository.streamAllIdsNotToDelete()) {
                parentIds.forEach(this::execute);
            }
        } else {
            execute(null);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void execute(UUID parentId) {
        int total;
        int offset = 0;
        int batchSize = cognistreamerProperties.getBatchSize();

        do {
            log.trace("Request offset: {} - limit: {}", offset, batchSize);

            BaseResponseModelList<ApiEntity> response = this.fetchData(parentId, offset, batchSize);
            List<ApiEntity> responseData = this.filterData(response.getData());
            responseData = this.beforeConvert(responseData);
            List<Entity> entities = this.convertData(responseData);

            entities.forEach(entity -> {
                this.beforePersist(parentId, entity);
                entity = repository.save(entity);
                this.afterPersist(entity);
                this.fetchImages(entity);
            });

            total = response.getMeta().getTotal();
            offset += batchSize;
        } while (offset < total);
    }

    /**
     * Recupera le immagini per le entity che sono state appena sincronizzate.
     * Settare la variabile "fetchImages" a true dall'implementazione per far eseguire il fetch.
     */
    private void fetchImages(Entity entity) {
        String entityName = getEntityName();

        if (!fetchImages) {
            log.trace("Fetch images disabled for {}", entityName);
            return;
        }

        UUID entityId = entity.getId();

        if (entity.getImage() != null) {
            entity.getImage().forEach((size, v) -> {
                String imageUrl = v.get("url");

                if (!StringUtils.hasLength(imageUrl)) {
                    log.trace("Skipping {} '{}', no image URL", entityName, entityId);

                } else {
                    try (Response response = cognistreamerClient.fetchImage(URI.create(imageUrl))) {
                        if (response.status() == HttpStatus.OK.value()) {
                            ImageFetchedEvent event = new ImageFetchedEvent(entityId, getEntityType(), size, response.body().asInputStream());
                            eventPublisher.publishEvent(event);
                        } else {
                            log.warn("Unexpected HTTP Status code ({}) while fetching image '{}' for {} '{}'", response.status(), size, entityName, entityId);
                        }
                    } catch (IOException e) {
                        log.error("Error while writing image image '{}' to filesystem for {} '{}'", size, entityName, entityId);
                        log.error(e.getMessage(), e);
                    }
                }
            });
            log.debug("Fetched images for {} '{}'", entityName, entityId);
        } else {
            log.trace("Skipping images for {} '{}', no images property", entityName, entityId);
        }
    }

    /**
     * Rimuove le immagini di entities che sono state eliminate
     */
    @Override
    public void cleanupImages() {
        String entityName = getEntityName();
        Path entityPath = Path.of(applicationProperties.getImagesPath(), entityName);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start(String.format("Cleanup %s Images", entityName));

        if (entityPath.toFile().exists()) {
            try (Stream<Path> stream = Files.list(entityPath)) {
                stream.forEach(path -> {
                    String folderId = path.getFileName().toString();

                    if (!repository.existsById(UUID.fromString(folderId))) {
                        try {
                            FileSystemUtils.deleteRecursively(path);
                            log.debug("Deleted images for {} '{}', path '{}'", entityName, folderId, path);
                        } catch (IOException e) {
                            log.error("Error deleting images for {} '{}', path '{}'", entityName, folderId, path);
                            log.error(e.getMessage(), e);
                        }
                    }
                });
            } catch (IOException e) {
                log.error("Error while reading images for {}", entityName);
                log.error(e.getMessage(), e);
            }
        }

        stopWatch.stop();
        log.info("{} images cleaned in {} seconds", entityName, stopWatch.getTotalTimeSeconds());
    }

    /**
     * Estrae il nome delle entity dalla tipologia di generic dell'implementazione
     *
     * @return il nome della classe dell'entity
     */
    private String getEntityName() {
        return ((Class<?>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1]).getSimpleName().toLowerCase();
    }

    /**
     * Estrae il tipo delle entity dalla tipologia di generic dell'implementazione
     *
     * @return classe dell entity
     */
    private Class<?> getEntityType() {
        return ((Class<?>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1]);
    }
}
