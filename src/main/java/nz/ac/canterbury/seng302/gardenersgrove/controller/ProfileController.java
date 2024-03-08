package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.controller.dataCollection.RegistrationData;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Validator;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import nz.ac.canterbury.seng302.gardenersgrove.validation.InputValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Objects;

import static nz.ac.canterbury.seng302.gardenersgrove.controller.AccountController.pageWithError;


@Controller
public class ProfileController {
    @Autowired
    public ProfileController(UserService userService) {
        this.userService = userService;
    }
    private final UserService userService;

    Logger logger = LoggerFactory.getLogger(ProfileController.class);

    @GetMapping("/editProfile")
    public String getEditProfilePage(Model model) {
        logger.info("GET /editProfile");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User currentUser = userService.getUserByEmail(currentPrincipalName);
        model.addAttribute("fName", currentUser.getFname());

        if (!currentUser.getLname().isEmpty()){
            model.addAttribute("lName", currentUser.getLname());
        }
        model.addAttribute("email", currentUser.getEmail());
        model.addAttribute("password", currentUser.getPassword());
        model.addAttribute("retypePassword", currentUser.getPassword());
        model.addAttribute("dateOfBirth", currentUser.getDateOfBirth());


        return "pages/editProfilePage";
    }

    @PostMapping("/editProfile")
    public String editProfile(RegistrationData updatedUser, Model model) {
        logger.info("POST /editProfile");
        String oldEmail = updatedUser.getEmail();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User currentUser = userService.getUserByEmail(currentPrincipalName);
        InputValidation inputValidation = new InputValidation(userService);

        Validator error;
        if (Objects.equals(currentUser.getEmail(), updatedUser.getEmail())) {
            error = inputValidation.dataCheck(updatedUser,true);
        } else {
            error = inputValidation.dataCheck(updatedUser,false);
        }
        if (!error.getStatus()) {
            return pageWithError("pages/editProfilePage", model, error.getMessage());
        }

        if (updatedUser.getfName() != null &&!Objects.equals(updatedUser.getfName(), currentUser.getFname())) {
            currentUser.setFname(updatedUser.getfName());
        }

        if (updatedUser.getlName() != null && !Objects.equals(updatedUser.getlName(), currentUser.getLname())) {
            currentUser.setLname(updatedUser.getlName());
        }

        if (updatedUser.getEmail() != null && !Objects.equals(updatedUser.getEmail(), currentUser.getEmail())){
            currentUser.setEmail(updatedUser.getEmail());
        }

        if (updatedUser.getPassword() != null && !Objects.equals(updatedUser.getPassword(), currentUser.getPassword())){
            currentUser.setPassword(updatedUser.getPassword());
        }

        if (updatedUser.getDob() != null && !Objects.equals(updatedUser.getDob(), currentUser.getDateOfBirth())){
            currentUser.setDateOfBirth(updatedUser.getDob());
        }

//        userService.updateUserByEmail(oldEmail, currentUser);


        return "redirect:/profile";
    }
}
