package it.keybiz.cdp.innova.config;

/**
 * Application constants.
 */
public interface Constants {
    // Regex for acceptable logins
    String LOGIN_REGEX = "^(?>[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*)|(?>[_.@A-Za-z0-9-]+)$";

    String SYSTEM = "system";

    String DEFAULT_LANGUAGE = "it";

    String IMAGES_ENDPOINT_BASE_PATH = "/api/images";

    interface NotificationConfigKeys {
        String EVANGELIST_SUBSCRIPTION = "evangelist-subscription";
        String IDEA_SUBMISSION = "idea-submission";
    }
}
