package it.keybiz.cdp.innova.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
public abstract class BaseControllerTest {
    @Autowired
    protected MockMvc mockMvc;

    protected final String baseUrl = this.getControllerBaseUrl();

    protected abstract String getControllerBaseUrl();

    protected final ObjectMapper objectMapper = new ObjectMapper();

    protected final PodamFactory factory = new PodamFactoryImpl();
}
