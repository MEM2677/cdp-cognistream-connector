package it.keybiz.cdp.innova.web.rest.back;

import it.keybiz.cdp.innova.config.ApplicationProperties;
import it.keybiz.cdp.innova.domain.EvangelistProcessStep;
import it.keybiz.cdp.innova.dto.DragDropSortDTO;
import it.keybiz.cdp.innova.dto.EvangelistProcessStepEditDTO;
import it.keybiz.cdp.innova.dto.EvangelistProcessStepSearchDTO;
import it.keybiz.cdp.innova.mapper.EvangelistProcessStepMapperImpl;
import it.keybiz.cdp.innova.service.EvangelistProcessStepService;
import it.keybiz.cdp.innova.service.FileStorageServiceImpl;
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
import java.util.Base64;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {
    ApplicationProperties.class,
    EvangelistProcessStepBackController.class,
    EvangelistProcessStepMapperImpl.class,
    ExceptionTranslator.class,
    FileStorageServiceImpl.class,
})
public class EvangelistProcessStepBackControllerTest extends BaseControllerTest {
    @MockBean
    private EvangelistProcessStepService evangelistProcessStepService;

    @Override
    protected String getControllerBaseUrl() {
        return "/api/evangelist-process";
    }

    @Test
    @DisplayName("Get all Steps")
    void findAll() throws Exception {
        List<EvangelistProcessStep> result = List.of(
            factory.manufacturePojo(EvangelistProcessStep.class),
            factory.manufacturePojo(EvangelistProcessStep.class),
            factory.manufacturePojo(EvangelistProcessStep.class)
        );

        when(evangelistProcessStepService.findAll(any(EvangelistProcessStepSearchDTO.class))).thenReturn(result);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(baseUrl)
            .characterEncoding(StandardCharsets.UTF_8);

        mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(result.size())))
            .andExpect(jsonPath("$.[0]", allOf(
                aMapWithSize(4),
                hasKey("id"),
                hasKey("position"),
                hasKey("description"),
                hasKey("iconUrl")
            )))
            .andExpect(jsonPath("$.[0].id", notNullValue()));
    }

    @Test
    @DisplayName("Get one Step")
    void findOne() throws Exception {
        UUID uuid = UUID.randomUUID();

        when(evangelistProcessStepService.findOne(uuid)).thenReturn(factory.manufacturePojo(EvangelistProcessStep.class));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(Path.of(baseUrl, uuid.toString()).toString())
            .characterEncoding(StandardCharsets.UTF_8);

        mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", allOf(
                aMapWithSize(4),
                hasKey("id"),
                hasKey("position"),
                hasKey("description"),
                hasKey("iconUrl")
            )))
            .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    @DisplayName("Get one Step, not found")
    void findOneNotFound() throws Exception {
        UUID uuid = UUID.randomUUID();

        when(evangelistProcessStepService.findOne(uuid)).thenThrow(NoSuchElementException.class);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(Path.of(baseUrl, uuid.toString()).toString())
            .characterEncoding(StandardCharsets.UTF_8);

        mockMvc.perform(request)
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Create Step")
    void create() throws Exception {
        EvangelistProcessStepEditDTO evangelistProcessStepEditDTO = factory.manufacturePojo(EvangelistProcessStepEditDTO.class);
        evangelistProcessStepEditDTO.setPosition(1);
        evangelistProcessStepEditDTO.setIcon(Base64.getEncoder().encodeToString("icona".getBytes()));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(baseUrl)
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(evangelistProcessStepEditDTO));

        mockMvc.perform(request)
            .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Create Step, invalid base64 icon")
    void createInvalidIcon() throws Exception {
        EvangelistProcessStepEditDTO evangelistProcessStepEditDTO = factory.manufacturePojo(EvangelistProcessStepEditDTO.class);
        evangelistProcessStepEditDTO.setPosition(1);
        evangelistProcessStepEditDTO.setIcon("invalid_base64");

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(baseUrl)
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(evangelistProcessStepEditDTO));

        mockMvc.perform(request)
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Create Step, invalid request")
    void createInvalid() throws Exception {
        EvangelistProcessStepEditDTO evangelistProcessStepEditDTO = new EvangelistProcessStepEditDTO();
        evangelistProcessStepEditDTO.setDescription(""); // non valido

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(baseUrl)
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(evangelistProcessStepEditDTO));

        mockMvc.perform(request)
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Update Step")
    void update() throws Exception {
        EvangelistProcessStepEditDTO evangelistProcessStepEditDTO = factory.manufacturePojo(EvangelistProcessStepEditDTO.class);
        evangelistProcessStepEditDTO.setPosition(1);
        evangelistProcessStepEditDTO.setIcon(Base64.getEncoder().encodeToString("icona".getBytes()));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.patch(Path.of(baseUrl, UUID.randomUUID().toString()).toString())
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(evangelistProcessStepEditDTO));

        mockMvc.perform(request)
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Update Step, invalid request")
    void updateInvalid() throws Exception {
        EvangelistProcessStepEditDTO evangelistProcessStepEditDTO = factory.manufacturePojo(EvangelistProcessStepEditDTO.class);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.patch(baseUrl + "/1")
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(evangelistProcessStepEditDTO));

        mockMvc.perform(request)
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Sort Step order")
    void sortStepOrder() throws Exception {
        List<DragDropSortDTO> dragDropSortDTOList = List.of(
            new DragDropSortDTO(UUID.randomUUID(), 1),
            new DragDropSortDTO(UUID.randomUUID(), 2),
            new DragDropSortDTO(UUID.randomUUID(), 3)
        );

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(baseUrl + "/sort")
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dragDropSortDTOList));

        mockMvc.perform(request)
            .andExpect(status().isAccepted());
    }

    @Test
    @DisplayName("Sort Step order, invalid")
    void sortStepOrderInvalid() throws Exception {
        List<DragDropSortDTO> dragDropSortDTOList = List.of(
            factory.manufacturePojo(DragDropSortDTO.class),
            factory.manufacturePojo(DragDropSortDTO.class),
            factory.manufacturePojo(DragDropSortDTO.class)
        );

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(baseUrl + "/sort")
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dragDropSortDTOList));

        mockMvc.perform(request)
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Delete Step")
    void delete() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete(Path.of(baseUrl, UUID.randomUUID().toString()).toString())
            .characterEncoding(StandardCharsets.UTF_8);

        mockMvc.perform(request)
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Delete Step, invalid request")
    void deleteInvalid() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete(baseUrl + "/0")
            .characterEncoding(StandardCharsets.UTF_8);

        mockMvc.perform(request)
            .andExpect(status().isBadRequest());
    }
}
