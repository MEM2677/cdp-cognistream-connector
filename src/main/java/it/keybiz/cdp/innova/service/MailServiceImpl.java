package it.keybiz.cdp.innova.service;

import it.keybiz.cdp.innova.config.ApplicationProperties;
import it.keybiz.cdp.innova.config.Constants;
import it.keybiz.cdp.innova.config.NotificationConfig;
import it.keybiz.cdp.innova.config.NotificationsProperties;
import it.keybiz.cdp.innova.event.EvangelistSubscribedEvent;
import it.keybiz.cdp.innova.event.IdeaSubmittedEvent;
import it.keybiz.cdp.innova.exception.NotificationSendException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.file.Files;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private final ApplicationProperties applicationProperties;

    private final JavaMailSender mailSender;

    private final String EMAIL_TEMPLATES_PATH = "templates/email/";

    private final long RETRY_DELAY = 5000; // 5 secondi

    @Async
    @EventListener(EvangelistSubscribedEvent.class)
    @Retryable(value = NotificationSendException.class, backoff = @Backoff(RETRY_DELAY))
    public void sendSubscriptionEmail(EvangelistSubscribedEvent event) throws IOException {
        NotificationsProperties notificationsProperties = applicationProperties.getNotifications();
        NotificationConfig notificationConfig = notificationsProperties.getConfigs().get(Constants.NotificationConfigKeys.EVANGELIST_SUBSCRIPTION);
        String htmlTemplate = Files.readString(new ClassPathResource(EMAIL_TEMPLATES_PATH + "subscription.html").getFile().toPath());
        String emailContent = fillSubscriptionEmail(event, notificationConfig, htmlTemplate);
        send(notificationConfig, emailContent);
        log.info("Email for evangelist subscription sent to {}", notificationsProperties.getTo());
    }

    @Async
    @EventListener(IdeaSubmittedEvent.class)
    @Retryable(value = NotificationSendException.class, backoff = @Backoff(RETRY_DELAY))
    public void sendIdeaSubmittedEmail(IdeaSubmittedEvent event) throws IOException {
        NotificationsProperties notificationsProperties = applicationProperties.getNotifications();
        NotificationConfig notificationConfig = notificationsProperties.getConfigs().get(Constants.NotificationConfigKeys.IDEA_SUBMISSION);
        String htmlTemplate = Files.readString(new ClassPathResource(EMAIL_TEMPLATES_PATH + "idea_submitted.html").getFile().toPath());
        String emailContent = fillIdeaSubmittedEmail(event, notificationConfig, htmlTemplate);
        send(notificationConfig, emailContent);
        log.info("Email for idea submission sent to {}", notificationsProperties.getTo());
    }

    /**
     * Invia l'email con le configurazioni passate come argomento e con il contenuto passato come argomento
     *
     * @param notificationConfig configurazione specifica della notifica
     * @param htmlContent        contenuto HTML dell'email
     */
    private void send(NotificationConfig notificationConfig, String htmlContent) {
        try {
            NotificationsProperties notificationsProperties = applicationProperties.getNotifications();

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            messageHelper.setFrom(notificationsProperties.getFrom());
            messageHelper.setSubject(notificationConfig.getSubject());
            messageHelper.setText(htmlContent, true);

            // ciclo in modo che ogni destinatario non vede a chi altro viene inviata la mail
            for (String destinatary : notificationsProperties.getTo()) {
                messageHelper.setTo(destinatary);
                mailSender.send(mimeMessage);
            }
        } catch (MessagingException e) {
            log.error(e.getMessage(), e);
            throw new NotificationSendException(e.getMessage());
        }
    }

    /**
     * Fa la replace dei placeholder dell'email con i dati esatti dell'evangelist
     *
     * @param event              evento contenente le informazioni dell'Evangelist
     * @param notificationConfig configurazione relativa alla specifica notifica
     * @param htmlContent        contenuto HTML della mail
     * @return contentuto della mail con placeholder sostituiti
     */
    private String fillSubscriptionEmail(EvangelistSubscribedEvent event, NotificationConfig notificationConfig, String htmlContent) {
        return htmlContent
            .replaceAll("£link", applicationProperties.getFrontendUrl() + notificationConfig.getFrontendPath()) // todo magari filtrare lo stato sulla pagina FE
            .replaceAll("£title", notificationConfig.getSubject())
            .replaceAll("£name", event.getName())
            .replaceAll("£surname", event.getSurname())
            .replaceAll("£email", event.getEmail())
            .replaceAll("£phone", event.getPhone())
            .replaceAll("£reason", event.getReason());
    }

    /**
     * Fa la replace dei placeholder dell'email con i dati esatti dell'idea inviata
     *
     * @param event              evento contenente le informazioni dell'Evangelist
     * @param notificationConfig configurazione relativa alla specifica notifica
     * @param htmlContent        contenuto HTML della mail
     * @return contentuto della mail con placeholder sostituiti
     */
    private String fillIdeaSubmittedEmail(IdeaSubmittedEvent event, NotificationConfig notificationConfig, String htmlContent) {
        return htmlContent
            .replaceAll("£title", notificationConfig.getSubject())
            .replaceAll("£name", event.getName())
            .replaceAll("£surname", event.getSurname())
            .replaceAll("£email", event.getEmail())
            .replaceAll("£phone", event.getPhone())
            .replaceAll("£problem", event.getProblem())
            .replaceAll("£expectations", event.getExpectations());
    }
}
