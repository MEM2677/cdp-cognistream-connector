package it.keybiz.cdp.innova.service;

import it.keybiz.cdp.innova.domain.Servizio;
import it.keybiz.cdp.innova.domain.Servizio_;
import it.keybiz.cdp.innova.dto.ServiceSearchDTO;
import it.keybiz.cdp.innova.mapper.ServiceMapper;
import it.keybiz.cdp.innova.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService {
    private final ServiceRepository serviceRepository;

    private final ServiceMapper serviceMapper;

    @Override
    public List<Servizio> findAll(ServiceSearchDTO searchDTO) {
        log.info("Requested all Services");
        return serviceRepository.findAll(ServiceRepository.Specifications.filter(searchDTO), Sort.by(Servizio_.POSITION));
    }

    @Override
    public Servizio findOne(UUID id) {
        log.info("Requested Service with id {}", id);
        return serviceRepository.findById(id).orElseThrow();
    }

    @Override
    public void create(Servizio service) {
        log.info("Creating new Service");
        serviceRepository.save(service);
    }

    @Override
    public void update(UUID id, Servizio service) {
        log.info("Updating Service '{}'", id);
        Servizio servizioDB = serviceRepository.findById(id).orElseThrow();
        serviceMapper.copyValues(service, servizioDB);
        serviceRepository.save(servizioDB);
    }

    @Override
    @Transactional
    public void sort(List<Servizio> services) {
        log.info("Sorting Services");
        services.forEach(step -> {
            Servizio serviceDB = serviceRepository.findById(step.getId()).orElseThrow();
            serviceMapper.copyValues(step, serviceDB);
            serviceRepository.save(serviceDB);
        });
    }

    @Override
    public void delete(UUID id) {
        log.info("Deleting Service '{}'", id);
        Servizio service = serviceRepository.findById(id).orElseThrow();
        serviceRepository.delete(service);

        log.info("Scaling up positions for deleted service");
        List<Servizio> services = serviceRepository.findAllByPositionGreaterThan(service.getPosition());
        services.forEach(greaterService -> greaterService.setPosition(greaterService.getPosition() - 1));
        serviceRepository.saveAll(services);
    }
}
