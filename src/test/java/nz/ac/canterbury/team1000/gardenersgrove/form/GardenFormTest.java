package nz.ac.canterbury.team1000.gardenersgrove.form;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.validation.BindingResult;

class GardenFormTest {
    GardenForm gardenForm = new GardenForm();

    @Mock
    BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        // set the garden form to a new user with valid data
        gardenForm.setName("Green Garden");
        gardenForm.setLocation("My house");
        gardenForm.setSize("6.5");

        bindingResult = Mockito.mock(BindingResult.class);
        Mockito.when(bindingResult.hasErrors()).thenReturn(false);
        Mockito.doAnswer(invocation -> {
            Mockito.when(bindingResult.hasErrors()).thenReturn(true);
            return null;
        }).when(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_NameEmpty_AddsError() {
        gardenForm.setName("");
        GardenForm.validate(gardenForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_NameBlank_AddsError() {
        gardenForm.setName("     ");
        GardenForm.validate(gardenForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_NameInvalid_AddsError() {
        gardenForm.setName("$teve");
        GardenForm.validate(gardenForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_NameTooLong_AddsError() {
        gardenForm.setName("a".repeat(10000));
        GardenForm.validate(gardenForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_NameValid_DoesNotAddError() {
        gardenForm.setName("A1-,.");
        GardenForm.validate(gardenForm, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_LocationEmpty_AddsError() {
        gardenForm.setLocation("");
        GardenForm.validate(gardenForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_LocationBlank_AddsError() {
        gardenForm.setLocation("     ");
        GardenForm.validate(gardenForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_LocationInvalid_AddsError() {
        gardenForm.setLocation("$teve");
        GardenForm.validate(gardenForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_LocationTooLong_AddsError() {
        gardenForm.setLocation("a".repeat(10000));
        GardenForm.validate(gardenForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_LocationValid_DoesNotAddError() {
        gardenForm.setLocation("A1-,.");
        GardenForm.validate(gardenForm, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_SizeEmpty_DoesNotAddError() {
        gardenForm.setSize("");
        GardenForm.validate(gardenForm, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_SizeBlank_DoesNotAddError() {
        gardenForm.setSize("    ");
        GardenForm.validate(gardenForm, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_SizeNegative_AddsError() {
        gardenForm.setSize("-5");
        GardenForm.validate(gardenForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_SizeInvalid_AddsError() {
        gardenForm.setSize("Not a double");
        GardenForm.validate(gardenForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_SizeExactlyBig_DoesNotAddError() {
        gardenForm.setSize("2147483647");
        GardenForm.validate(gardenForm, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_SizeTooBig_AddsError() {
        gardenForm.setSize("2147483648");
        GardenForm.validate(gardenForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_SizeDecimal_DoesNotAddError() {
        gardenForm.setSize("0.2");
        GardenForm.validate(gardenForm, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_SizeComma_DoesNotAddError() {
        gardenForm.setSize("0,2");
        GardenForm.validate(gardenForm, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_SizeJustDecimal_DoesNotAddError() {
        gardenForm.setSize(".2");
        GardenForm.validate(gardenForm, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_MultipleDecimals_AddsError() {
        gardenForm.setSize("1.2.3");
        GardenForm.validate(gardenForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }
}