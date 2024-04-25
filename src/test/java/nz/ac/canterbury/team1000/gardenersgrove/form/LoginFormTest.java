package nz.ac.canterbury.team1000.gardenersgrove.form;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.validation.BindingResult;

public class LoginFormTest {

    final LoginForm loginForm = new LoginForm();

    @Mock
    BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        loginForm.setEmail("jane@doe.com");
        loginForm.setPassword("Password123!");

        bindingResult = Mockito.mock(BindingResult.class);
        Mockito.when(bindingResult.hasErrors()).thenReturn(false);
        Mockito.doAnswer(invocation -> {
            Mockito.when(bindingResult.hasErrors()).thenReturn(true);
            return null;
        }).when(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_BlankEmail_AddsError() {
        loginForm.setEmail("");
        LoginForm.validate(loginForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_InvalidEmail_AddsError() {
        loginForm.setEmail("jane@doe");
        LoginForm.validate(loginForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_InvalidEmail2_AddsError() {
        loginForm.setEmail("abc123!");
        LoginForm.validate(loginForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_InvalidEmail3_AddsError() {
        loginForm.setEmail("jane@");
        LoginForm.validate(loginForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_InvalidEmail4_AddsError() {
        loginForm.setEmail("@doe.com");
        LoginForm.validate(loginForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_InvalidEmail5_AddsError() {
        loginForm.setEmail("jane!@doe.com");
        LoginForm.validate(loginForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_EmptyPassword_AddsError() {
        loginForm.setPassword("");
        LoginForm.validate(loginForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }
}
