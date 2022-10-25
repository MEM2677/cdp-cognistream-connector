package it.keybiz.cdp.innova.client.cognistreamer.models;

import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
public class BaseResponseModel {
    private UUID id;
    private String title;
    private Map<String, Map<String, String>> image;
}
