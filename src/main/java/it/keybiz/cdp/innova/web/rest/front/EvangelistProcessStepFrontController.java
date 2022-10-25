package it.keybiz.cdp.innova.web.rest.front;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.keybiz.cdp.innova.domain.EvangelistProcessStep;
import it.keybiz.cdp.innova.dto.EvangelistProcessStepSearchDTO;
import it.keybiz.cdp.innova.service.EvangelistProcessStepService;
import it.keybiz.cdp.innova.dto.EvangelistProcessStepDTO;
import it.keybiz.cdp.innova.mapper.EvangelistProcessStepMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/front/evangelist-process")
@RequiredArgsConstructor
@Api(tags = "Evangelist Process Steps")
@Tag(name = "Evangelist Process Steps")
public class EvangelistProcessStepFrontController {
    private final EvangelistProcessStepService evangelistProcessStepService;

    private final EvangelistProcessStepMapper evangelistProcessStepMapper;

    @GetMapping
    public List<EvangelistProcessStepDTO> findAll() {
        List<EvangelistProcessStep> processs = evangelistProcessStepService.findAll(new EvangelistProcessStepSearchDTO());
        return evangelistProcessStepMapper.processsToDtoFrontList(processs);
    }
}
