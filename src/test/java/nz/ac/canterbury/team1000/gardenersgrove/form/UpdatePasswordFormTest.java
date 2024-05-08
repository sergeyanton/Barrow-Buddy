package nz.ac.canterbury.team1000.gardenersgrove.form;

import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class UpdatePasswordFormTest {
    final UpdatePasswordForm updatePasswordForm = new UpdatePasswordForm();

    @Mock
    BindingResult bindingResult;

    ArgumentCaptor<FieldError> fieldErrorCaptor = ArgumentCaptor.forClass(FieldError.class);

    @BeforeEach
    void setUp() {
        // create the user we are updating
        User user = new User("Morgan", "English", "morgan.loves.cats@xtra.com", "MorgLov3zCat!",
                LocalDate.of(1998, 10, 18), "default.png");

        updatePasswordForm.setExistingUser(user);

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

    @Test
    void validate_PasswordIncludingFirstNames_AddsError() {
        User existingUser = updatePasswordForm.getExistingUser();
        existingUser.setFname("Morgan");
        updatePasswordForm.setNewPassword("MorganEatsPickles123$");
        updatePasswordForm.setRetypeNewPassword("MorganEatsPickles123$");
        UpdatePasswordForm.validate(updatePasswordForm, bindingResult);
        // check that the error was added and has correct message
        Mockito.verify(bindingResult).addError(fieldErrorCaptor.capture());
        FieldError fieldError = fieldErrorCaptor.getValue();
        assertEquals(
                "Your password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character.",
                fieldError.getDefaultMessage());
    }
}
