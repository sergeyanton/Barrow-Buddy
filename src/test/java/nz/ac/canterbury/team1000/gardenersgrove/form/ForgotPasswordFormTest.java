package nz.ac.canterbury.team1000.gardenersgrove.form;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.validation.BindingResult;
public class ForgotPasswordFormTest {
    ForgotPasswordForm forgotPasswordForm = new ForgotPasswordForm();

    @Mock
    BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        forgotPasswordForm.setEmail("john@doe.com");

        bindingResult = Mockito.mock(BindingResult.class);
        Mockito.when(bindingResult.hasErrors()).thenReturn(false);
        Mockito.doAnswer(invocation -> {
            Mockito.when(bindingResult.hasErrors()).thenReturn(true);
            return null;
        }).when(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_WithBlankEmail_AddsError() {
        forgotPasswordForm.setEmail("");
        ForgotPasswordForm.validate(forgotPasswordForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_WithInvalidEmail_AddsError() {
        forgotPasswordForm.setEmail("john@doe");
        ForgotPasswordForm.validate(forgotPasswordForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

}
