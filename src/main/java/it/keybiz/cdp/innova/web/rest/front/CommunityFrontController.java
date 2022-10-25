package it.keybiz.cdp.innova.web.rest.front;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.keybiz.cdp.innova.domain.Community;
import it.keybiz.cdp.innova.dto.TypologicDTO;
import it.keybiz.cdp.innova.mapper.CommunityMapper;
import it.keybiz.cdp.innova.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/front/communities")
@RequiredArgsConstructor
@Api(tags = "Community")
@Tag(name = "Community")
public class CommunityFrontController {
    private final CommunityMapper communityMapper;
    private final CommunityService communityService;

    @GetMapping("/types")
    @ApiOperation("Find all types of Communities")
    public List<TypologicDTO> findAllTypologic() {
        List<Community> communities = communityService.findAll();
        return communityMapper.communitiesToTypologicDTO(communities);
    }
}
