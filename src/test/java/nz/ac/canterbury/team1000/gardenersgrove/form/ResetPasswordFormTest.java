package nz.ac.canterbury.team1000.gardenersgrove.form;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.validation.BindingResult;


public class ResetPasswordFormTest {
    ResetPasswordForm resetPasswordForm = new ResetPasswordForm();

    @Mock
    BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        resetPasswordForm.setNewPassword("Password123!");
        resetPasswordForm.setRetypePassword("Password123!");

        bindingResult = Mockito.mock(BindingResult.class);
        Mockito.when(bindingResult.hasErrors()).thenReturn(false);
        Mockito.doAnswer(invocation -> {
            Mockito.when(bindingResult.hasErrors()).thenReturn(true);
            return null;
        }).when(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_Valid_DoesNotAddError() {
        ResetPasswordForm.validate(resetPasswordForm, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_EmptyNewPasswordAndDontMatch_Adds2Errors() {
        resetPasswordForm.setNewPassword("");
        ResetPasswordForm.validate(resetPasswordForm, bindingResult);
        Mockito.verify(bindingResult, Mockito.times(2)).addError(Mockito.any());
    }

    @Test
    void validate_EmptyRetypePassword_AddsError() {
        resetPasswordForm.setRetypePassword("");
        ResetPasswordForm.validate(resetPasswordForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_WeakPassword_AddsError() {
        resetPasswordForm.setNewPassword("abc");
        resetPasswordForm.setRetypePassword("abc");
        ResetPasswordForm.validate(resetPasswordForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_PasswordsDontMatch_AddsError() {
        resetPasswordForm.setNewPassword("Password123!");
        resetPasswordForm.setRetypePassword("OtherPassword456!");
        ResetPasswordForm.validate(resetPasswordForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_WeakAndNoMatch_Adds2Errors() {
        resetPasswordForm.setNewPassword("abc");
        resetPasswordForm.setRetypePassword("123");
        ResetPasswordForm.validate(resetPasswordForm, bindingResult);
        Mockito.verify(bindingResult, Mockito.times(2)).addError(Mockito.any());
    }


}
