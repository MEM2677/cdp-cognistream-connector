package it.keybiz.cdp.innova.event;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ImageUploadedEvent {
    private UUID id;
    private String data;
    private Class<?> type;
}
