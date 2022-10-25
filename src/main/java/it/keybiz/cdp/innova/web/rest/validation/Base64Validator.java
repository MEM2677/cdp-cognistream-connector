package it.keybiz.cdp.innova.web.rest.validation;

import javax.annotation.Nullable;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class Base64Validator implements ConstraintValidator<Base64, String> {
    @Override
    public boolean isValid(@Nullable String base64, ConstraintValidatorContext validatorContext) {
        try {
            // nullable, se si vuole il campo NotNull aggiungere altra annotation nel modello
            if (base64 != null && !base64.isBlank()) {
                java.util.Base64.getDecoder().decode(base64);
            }
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
