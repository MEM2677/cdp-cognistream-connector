package it.keybiz.cdp.innova.service;

import it.keybiz.cdp.innova.domain.Role;
import it.keybiz.cdp.innova.dto.RoleSearchDTO;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Validated
public interface RoleService {
    List<Role> findAll(@NotNull RoleSearchDTO searchDTO);

    Role findOne(@NotNull UUID id);

    void create(@NotNull Role role);

    void update(@NotNull UUID id, @NotNull Role role);

    void delete(@NotNull UUID id);
}
