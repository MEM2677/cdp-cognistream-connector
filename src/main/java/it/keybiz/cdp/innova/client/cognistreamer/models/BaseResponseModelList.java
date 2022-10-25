package it.keybiz.cdp.innova.client.cognistreamer.models;

import lombok.Data;

import java.util.List;

@Data
public class BaseResponseModelList<T> {
    private List<T> data;
    private MetadataResponse meta;
}
