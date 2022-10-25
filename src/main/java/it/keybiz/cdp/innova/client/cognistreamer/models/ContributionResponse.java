package it.keybiz.cdp.innova.client.cognistreamer.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class ContributionResponse extends BaseResponseModel {
    private Date dateCreatedUtc;
    private String description;
    private CreatorResponse creator;
    private String uri;
    private List<Map<String, String>> fields;
    private Integer upvoteCount;
    private Integer downvoteCount;
    private Integer commentCount;
}
