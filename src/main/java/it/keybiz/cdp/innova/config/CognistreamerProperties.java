package it.keybiz.cdp.innova.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "cognistreamer")
public class CognistreamerProperties {
    private CognistreamerApiProperties api;
    private List<String> enabledCommunities;
    private int batchSize;
}
