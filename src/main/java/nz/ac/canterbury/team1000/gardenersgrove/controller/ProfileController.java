package nz.ac.canterbury.team1000.gardenersgrove.controller;

import jakarta.servlet.http.HttpServletRequest;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.form.EditUserForm;
import nz.ac.canterbury.team1000.gardenersgrove.form.UpdatePasswordForm;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;
import nz.ac.canterbury.team1000.gardenersgrove.util.Password;

import static nz.ac.canterbury.team1000.gardenersgrove.form.FormUtils.checkBlank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.format.DateTimeFormatter;


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
     * 
     * @param editUserForm the form used to edit the user's profile
     * @return A string that represents the link to the profile page
     */
    @GetMapping("/editProfile")
    public String getEditProfilePage(@ModelAttribute("editUserForm") EditUserForm editUserForm) {
        logger.info("GET /editProfile");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User currentUser = userService.findEmail(currentPrincipalName);

        editUserForm.setFirstName(currentUser.getFname());
        editUserForm.setLastName(currentUser.getLname());
        editUserForm.setEmail(currentUser.getEmail());
        if (currentUser.getDateOfBirth() != null) editUserForm.setDob(currentUser.getDateOfBirthString());
        editUserForm.setNoSurnameCheckBox(editUserForm.getLastName() == null || editUserForm.getLastName().isEmpty());

        return "pages/editProfilePage";
    }

    /**
     * This method is used to check the data passed in by the user and then if it is valid, update
     * the user's data
     * 
     * @param request the request object
     * @param editUserForm the form used to edit the user's profile
     * @param bindingResult the binding result for binding the form errors
     * @return A string that represents the link to the profile page
     */
    @PostMapping("/editProfile")
    public String editProfile(HttpServletRequest request,
                              @ModelAttribute("editUserForm") EditUserForm editUserForm,
                              BindingResult bindingResult) {
        logger.info("POST /editProfile");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User currentUser = userService.findEmail(currentPrincipalName);
        String oldEmail = currentUser.getEmail();

        EditUserForm.validate(editUserForm, bindingResult, currentUser);

        if (!bindingResult.hasFieldErrors("email") && !currentUser.getEmail().equals(oldEmail) && userService.checkEmail(editUserForm.getEmail())) {
            bindingResult.addError(new FieldError("registrationForm", "email", editUserForm.getEmail(), false, null, null, "Email address is already in use"));
        }

        if (bindingResult.hasErrors()) {
            return "pages/editProfilePage";
        }

        User edit = editUserForm.getUser();

        currentUser.setFname(edit.getFname());
        currentUser.setLname(edit.getLname());
        currentUser.setEmail(edit.getEmail());
        if (!editUserForm.getPassword().isEmpty()) {
            currentUser.setPassword(edit.getPassword());
        }
        currentUser.setDateOfBirth(edit.getDateOfBirth());

        userService.updateUserByEmail(oldEmail, currentUser);
        userService.authenticateUser(authenticationManager, currentUser, request);

        return "redirect:/profile";
    }

    /**
     * This method is used to get the update password page for the user
     * 
     * @param updatePasswordForm the form used to update the user's password
     * @return a string that represents the link to the update password page
     */
    @GetMapping("/editProfile/updatePassword")
    public String getUpdatePassword(UpdatePasswordForm updatePasswordForm) {
        logger.info("GET /editProfile/updatePassword");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User currentUser = userService.findEmail(currentPrincipalName);

        updatePasswordForm.setFormUser(currentUser);

        return "pages/updatePasswordPage";
    }

    /**
     * This method is used to check the old and new password entered by the user and if it is valid,
     * updates the user's password to the new password
     * 
     * @param updatePasswordForm the form used to update the user's password
     * @return A string that represents the link to the profile page
     */
    @PostMapping("/editProfile/updatePassword")
    public String postUpdatePassword(HttpServletRequest request, UpdatePasswordForm updatePasswordForm) {
        logger.info("POST /editProfile/updatePassword");

        return "redirect:/editProfile";
    }
}
