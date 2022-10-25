package it.keybiz.cdp.innova.mapper;

import it.keybiz.cdp.innova.client.cognistreamer.models.CommunityResponse;
import it.keybiz.cdp.innova.domain.Community;
import it.keybiz.cdp.innova.dto.TypologicDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public abstract class CommunityMapper {
    public abstract List<Community> communitiesResponseToEntities(List<CommunityResponse> communitiesResponse);

    public abstract List<TypologicDTO> communitiesToTypologicDTO(List<Community> communities);
}
