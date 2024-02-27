package nz.ac.canterbury.seng302.gardenersgrove.classes;


/**
 * A class that assists with checking the validity of garden variables.
 */
public class ValidityCheck {
    // A garden name is considered valid
    public static boolean validGardenName(String name) 
    {
        if (name == "" || !name.matches("[a-zA-Z0-9 ,.'-]+")) {return false;}
        return true;
    }
}
