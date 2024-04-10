package nz.ac.canterbury.team1000.gardenersgrove.form;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.validation.BindingResult;

public class RegistrationFormTest {
    RegistrationForm registrationFormForm = new EditUserForm();
    
    @Mock
    BindingResult bindingResult;

    @BeforeEach
    void setUp() {

        // set the registration form to a new user with valid data
        registrationFormForm.setFirstName("John");
        registrationFormForm.setLastName("Doe");
        registrationFormForm.setEmail("john@doe.com");
        registrationFormForm.setPassword("MorganLikesDogs1234!");
        registrationFormForm.setRetypePassword("MorganLikesDogs1234!");
        registrationFormForm.setNoSurnameCheckBox(false);
        registrationFormForm.setDob("01/01/2000");

        bindingResult = Mockito.mock(BindingResult.class);
        Mockito.when(bindingResult.hasErrors()).thenReturn(false);
        Mockito.doAnswer(invocation -> {
            Mockito.when(bindingResult.hasErrors()).thenReturn(true);
            return null;
        }).when(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_WithBlankFirstName_AddsError() {
        registrationFormForm.setFirstName("");
        RegistrationForm.validate(registrationFormForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_WithFirstNameOver64Characters_AddsError() {
        registrationFormForm.setFirstName("a".repeat(65));
        RegistrationForm.validate(registrationFormForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_WithFirstNameExactly64Characters_DoesNotAddError() {
        registrationFormForm.setFirstName("a".repeat(64));
        RegistrationForm.validate(registrationFormForm, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_WithFirstNameWithInvalidCharacters_AddsError() {
        registrationFormForm.setFirstName("John123");
        RegistrationForm.validate(registrationFormForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_WithBlankLastName_AddsError() {
        registrationFormForm.setLastName("");
        RegistrationForm.validate(registrationFormForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_WithLastNameOver64Characters_AddsError() {
        registrationFormForm.setLastName("a".repeat(65));
        RegistrationForm.validate(registrationFormForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_WithLastNameWithInvalidCharacters_AddsError() {
        registrationFormForm.setLastName("Doe123");
        RegistrationForm.validate(registrationFormForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_WithBlankLastNameAndNoSurnameCheckBox_DoesNotAddError() {
        registrationFormForm.setLastName("");
        registrationFormForm.setNoSurnameCheckBox(true);
        RegistrationForm.validate(registrationFormForm, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_WithBlankEmail_AddsError() {
        registrationFormForm.setEmail("");
        RegistrationForm.validate(registrationFormForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_WithInvalidEmail_AddsError() {
        registrationFormForm.setEmail("john@doe");
        RegistrationForm.validate(registrationFormForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_WithEmailExactlyMaxLength_AddsError() {
        String ext = "@gmail.com";
        registrationFormForm.setEmail("a".repeat(FormUtils.MAX_DB_STR_LEN - ext.length()) + ext);
        RegistrationForm.validate(registrationFormForm, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }
    @Test
    void validate_WithEmailTooLong_AddsError() {
        String ext = "@gmail.com";
        registrationFormForm.setEmail("a".repeat(FormUtils.MAX_DB_STR_LEN - ext.length() + 1) + ext);
        RegistrationForm.validate(registrationFormForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_WithPasswordAndRetypePasswordMismatch_AddsError() {
        registrationFormForm.setPassword("password");
        registrationFormForm.setRetypePassword("password1");
        RegistrationForm.validate(registrationFormForm, bindingResult);
        Mockito.verify(bindingResult, Mockito.times(2)).addError(Mockito.any());
    }

    @Test
    void validate_WithBlankPasswordButRetypePassword_AddsError() {
        registrationFormForm.setPassword("");
        registrationFormForm.setRetypePassword("password");
        RegistrationForm.validate(registrationFormForm, bindingResult);
        // error should be added for password and retypePassword
        Mockito.verify(bindingResult, Mockito.times(2)).addError(Mockito.any());
    }

    @Test
    void validate_WithMatchingPasswordUnder8Characters_AddsError() {
        registrationFormForm.setPassword("pass");
        registrationFormForm.setRetypePassword("pass");
        RegistrationForm.validate(registrationFormForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }
}
