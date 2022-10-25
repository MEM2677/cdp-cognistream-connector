package it.keybiz.cdp.innova.dto;

import it.keybiz.cdp.innova.enums.EvangelistStatus;
import it.keybiz.cdp.innova.web.rest.validation.Base64;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class EvangelistEditDTO {
    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String phone;

    @Base64(message = "L'immagine deve essere in formato base64")
    private String image;

    @NotNull
    private EvangelistStatus status;

    @NotNull
    private UUID roleId;
}
