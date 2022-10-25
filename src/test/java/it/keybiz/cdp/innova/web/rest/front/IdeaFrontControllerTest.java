package it.keybiz.cdp.innova.web.rest.front;

import it.keybiz.cdp.innova.domain.Idea;
import it.keybiz.cdp.innova.dto.IdeaSumbissionDTO;
import it.keybiz.cdp.innova.mapper.IdeaMapperImpl;
import it.keybiz.cdp.innova.service.IdeaService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {IdeaFrontController.class, IdeaMapperImpl.class, ExceptionTranslator.class})
public class IdeaFrontControllerTest extends BaseControllerTest {
    @MockBean
    private IdeaService ideaService;

    @Override
    protected String getControllerBaseUrl() {
        return "/api/front/idea";
    }

    @Test
    @DisplayName("Submit Idea")
    void submit() throws Exception {
        IdeaSumbissionDTO ideaSumbissionDTO = factory.manufacturePojo(IdeaSumbissionDTO.class);
        ideaSumbissionDTO.setEmail("test@email.com");

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(baseUrl)
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(ideaSumbissionDTO));

        mockMvc.perform(request)
            .andExpect(status().isCreated());

        verify(ideaService).submit(any(Idea.class));
    }

    @Test
    @DisplayName("Submit Idea, invalid")
    void submitInvalid() throws Exception {
        IdeaSumbissionDTO ideaSumbissionDTO = factory.manufacturePojo(IdeaSumbissionDTO.class);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(baseUrl)
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(ideaSumbissionDTO));

        mockMvc.perform(request)
            .andExpect(status().isBadRequest());

        verifyNoInteractions(ideaService);
    }
}
