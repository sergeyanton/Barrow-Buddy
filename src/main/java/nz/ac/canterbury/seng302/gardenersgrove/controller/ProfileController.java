package nz.ac.canterbury.seng302.gardenersgrove.controller;

import jakarta.servlet.http.HttpServletRequest;
import nz.ac.canterbury.seng302.gardenersgrove.controller.dataCollection.RegistrationData;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.validation.Validator;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import nz.ac.canterbury.seng302.gardenersgrove.validation.InputValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Objects;

import static nz.ac.canterbury.seng302.gardenersgrove.util.PageUtils.pageWithError;
import static nz.ac.canterbury.seng302.gardenersgrove.validation.InputValidation.hashPassword;


@Controller
public class ProfileController {
    @Autowired
    public ProfileController(UserService userService) {
        this.userService = userService;
    }
    @Autowired
    private AuthenticationManager authenticationManager;
    private final UserService userService;

    Logger logger = LoggerFactory.getLogger(ProfileController.class);

    /**
     * This method is used to get the profile page for the user
     * @param model Which is used to pass data to the view
     * @return A string that represents the link to the profile page
     */
    @GetMapping("/editProfile")
    public String getEditProfilePage(Model model) {
        logger.info("GET /editProfile");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User currentUser = userService.findEmail(currentPrincipalName);
        model.addAttribute("fName", currentUser.getFname());

        if (!currentUser.getLname().isEmpty()){
            model.addAttribute("lName", currentUser.getLname());
        }
        model.addAttribute("email", currentUser.getEmail());
        model.addAttribute("dateOfBirth", currentUser.getDateOfBirth());
        model.addAttribute("noSurnameCheckBox", currentUser.getLname().isEmpty());


        return "pages/editProfilePage";
    }

    /**
     * This method is used to check the data passed in by the user and then if it is valid, update the user's data
     * @param updatedUser A RegistrationData object that contains the updated user data
     * @param model Which is used to pass data to the view
     * @return A string that represents the link to the profile page
     */
    @PostMapping("/editProfile")
    public String editProfile(HttpServletRequest request, RegistrationData updatedUser, Model model) {
        logger.info("POST /editProfile");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User currentUser = userService.findEmail(currentPrincipalName);
        String oldEmail = currentUser.getEmail();

        InputValidation inputValidation = new InputValidation(userService);

        Validator error;
        if (Objects.equals(currentUser.getEmail(), updatedUser.getEmail())) {
            error = inputValidation.checkRegistrationData(updatedUser,true);
        } else {
            error = inputValidation.checkRegistrationData(updatedUser,false);
        }
        if (!error.getStatus()) {
            return pageWithError(getEditProfilePage(model), model, error.getMessage());
        }

        if (updatedUser.getPassword().isBlank() && !updatedUser.getRetypePassword().isBlank()) {
            return pageWithError(getEditProfilePage(model), model, "Passwords do not match");
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

        if (!updatedUser.getPassword().isEmpty() && !Objects.equals(updatedUser.getPassword(), currentUser.getPassword())){
            currentUser.setPassword(hashPassword(updatedUser.getPassword()));
        }

        if (updatedUser.getDob() != null && !Objects.equals(updatedUser.getDob(), currentUser.getDateOfBirth())){
            currentUser.setDateOfBirth(updatedUser.getDob());
        }

        userService.updateUserByEmail(oldEmail, currentUser);
        userService.authenticateUser(authenticationManager, currentUser, request);

        return "redirect:/profile";
    }
}
