package it.keybiz.cdp.innova.dto;

import it.keybiz.cdp.innova.enums.EvangelistStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class EvangelistSearchDTO {
    private String name;
    private String surname;
    private EvangelistStatus status;
    private UUID roleId;
    private UUID organizationId;
}
