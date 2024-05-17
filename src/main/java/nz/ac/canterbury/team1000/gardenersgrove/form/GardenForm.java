package nz.ac.canterbury.team1000.gardenersgrove.form;

import nz.ac.canterbury.team1000.gardenersgrove.api.LocationSearchService;
import nz.ac.canterbury.team1000.gardenersgrove.entity.Garden;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import org.springframework.validation.BindingResult;
import java.util.Arrays;
import java.util.List;

import static nz.ac.canterbury.team1000.gardenersgrove.form.FormUtils.*;

public class GardenForm {
    protected String name;
    protected String address;
    protected String suburb;
    protected String city;
    protected String postcode;
    protected String country;
    protected boolean locationValid;
    protected String size;
    protected String description;
    protected boolean isPublic;
    private static final LocationSearchService locationSearchService = new LocationSearchService();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getPublicity() {
        return this.isPublic;
    }

    public void setPublicity(boolean publicity) {
        this.isPublic = publicity;
    }

    public String getAddress() {
        return address;
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

    public boolean getLocationValid() {
        return locationValid;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public void setLocationValid(boolean locationValid) {
        this.locationValid = locationValid;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void locationIsValid() {
        String[] location = Arrays.asList(this.address, this.city, this.postcode, this.country).toArray(new String[0]);
        List<Double> latAndLon = locationSearchService.getCoordinates(location);
        if (latAndLon.get(0) == null && latAndLon.get(1) == null) {
            this.locationValid = false;
        } else {
            this.locationValid = true;
        }
    }

    /**
     * Generates a Garden object with the values from the form.
     *
     * @param owner the User object that will be the owner of the garden
     * @return new Garden with attributes directly from the input values in the form.
     */
    public Garden getGarden(User owner) {
        locationIsValid();
        return new Garden(
                this.name,
                this.address,
                this.suburb,
                this.city,
                this.postcode,
                this.country,
                this.locationValid,
                getSizeDouble(), //TODO could get rid of some constructor redundancy in either Garden or User
                this.description,
                owner,
                this.isPublic
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
        } else if (checkOverMaxLength(createGardenForm.getName(), MAX_DB_STR_LEN)) {
            errors.add("name", "Name must be 255 characters or less", createGardenForm.getName());
        }

        /*
         * TODO: validate the garden description
         * Default error message: Description must be 512 characters or less and contain some text
         * - Check the description contains letters. Otherwise, display error message
         * - Check the description length is 512 characters or less. Otherwise, display error message
         */

        // Validate garden location - Address
        if (!checkBlank(createGardenForm.getAddress())) {
            if (!checkValidLocationName(createGardenForm.getAddress())) {
                errors.add("address", "Address must only include letters, numbers, spaces, commas, dots, hyphens or apostrophes", createGardenForm.getAddress());
            } else if (checkOverMaxLength(createGardenForm.getAddress(), MAX_DB_STR_LEN)) {
                errors.add("address", "Address must be 255 characters or less", createGardenForm.getAddress());
            }
        }

        // Validate garden location - Suburb
        if (!checkBlank(createGardenForm.getSuburb())) {
            if (!checkValidLocationName(createGardenForm.getSuburb())) {
                errors.add("suburb", "Suburb must only include letters, numbers, spaces, commas, dots, hyphens or apostrophes", createGardenForm.getSuburb());
            } else if (checkOverMaxLength(createGardenForm.getSuburb(), MAX_DB_STR_LEN)) {
                errors.add("suburb", "Suburb must be 255 characters or less", createGardenForm.getSuburb());
            }
        }

        // Validate garden location - City
        if (checkBlank(createGardenForm.getCity())) {
            errors.add("city", "City and Country are required", createGardenForm.getCity());
        } else if (!checkValidLocationName(createGardenForm.getCity())) {
            errors.add("city", "City must only include letters, numbers, spaces, commas, dots, hyphens or apostrophes", createGardenForm.getCity());
        } else if (checkOverMaxLength(createGardenForm.getCity(), MAX_DB_STR_LEN)) {
            errors.add("city", "City must be 255 characters or less", createGardenForm.getCity());
        }

        // Validate garden location - Country
        if (checkBlank(createGardenForm.getCountry())) {
            errors.add("country", "City and Country are required", createGardenForm.getCountry());
        } else if (!checkValidLocationName(createGardenForm.getCountry())) {
            errors.add("country", "Country must only include letters, numbers, spaces, commas, dots, hyphens or apostrophes", createGardenForm.getCountry());
        } else if (checkOverMaxLength(createGardenForm.getCountry(), MAX_DB_STR_LEN)) {
            errors.add("country", "Country must be 255 characters or less", createGardenForm.getCountry());
        }

        // Validate garden location - Postcode
        if (!checkBlank(createGardenForm.getPostcode())) {
            if (!checkValidLocationName(createGardenForm.getPostcode())) {
                errors.add("postcode", "Postcode must only include letters, numbers, spaces, commas, dots, hyphens or apostrophes", createGardenForm.getPostcode());
            } else if (checkOverMaxLength(createGardenForm.getPostcode(), MAX_DB_STR_LEN)) {
                errors.add("postcode", "Postcode must be 255 characters or less", createGardenForm.getPostcode());
            }
        }

        // Validate garden size (if there is one)
        if (!checkBlank(createGardenForm.getSize())) {
            // Handle european decimal format
            createGardenForm.setSize(createGardenForm.getSize().replace(',', '.'));

            if (checkDoubleTooBig(createGardenForm.getSize())) {
                errors.add("size", "Garden size must be at most 72,000m²", createGardenForm.getSize());
            } else if (checkDoubleIsInvalid(createGardenForm.getSize()) || checkDoubleNotPositive(createGardenForm.getSize()) || checkDoubleExceedMaxValue(createGardenForm.getSize(), null)) {
                errors.add("size", "Garden size must be a positive number", createGardenForm.getSize());
            }
        }
    }
}
