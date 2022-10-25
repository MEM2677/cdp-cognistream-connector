package it.keybiz.cdp.innova.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ServiceDTO {
    private UUID id;
    private String title;
    private String description;
    private String link;
    private Integer position;
}
