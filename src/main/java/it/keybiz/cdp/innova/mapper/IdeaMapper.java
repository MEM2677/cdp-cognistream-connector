package it.keybiz.cdp.innova.mapper;

import it.keybiz.cdp.innova.domain.Idea;
import it.keybiz.cdp.innova.event.IdeaSubmittedEvent;
import it.keybiz.cdp.innova.dto.IdeaSumbissionDTO;
import org.mapstruct.Mapper;

@Mapper
public abstract class IdeaMapper {
    public abstract Idea ideaSubmissionDTOtoEntity(IdeaSumbissionDTO ideaSumbissionDTO);

    public abstract IdeaSubmittedEvent ideaToIdeaSumbittedEvent(Idea idea);
}
