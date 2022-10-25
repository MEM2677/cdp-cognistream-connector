package it.keybiz.cdp.innova.service;

import it.keybiz.cdp.innova.domain.Idea;
import it.keybiz.cdp.innova.event.IdeaSubmittedEvent;
import it.keybiz.cdp.innova.repository.IdeaRepository;
import it.keybiz.cdp.innova.mapper.IdeaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class IdeaServiceImpl implements IdeaService {
    private final ApplicationEventPublisher eventPublisher;

    private final IdeaRepository ideaRepository;

    private final IdeaMapper ideaMapper;

    @Override
    public void submit(Idea idea) {
        idea = ideaRepository.save(idea);

        IdeaSubmittedEvent ideaSubmittedEvent = ideaMapper.ideaToIdeaSumbittedEvent(idea);
        eventPublisher.publishEvent(ideaSubmittedEvent);

        log.info("New Idea submitted '{}'", idea.getId());
    }
}
