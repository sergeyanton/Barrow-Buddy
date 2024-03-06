package nz.ac.canterbury.seng302.gardenersgrove.Validation;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Validator;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDate;

/**
 * This class provides methods for input validation, including checking usernames,
 * email addresses, passwords, and date of birth according to the restriction.
 */
public class InputValidation {


    /**
     * Checks if the provided name from the user is valid.
     * @param userName The username to validate.
     * @return A Validator object indication whether the username is valid and an accompanying message.
     */
    public static Validator checkName(String userName) {
        Validator isValid = new Validator(true, "Ok");
        if (userName.isBlank()) {isValid.setValid(false,"{First/Last} name cannot be empty and must only include letters, spaces, hyphens or apostrophes");}
        if (userName.length() > 64) {isValid.setValid(false,"{First/Last} name cannot be empty and must only include letters, spaces, hyphens or apostrophes");}
        if (!userName.matches("^[a-zA-Z\\s'-]+$")) {isValid.setValid(false,"{First/Last} name cannot be empty and must only include letters, spaces, hyphens or apostrophes");}
        return isValid;
    }

    /**
     * Checks if the provided email for signup is valid and not already in use.
     * @param userEmail The email address to validate for signup.
     * @param userService An instance of the UserService class to check if the email is already in use.
     * @return A Validator object indicating whether the email address is valid and an accompanying message.
     */
    public static Validator checkEmailSignup(String userEmail, UserService userService) {
        Validator isValid = new Validator(true, "Ok");
        if (userEmail.isBlank()) {isValid.setValid(false,"Email address must be in the form ‘jane@doe.nz’");}
        if (!userEmail.matches("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$")) {isValid.setValid(false,"Email address must be in the form ‘jane@doe.nz’");}

        if (userService.checkEmail(userEmail)) {isValid.setValid(false,"Email address is already in use");}

        return isValid;
    }

    /**
     * Checks if the provided email address for login is valid.
     * @param userEmail The email address to validate for login.
     * @return A Validator object indicating whether the email address is valid.
     */
    public static Validator checkEmailLogin(String userEmail) {
        Validator isValid = new Validator(true, "Ok");
        if (userEmail.isBlank()) {isValid.setValid(false,"Email cannot be empty.");}
        if (!userEmail.matches("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$")) {isValid.setValid(false,"Email address must be in the form ‘jane@doe.nz’");}

        return isValid;
    }

    /**
     * Checks if the provided password is empty.
     * @param password The password to validate.
     * @return A Validator object indicating whether the password is valid and an accompanying message.
     */
    public static Validator checkPasswordEmpty(String password) {
        Validator isValid = new Validator(true,"Ok");
        if (password.isBlank()) {
            isValid.setValid(false,"Password cannot be empty.");
        }
        return isValid;
    }

    /**
     * Checks if the provided date of birth is valid, within a valid age range, and not in the future.
     * @param userDob The date of birth to validate.
     * @return A Validator object indicating whether the date of birth is valid and an accompanying message.
     */
    public static Validator checkDob(LocalDate userDob) {
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        int currentMonth = currentDate.getMonthValue();
        int currentDay = currentDate.getDayOfMonth();

        Validator isValid = new Validator(true, "Ok");

        int year = userDob.getYear();
        int month = userDob.getMonthValue();
        int day = userDob.getDayOfMonth();

        if (month %2 != 0 && day > 30) {isValid.setValid(false,"Date in not in valid format, DD/MM/YYYY)");}
        if (year %4 == 0 && month == 2 && day > 29){isValid.setValid(false,"Date in not in valid format, DD/MM/YYYY)");}
        if (year %4 != 0 && month == 2 && day > 28){isValid.setValid(false,"Date in not in valid format, DD/MM/YYYY)");}

        if (year > currentYear - 13){isValid.setValid(false,"You must be 13 years or older to create an account");}
        if (year < currentYear - 120){isValid.setValid(false,"The maximum age allowed is 120 years");}

        // checks that date given is not in the future
        if (year > currentYear) {
            // invalid year
            isValid.setValid(false, "Date cannot be in the future");
        } else if (year == currentYear && month > currentMonth) {
            // invalid month
            isValid.setValid(false, "Date cannot be in the future");
        } else if (year == currentYear && month == currentMonth && day > currentDay) {
            // invalid day
            isValid.setValid(false, "Date cannot be in the future");
        }

        return isValid;
    }

    /**
     * Checks if the provided password meets the required criteria.
     * @param password The password to validate.
     * @return A Validator object indicating whether the password is valid and an accompanying message.
     */
    public static Validator checkPassword(String password) {
        Validator isValid = new Validator(true,"Ok");
        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z\\d]).{8,}$")) {
            isValid.setValid(false,"Your password must be  at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character.");
        }
        return isValid;
    }

    /**
     * Hashes a given plain text password to be stored in the database
     * @param password plain text String
     * @return String of hashed password
     */
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }


    /**
     * Checks if a given plain text password is equal to a given hashed password.
     * @param givenPassword plain text password
     * @param hashedPassword to be compared with the givenPassword
     * @return true if equal, otherwise false
     */
    public static boolean verifyPassword(String givenPassword, String hashedPassword) {
        return BCrypt.checkpw(givenPassword, hashedPassword);
    }

}
