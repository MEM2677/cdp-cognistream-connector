package it.keybiz.cdp.innova.web.rest.front;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.keybiz.cdp.innova.domain.Program;
import it.keybiz.cdp.innova.service.ProgramService;
import it.keybiz.cdp.innova.dto.TypologicDTO;
import it.keybiz.cdp.innova.mapper.ProgramMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/front/programs")
@RequiredArgsConstructor
@Api(tags = "Program")
@Tag(name = "Program")
public class ProgramFrontController {
    private final ProgramMapper programMapper;

    private final ProgramService programService;

    @GetMapping("/types")
    @ApiOperation("Find all types of Programs")
    public List<TypologicDTO> findAllTypologic() {
        List<Program> programs = programService.findAll();
        return programMapper.programsToTypologicDTO(programs);
    }
}
