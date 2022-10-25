package it.keybiz.cdp.innova.web.rest.back;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.keybiz.cdp.innova.domain.EvangelistProcessStep;
import it.keybiz.cdp.innova.dto.DragDropSortDTO;
import it.keybiz.cdp.innova.dto.EvangelistProcessStepDTO;
import it.keybiz.cdp.innova.dto.EvangelistProcessStepEditDTO;
import it.keybiz.cdp.innova.dto.EvangelistProcessStepSearchDTO;
import it.keybiz.cdp.innova.mapper.EvangelistProcessStepMapper;
import it.keybiz.cdp.innova.service.EvangelistProcessStepService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/evangelist-process")
@RequiredArgsConstructor
@Api(tags = "Evangelist Process Steps")
@Tag(name = "Evangelist Process Steps")
public class EvangelistProcessStepBackController {
    private final EvangelistProcessStepService evangelistProcessStepService;

    private final EvangelistProcessStepMapper evangelistProcessStepMapper;

    @GetMapping
    public List<EvangelistProcessStepDTO> findAll(EvangelistProcessStepSearchDTO searchDTO) {
        List<EvangelistProcessStep> processs = evangelistProcessStepService.findAll(searchDTO);
        return evangelistProcessStepMapper.processsToDtoBackList(processs);
    }

    @GetMapping("/{id}")
    public EvangelistProcessStepDTO findOne(@PathVariable @NotNull UUID id) {
        EvangelistProcessStep evangelistProcessStep = evangelistProcessStepService.findOne(id);
        return evangelistProcessStepMapper.processToDTO(evangelistProcessStep);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid @NotNull EvangelistProcessStepEditDTO evangelistProcessStepEditDTO) {
        EvangelistProcessStep evangelistProcessStep = evangelistProcessStepMapper.processEditDtoToEntity(evangelistProcessStepEditDTO);
        evangelistProcessStepService.create(evangelistProcessStep, evangelistProcessStepEditDTO.getIcon());
    }

    @PatchMapping("/{id}")
    public void update(@PathVariable @NotNull UUID id, @RequestBody @Valid EvangelistProcessStepEditDTO evangelistProcessStepEditDTO) {
        EvangelistProcessStep evangelistProcessStep = evangelistProcessStepMapper.processEditDtoToEntity(evangelistProcessStepEditDTO);
        evangelistProcessStepService.update(id, evangelistProcessStep, evangelistProcessStepEditDTO.getIcon());
    }

    @PostMapping("/sort")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void sort(@RequestBody @Valid @NotNull List<DragDropSortDTO> dragDropSortDTOList) {
        List<EvangelistProcessStep> evangelistProcessSteps = evangelistProcessStepMapper.processOrderToEntityList(dragDropSortDTOList);
        evangelistProcessStepService.sort(evangelistProcessSteps);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable @NotNull UUID id) {
        evangelistProcessStepService.delete(id);
    }
}
