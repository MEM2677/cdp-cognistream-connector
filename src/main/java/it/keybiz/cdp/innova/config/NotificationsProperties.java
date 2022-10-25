package it.keybiz.cdp.innova.config;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class NotificationsProperties {
    private String from;
    private List<String> to;
    private Map<String, NotificationConfig> configs;
}
