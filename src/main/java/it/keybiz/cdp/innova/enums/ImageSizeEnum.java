package it.keybiz.cdp.innova.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ImageSizeEnum {
    SMALL("small"),
    LARGE("large");

    private final String size;
}
