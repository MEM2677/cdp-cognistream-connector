package it.keybiz.cdp.innova.service;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.nio.file.Path;

@Validated
public interface FileStorageService {
    String getImageUrl(@NotNull Path filePath);
}
