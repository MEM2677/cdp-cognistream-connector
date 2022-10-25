package it.keybiz.cdp.innova.web.rest.front;

import it.keybiz.cdp.innova.config.ApplicationProperties;
import it.keybiz.cdp.innova.domain.EvangelistProcessStep;
import it.keybiz.cdp.innova.dto.EvangelistProcessStepSearchDTO;
import it.keybiz.cdp.innova.mapper.EvangelistProcessStepMapperImpl;
import it.keybiz.cdp.innova.service.EvangelistProcessStepService;
import it.keybiz.cdp.innova.service.FileStorageServiceImpl;
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

@ContextConfiguration(classes = {
    ApplicationProperties.class,
    EvangelistProcessStepFrontController.class,
    EvangelistProcessStepMapperImpl.class,
    ExceptionTranslator.class,
    FileStorageServiceImpl.class,
})
public class EvangelistProcessStepFrontControllerTest extends BaseControllerTest {
    @MockBean
    private EvangelistProcessStepService evangelistProcessStepService;

    @Override
    protected String getControllerBaseUrl() {
        return "/api/front/evangelist-process";
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
            .andExpect(jsonPath("$.[0].id", nullValue()));
    }
}
