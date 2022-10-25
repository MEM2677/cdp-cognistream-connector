package it.keybiz.cdp.innova.service.sync;

import it.keybiz.cdp.innova.client.cognistreamer.CognistreamerClient;
import it.keybiz.cdp.innova.client.cognistreamer.models.BaseResponseModelList;
import it.keybiz.cdp.innova.client.cognistreamer.models.ChallengeResponse;
import it.keybiz.cdp.innova.client.cognistreamer.models.MetadataResponse;
import it.keybiz.cdp.innova.config.ApplicationProperties;
import it.keybiz.cdp.innova.config.CognistreamerProperties;
import it.keybiz.cdp.innova.domain.Challenge;
import it.keybiz.cdp.innova.domain.Program;
import it.keybiz.cdp.innova.mapper.ChallengeMapper;
import it.keybiz.cdp.innova.repository.ChallengeRepository;
import it.keybiz.cdp.innova.repository.ProgramRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service("3")
class ChallengeSynchronizator extends AbstractSynchronizator<ChallengeResponse, Challenge, Program> {
    private final ChallengeMapper challengeMapper;

    ChallengeSynchronizator(
        ApplicationEventPublisher eventPublisher,
        ApplicationProperties applicationProperties,
        CognistreamerProperties cognistreamerProperties,
        CognistreamerClient cognistreamerClient,
        ProgramRepository programRepository,
        ChallengeRepository repository,
        ChallengeMapper challengeMapper
    ) {
        super(eventPublisher, applicationProperties, cognistreamerProperties, cognistreamerClient, programRepository, repository);
        this.challengeMapper = challengeMapper;
        this.fetchImages = true;
    }

    @Override
    BaseResponseModelList<ChallengeResponse> fetchData(UUID parentId, int offset, int batchSize) {
        if (loadTest) {
            MetadataResponse metadataResponse = new MetadataResponse();
            metadataResponse.setTotal(batchSize);

            List<ChallengeResponse> data = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                ChallengeResponse challengeResponse = new ChallengeResponse();
                challengeResponse.setId(UUID.randomUUID());
                data.add(challengeResponse);
            }

            BaseResponseModelList<ChallengeResponse> responseModelList = new BaseResponseModelList<>();
            responseModelList.setData(data);
            responseModelList.setMeta(metadataResponse);

            return responseModelList;
        }

        return cognistreamerClient.fetchChallenges(parentId, true, offset, batchSize);
    }

    @Override
    List<ChallengeResponse> filterData(List<ChallengeResponse> responseData) {
        // recuperiamo solo le challenge con type a 0, che sembrano essere quelle "buone"
        return responseData.stream()
            .filter(challengeResponse -> challengeResponse.getType() == 0)
            .collect(Collectors.toList());
    }

    @Override
    List<Challenge> convertData(List<ChallengeResponse> responseData) {
        return challengeMapper.challengesResponseToEntities(responseData);
    }

    @Override
    void beforePersist(UUID parentId, Challenge challenge) {
        Program program = new Program();
        program.setId(parentId);
        challenge.setProgram(program);
    }
}
