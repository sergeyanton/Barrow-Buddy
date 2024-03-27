package nz.ac.canterbury.team1000.gardenersgrove.util;

import org.mindrot.jbcrypt.BCrypt;

public class Password {
    /**
     * Hashes a given plain text password to be stored in the database
     *
     * @param password plain text String
     * @return String of hashed password
     */
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * Checks if a given plain text password is equal to a given hashed password.
     *
     * @param givenPassword plain text password
     * @param hashedPassword to be compared with the givenPassword
     * @return true if equal, otherwise false
     */
    public static boolean verifyPassword(String givenPassword, String hashedPassword) {
        return BCrypt.checkpw(givenPassword, hashedPassword);
    }
}
