package it.keybiz.cdp.innova.web.rest.back;

import it.keybiz.cdp.innova.domain.Servizio;
import it.keybiz.cdp.innova.dto.DragDropSortDTO;
import it.keybiz.cdp.innova.dto.ServiceEditDTO;
import it.keybiz.cdp.innova.dto.ServiceSearchDTO;
import it.keybiz.cdp.innova.mapper.ServiceMapperImpl;
import it.keybiz.cdp.innova.service.ServiceService;
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

@ContextConfiguration(classes = {ServiceBackController.class, ServiceMapperImpl.class, ExceptionTranslator.class})
public class ServiceBackControllerTest extends BaseControllerTest {
    @MockBean
    private ServiceService serviceService;

    @Override
    protected String getControllerBaseUrl() {
        return "/api/service";
    }

    @Test
    @DisplayName("Get all Services")
    void findAll() throws Exception {
        List<Servizio> resultPage = List.of(
            factory.manufacturePojo(Servizio.class),
            factory.manufacturePojo(Servizio.class),
            factory.manufacturePojo(Servizio.class)
        );

        when(serviceService.findAll(any(ServiceSearchDTO.class))).thenReturn(resultPage);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(baseUrl)
            .characterEncoding(StandardCharsets.UTF_8);

        mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(resultPage.size())))
            .andExpect(jsonPath("$.[0]", allOf(
                aMapWithSize(5),
                hasKey("id"),
                hasKey("title"),
                hasKey("description"),
                hasKey("link"),
                hasKey("position")
            )))
            .andExpect(jsonPath("$.[0].id", notNullValue()));
    }

    @Test
    @DisplayName("Get one Service")
    void findOne() throws Exception {
        when(serviceService.findOne(any())).thenReturn(factory.manufacturePojo(Servizio.class));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(Path.of(baseUrl, UUID.randomUUID().toString()).toString())
            .characterEncoding(StandardCharsets.UTF_8);

        mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", allOf(
                aMapWithSize(5),
                hasKey("id"),
                hasKey("title"),
                hasKey("description"),
                hasKey("link"),
                hasKey("position")
            )))
            .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    @DisplayName("Get one Service, not found")
    void findOneNotFound() throws Exception {
        UUID uuid = UUID.randomUUID();

        when(serviceService.findOne(uuid)).thenThrow(NoSuchElementException.class);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(Path.of(baseUrl, uuid.toString()).toString())
            .characterEncoding(StandardCharsets.UTF_8);

        mockMvc.perform(request)
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Create Service")
    void create() throws Exception {
        ServiceEditDTO serviceEditDTO = factory.manufacturePojo(ServiceEditDTO.class);
        serviceEditDTO.setPosition(1);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(baseUrl)
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(serviceEditDTO));

        mockMvc.perform(request)
            .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Create Service, invalid request")
    void createInvalid() throws Exception {
        ServiceEditDTO serviceEditDTO = new ServiceEditDTO();
        serviceEditDTO.setTitle(""); // non valido

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(baseUrl)
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(serviceEditDTO));

        mockMvc.perform(request)
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Update Service")
    void update() throws Exception {
        ServiceEditDTO serviceEditDTO = factory.manufacturePojo(ServiceEditDTO.class);
        serviceEditDTO.setPosition(1);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.patch(Path.of(baseUrl, UUID.randomUUID().toString()).toString())
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(serviceEditDTO));

        mockMvc.perform(request)
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Update Service, invalid request")
    void updateInvalid() throws Exception {
        ServiceEditDTO serviceEditDTO = factory.manufacturePojo(ServiceEditDTO.class);
        serviceEditDTO.setTitle("");

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.patch(baseUrl + "/1")
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(serviceEditDTO));

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
    @DisplayName("Delete Service")
    void delete() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete(Path.of(baseUrl, UUID.randomUUID().toString()).toString())
            .characterEncoding(StandardCharsets.UTF_8);

        mockMvc.perform(request)
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Delete Service, invalid request")
    void deleteInvalid() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete(baseUrl + "/0")
            .characterEncoding(StandardCharsets.UTF_8);

        mockMvc.perform(request)
            .andExpect(status().isBadRequest());
    }
}
