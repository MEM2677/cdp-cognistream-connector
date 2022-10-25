package it.keybiz.cdp.innova.web.rest.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = Base64Validator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Base64 {
    String message() default "Il valore deve essere in formato base64";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
