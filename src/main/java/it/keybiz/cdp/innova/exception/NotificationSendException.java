package it.keybiz.cdp.innova.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationSendException extends RuntimeException {
    private String message;
}
