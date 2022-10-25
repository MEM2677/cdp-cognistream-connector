package it.keybiz.cdp.innova.service;

import it.keybiz.cdp.innova.domain.Role;
import it.keybiz.cdp.innova.dto.RoleSearchDTO;
import it.keybiz.cdp.innova.mapper.RoleMapper;
import it.keybiz.cdp.innova.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    private final RoleMapper roleMapper;

    @Override
    public List<Role> findAll(RoleSearchDTO searchDTO) {
        log.info("Requested all Roles");
        return roleRepository.findAll(RoleRepository.Specifications.filter(searchDTO));
    }

    @Override
    public Role findOne(UUID id) {
        log.info("Requested Role with id {}", id);
        return roleRepository.findById(id).orElseThrow();
    }

    @Override
    public void create(Role role) {
        log.info("Creating new Role");
        roleRepository.save(role);
    }

    @Override
    public void update(UUID id, Role role) {
        log.info("Updating Role '{}'", id);
        Role roleDB = roleRepository.findById(id).orElseThrow();
        roleMapper.copyValues(role, roleDB);
        roleRepository.save(roleDB);
    }

    @Override
    public void delete(UUID id) {
        log.info("Deleting Role '{}'", id);
        roleRepository.deleteById(id);
    }
}
