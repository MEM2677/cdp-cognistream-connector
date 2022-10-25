package it.keybiz.cdp.innova.dto;

import lombok.Data;

import java.util.List;

@Data
public class ChallengeDTO {
    private String id;
    private String title;
    private String description;
    private String communityTitle;
    private String programTitle;
    private String imageUrl;
    private int totalContributionCount;
    private List<ContributionDTO> contributions;
}
