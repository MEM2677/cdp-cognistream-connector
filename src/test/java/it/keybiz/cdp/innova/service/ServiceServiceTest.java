package it.keybiz.cdp.innova.service;

import it.keybiz.cdp.innova.domain.Servizio;
import it.keybiz.cdp.innova.dto.ServiceSearchDTO;
import it.keybiz.cdp.innova.mapper.ServiceMapperImpl;
import it.keybiz.cdp.innova.repository.ServiceRepository;
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
@ContextConfiguration(classes = {ServiceServiceImpl.class, ServiceMapperImpl.class, ValidationAutoConfiguration.class})
public class ServiceServiceTest {
    private final PodamFactory factory = new PodamFactoryImpl();

    @Autowired
    private ServiceService serviceService;

    @MockBean
    private ServiceRepository serviceRepository;

    @Test
    @DisplayName("Find all Services")
    void findAll() {
        List<Servizio> services = List.of(
            factory.manufacturePojo(Servizio.class),
            factory.manufacturePojo(Servizio.class),
            factory.manufacturePojo(Servizio.class)
        );

        when(serviceRepository.findAll(any(Specification.class), any(Sort.class))).thenReturn(services);

        List<Servizio> all = serviceService.findAll(new ServiceSearchDTO());

        assertEquals(services.size(), all.size());
    }

    @Test
    @DisplayName("Find Service by id")
    void findOne() {
        Servizio service = factory.manufacturePojo(Servizio.class);

        when(serviceRepository.findById(service.getId())).thenReturn(Optional.of(service));

        Servizio result = serviceService.findOne(service.getId());

        assertEquals(service.getDescription(), result.getDescription());
    }

    @Test
    @DisplayName("Find Service by id, invalid")
    void findOneInvalid() {
        assertThrows(ConstraintViolationException.class, () -> serviceService.findOne(null));
    }

    @Test
    @DisplayName("Find Service by id, not found")
    void findOneNotFound() {
        assertThrows(NoSuchElementException.class, () -> serviceService.findOne(UUID.randomUUID()));
    }

    @Test
    @DisplayName("Create Service")
    void create() {
        Servizio service = factory.manufacturePojo(Servizio.class);
        service.setId(null);
        service.setPosition(1);

        assertDoesNotThrow(() -> serviceService.create(service));

        verify(serviceRepository).save(service);
        verifyNoMoreInteractions(serviceRepository);
    }

    @Test
    @DisplayName("Update Service, not found")
    void updateNotFound() {
        Servizio service = new Servizio();
        service.setPosition(1);

        assertThrows(NoSuchElementException.class, () -> serviceService.update(UUID.randomUUID(), service));
    }

    @Test
    @DisplayName("Update Service")
    void update() {
        Servizio serviceDB = factory.manufacturePojo(Servizio.class);
        Servizio service = factory.manufacturePojo(Servizio.class);
        service.setId(null);
        service.setPosition(1);
        service.setCreatedBy("test_user");

        when(serviceRepository.findById(serviceDB.getId())).thenReturn(Optional.of(serviceDB));

        assertDoesNotThrow(() -> serviceService.update(serviceDB.getId(), service));
        assertEquals(serviceDB.getCreatedBy(), service.getCreatedBy());

        verify(serviceRepository).findById(serviceDB.getId());
        verify(serviceRepository).save(serviceDB);
        verifyNoMoreInteractions(serviceRepository);
    }

    @Test
    @DisplayName("Sort Services")
    void sort() {
        Servizio serviceDB1 = new Servizio();
        serviceDB1.setId(UUID.randomUUID());
        serviceDB1.setPosition(1);

        Servizio serviceDB2 = new Servizio();
        serviceDB2.setId(UUID.randomUUID());
        serviceDB2.setPosition(2);

        Servizio service1 = new Servizio();
        service1.setId(serviceDB1.getId());
        service1.setPosition(2);

        Servizio service2 = new Servizio();
        service2.setId(serviceDB2.getId());
        service2.setPosition(1);

        List<Servizio> services = List.of(service1, service2);

        when(serviceRepository.findById(serviceDB1.getId())).thenReturn(Optional.of(serviceDB1));
        when(serviceRepository.findById(serviceDB2.getId())).thenReturn(Optional.of(serviceDB2));

        assertDoesNotThrow(() -> serviceService.sort(services));
        assertEquals(serviceDB1.getPosition(), service1.getPosition());
        assertEquals(serviceDB2.getPosition(), service2.getPosition());

        verify(serviceRepository).findById(serviceDB1.getId());
        verify(serviceRepository).findById(serviceDB2.getId());
        verify(serviceRepository).save(serviceDB1);
        verify(serviceRepository).save(serviceDB2);
        verifyNoMoreInteractions(serviceRepository);
    }

    @Test
    @DisplayName("Delete Service, invalid")
    void deleteInvalid() {
        assertThrows(ConstraintViolationException.class, () -> serviceService.delete(null));
    }

    @Test
    @DisplayName("Delete Service")
    void delete() {
        Servizio servizio1 = factory.manufacturePojo(Servizio.class);
        servizio1.setPosition(1);
        Servizio servizio2 = factory.manufacturePojo(Servizio.class);
        servizio2.setPosition(2);
        Servizio servizio3 = factory.manufacturePojo(Servizio.class);
        servizio3.setPosition(3);
        Servizio servizio4 = factory.manufacturePojo(Servizio.class);
        servizio4.setPosition(4);

        when(serviceRepository.findById(servizio2.getId())).thenReturn(Optional.of(servizio2));
        when(serviceRepository.findAllByPositionGreaterThan(2)).thenReturn(List.of(servizio3, servizio4));

        // elimino il servizio2
        assertDoesNotThrow(() -> serviceService.delete(servizio2.getId()));
        assertEquals(1, servizio1.getPosition());
        assertEquals(2, servizio3.getPosition());
        assertEquals(3, servizio4.getPosition());

        verify(serviceRepository).findById(servizio2.getId());
        verify(serviceRepository).delete(servizio2);
        verify(serviceRepository).findAllByPositionGreaterThan(servizio2.getPosition());
        verify(serviceRepository).saveAll(List.of(servizio3, servizio4));
        verifyNoMoreInteractions(serviceRepository);
    }
}
