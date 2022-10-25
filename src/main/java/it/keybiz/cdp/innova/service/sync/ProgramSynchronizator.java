package it.keybiz.cdp.innova.service.sync;

import it.keybiz.cdp.innova.client.cognistreamer.CognistreamerClient;
import it.keybiz.cdp.innova.client.cognistreamer.models.BaseResponseModelList;
import it.keybiz.cdp.innova.client.cognistreamer.models.MetadataResponse;
import it.keybiz.cdp.innova.client.cognistreamer.models.ProgramResponse;
import it.keybiz.cdp.innova.config.ApplicationProperties;
import it.keybiz.cdp.innova.config.CognistreamerProperties;
import it.keybiz.cdp.innova.domain.Community;
import it.keybiz.cdp.innova.domain.Program;
import it.keybiz.cdp.innova.mapper.ProgramMapper;
import it.keybiz.cdp.innova.repository.CommunityRepository;
import it.keybiz.cdp.innova.repository.ProgramRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service("2")
class ProgramSynchronizator extends AbstractSynchronizator<ProgramResponse, Program, Community> {
    private final ProgramMapper programMapper;

    ProgramSynchronizator(
        ApplicationEventPublisher eventPublisher,
        ApplicationProperties applicationProperties,
        CognistreamerProperties cognistreamerProperties,
        CognistreamerClient cognistreamerClient,
        CommunityRepository communityRepository,
        ProgramRepository repository,
        ProgramMapper programMapper
    ) {
        super(eventPublisher, applicationProperties, cognistreamerProperties, cognistreamerClient, communityRepository, repository);
        this.programMapper = programMapper;
    }

    @Override
    BaseResponseModelList<ProgramResponse> fetchData(UUID parentId, int offset, int batchSize) {
        if (loadTest) {
            MetadataResponse metadataResponse = new MetadataResponse();
            metadataResponse.setTotal(batchSize);

            List<ProgramResponse> data = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                ProgramResponse programResponse = new ProgramResponse();
                programResponse.setId(UUID.randomUUID());
                data.add(programResponse);
            }

            BaseResponseModelList<ProgramResponse> responseModelList = new BaseResponseModelList<>();
            responseModelList.setData(data);
            responseModelList.setMeta(metadataResponse);

            return responseModelList;
        }

        return cognistreamerClient.fetchPrograms(parentId, true, true, offset, batchSize);
    }

    @Override
    List<Program> convertData(List<ProgramResponse> responseData) {
        return programMapper.programsResponseToEntities(responseData);
    }

    @Override
    void beforePersist(UUID parentId, Program program) {
        Community community = new Community();
        community.setId(parentId);
        program.setCommunity(community);
    }
}
