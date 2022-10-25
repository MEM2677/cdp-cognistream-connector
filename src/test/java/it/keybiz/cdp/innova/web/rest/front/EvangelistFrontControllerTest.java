package it.keybiz.cdp.innova.web.rest.front;

import it.keybiz.cdp.innova.config.ApplicationProperties;
import it.keybiz.cdp.innova.domain.Evangelist;
import it.keybiz.cdp.innova.dto.EvangelistSubscriptionDTO;
import it.keybiz.cdp.innova.mapper.EvangelistMapperImpl;
import it.keybiz.cdp.innova.service.EvangelistService;
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
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {
    ApplicationProperties.class,
    EvangelistFrontController.class,
    EvangelistMapperImpl.class,
    ExceptionTranslator.class,
    FileStorageServiceImpl.class,
})
public class EvangelistFrontControllerTest extends BaseControllerTest {
    @MockBean
    private EvangelistService evangelistService;

    @Override
    protected String getControllerBaseUrl() {
        return "/api/front/evangelist";
    }

    @Test
    @DisplayName("Get all active Evangelist grouped by OU")
    void findAllActive() throws Exception {
        List<Evangelist> result = List.of(
            factory.manufacturePojo(Evangelist.class)
        );

        when(evangelistService.findAllActive()).thenReturn(result);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(baseUrl)
            .characterEncoding(StandardCharsets.UTF_8);

        mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", aMapWithSize(result.size())))
            .andExpect(jsonPath("$.*.[0]", allOf(
                hasItem(aMapWithSize(4)),
                hasItem(hasKey("name")),
                hasItem(hasKey("surname")),
                hasItem(hasKey("imageUrl")),
                hasItem(hasKey("role"))
            )));
    }

    @Test
    @DisplayName("Subscribe Evangelist")
    void subscribe() throws Exception {
        EvangelistSubscriptionDTO subscriptionDTO = factory.manufacturePojo(EvangelistSubscriptionDTO.class);
        subscriptionDTO.setEmail("email@example.com");

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(baseUrl)
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(subscriptionDTO));

        mockMvc.perform(request)
            .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Subscribe Evangelist, invalid")
    void subscribeInvalid() throws Exception {
        EvangelistSubscriptionDTO subscriptionDTO = factory.manufacturePojo(EvangelistSubscriptionDTO.class);
        subscriptionDTO.setEmail("");

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(baseUrl)
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(subscriptionDTO));

        mockMvc.perform(request)
            .andExpect(status().isBadRequest());
    }
}
