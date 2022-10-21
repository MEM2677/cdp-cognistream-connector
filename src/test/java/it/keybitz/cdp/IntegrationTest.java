package it.keybitz.cdp;

import it.keybitz.cdp.CdpinnovaApp;
import it.keybitz.cdp.config.TestSecurityConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { CdpinnovaApp.class, TestSecurityConfiguration.class })
public @interface IntegrationTest {
}
