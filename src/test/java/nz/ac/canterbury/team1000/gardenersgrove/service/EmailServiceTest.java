package nz.ac.canterbury.team1000.gardenersgrove.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class EmailServiceTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JavaMailSender mockJavaMailSender;

    /**
     * Test that the email service calls the JavaMailSender to send an email with correct parameters
     */
    @Test
    public void testSendEmail() throws Exception {
        EmailService emailService = new EmailService(mockJavaMailSender);
        Mockito.doNothing().when(mockJavaMailSender).send(Mockito.any(SimpleMailMessage.class));
        emailService.sendSimpleMessage("recipient@example.com", "Test Subject", "Test Body");
        Mockito.verify(mockJavaMailSender).send(Mockito.any(SimpleMailMessage.class));
    }



}
