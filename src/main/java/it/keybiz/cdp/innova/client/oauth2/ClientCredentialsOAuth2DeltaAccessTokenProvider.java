package it.keybiz.cdp.innova.client.oauth2;

import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.ResponseExtractor;

import java.util.List;

public class ClientCredentialsOAuth2DeltaAccessTokenProvider extends ClientCredentialsAccessTokenProvider {
    @Override
    protected ResponseExtractor<OAuth2AccessToken> getResponseExtractor() {
        return new HttpMessageConverterExtractor<OAuth2AccessToken>(OAuth2DeltaAccessToken.class, List.of(new OAuth2DeltaAccessTokenHttpMessageConverter()));
    }
}
