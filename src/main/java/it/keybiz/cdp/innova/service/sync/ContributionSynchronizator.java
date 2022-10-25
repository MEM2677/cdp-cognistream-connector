package it.keybiz.cdp.innova.service.sync;

import it.keybiz.cdp.innova.client.cognistreamer.CognistreamerClient;
import it.keybiz.cdp.innova.client.cognistreamer.models.BaseResponseModelList;
import it.keybiz.cdp.innova.client.cognistreamer.models.ContributionResponse;
import it.keybiz.cdp.innova.client.cognistreamer.models.MetadataResponse;
import it.keybiz.cdp.innova.config.ApplicationProperties;
import it.keybiz.cdp.innova.config.CognistreamerProperties;
import it.keybiz.cdp.innova.domain.Challenge;
import it.keybiz.cdp.innova.domain.Contribution;
import it.keybiz.cdp.innova.mapper.ContributionMapper;
import it.keybiz.cdp.innova.repository.ChallengeRepository;
import it.keybiz.cdp.innova.repository.ContributionRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service("4")
class ContributionSynchronizator extends AbstractSynchronizator<ContributionResponse, Contribution, Challenge> {
    private final ContributionMapper contributionMapper;

    ContributionSynchronizator(
        ApplicationEventPublisher eventPublisher,
        ApplicationProperties applicationProperties,
        CognistreamerProperties cognistreamerProperties,
        CognistreamerClient cognistreamerClient,
        ChallengeRepository challengeRepository,
        ContributionRepository repository,
        ContributionMapper contributionMapper
    ) {
        super(eventPublisher, applicationProperties, cognistreamerProperties, cognistreamerClient, challengeRepository, repository);
        this.contributionMapper = contributionMapper;
        this.fetchImages = true;
    }

    @Override
    BaseResponseModelList<ContributionResponse> fetchData(UUID parentId, int offset, int batchSize) {
        if (loadTest) {
            MetadataResponse metadataResponse = new MetadataResponse();
            metadataResponse.setTotal(batchSize);

            List<ContributionResponse> data = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                ContributionResponse contributionResponse = new ContributionResponse();
                contributionResponse.setId(UUID.randomUUID());
                data.add(contributionResponse);
            }

            BaseResponseModelList<ContributionResponse> responseModelList = new BaseResponseModelList<>();
            responseModelList.setData(data);
            responseModelList.setMeta(metadataResponse);

            return responseModelList;
        }

        return cognistreamerClient.fetchContributions(parentId, offset, batchSize);
    }

    @Override
    List<ContributionResponse> beforeConvert(List<ContributionResponse> responseData) {
        return responseData.stream()
            .map(response -> cognistreamerClient.fetchContribution(response.getId()).getData())
            .collect(Collectors.toList());
    }

    @Override
    List<Contribution> convertData(List<ContributionResponse> responseData) {
        return contributionMapper.contributionsResponseToEntities(responseData);
    }

    @Override
    void beforePersist(UUID parentId, Contribution contribution) {
        Challenge challenge = new Challenge();
        challenge.setId(parentId);
        contribution.setChallenge(challenge);
    }
}
