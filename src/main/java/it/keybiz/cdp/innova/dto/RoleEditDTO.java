package it.keybiz.cdp.innova.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class RoleEditDTO {
    @NotBlank
    private String name;

    @NotNull
    private UUID organizationUnitId;
}
