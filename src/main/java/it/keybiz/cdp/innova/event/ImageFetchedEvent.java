package it.keybiz.cdp.innova.event;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.InputStream;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ImageFetchedEvent {
    private UUID entityId;
    private Class<?> type;
    private String imageId;
    private InputStream data;
}
