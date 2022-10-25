package it.keybiz.cdp.innova.service;

import it.keybiz.cdp.innova.domain.Idea;
import it.keybiz.cdp.innova.mapper.IdeaMapperImpl;
import it.keybiz.cdp.innova.repository.IdeaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import javax.validation.ConstraintViolationException;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = {IdeaServiceImpl.class, IdeaMapperImpl.class, ValidationAutoConfiguration.class})
public class IdeaServiceTest {
    private final PodamFactory factory = new PodamFactoryImpl();

    @Autowired
    private IdeaService ideaService;

    @MockBean
    private IdeaRepository ideaRepository;

    @Test
    @DisplayName("Submit Idea")
    void submit() {
        Idea idea = factory.manufacturePojo(Idea.class);

        when(ideaRepository.save(idea)).thenReturn(idea);

        ideaService.submit(idea);

        verify(ideaRepository).save(idea);
        verifyNoMoreInteractions(ideaRepository);
    }

    @Test
    @DisplayName("Submit Idea null argument should fail")
    void submitNullArgument() {
        assertThrows(ConstraintViolationException.class, () -> ideaService.submit(null));
    }
}
