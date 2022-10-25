package it.keybiz.cdp.innova.config;

import lombok.Data;

@Data
public class CognistreamerApiProperties {
    private String baseUrl;
    private String accessTokenUri;
    private String clientId;
    private String clientSecret;
}
