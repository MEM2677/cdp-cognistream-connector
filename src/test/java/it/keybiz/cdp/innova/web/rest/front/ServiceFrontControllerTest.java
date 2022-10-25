package it.keybiz.cdp.innova.web.rest.front;

import it.keybiz.cdp.innova.domain.Servizio;
import it.keybiz.cdp.innova.dto.ServiceSearchDTO;
import it.keybiz.cdp.innova.mapper.ServiceMapperImpl;
import it.keybiz.cdp.innova.service.ServiceService;
import it.keybiz.cdp.innova.web.rest.BaseControllerTest;
import it.keybiz.cdp.innova.web.rest.errors.ExceptionTranslator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {ServiceFrontController.class, ServiceMapperImpl.class, ExceptionTranslator.class})
public class ServiceFrontControllerTest extends BaseControllerTest {
    @MockBean
    private ServiceService serviceService;

    @Override
    protected String getControllerBaseUrl() {
        return "/api/front/service";
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
            .andExpect(jsonPath("$.[0].id", nullValue()));
    }
}
