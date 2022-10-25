package it.keybiz.cdp.innova.client.cognistreamer.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CommunityResponse extends BaseResponseModel {
    private String name;
    private String descriptionHtml;
    private String url;
}
