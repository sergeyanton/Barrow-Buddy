package nz.ac.canterbury.team1000.gardenersgrove.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

/**
 * Service class for sending emails.
 * This class provides methods to send simple text emails as well as HTML formatted emails using the JavaMailSender interface.
 */
@Component
public class EmailService {

    private final JavaMailSender emailSender;

    /**
     * Constructs an EmailService with the provided JavaMailSender.
     *
     * @param emailSender the JavaMailSender to use for sending emails
     */
    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    /**
     * Sends a simple text email.
     *
     * @param to      the recipient's email address
     * @param subject the subject of the email
     * @param text    the plain text of the email
     */
    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@team1000.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    /**
     * Sends an HTML formatted email.
     *
     * @param to          the recipient's email address
     * @param subject     the subject of the email
     * @param htmlContent the HTML content of the email
     * @throws            MessagingException if an error occurs when creating or sending the email
     */
    public void sendHtmlMessage(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom("noreply@team1000.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true); // true indicates this is HTML content

        emailSender.send(message);
    }
}