package it.keybiz.cdp.innova.web.rest.back;

import it.keybiz.cdp.innova.domain.OrganizationUnit;
import it.keybiz.cdp.innova.dto.OrganizationUnitEditDTO;
import it.keybiz.cdp.innova.dto.OrganizationUnitSearchDTO;
import it.keybiz.cdp.innova.mapper.OrganizationUnitMapperImpl;
import it.keybiz.cdp.innova.service.OrganizationUnitService;
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

@ContextConfiguration(classes = {OrganizationUnitController.class, OrganizationUnitMapperImpl.class, ExceptionTranslator.class})
public class OrganizationUnitControllerTest extends BaseControllerTest {
    @MockBean
    private OrganizationUnitService organizationUnitService;

    @Override
    protected String getControllerBaseUrl() {
        return "/api/organization-unit";
    }

    @Test
    @DisplayName("Get all Organization Units")
    void findAll() throws Exception {
        List<OrganizationUnit> resultPage = List.of(
            factory.manufacturePojo(OrganizationUnit.class),
            factory.manufacturePojo(OrganizationUnit.class),
            factory.manufacturePojo(OrganizationUnit.class)
        );

        when(organizationUnitService.findAll(any(OrganizationUnitSearchDTO.class))).thenReturn(resultPage);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(baseUrl)
            .characterEncoding(StandardCharsets.UTF_8);

        mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(resultPage.size())))
            .andExpect(jsonPath("$.[0]", allOf(
                aMapWithSize(3),
                hasKey("id"),
                hasKey("name"),
                hasKey("roles")
            )))
            .andExpect(jsonPath("$.[0].id", notNullValue()))
            .andExpect(jsonPath("$.[0].roles").isArray())
            .andExpect(jsonPath("$.[0].roles.[0]", allOf(
                aMapWithSize(3),
                hasKey("id"),
                hasKey("name"),
                hasKey("organizationUnit")
            )))
            .andExpect(jsonPath("$.[0].roles.[0].organizationUnit", nullValue()));
    }

    @Test
    @DisplayName("Get one Organization Unit")
    void findOne() throws Exception {
        when(organizationUnitService.findOne(any())).thenReturn(factory.manufacturePojo(OrganizationUnit.class));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(Path.of(baseUrl, UUID.randomUUID().toString()).toString())
            .characterEncoding(StandardCharsets.UTF_8);

        mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", allOf(
                aMapWithSize(3),
                hasKey("id"),
                hasKey("name"),
                hasKey("roles")
            )))
            .andExpect(jsonPath("$.id", notNullValue()))
            .andExpect(jsonPath("$.roles").isArray())
            .andExpect(jsonPath("$.roles.[0]", allOf(
                aMapWithSize(3),
                hasKey("id"),
                hasKey("name"),
                hasKey("organizationUnit")
            )))
            .andExpect(jsonPath("$.roles.[0].organizationUnit", nullValue()));
    }

    @Test
    @DisplayName("Get one Organization Unit, not found")
    void findOneNotFound() throws Exception {
        UUID uuid = UUID.randomUUID();

        when(organizationUnitService.findOne(uuid)).thenThrow(NoSuchElementException.class);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(Path.of(baseUrl, uuid.toString()).toString())
            .characterEncoding(StandardCharsets.UTF_8);

        mockMvc.perform(request)
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Create Organization Unit")
    void create() throws Exception {
        OrganizationUnitEditDTO organizationUnitEditDTO = factory.manufacturePojo(OrganizationUnitEditDTO.class);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(baseUrl)
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(organizationUnitEditDTO));

        mockMvc.perform(request)
            .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Create Organization Unit, invalid request")
    void createInvalid() throws Exception {
        OrganizationUnitEditDTO organizationUnitEditDTO = new OrganizationUnitEditDTO();
        organizationUnitEditDTO.setName(""); // non valido

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(baseUrl)
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(organizationUnitEditDTO));

        mockMvc.perform(request)
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Update Organization Unit")
    void update() throws Exception {
        OrganizationUnitEditDTO organizationUnitEditDTO = factory.manufacturePojo(OrganizationUnitEditDTO.class);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.patch(Path.of(baseUrl, UUID.randomUUID().toString()).toString())
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(organizationUnitEditDTO));

        mockMvc.perform(request)
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Update Organization Unit, invalid request")
    void updateInvalid() throws Exception {
        OrganizationUnitEditDTO organizationUnitEditDTO = factory.manufacturePojo(OrganizationUnitEditDTO.class);
        organizationUnitEditDTO.setName("");

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.patch(baseUrl + "/1")
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(organizationUnitEditDTO));

        mockMvc.perform(request)
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Delete Organization Unit")
    void delete() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete(Path.of(baseUrl, UUID.randomUUID().toString()).toString())
            .characterEncoding(StandardCharsets.UTF_8);

        mockMvc.perform(request)
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Delete Organization Unit, invalid request")
    void deleteInvalid() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete(baseUrl + "/0")
            .characterEncoding(StandardCharsets.UTF_8);

        mockMvc.perform(request)
            .andExpect(status().isBadRequest());
    }
}
