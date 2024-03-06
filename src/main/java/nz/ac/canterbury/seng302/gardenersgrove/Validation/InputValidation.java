package nz.ac.canterbury.seng302.gardenersgrove.Validation;

import nz.ac.canterbury.seng302.gardenersgrove.controller.dataCollection.LogInData;
import nz.ac.canterbury.seng302.gardenersgrove.controller.dataCollection.RegistrationData;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Validator;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;

import java.time.LocalDate;
import java.util.Objects;

public class InputValidation {
    public static Validator checkRegistrationData(RegistrationData newUser, UserService userService){
        Validator nameCheck = checkName(newUser.getfName());
        if (!nameCheck.getStatus()) return nameCheck;

        if (!newUser.getNoSurnameCheckBox()) {
            Validator surnameCheck = checkName(newUser.getlName());
            if (!surnameCheck.getStatus()) return surnameCheck;
        }

        Validator emailCheck = checkEmailSignup(newUser.getEmail(), userService);
        if (!emailCheck.getStatus()) return emailCheck;

        if(!Objects.equals(newUser.getPassword(), newUser.getRetypePassword())){
            return new Validator(false, "Passwords do not match");
        }

        Validator passwordCheck = checkPassword(newUser.getPassword());
        if (!passwordCheck.getStatus()) return passwordCheck;

        Validator dobCheck = checkDob(newUser.getDob());
        if (!dobCheck.getStatus()){return dobCheck;}

        return new Validator(true, "");
    }

    public static Validator checkLoginData(LogInData newUser){
        Validator emailCheck = checkEmailLogin(newUser.getEmail());
        if (!emailCheck.getStatus()) return emailCheck;

        Validator passwordCheck = checkPasswordEmpty(newUser.getPassword());
        if (!passwordCheck.getStatus()) return passwordCheck;

        return new Validator(true, "");
    }

    public static Validator checkName(String userName) {
        Validator isValid = new Validator(true, "Ok");
        if (userName.isBlank()) {isValid.setValid(false,"{First/Last} name cannot be empty and must only include letters, spaces, hyphens or apostrophes");}
        if (userName.length() > 64) {isValid.setValid(false,"{First/Last} name cannot be empty and must only include letters, spaces, hyphens or apostrophes");}
        if (!userName.matches("^[a-zA-Z\\s'-]+$")) {isValid.setValid(false,"{First/Last} name cannot be empty and must only include letters, spaces, hyphens or apostrophes");}
        return isValid;
    }

    public static Validator checkEmailSignup(String userEmail, UserService userService) {
        Validator isValid = new Validator(true, "Ok");
        if (userEmail.isBlank()) {isValid.setValid(false,"Email address must be in the form ‘jane@doe.nz’");}
        if (!userEmail.matches("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$")) {isValid.setValid(false,"Email address must be in the form ‘jane@doe.nz’");}

        if (userService.checkEmail(userEmail)) {isValid.setValid(false,"Email address is already in use");}

        return isValid;
    }

    public static Validator checkEmailLogin(String userEmail) {
        Validator isValid = new Validator(true, "Ok");
        if (userEmail.isBlank()) {isValid.setValid(false,"Email cannot be empty.");}
        if (!userEmail.matches("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$")) {isValid.setValid(false,"Email address must be in the form ‘jane@doe.nz’");}

        return isValid;
    }

    public static Validator checkPasswordEmpty(String password) {
        Validator isValid = new Validator(true,"Ok");
        if (password.isBlank()) {
            isValid.setValid(false,"Password cannot be empty.");
        }
        return isValid;
    }

    /**
     * Checks that the given date of birth is a valid format, valid age range, and not in the future.
     * Returns an isValid object, containing a boolean date validity and an accompanying message.
     * @param userDob the given date of birth entered by the user.
     * @return isValid
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

    public static Validator checkPassword(String password) {
        Validator isValid = new Validator(true,"Ok");
        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z\\d]).{8,}$")) {
            isValid.setValid(false,"Your password must be  at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character.");
        }
        return isValid;
    }

}
