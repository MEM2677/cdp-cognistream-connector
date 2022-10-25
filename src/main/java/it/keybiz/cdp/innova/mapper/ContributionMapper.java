package it.keybiz.cdp.innova.mapper;

import it.keybiz.cdp.innova.client.cognistreamer.models.ContributionResponse;
import it.keybiz.cdp.innova.domain.Contribution;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public abstract class ContributionMapper {
    public abstract List<Contribution> contributionsResponseToEntities(List<ContributionResponse> contributionsResponse);
}
