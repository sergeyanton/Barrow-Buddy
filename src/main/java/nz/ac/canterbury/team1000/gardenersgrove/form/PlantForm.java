package nz.ac.canterbury.team1000.gardenersgrove.form;

import nz.ac.canterbury.team1000.gardenersgrove.entity.Garden;
import nz.ac.canterbury.team1000.gardenersgrove.entity.Plant;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;

import static nz.ac.canterbury.team1000.gardenersgrove.form.FormUtils.*;

public class PlantForm {
    protected String name;
    protected String plantCount;
    protected String description;
    protected String plantedOnDate;

    protected Long gardenId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlantCount() {
        return plantCount;
    }

    public void setPlantCount(String plantCount) {
        this.plantCount = plantCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlantedOnDate() {
        return plantedOnDate;
    }

    public void setPlantedOnDate(String plantedOnDate) {
        this.plantedOnDate = plantedOnDate;
    }

    public Long getGardenId() {
        return gardenId;
    }

    public void setGardenId(Long gardenId) {
        this.gardenId = gardenId;
    }

    /**
     * Generates a Plant object with the values from the form.
     * @return new Plant with attributes directly from the input values in the form.
     */
    public Plant getPlant() {
        return new Plant(
                this.name,
                this.plantCount,
                this.description,
                this.plantedOnDate,
                this.gardenId);
    }

    /**
     * Validates the 'New Plant' form data and adds validation errors to the BindingResult.
     *
     * @param createPlantForm the PlantForm object representing the details of the garden being created
     * @param bindingResult   the BindingResult object for validation errors
     */
    public static void validate(PlantForm createPlantForm, BindingResult bindingResult) {
        // Create an ErrorAdder instance with the BindingResult and object name
        ErrorAdder errors = new ErrorAdder(bindingResult, "createPlantForm");

        // Validate plant name
        if (checkBlank(createPlantForm.getName())) {
            errors.add("name", "Plant name must not be empty", createPlantForm.getName());
        } else if (!checkValidPlantName(createPlantForm.getName())) {
            errors.add("name", "Plant name must only include letters, numbers, spaces, dots, hyphens or apostrophes", createPlantForm.getName());
        } else if (checkOverMaxLength(createPlantForm.getName(), 255)) { //TODO replace with constant
            errors.add("name", "Name must be 255 characters or less", createPlantForm.getName());
        }

        // Validate plant count (if there is one)
        if (!checkBlank(createPlantForm.getPlantCount())) {
            if (checkIntegerIsInvalid(createPlantForm.getPlantCount())) {
                errors.add("plantCount", "Plant count must be a positive integer", createPlantForm.getPlantCount());
            } else if (checkIntegerTooBig(createPlantForm.getPlantCount())) {
                errors.add("plantCount", "Plant count must be at most " + Integer.MAX_VALUE, createPlantForm.getPlantCount());
            }
        }

        // Validate description (if there is one)
        if (!checkBlank(createPlantForm.getDescription())) {
            if (checkOverMaxLength(createPlantForm.getPlantCount(), 512)) {
                errors.add("description", "Plant description must be less than 512 characters", createPlantForm.getDescription());
            }
        }

        // Validate date of birth (if there is one)
        if (!checkBlank(createPlantForm.getPlantedOnDate())) {
            if (checkDateNotInCorrectFormat(createPlantForm.getPlantedOnDate())) {
                errors.add("plantedOnDate", "Date is not in valid format, DD/MM/YYYY", createPlantForm.getPlantedOnDate());
            } else if (!checkDateBefore(createPlantForm.getPlantedOnDate(), LocalDate.now().plusDays(1))) {
                errors.add("plantedOnDate", "Date cannot be in the future", createPlantForm.getPlantedOnDate());
            }
        }
    }
}
