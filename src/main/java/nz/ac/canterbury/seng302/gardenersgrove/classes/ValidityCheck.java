package nz.ac.canterbury.seng302.gardenersgrove.classes;


/**
 * A class that assists with checking the validity of garden variables.
 */
public class ValidityCheck {
    
    /**
     * This method checks if the entered garden name contains only alphanumeric or valid characters
     * @param name the garden name entered by user
     * @return true if entered garden name is valid, otherwise false
     */
    public static boolean validGardenName(String name) 
    {
        return !name.isEmpty() && name.matches("[a-zA-Z0-9 ,.'-]+");
    }

    /**
     * This method checks if the entered garden location contains only alphanumeric or valid characters
     * @param location the garden location entered by user
     * @return true if entered garden location is valid, otherwise false
     */
    public static boolean validGardenLocation(String location) 
    {
        return !location.isEmpty() && location.matches("[a-zA-Z0-9 ,.'-]+");
    }

    /**
     * This method checks if the entered garden size contains only numeric characters
     * @param size the garden size string entered by user
     * @return true if entered garden location is valid, otherwise false
     */
    public static boolean validGardenSize(String size)
    {
        return !size.isEmpty() && size.matches("[0-9,.]+");
    }
}
