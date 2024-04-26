package nz.ac.canterbury.team1000.gardenersgrove.form;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.validation.BindingResult;

public class UpdatePasswordFormTest {
    final UpdatePasswordForm updatePasswordForm = new UpdatePasswordForm();

    @Mock
    BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        // set the garden form to a new user with valid data
        updatePasswordForm.setPassword("Pass123$");
        updatePasswordForm.setNewPassword("NewPass456$");
        updatePasswordForm.setRetypeNewPassword("NewPass456$");

        bindingResult = Mockito.mock(BindingResult.class);
        Mockito.when(bindingResult.hasErrors()).thenReturn(false);
        Mockito.doAnswer(invocation -> {
            Mockito.when(bindingResult.hasErrors()).thenReturn(true);
            return null;
        }).when(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_Valid_DoesNotAddError() {
        UpdatePasswordForm.validate(updatePasswordForm, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_EmptyPassword_AddsError() {
        updatePasswordForm.setPassword("");
        UpdatePasswordForm.validate(updatePasswordForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_EmptyNewPassword_AddsError() {
        updatePasswordForm.setNewPassword("");
        updatePasswordForm.setRetypeNewPassword("");
        UpdatePasswordForm.validate(updatePasswordForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_WeakNewPassword_AddsError() {
        updatePasswordForm.setNewPassword("badPass");
        updatePasswordForm.setRetypeNewPassword("badPass");
        UpdatePasswordForm.validate(updatePasswordForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_NewPasswordRetypePasswordNoMatch_AddsError() {
        updatePasswordForm.setNewPassword("goodPass123$");
        updatePasswordForm.setRetypeNewPassword("differentGood$333");
        UpdatePasswordForm.validate(updatePasswordForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_WeakAndNoMatch_AddsError() {
        updatePasswordForm.setNewPassword("badPass");
        updatePasswordForm.setRetypeNewPassword("differentBad");
        UpdatePasswordForm.validate(updatePasswordForm, bindingResult);
        Mockito.verify(bindingResult, Mockito.times(2)).addError(Mockito.any());
    }

    @Test
    void validate_EmptyAndNoMatch_AddsError() {
        updatePasswordForm.setNewPassword("");
        updatePasswordForm.setRetypeNewPassword("yoyo123$H");
        UpdatePasswordForm.validate(updatePasswordForm, bindingResult);
        Mockito.verify(bindingResult, Mockito.times(2)).addError(Mockito.any());
    }
}
