package it.keybiz.cdp.innova.event;

import lombok.Data;

@Data
public class EvangelistSubscribedEvent {
    private String name;
    private String surname;
    private String email;
    private String phone;
    private String reason;
}
