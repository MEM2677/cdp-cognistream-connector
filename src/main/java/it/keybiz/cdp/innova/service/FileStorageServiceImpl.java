package it.keybiz.cdp.innova.service;

import it.keybiz.cdp.innova.config.ApplicationProperties;
import it.keybiz.cdp.innova.config.Constants;
import it.keybiz.cdp.innova.event.ImageFetchedEvent;
import it.keybiz.cdp.innova.event.ImageUploadedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {
    private final ApplicationProperties applicationProperties;

    @Override
    public String getImageUrl(Path filePath) {
        Path fsPath = Path.of(applicationProperties.getImagesPath(), filePath.toString());
        return fsPath.toFile().exists() ? Path.of(Constants.IMAGES_ENDPOINT_BASE_PATH, filePath.toString()).toString() : null;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void storeImage(ImageUploadedEvent event) {
        try {
            log.debug("Starting to save image to filesystem");
            String entityName = event.getType().getSimpleName().toLowerCase();
            Path entityPath = Path.of(applicationProperties.getImagesPath(), entityName, event.getId().toString());

            if (event.getData() != null && !event.getData().isBlank()) {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(event.getData()));
                FileUtils.copyInputStreamToFile(inputStream, entityPath.toFile());
                log.info("File '{}' saved on filesystem.", entityPath.toAbsolutePath());
            } else {
                Files.delete(entityPath);
                log.info("File '{}' deleted from filesystem.", entityPath.toAbsolutePath());
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("Errore durante la gestione dell'immagine");
        }
    }

    @EventListener
    public void storeImage(ImageFetchedEvent event) {
        try {
            log.debug("Saving image to filesystem");
            String entityName = event.getType().getSimpleName().toLowerCase();
            Path imagePath = Path.of(applicationProperties.getImagesPath(), entityName, event.getEntityId().toString(), event.getImageId());
            FileUtils.copyInputStreamToFile(event.getData(), imagePath.toFile());
            log.trace("Fetched and saved image '{}' for {} '{}' in {}", event.getImageId(), entityName, event.getEntityId(), imagePath.toAbsolutePath());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("Errore durante il salvataggio dell'immagine");
        }
    }
}
