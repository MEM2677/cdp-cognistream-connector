package it.keybiz.cdp.innova.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class EvangelistProcessStepDTO {
    private UUID id;
    private Integer position;
    private String description;
    private String iconUrl;
}
