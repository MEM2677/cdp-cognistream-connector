package it.keybiz.cdp.innova.service;

import it.keybiz.cdp.innova.domain.Role;
import it.keybiz.cdp.innova.dto.RoleSearchDTO;
import it.keybiz.cdp.innova.mapper.RoleMapperImpl;
import it.keybiz.cdp.innova.repository.RoleRepository;
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
@ContextConfiguration(classes = {RoleServiceImpl.class, RoleMapperImpl.class, ValidationAutoConfiguration.class})
public class RoleServiceTest {
    private final PodamFactory factory = new PodamFactoryImpl();

    @Autowired
    private RoleService roleService;

    @MockBean
    private RoleRepository roleRepository;

    @Test
    @DisplayName("Find all Roles")
    void findAll() {
        List<Role> roles = List.of(
            factory.manufacturePojo(Role.class),
            factory.manufacturePojo(Role.class),
            factory.manufacturePojo(Role.class)
        );

        when(roleRepository.findAll(any(Specification.class))).thenReturn(roles);

        List<Role> all = roleService.findAll(new RoleSearchDTO());

        assertEquals(roles.size(), all.size());
    }

    @Test
    @DisplayName("Find Role by id")
    void findOne() {
        Role role = factory.manufacturePojo(Role.class);

        when(roleRepository.findById(role.getId())).thenReturn(Optional.of(role));

        Role result = roleService.findOne(role.getId());

        assertEquals(role.getName(), result.getName());
    }

    @Test
    @DisplayName("Find Role by id, invalid")
    void findOneInvalid() {
        assertThrows(ConstraintViolationException.class, () -> roleService.findOne(null));
    }

    @Test
    @DisplayName("Find Role by id, not found")
    void findOneNotFound() {
        assertThrows(NoSuchElementException.class, () -> roleService.findOne(UUID.randomUUID()));
    }

    @Test
    @DisplayName("Create Role")
    void create() {
        Role role = factory.manufacturePojo(Role.class);
        role.setId(null);

        assertDoesNotThrow(() -> roleService.create(role));

        verify(roleRepository).save(role);
        verifyNoMoreInteractions(roleRepository);
    }

    @Test
    @DisplayName("Update Role, not found")
    void updateNotFound() {
        Role role = new Role();

        assertThrows(NoSuchElementException.class, () -> roleService.update(UUID.randomUUID(), role));
    }

    @Test
    @DisplayName("Update Role")
    void update() {
        Role roleDB = factory.manufacturePojo(Role.class);
        Role role = factory.manufacturePojo(Role.class);
        role.setId(null);
        role.setCreatedBy("test_user");

        when(roleRepository.findById(roleDB.getId())).thenReturn(Optional.of(roleDB));

        assertDoesNotThrow(() -> roleService.update(roleDB.getId(), role));
        assertEquals(roleDB.getCreatedBy(), role.getCreatedBy());

        verify(roleRepository).findById(roleDB.getId());
        verify(roleRepository).save(roleDB);
        verifyNoMoreInteractions(roleRepository);
    }

    @Test
    @DisplayName("Delete Role, invalid")
    void deleteInvalid() {
        assertThrows(ConstraintViolationException.class, () -> roleService.delete(null));
    }

    @Test
    @DisplayName("Delete Role")
    void delete() {
        assertDoesNotThrow(() -> roleService.delete(UUID.randomUUID()));

        verify(roleRepository).deleteById(any(UUID.class));
        verifyNoMoreInteractions(roleRepository);
    }
}
