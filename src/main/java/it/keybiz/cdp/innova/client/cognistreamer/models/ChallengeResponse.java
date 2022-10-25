package it.keybiz.cdp.innova.client.cognistreamer.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ChallengeResponse extends BaseResponseModel {
    private String programId;
    private String stageId;
    private String descriptionHtml;
    private int contributionCount;
    private int type;
}
