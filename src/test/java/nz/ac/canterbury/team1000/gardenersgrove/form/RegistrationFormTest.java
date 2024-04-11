package nz.ac.canterbury.team1000.gardenersgrove.form;

import com.fasterxml.jackson.datatype.jsr310.deser.key.LocalDateKeyDeserializer;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.validation.BindingResult;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class RegistrationFormTest {
    RegistrationForm registrationForm = new EditUserForm();
    @Mock
    BindingResult bindingResult;
    private static MockedStatic<Clock> mockedClock;
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
        Mockito.verify(bindingResult).addError(Mockito.any());
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
    void validate_WithFirstNameWithInvalidCharacters_AddsError() {
        registrationForm.setFirstName("John123");
        RegistrationForm.validate(registrationForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
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
    void validate_WithBlankEmail_AddsError() {
        registrationForm.setEmail("");
        RegistrationForm.validate(registrationForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_WithInvalidEmail_AddsError() {
        registrationForm.setEmail("john@doe");
        RegistrationForm.validate(registrationForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
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
    void validate_WithValidDOB_DoesNotAddError() {
        registrationForm.setDob("03/04/2003");
        RegistrationForm.validate(registrationForm, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_WithJust13yo_DoesNotAddError() {
        registrationForm.setDob("03/04/2011");
        RegistrationForm.validate(registrationForm, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_WithJust120yo_DoesNotAddError() {
        registrationForm.setDob("03/04/1904");
        RegistrationForm.validate(registrationForm, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_WithYoungerThan13yo_AddsError() {
        registrationForm.setDob("03/04/2012");
        RegistrationForm.validate(registrationForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_WithOlderThan120yo_AddsError() {
        registrationForm.setDob("03/04/1903");
        RegistrationForm.validate(registrationForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }
}
