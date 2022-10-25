package it.keybiz.cdp.innova.web.rest.front;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.keybiz.cdp.innova.domain.Challenge;
import it.keybiz.cdp.innova.dto.ChallengeDTO;
import it.keybiz.cdp.innova.dto.ChallengeListDTO;
import it.keybiz.cdp.innova.dto.ChallengeSearchDTO;
import it.keybiz.cdp.innova.mapper.ChallengeMapper;
import it.keybiz.cdp.innova.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/front/challenges")
@RequiredArgsConstructor
@Api(tags = "Challenge")
@Tag(name = "Challenge")
public class ChallengeFrontController {
    private final ChallengeService challengeService;

    private final ChallengeMapper challengeMapper;

    @GetMapping
    @ApiOperation("Find all challenges")
    public List<ChallengeListDTO> findAll(ChallengeSearchDTO searchDTO, @SortDefault("title") Sort sort) {
        List<Challenge> challenges = challengeService.findAll(searchDTO, sort);
        return challengeMapper.challengesToDtoList(challenges);
    }

    @GetMapping("/paged")
    @ApiOperation("Find all challenges paged")
    public Page<ChallengeListDTO> findAllPaged(@PageableDefault(size = 3) Pageable pageable) {
        Page<Challenge> challenges = challengeService.findAllPaged(new ChallengeSearchDTO(), pageable);
        return challengeMapper.challengeToDtoPage(challenges, pageable);
    }

    @GetMapping("/{challengeId}")
    @ApiOperation("Find single challenge by ID")
    public ChallengeDTO findOne(@PathVariable UUID challengeId) {
        Challenge challenge = challengeService.findOne(challengeId);
        return challengeMapper.challengeToDto(challenge);
    }
}
