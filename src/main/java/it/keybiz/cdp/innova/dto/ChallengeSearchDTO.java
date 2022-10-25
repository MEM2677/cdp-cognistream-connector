package it.keybiz.cdp.innova.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ChallengeSearchDTO {
    private UUID communityId;
    private UUID programId;
}
