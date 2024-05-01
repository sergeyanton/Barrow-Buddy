package nz.ac.canterbury.team1000.gardenersgrove.form;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.validation.BindingResult;
import nz.ac.canterbury.team1000.gardenersgrove.entity.Plant;

class PlantFormTest {

    final PlantForm plantForm = new PlantForm();

    @Mock
    BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        // set the garden form to a new user with valid data
        plantForm.setName("Violet");
        plantForm.setPlantCount("3");
        plantForm.setDescription("Smells nice");
        plantForm.setPlantedOnDate("25/12/2000");
        plantForm.setGardenId(1L);
        plantForm.setPictureFile(new MockMultipartFile("pictureFile", new byte[0]));

        bindingResult = Mockito.mock(BindingResult.class);
        Mockito.when(bindingResult.hasErrors()).thenReturn(false);
        Mockito.doAnswer(invocation -> {
            Mockito.when(bindingResult.hasErrors()).thenReturn(true);
            return null;
        }).when(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_NameEmpty_AddsError() {
        plantForm.setName("");
        PlantForm.validate(plantForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_NameBlank_AddsError() {
        plantForm.setName("     ");
        PlantForm.validate(plantForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_NameInvalid_AddsError() {
        plantForm.setName("$teve");
        PlantForm.validate(plantForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_NameTooLong_AddsError() {
        plantForm.setName("a".repeat(10000));
        PlantForm.validate(plantForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_NameValid_DoesNotAddError() {
        plantForm.setName("A1-,.");
        PlantForm.validate(plantForm, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_CountEmpty_DoesNotAddError() {
        plantForm.setPlantCount("");
        PlantForm.validate(plantForm, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_CountInvalid_AddsError() {
        plantForm.setPlantCount("NotAnInteger");
        PlantForm.validate(plantForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_CountDouble_AddsError() {
        plantForm.setPlantCount("5.0");
        PlantForm.validate(plantForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_CountNegative_AddsError() {
        plantForm.setPlantCount("-2");
        PlantForm.validate(plantForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_CountExactlyBig_DoesNotAddError() {
        plantForm.setPlantCount("2147483647");
        PlantForm.validate(plantForm, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_CountTooBig_AddsError() {
        plantForm.setPlantCount("2147483648");
        PlantForm.validate(plantForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_CountValid_DoesNotAddError() {
        plantForm.setPlantCount("25");
        PlantForm.validate(plantForm, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_DescriptionEmpty_DoesNotAddError() {
        plantForm.setDescription("");
        PlantForm.validate(plantForm, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }
    @Test
    void validate_DescriptionValid_DoesNotAddError() {
        plantForm.setDescription("Hi this is a description");
        PlantForm.validate(plantForm, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_DescriptionExactly512_DoesNotAddError() {
        plantForm.setDescription("a".repeat(512));
        PlantForm.validate(plantForm, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_DescriptionOver512_AddsError() {
        plantForm.setDescription("a".repeat(513));
        PlantForm.validate(plantForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_PlantedOnDateValid_DoesNotAddError() {
        plantForm.setPlantedOnDate("01/01/2001");
        PlantForm.validate(plantForm, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_PlantedOnDateInvalid_AddsError() {
        plantForm.setPlantedOnDate("bad");
        PlantForm.validate(plantForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_PlantedOnDateInvalidLeapDay_AddsError() {
        plantForm.setPlantedOnDate("29/02/2003");
        PlantForm.validate(plantForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_PlantedOnDateValidLeapDay_DoesNotAddError() {
        plantForm.setPlantedOnDate("29/02/2004");
        PlantForm.validate(plantForm, bindingResult);
        Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    }

    @Test
    void validate_PlantedOnDateLocalDateFormat_AddsError() {
        plantForm.setPlantedOnDate("2003-02-02");
        PlantForm.validate(plantForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void validate_PlantCountIsZero_AddsError() {
        plantForm.setPlantCount("0");
        PlantForm.validate(plantForm, bindingResult);
        Mockito.verify(bindingResult).addError(Mockito.any());
    }

    @Test
    void fromPlant_NullPlant_ReturnsEmptyForm() {
        PlantForm form = PlantForm.fromPlant(null);
        assertTrue(form.getName().isBlank());
        assertTrue(form.getPlantCount().isBlank());
        assertTrue(form.getDescription().isBlank());
        assertTrue(form.getPlantedOnDate().isBlank());
        assertNull(form.getGardenId());
    }

    @Test
    void fromPlant_ValidPlant_ReturnsForm() {
        Plant plant = new Plant("Poppy", 5, "Red", LocalDate.of(2024, 04, 26), "/images/defaultPlantPic.png", 1L);
        PlantForm form = PlantForm.fromPlant(plant);
        assertEquals("Poppy", form.getName());
        assertEquals("5", form.getPlantCount());
        assertEquals("Red", form.getDescription());
        assertEquals("26/04/2024", form.getPlantedOnDate());
        assertEquals(1L, form.getGardenId());
    }

    @Test
    void fromPlant_ValidPlantWithNulls_ReturnsForm() {
        Plant plant = new Plant("Poppy", null, null, LocalDate.of(2024, 04, 26), "/images/defaultPlantPic.png", 1L);
        PlantForm form = PlantForm.fromPlant(plant);
        assertEquals("Poppy", form.getName());
        assertTrue(form.getPlantCount().isBlank());
        assertTrue(form.getDescription().isBlank());
        assertEquals("26/04/2024", form.getPlantedOnDate());
        assertEquals(1L, form.getGardenId());
    }

    @Test
    void updatePlant_WithValidData_UpdatesPlant() {
        Plant plant = new Plant("Poppy", 5, "Red", LocalDate.of(2024, 04, 26), "/images/defaultPlantPic.png", 1L);
        plantForm.setName("Violet");
        plantForm.setPlantCount("3");
        plantForm.setDescription("Smells nice");
        plantForm.setPlantedOnDate("25/12/2000");
        plantForm.setGardenId(1L);
        plantForm.updatePlant(plant);
        assertEquals("Violet", plant.getName());
        assertEquals(3, plant.getPlantCount());
        assertEquals("Smells nice", plant.getDescription());
        assertEquals(LocalDate.of(2000, 12, 25), plant.getPlantedOnDate());
        assertEquals(1L, plant.getGardenId());
    }
}