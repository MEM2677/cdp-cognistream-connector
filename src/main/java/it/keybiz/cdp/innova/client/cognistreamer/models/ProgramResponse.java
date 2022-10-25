package it.keybiz.cdp.innova.client.cognistreamer.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProgramResponse extends BaseResponseModel {
    private String communityId;
    private String descriptionHtml;
    private String url;
    private boolean isPublished;
    private Date startDateUtc;
    private Date dateCreatedUtc;
}
