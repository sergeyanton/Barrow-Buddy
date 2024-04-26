package nz.ac.canterbury.team1000.gardenersgrove.form;

import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;

public class ProfilePictureFormTest {
    final User existingUser = new User(
            "John",
            "Doe",
            "john@doe.com",
            "password",
            LocalDate.of(2000, 1, 1),
            "/images/default_pic.jpg"
    );
    final ProfilePictureForm profilePictureForm = new ProfilePictureForm();

    @Mock
    BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        profilePictureForm.setPictureFile(new MockMultipartFile("pictureFile", new byte[0]));

        bindingResult = Mockito.mock(BindingResult.class);
        Mockito.when(bindingResult.hasErrors()).thenReturn(false);
        Mockito.doAnswer(invocation -> {
            Mockito.when(bindingResult.hasErrors()).thenReturn(true);
            return null;
        }).when(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_WithNoImage_DoesNotAddError() {
        ProfilePictureForm.validate(profilePictureForm, bindingResult, existingUser);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }
    @Test
    void validate_WithPngImage_DoesNotAddError() {
        profilePictureForm.setPictureFile(new MockMultipartFile(
                "pictureFile", "newPfp.png", "image/png", "file contents".getBytes()));
        ProfilePictureForm.validate(profilePictureForm, bindingResult, existingUser);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_WithJpegImage_DoesNotAddError() {
        profilePictureForm.setPictureFile(new MockMultipartFile(
                "pictureFile", "newPfp.jpeg", "image/jpeg", "file contents".getBytes()));
        ProfilePictureForm.validate(profilePictureForm, bindingResult, existingUser);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_WithSvgImage_DoesNotAddError() {
        profilePictureForm.setPictureFile(new MockMultipartFile(
                "pictureFile", "newPfp.svg", "image/svg+xml", "file contents".getBytes()));
        ProfilePictureForm.validate(profilePictureForm, bindingResult, existingUser);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_WithWrongImageType_AddsError() {
        profilePictureForm.setPictureFile(new MockMultipartFile(
                "pictureFile", "newPfp.webp", "image/webp", "file contents".getBytes()));
        ProfilePictureForm.validate(profilePictureForm, bindingResult, existingUser);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_WithExactlyBigImage_DoesNotAddError() {
        byte[] exactly10mb = new byte[10 * 1024 * 1024];
        profilePictureForm.setPictureFile(new MockMultipartFile(
                "pictureFile", "newPfp.jpeg", "image/jpeg", exactly10mb));
        ProfilePictureForm.validate(profilePictureForm, bindingResult, existingUser);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_WithTooBigImage_AddsError() {
        byte[] over10mb = new byte[10 * 1024 * 1024 + 1];
        profilePictureForm.setPictureFile(new MockMultipartFile(
                "pictureFile", "newPfp.jpeg", "image/jpeg", over10mb));
        ProfilePictureForm.validate(profilePictureForm, bindingResult, existingUser);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }
}
