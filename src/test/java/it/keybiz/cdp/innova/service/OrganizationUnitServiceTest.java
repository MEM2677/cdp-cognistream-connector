package it.keybiz.cdp.innova.service;

import it.keybiz.cdp.innova.domain.OrganizationUnit;
import it.keybiz.cdp.innova.dto.OrganizationUnitSearchDTO;
import it.keybiz.cdp.innova.mapper.OrganizationUnitMapperImpl;
import it.keybiz.cdp.innova.repository.OrganizationUnitRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
@ContextConfiguration(classes = {OrganizationUnitServiceImpl.class, OrganizationUnitMapperImpl.class, ValidationAutoConfiguration.class})
public class OrganizationUnitServiceTest {
    private final PodamFactory factory = new PodamFactoryImpl();

    @Autowired
    private OrganizationUnitService organizationUnitService;

    @MockBean
    private OrganizationUnitRepository organizationUnitRepository;

    @Test
    @DisplayName("Find all Organization Units")
    void findAll() {
        List<OrganizationUnit> organizationUnits = List.of(
            factory.manufacturePojo(OrganizationUnit.class),
            factory.manufacturePojo(OrganizationUnit.class),
            factory.manufacturePojo(OrganizationUnit.class)
        );

        when(organizationUnitRepository.findAll(any(Specification.class))).thenReturn(organizationUnits);

        List<OrganizationUnit> all = organizationUnitService.findAll(new OrganizationUnitSearchDTO());

        assertEquals(organizationUnits.size(), all.size());
    }

    @Test
    @DisplayName("Find Organization Unit by id")
    void findOne() {
        OrganizationUnit organizationUnit = factory.manufacturePojo(OrganizationUnit.class);

        when(organizationUnitRepository.findById(organizationUnit.getId())).thenReturn(Optional.of(organizationUnit));

        OrganizationUnit result = organizationUnitService.findOne(organizationUnit.getId());

        assertEquals(organizationUnit.getName(), result.getName());
    }

    @Test
    @DisplayName("Find Organization Unit by id, invalid")
    void findOneInvalid() {
        assertThrows(ConstraintViolationException.class, () -> organizationUnitService.findOne(null));
    }

    @Test
    @DisplayName("Find Organization Unit by id, not found")
    void findOneNotFound() {
        assertThrows(NoSuchElementException.class, () -> organizationUnitService.findOne(UUID.randomUUID()));
    }

    @Test
    @DisplayName("Create Organization Unit")
    void create() {
        OrganizationUnit organizationUnit = factory.manufacturePojo(OrganizationUnit.class);
        organizationUnit.setId(null);

        assertDoesNotThrow(() -> organizationUnitService.create(organizationUnit));

        verify(organizationUnitRepository).save(organizationUnit);
        verifyNoMoreInteractions(organizationUnitRepository);
    }

    @Test
    @DisplayName("Update Organization Unit, not found")
    void updateNotFound() {
        OrganizationUnit organizationUnit = new OrganizationUnit();

        assertThrows(NoSuchElementException.class, () -> organizationUnitService.update(UUID.randomUUID(), organizationUnit));
    }

    @Test
    @DisplayName("Update Organization Unit")
    void update() {
        OrganizationUnit organizationUnitDB = factory.manufacturePojo(OrganizationUnit.class);
        organizationUnitDB.setId(UUID.randomUUID());
        OrganizationUnit organizationUnit = factory.manufacturePojo(OrganizationUnit.class);
        organizationUnit.setId(null);
        organizationUnit.setCreatedBy("test_user");

        when(organizationUnitRepository.findById(organizationUnitDB.getId())).thenReturn(Optional.of(organizationUnitDB));

        assertDoesNotThrow(() -> organizationUnitService.update(organizationUnitDB.getId(), organizationUnit));
        assertEquals(organizationUnitDB.getCreatedBy(), organizationUnit.getCreatedBy());

        verify(organizationUnitRepository).findById(organizationUnitDB.getId());
        verify(organizationUnitRepository).save(organizationUnitDB);
        verifyNoMoreInteractions(organizationUnitRepository);
    }

    @Test
    @DisplayName("Delete Organization Unit, invalid")
    void deleteInvalid() {
        assertThrows(ConstraintViolationException.class, () -> organizationUnitService.delete(null));
    }

    @Test
    @DisplayName("Delete Organization Unit")
    void delete() {
        assertDoesNotThrow(() -> organizationUnitService.delete(UUID.randomUUID()));

        verify(organizationUnitRepository).deleteById(any(UUID.class));
        verifyNoMoreInteractions(organizationUnitRepository);
    }
}
