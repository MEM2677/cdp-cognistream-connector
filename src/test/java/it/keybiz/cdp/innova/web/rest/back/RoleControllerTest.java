package it.keybiz.cdp.innova.web.rest.back;

import it.keybiz.cdp.innova.domain.Role;
import it.keybiz.cdp.innova.dto.RoleEditDTO;
import it.keybiz.cdp.innova.dto.RoleSearchDTO;
import it.keybiz.cdp.innova.mapper.RoleMapperImpl;
import it.keybiz.cdp.innova.service.RoleService;
import it.keybiz.cdp.innova.web.rest.BaseControllerTest;
import it.keybiz.cdp.innova.web.rest.errors.ExceptionTranslator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {RoleController.class, RoleMapperImpl.class, ExceptionTranslator.class})
public class RoleControllerTest extends BaseControllerTest {
    @MockBean
    private RoleService roleService;

    @Override
    protected String getControllerBaseUrl() {
        return "/api/role";
    }

    @Test
    @DisplayName("Get all Roles")
    void findAll() throws Exception {
        List<Role> resultPage = List.of(
            factory.manufacturePojo(Role.class),
            factory.manufacturePojo(Role.class),
            factory.manufacturePojo(Role.class)
        );

        when(roleService.findAll(any(RoleSearchDTO.class))).thenReturn(resultPage);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(baseUrl)
            .characterEncoding(StandardCharsets.UTF_8);

        mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(resultPage.size())))
            .andExpect(jsonPath("$.[0]", allOf(
                aMapWithSize(3),
                hasKey("id"),
                hasKey("name"),
                hasKey("organizationUnit")
            )))
            .andExpect(jsonPath("$.[0].id", notNullValue()))
            .andExpect(jsonPath("$.[0].organizationUnit", allOf(
                aMapWithSize(3),
                hasKey("id"),
                hasKey("name"),
                hasKey("roles")
            )))
            .andExpect(jsonPath("$.[0].organizationUnit.roles", nullValue()));
    }

    @Test
    @DisplayName("Get one Role")
    void findOne() throws Exception {
        when(roleService.findOne(any())).thenReturn(factory.manufacturePojo(Role.class));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(Path.of(baseUrl, UUID.randomUUID().toString()).toString())
            .characterEncoding(StandardCharsets.UTF_8);

        mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", allOf(
                aMapWithSize(3),
                hasKey("id"),
                hasKey("name"),
                hasKey("organizationUnit")
            )))
            .andExpect(jsonPath("$.id", notNullValue()))
            .andExpect(jsonPath("$.organizationUnit", allOf(
                aMapWithSize(3),
                hasKey("id"),
                hasKey("name"),
                hasKey("roles")
            )))
            .andExpect(jsonPath("$.organizationUnit.roles", nullValue()));
    }

    @Test
    @DisplayName("Get one Role, not found")
    void findOneNotFound() throws Exception {
        UUID uuid = UUID.randomUUID();

        when(roleService.findOne(uuid)).thenThrow(NoSuchElementException.class);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(Path.of(baseUrl, uuid.toString()).toString())
            .characterEncoding(StandardCharsets.UTF_8);

        mockMvc.perform(request)
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Create Role")
    void create() throws Exception {
        RoleEditDTO roleEditDTO = factory.manufacturePojo(RoleEditDTO.class);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(baseUrl)
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(roleEditDTO));

        mockMvc.perform(request)
            .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Create Role, invalid request")
    void createInvalid() throws Exception {
        RoleEditDTO roleEditDTO = new RoleEditDTO();
        roleEditDTO.setName(""); // non valido

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(baseUrl)
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(roleEditDTO));

        mockMvc.perform(request)
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Update Role")
    void update() throws Exception {
        RoleEditDTO roleEditDTO = factory.manufacturePojo(RoleEditDTO.class);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.patch(Path.of(baseUrl, UUID.randomUUID().toString()).toString())
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(roleEditDTO));

        mockMvc.perform(request)
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Update Role, invalid request")
    void updateInvalid() throws Exception {
        RoleEditDTO roleEditDTO = factory.manufacturePojo(RoleEditDTO.class);
        roleEditDTO.setName("");

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.patch(baseUrl + "/1")
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(roleEditDTO));

        mockMvc.perform(request)
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Delete Role")
    void delete() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete(Path.of(baseUrl, UUID.randomUUID().toString()).toString())
            .characterEncoding(StandardCharsets.UTF_8);

        mockMvc.perform(request)
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Delete Role, invalid request")
    void deleteInvalid() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete(baseUrl + "/0")
            .characterEncoding(StandardCharsets.UTF_8);

        mockMvc.perform(request)
            .andExpect(status().isBadRequest());
    }
}
