package it.keybiz.cdp.innova.web.rest.back;

import it.keybiz.cdp.innova.config.ApplicationProperties;
import it.keybiz.cdp.innova.domain.Evangelist;
import it.keybiz.cdp.innova.dto.EvangelistEditDTO;
import it.keybiz.cdp.innova.dto.EvangelistSearchDTO;
import it.keybiz.cdp.innova.enums.EvangelistStatus;
import it.keybiz.cdp.innova.mapper.EvangelistMapperImpl;
import it.keybiz.cdp.innova.service.EvangelistService;
import it.keybiz.cdp.innova.service.FileStorageServiceImpl;
import it.keybiz.cdp.innova.web.rest.BaseControllerTest;
import it.keybiz.cdp.innova.web.rest.errors.ExceptionTranslator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {
    ApplicationProperties.class,
    EvangelistBackController.class,
    EvangelistMapperImpl.class,
    ExceptionTranslator.class,
    FileStorageServiceImpl.class,
})
public class EvangelistBackControllerTest extends BaseControllerTest {
    @MockBean
    private EvangelistService evangelistService;

    @Override
    protected String getControllerBaseUrl() {
        return "/api/evangelist";
    }

    @Test
    @DisplayName("Get all Evangelist paged")
    void findAll() throws Exception {
        PageImpl<Evangelist> resultPage = new PageImpl<>(List.of(
            factory.manufacturePojo(Evangelist.class),
            factory.manufacturePojo(Evangelist.class),
            factory.manufacturePojo(Evangelist.class)
        ));

        when(evangelistService.findAll(any(Pageable.class), any(EvangelistSearchDTO.class))).thenReturn(resultPage);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(baseUrl)
            .characterEncoding(StandardCharsets.UTF_8);

        mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(resultPage.getNumberOfElements())))
            .andExpect(jsonPath("$.content.[0]", allOf(
                aMapWithSize(9),
                hasKey("id"),
                hasKey("name"),
                hasKey("surname"),
                hasKey("imageUrl"),
                hasKey("email"),
                hasKey("phone"),
                hasKey("reason"),
                hasKey("status"),
                hasKey("role")
            )))
            .andExpect(jsonPath("$.content.[0].id", notNullValue()));
    }

    @Test
    @DisplayName("Get one Evangelist")
    void findOne() throws Exception {
        when(evangelistService.findOne(any())).thenReturn(factory.manufacturePojo(Evangelist.class));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(Path.of(baseUrl, UUID.randomUUID().toString()).toString())
            .characterEncoding(StandardCharsets.UTF_8);

        mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", allOf(
                aMapWithSize(9),
                hasKey("id"),
                hasKey("name"),
                hasKey("surname"),
                hasKey("imageUrl"),
                hasKey("email"),
                hasKey("phone"),
                hasKey("reason"),
                hasKey("status"),
                hasKey("role")
            )))
            .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    @DisplayName("Find all Evangelist statuses")
    void findAllStatuses() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(baseUrl + "/statuses")
            .characterEncoding(StandardCharsets.UTF_8);

        mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", allOf(Arrays.stream(EvangelistStatus.values()).map(status -> hasKey(status.name())).collect(Collectors.toList()))))
            .andExpect(jsonPath("$.*", hasSize(EvangelistStatus.values().length)));
    }

    @Test
    @DisplayName("Get one Evangelist, not found")
    void findOneNotFound() throws Exception {
        UUID uuid = UUID.randomUUID();

        when(evangelistService.findOne(uuid)).thenThrow(NoSuchElementException.class);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(Path.of(baseUrl, uuid.toString()).toString())
            .characterEncoding(StandardCharsets.UTF_8);

        mockMvc.perform(request)
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Create Evangelist")
    void create() throws Exception {
        EvangelistEditDTO evangelistEditDTO = factory.manufacturePojo(EvangelistEditDTO.class);
        evangelistEditDTO.setEmail("email@test.com");

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(baseUrl)
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(evangelistEditDTO));

        mockMvc.perform(request)
            .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Create Evangelist, invalid base64 image")
    void createInvalidImage() throws Exception {
        EvangelistEditDTO evangelistEditDTO = factory.manufacturePojo(EvangelistEditDTO.class);
        evangelistEditDTO.setEmail("email@test.com");
        evangelistEditDTO.setImage("invalid_base64");

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(baseUrl)
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(evangelistEditDTO));

        mockMvc.perform(request)
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Create Evangelist, invalid request")
    void createInvalid() throws Exception {
        EvangelistEditDTO evangelistEditDTO = new EvangelistEditDTO();
        evangelistEditDTO.setEmail("email@example.com");

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(baseUrl)
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(evangelistEditDTO));

        mockMvc.perform(request)
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Update Evangelist")
    void update() throws Exception {
        EvangelistEditDTO evangelistEditDTO = factory.manufacturePojo(EvangelistEditDTO.class);
        evangelistEditDTO.setEmail("email@test.com");

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.patch(Path.of(baseUrl, UUID.randomUUID().toString()).toString())
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(evangelistEditDTO));

        mockMvc.perform(request)
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Update Evangelist, invalid request")
    void updateInvalid() throws Exception {
        EvangelistEditDTO evangelistEditDTO = factory.manufacturePojo(EvangelistEditDTO.class);
        evangelistEditDTO.setEmail("email");

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.patch(baseUrl + "/1")
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(evangelistEditDTO));

        mockMvc.perform(request)
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Delete Evangelist")
    void delete() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete(Path.of(baseUrl, UUID.randomUUID().toString()).toString())
            .characterEncoding(StandardCharsets.UTF_8);

        mockMvc.perform(request)
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Delete Evangelist, invalid request")
    void deleteInvalid() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete(baseUrl + "/0")
            .characterEncoding(StandardCharsets.UTF_8);

        mockMvc.perform(request)
            .andExpect(status().isBadRequest());
    }
}
