package it.keybiz.cdp.innova.event;

import lombok.Data;

@Data
public class IdeaSubmittedEvent {
    private String name;
    private String surname;
    private String email;
    private String phone;
    private String problem;
    private String expectations;
}
