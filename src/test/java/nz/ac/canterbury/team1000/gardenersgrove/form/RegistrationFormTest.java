package nz.ac.canterbury.team1000.gardenersgrove.form;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.validation.BindingResult;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import org.springframework.validation.FieldError;

public class RegistrationFormTest {
    final RegistrationForm registrationForm = new RegistrationForm();
    @Mock
    BindingResult bindingResult;
    private static MockedStatic<Clock> mockedClock;
    ArgumentCaptor<FieldError> fieldErrorCaptor = ArgumentCaptor.forClass(FieldError.class);
    // Mocking the date to 2024 April 3rd just to test the dob
    private static final LocalDate APRIL_3_2024 = LocalDate.of(2024, 4, 3);

    @BeforeEach
    void setUp() {
        Instant instant = APRIL_3_2024.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Clock fixedClock = Clock.fixed(instant, ZoneId.systemDefault());
        mockedClock = Mockito.mockStatic(Clock.class);
        mockedClock.when(Clock::systemDefaultZone).thenReturn(fixedClock);
        // Checking if the date is actually mocked to the date we set it to be
        Assertions.assertEquals(APRIL_3_2024, LocalDate.now());

        // set the registration form to a new user with valid data
        registrationForm.setFirstName("John");
        registrationForm.setLastName("Doe");
        registrationForm.setEmail("john@doe.com");
        registrationForm.setPassword("MorganLikesDogs1234!");
        registrationForm.setRetypePassword("MorganLikesDogs1234!");
        registrationForm.setNoSurnameCheckBox(false);
        registrationForm.setDob("01/01/2000");

        bindingResult = Mockito.mock(BindingResult.class);
        Mockito.when(bindingResult.hasErrors()).thenReturn(false);
        Mockito.doAnswer(invocation -> {
            Mockito.when(bindingResult.hasErrors()).thenReturn(true);
            return null;
        }).when(bindingResult).addError(Mockito.any());
    }

    // Closing the mocked clock for the other tests
    @AfterEach
    void tear_down() {
        mockedClock.close();
    }

    @Test
    void validate_WithBlankFirstName_AddsError() {
        registrationForm.setFirstName("");
        RegistrationForm.validate(registrationForm, bindingResult);
        Mockito.verify(bindingResult).addError(fieldErrorCaptor.capture());
        FieldError fieldError = fieldErrorCaptor.getValue();
        Assertions.assertEquals("First name cannot be empty", fieldError.getDefaultMessage());
    }

