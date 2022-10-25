package it.keybiz.cdp.innova.mapper;

import it.keybiz.cdp.innova.domain.OrganizationUnit;
import it.keybiz.cdp.innova.domain.Role;
import it.keybiz.cdp.innova.dto.OrganizationUnitDTO;
import it.keybiz.cdp.innova.dto.RoleDTO;
import it.keybiz.cdp.innova.dto.RoleEditDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper
public abstract class RoleMapper {
    public abstract RoleDTO roleToDto(Role role);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void copyValues(Role source, @MappingTarget Role target);

    public abstract List<RoleDTO> rolesToDtoList(List<Role> roles);

    @Mapping(target = "roles", ignore = true)
    public abstract OrganizationUnitDTO mapOrganizationUnit(OrganizationUnit organizationUnit);

    @Mapping(source = "organizationUnitId", target = "organizationUnit.id")
    public abstract Role roleEditDtoToEntity(RoleEditDTO roleEditDTO);
}
