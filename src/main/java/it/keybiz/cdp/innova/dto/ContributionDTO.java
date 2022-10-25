package it.keybiz.cdp.innova.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ContributionDTO {
    private String id;
    private String title;
    private String description;
    private String imageUrl;
    private Date dateCreated;
    private Integer upvoteCount;
    private Integer commentCount;
}
