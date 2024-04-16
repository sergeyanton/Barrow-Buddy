package nz.ac.canterbury.team1000.gardenersgrove.form;

import nz.ac.canterbury.team1000.gardenersgrove.entity.Garden;
import org.springframework.validation.BindingResult;

import static nz.ac.canterbury.team1000.gardenersgrove.form.FormUtils.*;

public class GardenForm {
    protected String name;
    protected String street;
    protected String streetNumber;
    protected String suburb;
    protected String city;
    protected String postcode;
    protected String country;
    protected String size;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public String getLocation() {
//        return location;
//    }

    public String getStreet() {
        return street;
    }
    public String getStreetNumber() {
        return streetNumber;
    }
    public String getSuburb() {
        return suburb;
    }
    public String getCity() {
        return city;
    }
    public String getPostcode() {
        return postcode;
    }

    public String getCountry() {
        return country;
    }

    public void setStreet(String street) {
        this.street = street;
    }
    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }
    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public void setCountry(String country) {
        this.country = country;
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
                this.street,
                this.streetNumber,
                this.suburb,
                this.city,
                this.postcode,
                this.country,
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

        // Validate garden location - Street
        if (checkBlank(createGardenForm.getStreet())) {
            errors.add("street", "Street cannot be empty", createGardenForm.getStreet());
        } else if (!checkValidLocationName(createGardenForm.getStreet())) {
            errors.add("street", "Street name must only include letters, numbers, spaces, commas, dots, hyphens or apostrophes", createGardenForm.getStreet());
        } else if (checkOverMaxLength(createGardenForm.getStreet(), 255)) { //TODO replace with constant
            errors.add("street", "Street must be 255 characters or less", createGardenForm.getStreet());
        }

        // Validate garden location - Street Number
        if (checkBlank(createGardenForm.getStreetNumber())) {
            errors.add("streetNumber", "Street cannot be empty", createGardenForm.getStreetNumber());
        } else if (!checkValidLocationName(createGardenForm.getStreetNumber())) {
            errors.add("streetNumber", "Street number must only include letters, numbers, spaces, commas, dots, hyphens or apostrophes", createGardenForm.getStreetNumber());
        } else if (checkOverMaxLength(createGardenForm.getStreetNumber(), 255)) { //TODO replace with constant
            errors.add("streetNumber", "Street number must be 255 characters or less", createGardenForm.getStreetNumber());
        }

        // Validate garden location - Suburb
        if (checkBlank(createGardenForm.getSuburb())) {
            errors.add("suburb", "Suburb cannot be empty", createGardenForm.getSuburb());
        } else if (!checkValidLocationName(createGardenForm.getSuburb())) {
            errors.add("suburb", "Suburb name must only include letters, numbers, spaces, commas, dots, hyphens or apostrophes", createGardenForm.getSuburb());
        } else if (checkOverMaxLength(createGardenForm.getSuburb(), 255)) { //TODO replace with constant
            errors.add("suburb", "Suburb must be 255 characters or less", createGardenForm.getSuburb());
        }

        // Validate garden location - City
        if (checkBlank(createGardenForm.getCity())) {
            errors.add("city", "City cannot be empty", createGardenForm.getCity());
        } else if (!checkValidLocationName(createGardenForm.getCity())) {
            errors.add("city", "City name must only include letters, numbers, spaces, commas, dots, hyphens or apostrophes", createGardenForm.getCity());
        } else if (checkOverMaxLength(createGardenForm.getCity(), 255)) { //TODO replace with constant
            errors.add("city", "City must be 255 characters or less", createGardenForm.getCity());
        }

        // Validate garden location - Postcode
        if (checkBlank(createGardenForm.getPostcode())) {
            errors.add("postcode", "Street cannot be empty", createGardenForm.getPostcode());
        } else if (!checkValidLocationName(createGardenForm.getPostcode())) {
            errors.add("postcode", "Street name must only include letters, numbers, spaces, commas, dots, hyphens or apostrophes", createGardenForm.getPostcode());
        } else if (checkOverMaxLength(createGardenForm.getPostcode(), 255)) { //TODO replace with constant
            errors.add("postcode", "Street must be 255 characters or less", createGardenForm.getPostcode());
        }

        // Validate garden location - Country
        if (checkBlank(createGardenForm.getCountry())) {
            errors.add("country", "Country cannot be empty", createGardenForm.getCountry());
        } else if (!checkValidLocationName(createGardenForm.getCountry())) {
            errors.add("country", "Country name must only include letters, numbers, spaces, commas, dots, hyphens or apostrophes", createGardenForm.getCountry());
        } else if (checkOverMaxLength(createGardenForm.getCountry(), 255)) { //TODO replace with getCountry
            errors.add("country", "Country must be 255 characters or less", createGardenForm.getCountry());
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
