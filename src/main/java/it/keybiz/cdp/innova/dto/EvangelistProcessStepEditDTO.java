package it.keybiz.cdp.innova.dto;

import it.keybiz.cdp.innova.web.rest.validation.Base64;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class EvangelistProcessStepEditDTO {
    @NotNull
    @Min(1)
    private Integer position;

    @NotBlank
    private String description;

    @NotBlank
    @Base64(message = "L'icona deve essere in formato base64")
    private String icon;
}
