package it.keybiz.cdp.innova.client.oauth2;

import org.springframework.cloud.openfeign.security.OAuth2FeignRequestInterceptor;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;

public class OAuth2DeltaFeignRequestInterceptor extends OAuth2FeignRequestInterceptor {
    public OAuth2DeltaFeignRequestInterceptor(OAuth2ClientContext oAuth2ClientContext, OAuth2ProtectedResourceDetails resource) {
        super(oAuth2ClientContext, resource);
        setAccessTokenProvider(new ClientCredentialsOAuth2DeltaAccessTokenProvider());
    }
}
