package it.keybiz.cdp.innova.mapper;

import it.keybiz.cdp.innova.client.cognistreamer.models.ProgramResponse;
import it.keybiz.cdp.innova.domain.Program;
import it.keybiz.cdp.innova.dto.TypologicDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public abstract class ProgramMapper {
    public abstract List<Program> programsResponseToEntities(List<ProgramResponse> programsResponse);

    public abstract List<TypologicDTO> programsToTypologicDTO(List<Program> programs);

    @Mapping(source = "title", target = "name")
    public abstract TypologicDTO programToTypologicDTO(Program program);
}
