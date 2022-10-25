package it.keybiz.cdp.innova.service.sync;

import it.keybiz.cdp.innova.client.cognistreamer.CognistreamerClient;
import it.keybiz.cdp.innova.client.cognistreamer.models.BaseResponseModelList;
import it.keybiz.cdp.innova.client.cognistreamer.models.CommunityResponse;
import it.keybiz.cdp.innova.client.cognistreamer.models.MetadataResponse;
import it.keybiz.cdp.innova.config.ApplicationProperties;
import it.keybiz.cdp.innova.config.CognistreamerProperties;
import it.keybiz.cdp.innova.domain.Community;
import it.keybiz.cdp.innova.domain.SynchronizableEntity;
import it.keybiz.cdp.innova.mapper.CommunityMapper;
import it.keybiz.cdp.innova.repository.CommunityRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service("1")
class CommunitySynchronizator extends AbstractSynchronizator<CommunityResponse, Community, SynchronizableEntity> {
    private final CommunityMapper communityMapper;

    CommunitySynchronizator(
        ApplicationEventPublisher eventPublisher,
        ApplicationProperties applicationProperties,
        CognistreamerProperties cognistreamerProperties,
        CognistreamerClient cognistreamerClient,
        CommunityRepository communityRepository,
        CommunityMapper communityMapper
    ) {
        super(eventPublisher, applicationProperties, cognistreamerProperties, cognistreamerClient, null, communityRepository);
        this.communityMapper = communityMapper;
    }

    @Override
    BaseResponseModelList<CommunityResponse> fetchData(UUID parentId, int offset, int batchSize) {
        if (loadTest) {
            MetadataResponse metadataResponse = new MetadataResponse();
            metadataResponse.setTotal(batchSize);

            List<CommunityResponse> data = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                CommunityResponse communityResponse = new CommunityResponse();
                communityResponse.setId(UUID.randomUUID());
                data.add(communityResponse);
            }

            BaseResponseModelList<CommunityResponse> responseModelList = new BaseResponseModelList<>();
            responseModelList.setData(data);
            responseModelList.setMeta(metadataResponse);

            return responseModelList;
        }

        return cognistreamerClient.fetchCommunities(offset, batchSize);
    }

    @Override
    List<CommunityResponse> filterData(List<CommunityResponse> responseData) {
        List<String> enabledCommunities = cognistreamerProperties.getEnabledCommunities();
        return responseData
            .stream()
            .filter(communityResponse -> enabledCommunities == null || enabledCommunities.contains(communityResponse.getId().toString()))
            .collect(Collectors.toList());
    }

    @Override
    List<Community> convertData(List<CommunityResponse> responseData) {
        return communityMapper.communitiesResponseToEntities(responseData);
    }
}
