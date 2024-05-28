package nz.ac.canterbury.team1000.gardenersgrove.form;

import nz.ac.canterbury.team1000.gardenersgrove.controller.AccountController;
import nz.ac.canterbury.team1000.gardenersgrove.service.ModerationService;
import nz.ac.canterbury.team1000.gardenersgrove.service.ModerationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.validation.BindingResult;

class GardenFormTest {
    final GardenForm gardenForm = new GardenForm();

    @Mock
    BindingResult bindingResult;

    @Mock
    ModerationService moderationService;

    @BeforeEach
    void setUp() {
        // set the garden form to a new user with valid data
        gardenForm.setName("Green Garden");
        gardenForm.setAddress("A");
        gardenForm.setSuburb("C");
        gardenForm.setCity("D");
        gardenForm.setPostcode("E");
        gardenForm.setCountry("F");
        gardenForm.setSize("6.5");
        gardenForm.setDescription("");

        bindingResult = Mockito.mock(BindingResult.class);
        moderationService = Mockito.mock(ModerationService.class);
        Mockito.when(moderationService.isAllowed(Mockito.any())).thenReturn(true);
        Mockito.when(bindingResult.hasErrors()).thenReturn(false);
        Mockito.doAnswer(invocation -> {
            Mockito.when(bindingResult.hasErrors()).thenReturn(true);
            return null;
        }).when(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_NameEmpty_AddsError() {
        gardenForm.setName("");
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_NameBlank_AddsError() {
        gardenForm.setName("     ");
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_NameInvalid_AddsError() {
        gardenForm.setName("$teve");
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_NameTooLong_AddsError() {
        gardenForm.setName("a".repeat(10000));
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_NameValid_DoesNotAddError() {
        gardenForm.setName("A1-,.");
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_LocationCityEmpty_AddsError() {
        gardenForm.setCity("");
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_LocationCountryEmpty_AddsError() {
        gardenForm.setCountry("");
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_LocationCityBlank_AddsError() {
        gardenForm.setCity("     ");
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_LocationCountryBlank_AddsError() {
        gardenForm.setCountry("     ");
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_LocationAddressInvalid_AddsError() {
        gardenForm.setAddress("$teve");
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_LocationSuburbInvalid_AddsError() {
        gardenForm.setSuburb("$teve");
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_LocationCityInvalid_AddsError() {
        gardenForm.setCity("$teve");
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_LocationPostcodeInvalid_AddsError() {
        gardenForm.setPostcode("$teve");
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_LocationCountryInvalid_AddsError() {
        gardenForm.setCountry("$teve");
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_LocationAddressTooLong_AddsError() {
        gardenForm.setAddress("a".repeat(10000));
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_LocationSuburbTooLong_AddsError() {
        gardenForm.setSuburb("a".repeat(10000));
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_LocationCityTooLong_AddsError() {
        gardenForm.setCity("a".repeat(10000));
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_LocationPostcodeTooLong_AddsError() {
        gardenForm.setPostcode("a".repeat(10000));
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_LocationCountryTooLong_AddsError() {
        gardenForm.setCountry("a".repeat(10000));
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_EmptyAddressValid_DoesNotAddError() {
        gardenForm.setAddress("");
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_WhitespaceAddressValid_DoesNotAddError() {
        gardenForm.setAddress("     ");
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_EmptySuburbValid_DoesNotAddError() {
        gardenForm.setSuburb("");
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_WhitespaceSuburbValid_DoesNotAddError() {
        gardenForm.setSuburb("     ");
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_EmptyPostcodeValid_DoesNotAddError() {
        gardenForm.setPostcode("");
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_WhitespacePostcodeValid_DoesNotAddError() {
        gardenForm.setPostcode("      ");
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_LocationAddressValid_DoesNotAddError() {
        gardenForm.setAddress("A1-,.");
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_LocationSuburbValid_DoesNotAddError() {
        gardenForm.setSuburb("A1-,.");
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_LocationCityValid_DoesNotAddError() {
        gardenForm.setCity("A1-,.");
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_LocationPostcodeValid_DoesNotAddError() {
        gardenForm.setPostcode("A1-,.");
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_LocationCountryValid_DoesNotAddError() {
        gardenForm.setCountry("A1-,.");
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_SizeEmpty_DoesNotAddError() {
        gardenForm.setSize("");
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_SizeBlank_DoesNotAddError() {
        gardenForm.setSize("    ");
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_SizeNegative_AddsError() {
        gardenForm.setSize("-5");
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_SizeInvalid_AddsError() {
        gardenForm.setSize("Not a double");
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_SizeExactlyBig_DoesNotAddError() {
        gardenForm.setSize("72000");
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_SizeTooBig_AddsError() {
        gardenForm.setSize("72001");
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_SizeDecimal_DoesNotAddError() {
        gardenForm.setSize("0.2");
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_SizeEuropeanFormat_DoesNotAddError() {
        gardenForm.setSize("0,2");
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_SizeEuropeanFormat_ConvertsToDecimal() {
        gardenForm.setSize("1,2");
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Assertions.assertEquals("1.2", gardenForm.getSize());
    }

    @Test
    void validate_SizeJustDecimal_DoesNotAddError() {
        gardenForm.setSize(".2");
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_MultipleDecimals_AddsError() {
        gardenForm.setSize("1.2.3");
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_GardenSizeValid_DoesNotAddError() {
        gardenForm.setSize("6.5");
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_GardenSizeEmpty_DoesNotAddError() {
        gardenForm.setSize("");
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_GardenSizeBlank_DoesNotAddError() {
        gardenForm.setSize("    ");
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_GardenSizeZero_AddsError() {
        gardenForm.setSize("0");
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_GardenSizeNegative_AddsError() {
        gardenForm.setSize("-5");
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_GardenSizeValidZeroPointOne_DoesNotAddError() {
        gardenForm.setSize("0.1");
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @ParameterizedTest
    @ValueSource(strings = {"000.1", "000,1"})
    void validate_GardenSizeWithLeadingZeros_DoesNotAddError(String size) {
        gardenForm.setSize(size);
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @ParameterizedTest
    @ValueSource(strings = {"0.000000001", "0,000000001"})
    void validate_GardenSizeSuperSmallNonZero_DoesNotAddError(String size) {
        gardenForm.setSize(size);
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_GardenSizeAcceptsWorldLargestGardenSize_DoesNotAddError() {
        gardenForm.setSize("72000");
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_GardenDescriptionIsNull_DoesNotAddError() {
        gardenForm.setDescription(null);
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_GardenDescriptionIsEmpty_DoesNotAddError() {
        gardenForm.setDescription("");
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_GardenDescriptionIsWhitespace_DoesNotAddError() {
        gardenForm.setDescription(" ");
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_GardenDescriptionContainsAtLeastOneLetter_DoesNotAddError() {
        gardenForm.setDescription("123456789a");
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_GardenDescriptionLength512_DoesNotAddError() {
        gardenForm.setDescription("a".repeat(512));
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_GardenDescriptionDoesNotContainAnyLetters_AddsError() {
        gardenForm.setDescription("123456789");
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_GardenDescriptionLengthOver512_AddsError() {
        gardenForm.setDescription("a".repeat(513));
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_GardenDescriptionDisallowedLanguage_AddsError() {
        gardenForm.setDescription("I want to kill you");
        Mockito.when(moderationService.isAllowed(Mockito.any())).thenReturn(false);
        GardenForm.validate(gardenForm, moderationService, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }
}
