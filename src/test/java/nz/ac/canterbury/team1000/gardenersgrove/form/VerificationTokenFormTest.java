package nz.ac.canterbury.team1000.gardenersgrove.form;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.validation.BindingResult;

public class VerificationTokenFormTest {
    VerificationTokenForm verificationTokenForm = new VerificationTokenForm();
    @Mock
    BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        verificationTokenForm.setVerificationToken("12345");

        bindingResult = Mockito.mock(BindingResult.class);
        Mockito.when(bindingResult.hasErrors()).thenReturn(false);
        Mockito.doAnswer(invocation -> {
            Mockito.when(bindingResult.hasErrors()).thenReturn(true);
            return null;
        }).when(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_BlankVerificationToken_AddsError() {
        verificationTokenForm.setVerificationToken(" ");
        VerificationTokenForm.validate(verificationTokenForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_TooShortVerificationToken_AddsError() {
        verificationTokenForm.setVerificationToken("1");
        VerificationTokenForm.validate(verificationTokenForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_TooLongVerificationToken_AddsError() {
        verificationTokenForm.setVerificationToken("1234567");
        VerificationTokenForm.validate(verificationTokenForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

}
