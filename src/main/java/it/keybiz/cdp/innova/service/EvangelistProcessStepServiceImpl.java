package it.keybiz.cdp.innova.service;

import it.keybiz.cdp.innova.domain.EvangelistProcessStep;
import it.keybiz.cdp.innova.dto.EvangelistProcessStepSearchDTO;
import it.keybiz.cdp.innova.event.ImageUploadedEvent;
import it.keybiz.cdp.innova.mapper.EvangelistProcessStepMapper;
import it.keybiz.cdp.innova.repository.EvangelistProcessStepRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EvangelistProcessStepServiceImpl implements EvangelistProcessStepService {
    private final ApplicationEventPublisher eventPublisher;

    private final EvangelistProcessStepRepository evangelistProcessStepRepository;

    private final EvangelistProcessStepMapper evangelistProcessStepMapper;

    @Override
    public List<EvangelistProcessStep> findAll(EvangelistProcessStepSearchDTO searchDTO) {
        log.info("Requested all Evangelist Process Steps");
        return evangelistProcessStepRepository.findAll(EvangelistProcessStepRepository.Specifications.filter(searchDTO), Sort.by("position"));
    }

    @Override
    public EvangelistProcessStep findOne(UUID id) {
        log.info("Requested Evangelist Process Step with id {}", id);
        return evangelistProcessStepRepository.findById(id).orElseThrow();
    }

    @Override
    @Transactional
    public void create(EvangelistProcessStep evangelistProcessStep, String icon) {
        log.info("Creating new Evangelist Process Step");
        evangelistProcessStep = evangelistProcessStepRepository.saveAndFlush(evangelistProcessStep);
        eventPublisher.publishEvent(new ImageUploadedEvent(evangelistProcessStep.getId(), icon, EvangelistProcessStep.class));
    }

    @Override
    @Transactional
    public void update(UUID id, EvangelistProcessStep evangelistProcessStep, String icon) {
        log.info("Updating Evangelist Process Step '{}'", id);
        EvangelistProcessStep evangelistProcessStepDB = evangelistProcessStepRepository.findById(id).orElseThrow();
        evangelistProcessStepMapper.copyValues(evangelistProcessStep, evangelistProcessStepDB);
        evangelistProcessStepRepository.saveAndFlush(evangelistProcessStepDB);
        eventPublisher.publishEvent(new ImageUploadedEvent(id, icon, EvangelistProcessStep.class));
    }

    @Override
    @Transactional
    public void sort(List<EvangelistProcessStep> evangelistProcessSteps) {
        log.info("Sorting Evangelist Process Steps");
        evangelistProcessSteps.forEach(step -> {
            EvangelistProcessStep evangelistProcessStepDB = evangelistProcessStepRepository.findById(step.getId()).orElseThrow();
            evangelistProcessStepMapper.copyValues(step, evangelistProcessStepDB);
            evangelistProcessStepRepository.save(evangelistProcessStepDB);
        });
    }

    @Override
    public void delete(UUID id) {
        log.info("Deleting Evangelist Process Step '{}'", id);
        EvangelistProcessStep step = evangelistProcessStepRepository.findById(id).orElseThrow();
        evangelistProcessStepRepository.delete(step);

        log.info("Scaling up positions for deleted step");
        List<EvangelistProcessStep> steps = evangelistProcessStepRepository.findAllByPositionGreaterThan(step.getPosition());
        steps.forEach(greaterStep -> greaterStep.setPosition(greaterStep.getPosition() - 1));
        evangelistProcessStepRepository.saveAll(steps);
    }
}
