package it.keybiz.cdp.innova.web.rest.back;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.keybiz.cdp.innova.domain.Evangelist;
import it.keybiz.cdp.innova.dto.EvangelistBackDTO;
import it.keybiz.cdp.innova.dto.EvangelistEditDTO;
import it.keybiz.cdp.innova.dto.EvangelistSearchDTO;
import it.keybiz.cdp.innova.enums.EvangelistStatus;
import it.keybiz.cdp.innova.mapper.EvangelistMapper;
import it.keybiz.cdp.innova.service.EvangelistService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping("/api/evangelist")
@RequiredArgsConstructor
@Api(tags = "Evangelist")
@Tag(name = "Evangelist")
public class EvangelistBackController {
    private final EvangelistService evangelistService;

    private final EvangelistMapper evangelistMapper;

    @GetMapping
    public Page<EvangelistBackDTO> findAll(@PageableDefault Pageable pageable, EvangelistSearchDTO searchDTO) {
        Page<Evangelist> evangelists = evangelistService.findAll(pageable, searchDTO);
        return evangelistMapper.evangelistsToDtoPage(evangelists, pageable);
    }

    @GetMapping("/{id}")
    public EvangelistBackDTO findOne(@PathVariable @NotNull UUID id) {
        Evangelist evangelist = evangelistService.findOne(id);
        return evangelistMapper.evangelistToBackDto(evangelist);
    }

    @GetMapping("/statuses")
    public Map<String, String> findAllStatuses() {
        return Arrays
            .stream(EvangelistStatus.values())
            .collect(Collectors.toMap(Enum::name, EvangelistStatus::getDescription));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid EvangelistEditDTO evangelistEditDTO) {
        Evangelist evangelist = evangelistMapper.evangelistEditDtoToEntity(evangelistEditDTO);
        evangelistService.create(evangelist, evangelistEditDTO.getImage());
    }

    @PatchMapping("/{id}")
    public void update(@PathVariable @NotNull UUID id, @RequestBody @Valid EvangelistEditDTO evangelistEditDTO) {
        Evangelist evangelist = evangelistMapper.evangelistEditDtoToEntity(evangelistEditDTO);
        evangelistService.update(id, evangelist, evangelistEditDTO.getImage());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable @NotNull UUID id) {
        evangelistService.delete(id);
    }
}
