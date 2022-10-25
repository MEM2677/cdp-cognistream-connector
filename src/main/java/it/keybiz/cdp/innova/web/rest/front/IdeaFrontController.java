package it.keybiz.cdp.innova.web.rest.front;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.keybiz.cdp.innova.domain.Idea;
import it.keybiz.cdp.innova.service.IdeaService;
import it.keybiz.cdp.innova.dto.IdeaSumbissionDTO;
import it.keybiz.cdp.innova.mapper.IdeaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/front/idea")
@RequiredArgsConstructor
@Api(tags = "Idea")
@Tag(name = "Idea")
public class IdeaFrontController {
    private final IdeaService ideaService;

    private final IdeaMapper ideaMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void submit(@RequestBody @Valid IdeaSumbissionDTO ideaSumbissionDTO) {
        Idea idea = ideaMapper.ideaSubmissionDTOtoEntity(ideaSumbissionDTO);
        ideaService.submit(idea);
    }
}
