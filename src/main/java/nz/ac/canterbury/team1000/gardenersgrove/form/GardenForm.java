package nz.ac.canterbury.team1000.gardenersgrove.form;

import nz.ac.canterbury.team1000.gardenersgrove.entity.Garden;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;

import static nz.ac.canterbury.team1000.gardenersgrove.form.FormUtils.*;

public class GardenForm {
    protected String name;
    protected String location;
    protected String size;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSize() {
        return size;
    }

    public Double getSizeDouble() {
        try {
            return Double.parseDouble(size);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public void setSize(String size) {
        this.size = size;
    }

    /**
     * Generates a Garden object with the values from the form.
     * @return new Garden with attributes directly from the input values in the form.
     */
    public Garden getGarden() {
        return new Garden(
                this.name,
                this.location,
                getSizeDouble() //TODO could get rid of some constructor redundancy in either Garden or User
        );
    }

    /**
     * Validates the 'New Garden' form data and adds validation errors to the BindingResult.
     *
     * @param createGardenForm the GardenForm object representing the details of the garden being created
     * @param bindingResult    the BindingResult object for validation errors
     */
    public static void validate(GardenForm createGardenForm, BindingResult bindingResult) {
        // Create an ErrorAdder instance with the BindingResult and object name
        ErrorAdder errors = new ErrorAdder(bindingResult, "createGardenForm");

        // Validate garden name
        if (checkBlank(createGardenForm.getName())) {
            errors.add("name", "Garden name must not be empty", createGardenForm.getName());
        } else if (!checkValidGardenName(createGardenForm.getName())) {
            errors.add("name", "Garden name must only include letters, numbers, spaces, dots, hyphens or apostrophes", createGardenForm.getName());
        } else if (checkOverMaxLength(createGardenForm.getName(), 255)) { //TODO replace with constant
            errors.add("name", "Name must be 255 characters or less", createGardenForm.getName());
        }

        // Validate garden location
        if (checkBlank(createGardenForm.getLocation())) {
            errors.add("location", "Location cannot be empty", createGardenForm.getLocation());
        } else if (!checkValidLocationName(createGardenForm.getLocation())) {
            errors.add("location", "Location name must only include letters, numbers, spaces, commas, dots, hyphens or apostrophes", createGardenForm.getName());
        } else if (checkOverMaxLength(createGardenForm.getLocation(), 255)) { //TODO replace with constant
            errors.add("location", "Location must be 255 characters or less", createGardenForm.getLocation());
        }

        // Validate garden size (if there is one)
        if (!checkBlank(createGardenForm.getSize())) {
            if (checkDoubleIsInvalid(createGardenForm.getSize())) {
                errors.add("size", "Garden size must be a positive number", createGardenForm.getSize());
            } else if (checkDoubleTooBig(createGardenForm.getSize())) {
                errors.add("size", "Garden size must be at most " + Integer.MAX_VALUE + " m²", createGardenForm.getSize());
            }
        }
    }
}
