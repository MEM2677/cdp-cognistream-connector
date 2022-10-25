package it.keybiz.cdp.innova.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ServiceEditDTO {
    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotBlank
    private String link;

    @NotNull
    @Min(1)
    private Integer position;
}
