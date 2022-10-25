package it.keybiz.cdp.innova.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class OrganizationUnitEditDTO {
    @NotBlank
    private String name;
}
