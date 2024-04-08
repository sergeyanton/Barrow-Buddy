package nz.ac.canterbury.team1000.gardenersgrove.controller;

import jakarta.servlet.http.HttpServletRequest;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.form.EditUserForm;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


@Controller
public class ProfileController {
    @Autowired
    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    private AuthenticationManager authenticationManager;
    private final UserService userService;

    private static final String uploadDir = "C:/team-1000/src/main/resources/static/images/"; // "../images/";


    Logger logger = LoggerFactory.getLogger(ProfileController.class);

    /**
     * This method is used to get the profile page for the user
     * 
     * @param editUserForm the form used to edit the user's profile
     * @return A string that represents the link to the profile page
     */
    @GetMapping("/editProfile")
    public String getEditProfilePage(EditUserForm editUserForm) {
        logger.info("GET /editProfile");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User currentUser = userService.findEmail(currentPrincipalName);

        editUserForm.setFromUser(currentUser);

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
    public String editProfile(HttpServletRequest request, EditUserForm editUserForm, BindingResult bindingResult, @RequestParam("profilePicture") MultipartFile profilePicture) {
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

        currentUser.setFname(editUserForm.getFirstName());
        currentUser.setLname(editUserForm.getLastName());
        currentUser.setEmail(editUserForm.getEmail());
        if (!editUserForm.getPassword().isEmpty()) {
            currentUser.setPassword(editUserForm.getPassword());
        }

        // normalize the file path
         String fileName = StringUtils.cleanPath(profilePicture.getOriginalFilename());

        // save the file on the local file system
        try {
            Path filePath = Paths.get(uploadDir, fileName);
            //if (!Files.exists(uploadPath)) {
            Files.createDirectories(filePath.getParent());
            //}

            // Path filePath = uploadPath.resolve(fileName);
            // System.out.println("File Path: " + filePath);
            Files.copy(profilePicture.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            currentUser.setProfilePicturePath("/images/" + fileName);
            System.out.println(currentUser.getProfilePicturePath());

//            Path path = Paths.get(uploadDir);
//            Path filePath = path.resolve(fileName);
//            Files.copy(profilePicture.getInputStream(), filePath);StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        userService.updateUserByEmail(oldEmail, currentUser);
        userService.authenticateUser(authenticationManager, currentUser, request);

        return "redirect:/profile";
    }

}
