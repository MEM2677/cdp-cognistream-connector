package it.keybiz.cdp.innova.dto;

import lombok.Data;

@Data
public class ChallengeListDTO {
    private String id;
    private String title;
    private String description;
    private String communityTitle;
    private String programTitle;
    private String imageUrl;
    private int totalContributionCount;
}
