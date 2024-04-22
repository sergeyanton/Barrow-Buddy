package nz.ac.canterbury.team1000.gardenersgrove.form;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.validation.BindingResult;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import java.time.LocalDate;

public class EditUserFormTest {
    User existingUser = new User(
        "John",
        "Doe",
        "john@doe.com",
        "password",
        LocalDate.of(2000, 1, 1)
    );
    EditUserForm editUserForm = new EditUserForm();
    
    @Mock
    BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        editUserForm.setFirstName(existingUser.getFname());
        editUserForm.setLastName(existingUser.getLname());
        editUserForm.setEmail(existingUser.getEmail());
        editUserForm.setDob(existingUser.getDateOfBirth() != null ? existingUser.getDateOfBirthString() : "");
        editUserForm.setNoSurnameCheckBox(editUserForm.getLastName() == null || editUserForm.getLastName().isEmpty());

        bindingResult = Mockito.mock(BindingResult.class);
        Mockito.when(bindingResult.hasErrors()).thenReturn(false);
        Mockito.doAnswer(invocation -> {
            Mockito.when(bindingResult.hasErrors()).thenReturn(true);
            return null;
        }).when(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_WithBlankFirstName_AddsError() {
        editUserForm.setFirstName("");
        EditUserForm.validate(editUserForm, bindingResult, existingUser);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_WithFirstNameOver64Characters_AddsError() {
        editUserForm.setFirstName("a".repeat(65));
        EditUserForm.validate(editUserForm, bindingResult, existingUser);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_WithFirstNameExactly64Characters_DoesNotAddError() {
        editUserForm.setFirstName("a".repeat(64));
        EditUserForm.validate(editUserForm, bindingResult, existingUser);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_WithFirstNameWithInvalidCharacters_AddsError() {
        editUserForm.setFirstName("John123");
        EditUserForm.validate(editUserForm, bindingResult, existingUser);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_WithBlankLastName_AddsError() {
        editUserForm.setLastName("");
        EditUserForm.validate(editUserForm, bindingResult, existingUser);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_WithLastNameOver64Characters_AddsError() {
        editUserForm.setLastName("a".repeat(65));
        EditUserForm.validate(editUserForm, bindingResult, existingUser);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_WithLastNameWithInvalidCharacters_AddsError() {
        editUserForm.setLastName("Doe123");
        EditUserForm.validate(editUserForm, bindingResult, existingUser);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_WithBlankLastNameAndNoSurnameCheckBox_DoesNotAddError() {
        editUserForm.setLastName("");
        editUserForm.setNoSurnameCheckBox(true);
        EditUserForm.validate(editUserForm, bindingResult, existingUser);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_WithBlankEmail_AddsError() {
        editUserForm.setEmail("");
        EditUserForm.validate(editUserForm, bindingResult, existingUser);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_WithInvalidEmail_AddsError() {
        editUserForm.setEmail("john@doe");
        EditUserForm.validate(editUserForm, bindingResult, existingUser);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_WithBlankDateOfBirth_DoesNotAddError() {
        editUserForm.setDob("");
        EditUserForm.validate(editUserForm, bindingResult, existingUser);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_WithNonDayMonthYearDateOfBirth_AddsError() {
        editUserForm.setDob("10/28/2000");
        EditUserForm.validate(editUserForm, bindingResult, existingUser);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_WithDayMonthYearDateOfBirth_DoesNotAddError() {
        editUserForm.setDob("28/10/2000");
        EditUserForm.validate(editUserForm, bindingResult, existingUser);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }
}
