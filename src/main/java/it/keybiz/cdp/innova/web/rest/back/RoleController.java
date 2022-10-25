package it.keybiz.cdp.innova.web.rest.back;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.keybiz.cdp.innova.domain.Role;
import it.keybiz.cdp.innova.dto.RoleDTO;
import it.keybiz.cdp.innova.dto.RoleEditDTO;
import it.keybiz.cdp.innova.dto.RoleSearchDTO;
import it.keybiz.cdp.innova.mapper.RoleMapper;
import it.keybiz.cdp.innova.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
@Api(tags = "Role")
@Tag(name = "Role")
public class RoleController {
    private final RoleService roleService;

    private final RoleMapper roleMapper;

    @GetMapping
    public List<RoleDTO> findAll(RoleSearchDTO searchDTO) {
        List<Role> rolees = roleService.findAll(searchDTO);
        return roleMapper.rolesToDtoList(rolees);
    }

    @GetMapping("/{id}")
    public RoleDTO findOne(@PathVariable @NotNull UUID id) {
        Role role = roleService.findOne(id);
        return roleMapper.roleToDto(role);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid RoleEditDTO roleEditDTO) {
        Role role = roleMapper.roleEditDtoToEntity(roleEditDTO);
        roleService.create(role);
    }

    @PatchMapping("/{id}")
    public void update(@PathVariable @NotNull UUID id, @RequestBody @Valid RoleEditDTO roleEditDTO) {
        Role role = roleMapper.roleEditDtoToEntity(roleEditDTO);
        roleService.update(id, role);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable @NotNull UUID id) {
        roleService.delete(id);
    }
}
