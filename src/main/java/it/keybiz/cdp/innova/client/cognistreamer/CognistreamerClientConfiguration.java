package it.keybiz.cdp.innova.client.cognistreamer;

import feign.RequestInterceptor;
import it.keybiz.cdp.innova.client.oauth2.OAuth2DeltaFeignRequestInterceptor;
import it.keybiz.cdp.innova.config.CognistreamerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;

public class CognistreamerClientConfiguration {
    @Bean
    public ClientCredentialsResourceDetails clientCredentialsResourceDetails(CognistreamerProperties properties) {
        ClientCredentialsResourceDetails clientCredentialsResourceDetails = new ClientCredentialsResourceDetails();
        clientCredentialsResourceDetails.setAccessTokenUri(properties.getApi().getAccessTokenUri());
        clientCredentialsResourceDetails.setClientId(properties.getApi().getClientId());
        clientCredentialsResourceDetails.setClientSecret(properties.getApi().getClientSecret());
        return clientCredentialsResourceDetails;
    }

    @Bean
    public RequestInterceptor oauth2FeignRequestInterceptor(ClientCredentialsResourceDetails clientCredentialsResourceDetails) {
        return new OAuth2DeltaFeignRequestInterceptor(new DefaultOAuth2ClientContext(), clientCredentialsResourceDetails);
    }
}
