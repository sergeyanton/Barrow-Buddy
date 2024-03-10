package nz.ac.canterbury.seng302.gardenersgrove.classes;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;

/**
 * A class that assists with checking the validity of garden variables.
 */
public class ValidityCheck {

    /**
     * This method checks if the entered garden name contains only alphanumeric or valid characters
     * 
     * @param name the garden name entered by user
     * @return Returns an error message string if the garden size is invalid, otherwise returns an
     *         empty optional
     */
    public static Optional<String> validGardenName(String name) {
        boolean isBlank = name.isBlank();
        boolean isValidName = name.matches("[a-zA-Z0-9 ,.'-]+");
        if (isBlank) {
            return Optional.of("Garden name must not be empty");
        }
        if (!isValidName) {
            return Optional.of(
                    "Garden name must only include letters, numbers, spaces, dots, hyphens or apostrophes");
        }
        return Optional.empty();
    }

    /**
     * This method checks if the entered garden location contains only alphanumeric or valid
     * characters
     * 
     * @param location the garden location entered by user
     * @return Returns an error message string if the garden size is invalid, otherwise returns an
     *         empty optional
     */
    public static Optional<String> validGardenLocation(String location) {
        boolean isBlank = location.isBlank();
        boolean isValidName = location.matches("[a-zA-Z0-9 ,.'-]+");
        if (isBlank) {
            return Optional.of("Location cannot be empty");
        }
        if (!isValidName) {
            return Optional.of(
                    "Location name must only include letters, numbers, spaces, commas, dots, hyphens or apostrophes");
        }
        return Optional.empty();
    }

    /**
     * This method will validate that the entered garden size is a positive number, It will also
     * accept a number with a comma as a decimal separator
     * 
     * @param size the garden size entered by user
     * @return Returns an error message string if the garden size is invalid, otherwise returns an
     *         empty optional
     */
    public static Optional<String> validateGardenSize(String size) {
        boolean isNumber = size.matches("^[0-9]+((\\.|,)[0-9]+)?$");
        if (size.isBlank()) { // valid empty input for no size given
            return Optional.empty();
        }
        if (isNumber) {
            return Optional.empty();
        }
        return Optional.of("Garden size must be a positive number");
    }

    /**
     * Validates the entire garden form (name, location, size)
     *
     * @param name the string for the garden's name
     * @param location the string for the garden's name
     * @param size the string for the garden's size
     * @return true if all three inputs are valid
     */
    public static boolean validGardenForm(String name, String location, String size) {
        return (validGardenName(name).isEmpty() && validGardenLocation(location).isEmpty()
                && validateGardenSize(size).isEmpty());
    }

    /**
     * This method validates that the entered plant name will contain only valid characters (alphanumeric
     * characters, spaces dots, hyphens or apostrophes).
     * @param name the entered plant name
     * @return an error message if the entered plant name is invalid, otherwise returns an empty optional
     */
    public static Optional<String> validatePlantName(String name) {
        boolean isBlank = name.isBlank();
        boolean isValidName = name.matches("[a-zA-Z0-9 ,.'-]+");
        if (isBlank) {
            return Optional.of("Plant name must not be empty");
        }
        if (!isValidName) {
            return Optional.of(
                    "Plant name must only include letters, numbers, spaces, dots, hyphens or apostrophes");
        }
        return Optional.empty();
    }

    public static Optional<String> validatePlantCount(String plantCount) {
        return Optional.empty(); //TODO
    }

    /**
     * This method validates that the entered plant count can only either be empty or a positive integer.
     * @param count the plant count
     * @return an error message if the entered plant count is invalid, otherwise returns an empty optional
     */
    public static Optional<String> validatePlantCount(String count) {
        if (count.isBlank()) {
            return Optional.empty();
        } else {
            if (count.matches("^(?!0+$)[0-9]+$")) {
                return Optional.empty();
            } else {
                return Optional.of("Plant count must only be a positive integer");
            }
        }
    }

    /**
     * This method validates that the entered plant name will contain only up to 512 characters.
     * @param description the plant's description
     * @return an error message if the entered plant description contains more than 512 characters, otherwise
     * returns an empty optional.
     */
    public static Optional<String> validatePlantDescription(String description) {
        if (description.length() > 512) {
            return Optional.of("Plant description must be less than 512 characters");
        } return Optional.empty();
    }

    /**
     * This method will validate that the entered planted-on date is in the valid DD/MM/YYYY format
     * @param date the planted-on date
     * @return Returns an error message string if the date is invalid, otherwise returns an
     *      empty optional
     */
    public static Optional<String> validateDate(String date) {
        try {
            LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            return Optional.of("Date in not in valid format, DD/MM/YYYY)");
        }
        return Optional.empty();
    }

    /**
     * This method checks the entire plant form.
     * @param name the name of the plant
     * @param plantCount the number of plants in the garden
     * @param description a short description of the plant
     * @param plantedOnDate the date that the plant was planted on
     * @return true if plant form has all valid inputs
     */
    public static boolean validPlantForm(String name, String plantCount, String description, String plantedOnDate) {
        return (validatePlantName(name).isEmpty() && validatePlantCount(plantCount).isEmpty()
                && validatePlantDescription(description).isEmpty()) && validateDate(plantedOnDate).isEmpty();
    }
}
