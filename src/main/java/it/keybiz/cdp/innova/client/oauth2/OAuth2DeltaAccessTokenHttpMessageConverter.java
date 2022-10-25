package it.keybiz.cdp.innova.client.oauth2;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.lang.reflect.Type;

public class OAuth2DeltaAccessTokenHttpMessageConverter extends MappingJackson2HttpMessageConverter {
    @Nonnull
    @Override
    public Object read(@Nonnull Type type, Class<?> contextClass, @Nonnull HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return new OAuth2DeltaAccessToken((OAuth2AccessToken) super.read(type, contextClass, inputMessage));
    }

    @Override
    protected boolean supports(@Nonnull Class<?> clazz) {
        return OAuth2DeltaAccessToken.class.equals(clazz);
    }
}
