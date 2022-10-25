package it.keybiz.cdp.innova.mapper;

import it.keybiz.cdp.innova.domain.OrganizationUnit;
import it.keybiz.cdp.innova.domain.Role;
import it.keybiz.cdp.innova.dto.OrganizationUnitDTO;
import it.keybiz.cdp.innova.dto.OrganizationUnitEditDTO;
import it.keybiz.cdp.innova.dto.RoleDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper
public abstract class OrganizationUnitMapper {
    public abstract OrganizationUnitDTO organizationUnitToDTO(OrganizationUnit organizationUnit);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void copyValues(OrganizationUnit source, @MappingTarget OrganizationUnit target);

    @Mapping(target = "organizationUnit", ignore = true)
    public abstract RoleDTO mapRole(Role role);

    public abstract List<OrganizationUnitDTO> organizationUnitsToDtoList(List<OrganizationUnit> organizationUnits);

    public abstract OrganizationUnit organizationUnitEditDtoToEntity(OrganizationUnitEditDTO organizationUnitEditDTO);
}
