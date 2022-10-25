package it.keybiz.cdp.innova.service;

import it.keybiz.cdp.innova.domain.Evangelist;
import it.keybiz.cdp.innova.dto.EvangelistSearchDTO;
import it.keybiz.cdp.innova.enums.EvangelistStatus;
import it.keybiz.cdp.innova.event.EvangelistSubscribedEvent;
import it.keybiz.cdp.innova.event.ImageUploadedEvent;
import it.keybiz.cdp.innova.mapper.EvangelistMapper;
import it.keybiz.cdp.innova.repository.EvangelistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EvangelistServiceImpl implements EvangelistService {
    private final ApplicationEventPublisher eventPublisher;

    private final EvangelistRepository evangelistRepository;

    private final EvangelistMapper evangelistMapper;

    @Override
    public Page<Evangelist> findAll(Pageable pageable, EvangelistSearchDTO searchDTO) {
        log.info("Requested all paged Evangelists");
        return evangelistRepository.findAll(EvangelistRepository.Specifications.filter(searchDTO), pageable);
    }

    @Override
    public List<Evangelist> findAllActive() {
        log.info("Requested all active Evangelists");
        return evangelistRepository.findAllByStatus(EvangelistStatus.APPROVED);
    }

    @Override
    public Evangelist findOne(UUID id) {
        log.info("Requested Evangelist with id {}", id);
        return evangelistRepository.findById(id).orElseThrow();
    }

    @Override
    @Transactional
    public void create(Evangelist evangelist, @Nullable String image) {
        log.info("Creating new Evangelist");
        evangelist = evangelistRepository.saveAndFlush(evangelist);
        eventPublisher.publishEvent(new ImageUploadedEvent(evangelist.getId(), image, Evangelist.class));
    }

    @Override
    public void subscribe(Evangelist evangelist) {
        evangelist = evangelistRepository.save(evangelist);
        EvangelistSubscribedEvent event = evangelistMapper.evangelistToSubscribedEvent(evangelist);
        eventPublisher.publishEvent(event);
        log.info("new Evangelist subscribed '{}'", evangelist.getId());
    }

    @Override
    @Transactional
    public void update(UUID id, Evangelist evangelist, @Nullable String image) {
        log.info("Updating Evangelist '{}'", id);
        Evangelist evangelistDB = evangelistRepository.findById(id).orElseThrow();
        evangelistMapper.copyValues(evangelist, evangelistDB);
        evangelistRepository.saveAndFlush(evangelistDB);
        eventPublisher.publishEvent(new ImageUploadedEvent(evangelist.getId(), image, Evangelist.class));
    }

    @Override
    public void delete(UUID id) {
        log.info("Deleting Evangelist '{}'", id);
        evangelistRepository.deleteById(id);
    }
}
