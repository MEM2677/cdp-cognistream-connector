package it.keybiz.cdp.innova.web.rest;

import it.keybiz.cdp.innova.config.ApplicationProperties;
import it.keybiz.cdp.innova.config.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Path;

@Slf4j
@ApiIgnore
@RestController
@RequestMapping(Constants.IMAGES_ENDPOINT_BASE_PATH)
@RequiredArgsConstructor
public class ImagesController {
    private final ApplicationProperties applicationProperties;

    @RequestMapping("/**")
    public ResponseEntity<InputStreamResource> fetchImage(HttpServletRequest request) {
        String requestPath = new AntPathMatcher().extractPathWithinPattern(
            (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE),
            (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)
        );
        Path imagePath = Path.of(applicationProperties.getImagesPath(), requestPath);
        log.debug("Requested image '{}'", imagePath.toAbsolutePath());

        if (!imagePath.toFile().exists()) {
            log.warn("Image '{}' does not exists!", imagePath.toAbsolutePath());
            return ResponseEntity.notFound().build();
        }

        try {
            return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(new InputStreamResource(new ByteArrayInputStream(FileUtils.readFileToByteArray(imagePath.toFile()))));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("Error while reading image");
        }
    }
}
