package it.keybiz.cdp.innova.service;

import it.keybiz.cdp.innova.domain.OrganizationUnit;
import it.keybiz.cdp.innova.dto.OrganizationUnitSearchDTO;
import it.keybiz.cdp.innova.mapper.OrganizationUnitMapper;
import it.keybiz.cdp.innova.repository.OrganizationUnitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrganizationUnitServiceImpl implements OrganizationUnitService {
    private final OrganizationUnitRepository organizationUnitRepository;

    private final OrganizationUnitMapper organizationUnitMapper;

    @Override
    public List<OrganizationUnit> findAll(OrganizationUnitSearchDTO searchDTO) {
        log.info("Requested all Organization Units");
        return organizationUnitRepository.findAll(OrganizationUnitRepository.Specifications.filter(searchDTO));
    }

    @Override
    public OrganizationUnit findOne(UUID id) {
        log.info("Requested Organization Unit with id {}", id);
        return organizationUnitRepository.findById(id).orElseThrow();
    }

    @Override
    public void create(OrganizationUnit organizationUnit) {
        log.info("Creating new Organization Unit");
        organizationUnitRepository.save(organizationUnit);
    }

    @Override
    public void update(UUID id, OrganizationUnit organizationUnit) {
        log.info("Updating Organization Unit '{}'", id);
        OrganizationUnit organizationUnitDB = organizationUnitRepository.findById(id).orElseThrow();
        organizationUnitMapper.copyValues(organizationUnit, organizationUnitDB);
        organizationUnitRepository.save(organizationUnitDB);
    }

    @Override
    public void delete(UUID id) {
        log.info("Deleting Organization Unit '{}'", id);
        organizationUnitRepository.deleteById(id);
    }
}
