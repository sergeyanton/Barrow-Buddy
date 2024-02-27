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
    public static boolean validGardenName(String name) {
        if (name == "" || !name.matches("[a-zA-Z0-9 ,.'-]+")) {
            return false;
        }
        return true;
    }

    /**
     * This method checks if the entered garden location contains only alphanumeric or valid
     * characters
     * 
     * @param name the garden location entered by user
     * @return true if entered garden location is valid, otherwise false
     */
    public static boolean validGardenLocation(String location) {
        if (location == "" || !location.matches("[a-zA-Z0-9 ,.'-]+")) {
            return false;
        }
        return true;
    }

    /**
     * This method will validate that the entered garden size is a positive number, It will also
     * accept a number with a comma as a decimal separator
     * 
     * @param size the garden size entered by user
     * @return Returns a string if the garden size is invalid, otherwise returns an empty optional
     */
    public static Optional<String> validateGardenSize(String size) {
        Boolean isBlank = size.isBlank();
        Boolean isNumber = size.matches("^[0-9]+(,[0-9]+)?$");
        if (!isBlank && isNumber) {
            return Optional.empty();
        }
        return Optional.of("Invalid garden size");
    }
}
