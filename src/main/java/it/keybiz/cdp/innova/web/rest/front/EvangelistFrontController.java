package it.keybiz.cdp.innova.web.rest.front;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.keybiz.cdp.innova.domain.Evangelist;
import it.keybiz.cdp.innova.dto.EvangelistFrontDTO;
import it.keybiz.cdp.innova.dto.EvangelistSubscriptionDTO;
import it.keybiz.cdp.innova.mapper.EvangelistMapper;
import it.keybiz.cdp.innova.service.EvangelistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/api/front/evangelist")
@RequiredArgsConstructor
@Api(tags = "Evangelist")
@Tag(name = "Evangelist")
public class EvangelistFrontController {
    private final EvangelistService evangelistService;

    private final EvangelistMapper evangelistMapper;

    @GetMapping
    public Map<String, List<EvangelistFrontDTO>> findAllActiveGrouped() {
        List<Evangelist> evangelists = evangelistService.findAllActive();
        return evangelistMapper.evangelistsGroupedDtos(evangelists);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void subscribe(@RequestBody @Valid EvangelistSubscriptionDTO evangelistSubscriptionDTO) {
        Evangelist evangelist = evangelistMapper.evangelistSubscriptionDtoToEntity(evangelistSubscriptionDTO);
        evangelistService.subscribe(evangelist);
    }
}
