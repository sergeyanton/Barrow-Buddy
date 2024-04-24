package nz.ac.canterbury.team1000.gardenersgrove.form;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.validation.BindingResult;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import java.time.LocalDate;

public class EditUserFormTest {
    User existingUser = new User(
        "John",
        "Doe",
        "john@doe.com",
        "password",
        LocalDate.of(2000, 1, 1),
            "/images/default_pic.jpg"
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
        editUserForm.setPictureFile(new MockMultipartFile("pictureFile", new byte[0]));

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
    void validate_FirstNameContainsAccentedCharacters_DoesNotAddError() {
        editUserForm.setFirstName("María");
        EditUserForm.validate(editUserForm, bindingResult, existingUser);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_FirstNameContainsMacron_DoesNotAddError() {
        editUserForm.setFirstName("Mārama");
        EditUserForm.validate(editUserForm, bindingResult, existingUser);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
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
    void validate_LastNameContainsAccentedCharacters_DoesNotAddError() {
        editUserForm.setLastName("María");
        EditUserForm.validate(editUserForm, bindingResult, existingUser);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_LastNameContainsMacron_DoesNotAddError() {
        editUserForm.setLastName("Mārama");
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

    @Test
    void validate_WithPngImage_DoesNotAddError() {
        editUserForm.setPictureFile(new MockMultipartFile(
                "pictureFile", "newPfp.png", "image/png", "file contents".getBytes()));
        EditUserForm.validate(editUserForm, bindingResult, existingUser);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_WithJpegImage_DoesNotAddError() {
        editUserForm.setPictureFile(new MockMultipartFile(
                "pictureFile", "newPfp.jpeg", "image/jpeg", "file contents".getBytes()));
        EditUserForm.validate(editUserForm, bindingResult, existingUser);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_WithSvgImage_DoesNotAddError() {
        editUserForm.setPictureFile(new MockMultipartFile(
                "pictureFile", "newPfp.svg", "image/svg+xml", "file contents".getBytes()));
        EditUserForm.validate(editUserForm, bindingResult, existingUser);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_WithWrongImageType_AddsError() {
        editUserForm.setPictureFile(new MockMultipartFile(
                "pictureFile", "newPfp.webp", "image/webp", "file contents".getBytes()));
        EditUserForm.validate(editUserForm, bindingResult, existingUser);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_WithExactlyBigImage_DoesNotAddError() {
        byte[] exactly10mb = new byte[10 * 1024 * 1024];
        editUserForm.setPictureFile(new MockMultipartFile(
                "pictureFile", "newPfp.jpeg", "image/jpeg", exactly10mb));
        EditUserForm.validate(editUserForm, bindingResult, existingUser);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_WithTooBigImage_AddsError() {
        byte[] over10mb = new byte[10 * 1024 * 1024 + 1];
        editUserForm.setPictureFile(new MockMultipartFile(
                "pictureFile", "newPfp.jpeg", "image/jpeg", over10mb));
        EditUserForm.validate(editUserForm, bindingResult, existingUser);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }
}
