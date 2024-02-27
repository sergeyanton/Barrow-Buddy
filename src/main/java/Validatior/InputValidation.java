package Validatior;

import nz.ac.canterbury.seng302.gardenersgrove.entity.User;

public class InputValidation {
    public String checkInut(User userToCheck) {
        return null;
    }

    private String checkname(String userName) {
        if (userName.isEmpty()) {return "Name field is empty";}
        if (userName.length() > 64){return "Name too long";}
        if (userName.matches("[a-zA-Z\\s'-]+")) {return "Fist name cannot contain illegal characters";}

        return "First name valid";
    }
}