    @Test
    void validate_WithFirstNameOver64Characters_AddsError() {
        registrationForm.setFirstName("a".repeat(65));
        RegistrationForm.validate(registrationForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_WithFirstNameExactly64Characters_DoesNotAddError() {
        registrationForm.setFirstName("a".repeat(64));
        RegistrationForm.validate(registrationForm, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_WithFirstNameWithNumbers_AddsError() {
        registrationForm.setFirstName("John123");
        RegistrationForm.validate(registrationForm, bindingResult);
        Mockito.verify(bindingResult).addError(fieldErrorCaptor.capture());
        FieldError fieldError = fieldErrorCaptor.getValue();
        Assertions.assertEquals("First name must only include letters, spaces, hyphens or apostrophes", fieldError.getDefaultMessage());
    }

    @Test
    void validate_FirstNameContainsAccentedCharacters_DoesNotAddError() {
        registrationForm.setFirstName("María");
        RegistrationForm.validate(registrationForm, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_FirstNameContainsMacron_DoesNotAddError() {
        registrationForm.setFirstName("Mārama");
        RegistrationForm.validate(registrationForm, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_WithFirstNameWithEmoji_AddsError() {
        registrationForm.setFirstName("❤️");
        RegistrationForm.validate(registrationForm, bindingResult);
        Mockito.verify(bindingResult).addError(fieldErrorCaptor.capture());
        FieldError fieldError = fieldErrorCaptor.getValue();
        Assertions.assertEquals("First name must only include letters, spaces, hyphens or apostrophes", fieldError.getDefaultMessage());
    }

    @Test
    void validate_WithBlankLastName_AddsError() {
        registrationForm.setLastName("");
        RegistrationForm.validate(registrationForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_WithLastNameOver64Characters_AddsError() {
        registrationForm.setLastName("a".repeat(65));
        RegistrationForm.validate(registrationForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_WithLastNameWithInvalidCharacters_AddsError() {
        registrationForm.setLastName("Doe123");
        RegistrationForm.validate(registrationForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_WithBlankLastNameAndNoSurnameCheckBox_DoesNotAddError() {
        registrationForm.setLastName("");
        registrationForm.setNoSurnameCheckBox(true);
        RegistrationForm.validate(registrationForm, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_LastNameContainsAccentedCharacters_DoesNotAddError() {
        registrationForm.setLastName("María");
        RegistrationForm.validate(registrationForm, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_LastNameContainsMacron_DoesNotAddError() {
        registrationForm.setLastName("Mārama");
        RegistrationForm.validate(registrationForm, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_WithLastNameWithEmoji_AddsError() {
        registrationForm.setLastName("❤️");
        RegistrationForm.validate(registrationForm, bindingResult);
        Mockito.verify(bindingResult).addError(fieldErrorCaptor.capture());
        FieldError fieldError = fieldErrorCaptor.getValue();
        Assertions.assertEquals("Last name must only include letters, spaces, hyphens or apostrophes", fieldError.getDefaultMessage());
    }

    @Test
    void validate_WithBlankEmail_AddsError() {
        registrationForm.setEmail("");
        RegistrationForm.validate(registrationForm, bindingResult);
        Mockito.verify(bindingResult).addError(fieldErrorCaptor.capture());
        FieldError fieldError = fieldErrorCaptor.getValue();
        Assertions.assertEquals("Email address must be in the form ‘jane@doe.nz’", fieldError.getDefaultMessage());
    }

    @Test
    void validate_WithInvalidEmail_AddsError() {
        registrationForm.setEmail("john@doe");
        RegistrationForm.validate(registrationForm, bindingResult);
        Mockito.verify(bindingResult).addError(fieldErrorCaptor.capture());
        FieldError fieldError = fieldErrorCaptor.getValue();
        Assertions.assertEquals("Email address must be in the form ‘jane@doe.nz’", fieldError.getDefaultMessage());
    }

    @Test
    void validate_WithEmailExactlyMaxLength_DoesNotAddError() {
        String ext = "@gmail.com";
        registrationForm.setEmail("a".repeat(FormUtils.MAX_DB_STR_LEN - ext.length()) + ext);
        RegistrationForm.validate(registrationForm, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_WithEmailTooLong_AddsError() {
        String ext = "@gmail.com";
        registrationForm.setEmail("a".repeat(FormUtils.MAX_DB_STR_LEN - ext.length() + 1) + ext);
        RegistrationForm.validate(registrationForm, bindingResult);
        Mockito.verify(bindingResult).addError(fieldErrorCaptor.capture());
        FieldError fieldError = fieldErrorCaptor.getValue();
        Assertions.assertEquals("Email address must be 255 characters long or less", fieldError.getDefaultMessage());
    }

    @Test
    void validate_WithEmailWithEmoji_AddsError() {
        registrationForm.setEmail("john@❤️.com");
        RegistrationForm.validate(registrationForm, bindingResult);
        Mockito.verify(bindingResult).addError(fieldErrorCaptor.capture());
        FieldError fieldError = fieldErrorCaptor.getValue();
        Assertions.assertEquals("Email address must be in the form ‘jane@doe.nz’", fieldError.getDefaultMessage());
    }

    @Test
    void validate_WithPasswordAndRetypePasswordMismatch_AddsError() {
        registrationForm.setPassword("password");
        registrationForm.setRetypePassword("password1");
        RegistrationForm.validate(registrationForm, bindingResult);
        Mockito.verify(bindingResult, Mockito.times(2)).addError(Mockito.any());
    }

    @Test
    void validate_WithBlankPasswordButRetypePassword_AddsError() {
        registrationForm.setPassword("");
        registrationForm.setRetypePassword("password");
        RegistrationForm.validate(registrationForm, bindingResult);
        // error should be added for password and retypePassword
        Mockito.verify(bindingResult, Mockito.times(2)).addError(Mockito.any());
    }

    @Test
    void validate_WithMatchingPasswordUnder8Characters_AddsError() {
        registrationForm.setPassword("pass");
        registrationForm.setRetypePassword("pass");
        RegistrationForm.validate(registrationForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_WithValidDOB20YearsOld_DoesNotAddError() {
        registrationForm.setDob("03/04/2003");
        RegistrationForm.validate(registrationForm, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_WithJust13YearsOld_DoesNotAddError() {
        registrationForm.setDob("03/04/2011");
        RegistrationForm.validate(registrationForm, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_WithJust120YearsOld_DoesNotAddError() {
        registrationForm.setDob("03/04/1904");
        RegistrationForm.validate(registrationForm, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_WithYoungerThan13YearsOld_AddsError() {
        registrationForm.setDob("03/04/2012");
        RegistrationForm.validate(registrationForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_WithOlderThan120YearsOld_AddsError() {
        registrationForm.setDob("03/04/1903");
        RegistrationForm.validate(registrationForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_WithBlankDob_DoesNotAddError() {
        registrationForm.setDob("");
        RegistrationForm.validate(registrationForm, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }
}
