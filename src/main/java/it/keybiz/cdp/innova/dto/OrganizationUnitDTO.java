package it.keybiz.cdp.innova.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class OrganizationUnitDTO {
    private UUID id;
    private String name;
    private List<RoleDTO> roles;
}
