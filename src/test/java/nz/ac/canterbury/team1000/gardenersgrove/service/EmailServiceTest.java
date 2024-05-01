package nz.ac.canterbury.team1000.gardenersgrove.service;

import java.util.Objects;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Import(EmailService.class)
public class EmailServiceTest {
    @MockBean
    private JavaMailSender emailSenderMock;
    private EmailService emailService;

    @BeforeEach
    public void setup() {
        emailSenderMock = Mockito.mock(JavaMailSender.class);
        emailService = new EmailService(emailSenderMock);
    }

    /**
     * Test that the email service calls the JavaMailSender to send an email with correct parameters.
     * Written with help of GPT
     */

    @Test
    public void testSendSimpleMessage() {
        String to = "recipient@example.com";
        String subject = "Test Subject";
        String text = "Test Message";

        // Call the method under test
        emailService.sendSimpleMessage(to, subject, text);

        // Verify that send method of emailSenderMock is called once
        verify(emailSenderMock, times(1)).send(Mockito.any(SimpleMailMessage.class));

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(emailSenderMock).send(captor.capture());

        SimpleMailMessage message = captor.getValue();

        // Verify that the email message has the correct parameters
        Assertions.assertEquals(to, Objects.requireNonNull(message.getTo())[0]);
        Assertions.assertEquals(subject, message.getSubject());
        Assertions.assertEquals(text, message.getText());
    }
}
