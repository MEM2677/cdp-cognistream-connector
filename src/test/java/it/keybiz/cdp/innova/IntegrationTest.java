package it.keybiz.cdp.innova;

import it.keybiz.cdp.innova.config.TestSecurityConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { CdpinnovaApp.class, TestSecurityConfiguration.class })
public @interface IntegrationTest {
}
