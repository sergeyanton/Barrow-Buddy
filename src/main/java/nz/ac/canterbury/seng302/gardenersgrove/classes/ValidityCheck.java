package nz.ac.canterbury.seng302.gardenersgrove.classes;

import java.util.Optional;

/**
 * A class that assists with checking the validity of garden variables.
 */
public class ValidityCheck {

    /**
     * This method checks if the entered garden name contains only alphanumeric or valid characters
     * 
     * @param name the garden name entered by user
     * @return true if entered garden name is valid, otherwise false
     */
    public static Optional<String> validGardenName(String name) {
        boolean isBlank = name.isBlank();
        boolean isValidName = name.matches("[a-zA-Z0-9 ,.'-]+");
        if (isBlank) {
            return Optional.of("Garden name must not be empty");
        }
        if (!isValidName) {
            return Optional.of("Garden name must only include letters, numbers, spaces, dots, hyphens or apostrophes");
        }
        return Optional.empty();
    }

    /**
     * This method checks if the entered garden location contains only alphanumeric or valid
     * characters
     * 
     * @param location
     * the garden location entered by user
     * @return true if entered garden location is valid, otherwise false
     */
    public static Optional<String> validGardenLocation(String location) {
        boolean isBlank = location.isBlank();
        boolean isValidName = location.matches("[a-zA-Z0-9 ,.'-]+");
        if (isBlank) {
            return Optional.of("Location cannot be empty");
        }
        if (!isValidName) {
            return Optional.of("Location name must only include letters, numbers, spaces, commas, dots, hyphens or apostrophes");
        }
        return Optional.empty();
    }

    /**
     * This method will validate that the entered garden size is a positive number, It will also
     * accept a number with a comma as a decimal separator
     * 
     * @param size the garden size entered by user
     * @return Returns a string if the garden size is invalid, otherwise returns an empty optional
     */
    public static Optional<String> validateGardenSize(String size) {
        boolean isBlank = size.isBlank();
        boolean isNumber = size.matches("^[0-9]+((.|,)[0-9]+)?$");
        if (!isBlank && isNumber) {
            return Optional.empty();
        }
        return Optional.of("Garden size must be a positive number");
    }

    public static boolean validGardenForm(String name, String location, String size) {
        return (validGardenName(name).isEmpty() && validGardenLocation(location).isEmpty() &&
                validateGardenSize(size).isEmpty());
    }
}
