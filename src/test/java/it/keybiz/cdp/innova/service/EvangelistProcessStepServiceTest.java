package it.keybiz.cdp.innova.service;

import it.keybiz.cdp.innova.config.ApplicationProperties;
import it.keybiz.cdp.innova.domain.EvangelistProcessStep;
import it.keybiz.cdp.innova.dto.EvangelistProcessStepSearchDTO;
import it.keybiz.cdp.innova.event.ImageUploadedEvent;
import it.keybiz.cdp.innova.mapper.EvangelistProcessStepMapperImpl;
import it.keybiz.cdp.innova.repository.EvangelistProcessStepRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
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
    EvangelistProcessStepServiceImpl.class,
    EvangelistProcessStepMapperImpl.class,
    FileStorageServiceImpl.class,
    ValidationAutoConfiguration.class,
})
public class EvangelistProcessStepServiceTest {
    private final PodamFactory factory = new PodamFactoryImpl();

    @Autowired
    private EvangelistProcessStepService evangelistProcessStepService;

    @MockBean
    private FileStorageServiceImpl fileStorageService;

    @MockBean
    private EvangelistProcessStepRepository evangelistProcessStepRepository;

    @Test
    @DisplayName("Find all Steps")
    void findAll() {
        List<EvangelistProcessStep> evangelistProcessSteps = List.of(
            factory.manufacturePojo(EvangelistProcessStep.class),
            factory.manufacturePojo(EvangelistProcessStep.class),
            factory.manufacturePojo(EvangelistProcessStep.class)
        );

        when(evangelistProcessStepRepository.findAll(any(Specification.class), any(Sort.class))).thenReturn(evangelistProcessSteps);

        List<EvangelistProcessStep> all = evangelistProcessStepService.findAll(new EvangelistProcessStepSearchDTO());

        assertEquals(evangelistProcessSteps.size(), all.size());
    }

    @Test
    @DisplayName("Find Step by id")
    void findOne() {
        EvangelistProcessStep evangelistProcessStep = factory.manufacturePojo(EvangelistProcessStep.class);

        when(evangelistProcessStepRepository.findById(evangelistProcessStep.getId())).thenReturn(Optional.of(evangelistProcessStep));

        EvangelistProcessStep result = evangelistProcessStepService.findOne(evangelistProcessStep.getId());

        assertEquals(evangelistProcessStep.getDescription(), result.getDescription());
    }

    @Test
    @DisplayName("Find Step by id, invalid")
    void findOneInvalid() {
        assertThrows(ConstraintViolationException.class, () -> evangelistProcessStepService.findOne(null));
    }

    @Test
    @DisplayName("Find Step by id, not found")
    void findOneNotFound() {
        assertThrows(NoSuchElementException.class, () -> evangelistProcessStepService.findOne(UUID.randomUUID()));
    }

    @Test
    @DisplayName("Create Step")
    void create() {
        EvangelistProcessStep evangelistProcessStep = factory.manufacturePojo(EvangelistProcessStep.class);
        evangelistProcessStep.setId(null);
        evangelistProcessStep.setPosition(1);

        doNothing().when(fileStorageService).storeImage(any(ImageUploadedEvent.class));
        when(evangelistProcessStepRepository.saveAndFlush(any())).thenReturn(factory.manufacturePojo(EvangelistProcessStep.class));

        assertDoesNotThrow(() -> evangelistProcessStepService.create(evangelistProcessStep, null));

        verify(evangelistProcessStepRepository).saveAndFlush(evangelistProcessStep);
        verifyNoMoreInteractions(evangelistProcessStepRepository);
    }

    @Test
    @DisplayName("Update Step, not found")
    void updateNotFound() {
        EvangelistProcessStep evangelistProcessStep = new EvangelistProcessStep();
        evangelistProcessStep.setPosition(1);

        assertThrows(NoSuchElementException.class, () -> evangelistProcessStepService.update(UUID.randomUUID(), evangelistProcessStep, null));
    }

