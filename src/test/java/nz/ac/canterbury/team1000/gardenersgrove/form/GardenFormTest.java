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
        gardenForm.setStreet("A");
        gardenForm.setStreetNumber("B");
        gardenForm.setSuburb("C");
        gardenForm.setCity("D");
        gardenForm.setPostcode("E");
        gardenForm.setCountry("F");
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
    void validate_LocationStreetEmpty_AddsError() {
        gardenForm.setStreet("");
        GardenForm.validate(gardenForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_LocationStreetNumberEmpty_AddsError() {
        gardenForm.setStreetNumber("");
        GardenForm.validate(gardenForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }
    @Test
    void validate_LocationSuburbEmpty_AddsError() {
        gardenForm.setSuburb("");
        GardenForm.validate(gardenForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }
    @Test
    void validate_LocationCityEmpty_AddsError() {
        gardenForm.setCity("");
        GardenForm.validate(gardenForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }    @Test
    void validate_LocationPostcodeEmpty_AddsError() {
        gardenForm.setPostcode("");
        GardenForm.validate(gardenForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }    @Test
    void validate_LocationCountryEmpty_AddsError() {
        gardenForm.setCountry("");
        GardenForm.validate(gardenForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }


    @Test
    void validate_LocationStreetBlank_AddsError() {
        gardenForm.setStreet("     ");
        GardenForm.validate(gardenForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }
    @Test
    void validate_LocationStreetNumberBlank_AddsError() {
        gardenForm.setStreetNumber("     ");
        GardenForm.validate(gardenForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }
    @Test
    void validate_LocationSuburbBlank_AddsError() {
        gardenForm.setSuburb("     ");
        GardenForm.validate(gardenForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }
    @Test
    void validate_LocationCityBlank_AddsError() {
        gardenForm.setCity("     ");
        GardenForm.validate(gardenForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }
    @Test
    void validate_LocationPostcodeBlank_AddsError() {
        gardenForm.setPostcode("     ");
        GardenForm.validate(gardenForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }
    @Test
    void validate_LocationCountryBlank_AddsError() {
        gardenForm.setCountry("     ");
        GardenForm.validate(gardenForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_LocationStreetInvalid_AddsError() {
        gardenForm.setStreet("$teve");
        GardenForm.validate(gardenForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }
    @Test
    void validate_LocationStreetNumberInvalid_AddsError() {
        gardenForm.setStreetNumber("$teve");
        GardenForm.validate(gardenForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }
    @Test
    void validate_LocationSuburbInvalid_AddsError() {
        gardenForm.setSuburb("$teve");
        GardenForm.validate(gardenForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }
    @Test
    void validate_LocationCityInvalid_AddsError() {
        gardenForm.setCity("$teve");
        GardenForm.validate(gardenForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }
    @Test
    void validate_LocationPostcodeInvalid_AddsError() {
        gardenForm.setPostcode("$teve");
        GardenForm.validate(gardenForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }
    @Test
    void validate_LocationCountryInvalid_AddsError() {
        gardenForm.setCountry("$teve");
        GardenForm.validate(gardenForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_LocationStreetTooLong_AddsError() {
        gardenForm.setStreet("a".repeat(10000));
        GardenForm.validate(gardenForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }
    @Test
    void validate_LocationStreetNumberTooLong_AddsError() {
        gardenForm.setStreetNumber("a".repeat(10000));
        GardenForm.validate(gardenForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }
    @Test
    void validate_LocationSuburbTooLong_AddsError() {
        gardenForm.setSuburb("a".repeat(10000));
        GardenForm.validate(gardenForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }
    @Test
    void validate_LocationCityTooLong_AddsError() {
        gardenForm.setCity("a".repeat(10000));
        GardenForm.validate(gardenForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }
    @Test
    void validate_LocationPostcodeTooLong_AddsError() {
        gardenForm.setPostcode("a".repeat(10000));
        GardenForm.validate(gardenForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }
    @Test
    void validate_LocationCountryTooLong_AddsError() {
        gardenForm.setCountry("a".repeat(10000));
        GardenForm.validate(gardenForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_LocationStreetValid_DoesNotAddError() {
        gardenForm.setStreet("A1-,.");
        GardenForm.validate(gardenForm, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }
    @Test
    void validate_LocationStreetNumberValid_DoesNotAddError() {
        gardenForm.setStreetNumber("A1-,.");
        GardenForm.validate(gardenForm, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }
    @Test
    void validate_LocationSuburbValid_DoesNotAddError() {
        gardenForm.setSuburb("A1-,.");
        GardenForm.validate(gardenForm, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }
    @Test
    void validate_LocationCityValid_DoesNotAddError() {
        gardenForm.setCity("A1-,.");
        GardenForm.validate(gardenForm, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }
    @Test
    void validate_LocationPostcodeValid_DoesNotAddError() {
        gardenForm.setPostcode("A1-,.");
        GardenForm.validate(gardenForm, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }
    @Test
    void validate_LocationCountryValid_DoesNotAddError() {
        gardenForm.setCountry("A1-,.");
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