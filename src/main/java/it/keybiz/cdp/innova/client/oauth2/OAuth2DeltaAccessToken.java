package it.keybiz.cdp.innova.client.oauth2;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import java.time.Duration;
import java.time.Instant;

public class OAuth2DeltaAccessToken extends DefaultOAuth2AccessToken {
    @SuppressWarnings("FieldCanBeLocal")
    private final Integer TOKEN_EXPIRATION_DELTA = 2;

    public OAuth2DeltaAccessToken(OAuth2AccessToken accessToken) {
        super(accessToken);
    }

    @Override
    public boolean isExpired() {
        return getExpiration() != null && getExpiration().toInstant().minus(Duration.ofMinutes(TOKEN_EXPIRATION_DELTA)).isBefore(Instant.now());
    }
}