    @Test
    @DisplayName("Update Step")
    void update() {
        EvangelistProcessStep evangelistProcessStepDB = factory.manufacturePojo(EvangelistProcessStep.class);
        EvangelistProcessStep evangelistProcessStep = factory.manufacturePojo(EvangelistProcessStep.class);
        evangelistProcessStep.setId(null);
        evangelistProcessStep.setPosition(1);
        evangelistProcessStep.setCreatedBy("test_user");

        doNothing().when(fileStorageService).storeImage(any(ImageUploadedEvent.class));
        when(evangelistProcessStepRepository.findById(evangelistProcessStepDB.getId())).thenReturn(Optional.of(evangelistProcessStepDB));

        assertDoesNotThrow(() -> evangelistProcessStepService.update(evangelistProcessStepDB.getId(), evangelistProcessStep, null));
        assertEquals(evangelistProcessStepDB.getCreatedBy(), evangelistProcessStep.getCreatedBy());

        verify(evangelistProcessStepRepository).findById(evangelistProcessStepDB.getId());
        verify(evangelistProcessStepRepository).saveAndFlush(evangelistProcessStepDB);
        verifyNoMoreInteractions(evangelistProcessStepRepository);
    }

    @Test
    @DisplayName("Sort Steps")
    void sort() {
        EvangelistProcessStep evangelistProcessStepDB1 = new EvangelistProcessStep();
        evangelistProcessStepDB1.setId(UUID.randomUUID());
        evangelistProcessStepDB1.setPosition(1);

        EvangelistProcessStep evangelistProcessStepDB2 = new EvangelistProcessStep();
        evangelistProcessStepDB2.setId(UUID.randomUUID());
        evangelistProcessStepDB2.setPosition(2);

        EvangelistProcessStep evangelistProcessStep1 = new EvangelistProcessStep();
        evangelistProcessStep1.setId(evangelistProcessStepDB1.getId());
        evangelistProcessStep1.setPosition(2);

        EvangelistProcessStep evangelistProcessStep2 = new EvangelistProcessStep();
        evangelistProcessStep2.setId(evangelistProcessStep2.getId());
        evangelistProcessStep2.setPosition(1);

        List<EvangelistProcessStep> evangelistProcessSteps = List.of(evangelistProcessStep1, evangelistProcessStep2);

        when(evangelistProcessStepRepository.findById(evangelistProcessStep1.getId())).thenReturn(Optional.of(evangelistProcessStepDB1));
        when(evangelistProcessStepRepository.findById(evangelistProcessStep2.getId())).thenReturn(Optional.of(evangelistProcessStepDB2));

        assertDoesNotThrow(() -> evangelistProcessStepService.sort(evangelistProcessSteps));
        assertEquals(evangelistProcessStepDB1.getPosition(), evangelistProcessStep1.getPosition());
        assertEquals(evangelistProcessStepDB2.getPosition(), evangelistProcessStep2.getPosition());

        verify(evangelistProcessStepRepository).findById(evangelistProcessStep1.getId());
        verify(evangelistProcessStepRepository).findById(evangelistProcessStep2.getId());
        verify(evangelistProcessStepRepository).save(evangelistProcessStepDB1);
        verify(evangelistProcessStepRepository).save(evangelistProcessStepDB2);
        verifyNoMoreInteractions(evangelistProcessStepRepository);
    }

    @Test
    @DisplayName("Delete Step, invalid")
    void deleteInvalid() {
        assertThrows(ConstraintViolationException.class, () -> evangelistProcessStepService.delete(null));
    }

    @Test
    @DisplayName("Delete Step")
    void delete() {
        EvangelistProcessStep step1 = factory.manufacturePojo(EvangelistProcessStep.class);
        step1.setPosition(1);
        EvangelistProcessStep step2 = factory.manufacturePojo(EvangelistProcessStep.class);
        step2.setPosition(2);
        EvangelistProcessStep step3 = factory.manufacturePojo(EvangelistProcessStep.class);
        step3.setPosition(3);
        EvangelistProcessStep step4 = factory.manufacturePojo(EvangelistProcessStep.class);
        step4.setPosition(4);

        when(evangelistProcessStepRepository.findById(step2.getId())).thenReturn(Optional.of(step2));
        when(evangelistProcessStepRepository.findAllByPositionGreaterThan(2)).thenReturn(List.of(step3, step4));

        // elimino lo step2
        assertDoesNotThrow(() -> evangelistProcessStepService.delete(step2.getId()));
        assertEquals(1, step1.getPosition());
        assertEquals(2, step3.getPosition());
        assertEquals(3, step4.getPosition());

        verify(evangelistProcessStepRepository).findById(step2.getId());
        verify(evangelistProcessStepRepository).delete(step2);
        verify(evangelistProcessStepRepository).findAllByPositionGreaterThan(step2.getPosition());
        verify(evangelistProcessStepRepository).saveAll(List.of(step3, step4));
        verifyNoMoreInteractions(evangelistProcessStepRepository);
    }
}
