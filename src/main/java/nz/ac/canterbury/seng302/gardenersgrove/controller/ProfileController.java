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
        model.addAttribute("dateOfBirth", currentUser.getDateOfBirth());

        return "pages/editProfilePage";
    }

    @PostMapping
    public String editProfile(RegistrationData updatedUser) {
        logger.info("POST /editProfile");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User currentUser = userService.getUserByEmail(currentPrincipalName);

        InputValidation inputValidation = new InputValidation(userService);
        Validator error = inputValidation.dataCheck(updatedUser);

        if (updatedUser.getfName() != null) {
            currentUser.setFname(updatedUser.getfName());
        }

        if (updatedUser.getlName() != null) {
            currentUser.setLname(updatedUser.getlName());
        }

        if (updatedUser.getEmail() != null) {
            currentUser.setEmail(updatedUser.getEmail());
        }

        if (updatedUser.getPassword() != null) {
            currentUser.setPassword(updatedUser.getPassword());
        }

        if (updatedUser.getDob() != null) {
            currentUser.setDateOfBirth(updatedUser.getDob());
        }



        return "redirect:/profile";
    }
}
