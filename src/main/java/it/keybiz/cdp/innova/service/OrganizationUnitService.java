package it.keybiz.cdp.innova.service;

import it.keybiz.cdp.innova.domain.OrganizationUnit;
import it.keybiz.cdp.innova.dto.OrganizationUnitSearchDTO;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Validated
public interface OrganizationUnitService {
    List<OrganizationUnit> findAll(@NotNull OrganizationUnitSearchDTO searchDTO);

    OrganizationUnit findOne(@NotNull UUID id);

    void create(@NotNull OrganizationUnit organizationUnit);

    void update(@NotNull UUID id, @NotNull OrganizationUnit organizationUnit);

    void delete(@NotNull UUID id);
}
