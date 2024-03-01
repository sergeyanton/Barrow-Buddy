package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.dataCollection.RegistrationData;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Objects;

import static nz.ac.canterbury.seng302.gardenersgrove.entity.User.*;

@Controller
public class RegistrationController {
    Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    private String errorMessage = "";

    /**
     * Gets the thymeleaf page representing the /register page (a basic welcome screen with some links)
     * @return thymeleaf registrationPage
     */
    @GetMapping("/register")
    public String registration(Model model) {
        logger.info("GET /register");
        model.addAttribute("errorMessage", errorMessage);
        return "pages/registrationPage";
    }

    @PostMapping("/register")
    public String submitRegistration(RegistrationData newUser) {
        logger.info("POST /register");
        String errorMsg = dataCheck(newUser);
        return "redirect:./home";
    }

    private String dataCheck(RegistrationData newUser){

        Validator nameCheck = checkName(newUser.getfName());
        if (!nameCheck.getStatus()){return nameCheck.getMessage();}

        if (newUser.getNoSurnameCheckBox() != null) {
            Validator surnameCheck = checkName(newUser.getlName());
            if (!surnameCheck.getStatus()){return surnameCheck.getMessage();}
        }

        Validator emailCheck = checkEmail(newUser.getEmail());
        if (!emailCheck.getStatus()){return emailCheck.getMessage();}

//        Validator addressCheck = checkAddress(newUser.getAddress());

        if(!Objects.equals(newUser.getPassword(), newUser.getRetypePassword())){
            return "Passwords do not match";
        }

        Validator passwordCheck = checkPassword(newUser.getPassword());
        if (!passwordCheck.getStatus()){return passwordCheck.getMessage();}

        Validator dobCheck = checkDob(newUser.getDob());
        if (!dobCheck.getStatus()){return dobCheck.getMessage();}


        return "Ok";
    }

}
