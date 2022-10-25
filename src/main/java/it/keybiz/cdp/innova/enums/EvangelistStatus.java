package it.keybiz.cdp.innova.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EvangelistStatus {
    APPROVED("Approvato"),
    TO_APPROVE("Da Approvare"),
    REJECTED("Rifiutato");

    private final String description;
}
