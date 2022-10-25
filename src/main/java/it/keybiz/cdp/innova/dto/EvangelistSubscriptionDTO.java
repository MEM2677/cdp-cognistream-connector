package it.keybiz.cdp.innova.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class EvangelistSubscriptionDTO {
    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String phone;

    @Size(max = 1000)
    @NotBlank
    private String reason;
}
