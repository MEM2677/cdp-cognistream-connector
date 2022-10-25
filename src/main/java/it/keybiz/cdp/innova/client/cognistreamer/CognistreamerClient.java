package it.keybiz.cdp.innova.client.cognistreamer;

import feign.Response;
import it.keybiz.cdp.innova.client.cognistreamer.models.BaseResponseModelList;
import it.keybiz.cdp.innova.client.cognistreamer.models.ChallengeResponse;
import it.keybiz.cdp.innova.client.cognistreamer.models.CommunityResponse;
import it.keybiz.cdp.innova.client.cognistreamer.models.ContributionResponse;
import it.keybiz.cdp.innova.client.cognistreamer.models.ContributonByIdResponse;
import it.keybiz.cdp.innova.client.cognistreamer.models.ProgramResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;
import java.util.UUID;

@FeignClient(name = "cognistreamer-client", url = "${cognistreamer.api.base-url}", configuration = CognistreamerClientConfiguration.class)
public interface CognistreamerClient {
    @GetMapping("/public/api/v1/communities")
    BaseResponseModelList<CommunityResponse> fetchCommunities(@RequestParam(name = "pagination.offset") int offset, @RequestParam(name = "pagination.limit") int limit);

    @GetMapping("/public/api/v1/programs")
    BaseResponseModelList<ProgramResponse> fetchPrograms(@RequestParam(name = "communityId") UUID communityId, @RequestParam(name = "isActive") Boolean isActive, @RequestParam(name = "isPublished") Boolean isPublished, @RequestParam(name = "pagination.offset") int offset, @RequestParam(name = "pagination.limit") int limit);

    @GetMapping("/public/api/v1/programs/{programId}/modules")
    BaseResponseModelList<ChallengeResponse> fetchChallenges(@PathVariable(name = "programId") UUID programId, @RequestParam(name = "isActive") Boolean isActive, @RequestParam(name = "pagination.offset") int offset, @RequestParam(name = "pagination.limit") int limit);

    @GetMapping("/public/api/v1/modules/{moduleId}/contributions")
    BaseResponseModelList<ContributionResponse> fetchContributions(@PathVariable(name = "moduleId") UUID challengeId, @RequestParam(name = "pagination.offset") int offset, @RequestParam(name = "pagination.limit") int limit);

    @GetMapping("/public/api/v1/contributions/{contributionId}")
    ContributonByIdResponse fetchContribution(@PathVariable(name = "contributionId") UUID contributionId);

    @GetMapping
    Response fetchImage(URI uri);
}
