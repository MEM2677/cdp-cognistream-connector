package it.keybiz.cdp.innova.service;

import it.keybiz.cdp.innova.config.ApplicationProperties;
import it.keybiz.cdp.innova.domain.Evangelist;
import it.keybiz.cdp.innova.dto.EvangelistSearchDTO;
import it.keybiz.cdp.innova.enums.EvangelistStatus;
import it.keybiz.cdp.innova.event.ImageUploadedEvent;
import it.keybiz.cdp.innova.mapper.EvangelistMapperImpl;
import it.keybiz.cdp.innova.repository.EvangelistRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = {
    ApplicationProperties.class,
    EvangelistServiceImpl.class,
    EvangelistMapperImpl.class,
    FileStorageServiceImpl.class,
    ValidationAutoConfiguration.class
})
public class EvangelistServiceTest {
    private final PodamFactory factory = new PodamFactoryImpl();

    @Autowired
    private EvangelistService evangelistService;

    @MockBean
    private FileStorageServiceImpl fileStorageService;

    @MockBean
    private EvangelistRepository evangelistRepository;

    @Test
    @DisplayName("Find all Evangelists")
    void findAll() {
        PageImpl<Evangelist> evangelists = new PageImpl<>(List.of(
            factory.manufacturePojo(Evangelist.class),
            factory.manufacturePojo(Evangelist.class),
            factory.manufacturePojo(Evangelist.class)
        ));

        //noinspection unchecked
        when(evangelistRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(evangelists);

        Page<Evangelist> all = evangelistService.findAll(Pageable.ofSize(1), factory.manufacturePojo(EvangelistSearchDTO.class));

        assertEquals(evangelists.getTotalElements(), all.getTotalElements());
    }

    @Test
    @DisplayName("Find all active Evangelists")
    void findAllActive() {
        List<Evangelist> evangelists = List.of(factory.manufacturePojo(Evangelist.class));

        when(evangelistRepository.findAllByStatus(any())).thenReturn(evangelists);

        List<Evangelist> all = evangelistService.findAllActive();

        assertEquals(evangelists.size(), all.size());
    }

    @Test
    @DisplayName("Find Evangelist by id")
    void findOne() {
        Evangelist evangelist = factory.manufacturePojo(Evangelist.class);

        when(evangelistRepository.findById(evangelist.getId())).thenReturn(Optional.of(evangelist));

        Evangelist result = evangelistService.findOne(evangelist.getId());

        assertEquals(evangelist.getName(), result.getName());
    }

    @Test
    @DisplayName("Find Evangelist by id, invalid")
    void findOneInvalid() {
        assertThrows(ConstraintViolationException.class, () -> evangelistService.findOne(null));
    }

    @Test
    @DisplayName("Find Evangelist by id, not found")
    void findOneNotFound() {
        assertThrows(NoSuchElementException.class, () -> evangelistService.findOne(UUID.randomUUID()));
    }

    @Test
    @DisplayName("Create Evangelist")
    void create() {
        Evangelist evangelist = factory.manufacturePojo(Evangelist.class);
        evangelist.setId(null);

        doNothing().when(fileStorageService).storeImage(any(ImageUploadedEvent.class));
        when(evangelistRepository.saveAndFlush(any())).thenReturn(factory.manufacturePojo(Evangelist.class));

        assertDoesNotThrow(() -> evangelistService.create(evangelist, null));

        verify(evangelistRepository).saveAndFlush(evangelist);
        verifyNoMoreInteractions(evangelistRepository);
    }

    @Test
    @DisplayName("Subscribe Evangelist")
    void subscribe() {
        Evangelist evangelist = factory.manufacturePojo(Evangelist.class);
        evangelist.setId(null);
        evangelist.setStatus(EvangelistStatus.TO_APPROVE);

        when(evangelistRepository.save(evangelist)).thenReturn(evangelist);

        assertDoesNotThrow(() -> evangelistService.subscribe(evangelist));
        assertEquals(EvangelistStatus.TO_APPROVE, evangelist.getStatus());

        verify(evangelistRepository).save(evangelist);
        verifyNoMoreInteractions(evangelistRepository);
    }

    @Test
    @DisplayName("Update Evangelist, not found")
    void updateNotFound() {
        Evangelist evangelist = new Evangelist();

        assertThrows(NoSuchElementException.class, () -> evangelistService.update(UUID.randomUUID(), evangelist, null));
    }

    @Test
    @DisplayName("Update Evangelist")
    void update() {
        Evangelist evangelistDB = factory.manufacturePojo(Evangelist.class);
        Evangelist evangelist = factory.manufacturePojo(Evangelist.class);
        evangelist.setId(null);
        evangelist.setCreatedBy("test_user");

        doNothing().when(fileStorageService).storeImage(any(ImageUploadedEvent.class));
        when(evangelistRepository.findById(evangelistDB.getId())).thenReturn(Optional.of(evangelistDB));

        assertDoesNotThrow(() -> evangelistService.update(evangelistDB.getId(), evangelist, null));
        assertEquals(evangelistDB.getCreatedBy(), evangelist.getCreatedBy());

        verify(evangelistRepository).findById(evangelistDB.getId());
        verify(evangelistRepository).saveAndFlush(evangelistDB);
        verifyNoMoreInteractions(evangelistRepository);
    }

    @Test
    @DisplayName("Delete Evangelist, invalid")
    void deleteInvalid() {
        assertThrows(ConstraintViolationException.class, () -> evangelistService.delete(null));
    }

    @Test
    @DisplayName("Delete Evangelist")
    void delete() {
        assertDoesNotThrow(() -> evangelistService.delete(UUID.randomUUID()));

        verify(evangelistRepository).deleteById(any(UUID.class));
        verifyNoMoreInteractions(evangelistRepository);
    }
}
