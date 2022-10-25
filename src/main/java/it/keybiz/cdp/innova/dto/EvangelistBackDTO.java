package it.keybiz.cdp.innova.dto;

import it.keybiz.cdp.innova.enums.EvangelistStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class EvangelistBackDTO {
    private UUID id;
    private String name;
    private String surname;
    private String imageUrl;
    private String email;
    private String phone;
    private String reason;
    private EvangelistStatus status;
    private RoleDTO role;
}
