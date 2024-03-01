package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.controller.dataCollection.RegistrationData;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;

import static nz.ac.canterbury.seng302.gardenersgrove.validation.InputValidation.*;

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
    public String submitRegistration(RegistrationData newUser, RedirectAttributes redirectAttributes) {
        logger.info("POST /register");
        Validator error = dataCheck(newUser);
        if (!error.getStatus()){
            errorMessage = error.getMessage();
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/register";

        }
        return "redirect:./profile";
    }

    private Validator dataCheck(RegistrationData newUser){

        Validator nameCheck = checkName(newUser.getfName());
        if (!nameCheck.getStatus()){return nameCheck;}

        if (newUser.getNoSurnameCheckBox() != null) {
            Validator surnameCheck = checkName(newUser.getlName());
            if (!surnameCheck.getStatus()){return surnameCheck;}
        }

        Validator emailCheck = checkEmail(newUser.getEmail(), false);
        if (!emailCheck.getStatus()){return emailCheck;}

        Validator addressCheck = checkEmail(newUser.getAddress(), false);
        if (!addressCheck.getStatus()){return addressCheck;}

        if(!Objects.equals(newUser.getPassword(), newUser.getRetypePassword())){
            return new Validator(false, "Passwords do not match");
                }

        Validator passwordCheck = checkPassword(newUser.getPassword());
        if (!passwordCheck.getStatus()){return passwordCheck;}

        Validator dobCheck = checkDob(newUser.getDob());
        if (!dobCheck.getStatus()){return dobCheck;}

        return new Validator(true, "");
    }

}
