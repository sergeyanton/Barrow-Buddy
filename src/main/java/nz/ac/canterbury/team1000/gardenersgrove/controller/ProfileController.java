package nz.ac.canterbury.team1000.gardenersgrove.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import nz.ac.canterbury.team1000.gardenersgrove.entity.FriendRelationship;
import nz.ac.canterbury.team1000.gardenersgrove.entity.Garden;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.form.EditUserForm;
import nz.ac.canterbury.team1000.gardenersgrove.form.PictureForm;
import nz.ac.canterbury.team1000.gardenersgrove.form.SearchFriendsForm;
import nz.ac.canterbury.team1000.gardenersgrove.form.UpdatePasswordForm;
import nz.ac.canterbury.team1000.gardenersgrove.service.EmailService;
import nz.ac.canterbury.team1000.gardenersgrove.service.FriendRelationshipService;
import nz.ac.canterbury.team1000.gardenersgrove.service.GardenService;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;

import nz.ac.canterbury.team1000.gardenersgrove.util.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class ProfileController {
    private final UserService userService;
    private final EmailService emailService;

    private final FriendRelationshipService friendRelationshipService;

    private final GardenService gardenService;

    @Autowired
    public ProfileController(UserService userService, EmailService emailService, FriendRelationshipService friendRelationshipService,
        GardenService gardenService) {
        this.userService = userService;
        this.emailService = emailService;
        this.friendRelationshipService = friendRelationshipService;
        this.gardenService = gardenService;
    }

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";
    final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    /**
     * Helper method to check if two users are friends.
     * @param userId
     * @param otherUserId
     * @return
     */
    public boolean areUsersFriends(Long userId, Long otherUserId) {
        FriendRelationship sentRelationship = friendRelationshipService.getFriendRelationship(userId, otherUserId);
        FriendRelationship receivedRelationship = friendRelationshipService.getFriendRelationship(otherUserId, userId);
        boolean sentFriends = (sentRelationship != null && sentRelationship.getStatus().equals(
            Status.APPROVED));
        boolean receivedFriends = (receivedRelationship != null && receivedRelationship.getStatus().equals(
            Status.APPROVED));
        return sentFriends || receivedFriends;
    }

    /**
     * Tries to access a given user's profile if the logged in user is allowed.
     * Must be friends or that user's profile.
     * @param userId
     * @return
     * @throws ResponseStatusException
     */
    private User tryToViewUserProfile(Long userId) throws ResponseStatusException {
        try {
            User owner = userService.getUserById(userId);
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "User not found"
            );
        }

        boolean areFriends = areUsersFriends(userService.getLoggedInUser().getId(), userId);
        if (!areFriends) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "You can only view your own or your friends' gardens"
            );
        }
        return userService.getUserById(userId);
    }

    /**
     * Gets the thymeleaf page representing the /profile page, displaying the currently logged-in
     * user's account details. Will only work if the user is logged in.
     *
     * @return thymeleaf profilePage
     */
    @GetMapping("/profile")
    public String getProfilePage(Model model, @ModelAttribute("profilePictureForm") PictureForm profilePictureForm) {
        logger.info("GET /profile");
        User currentUser = userService.getLoggedInUser();

        model.addAttribute("fName", currentUser.getFname());
        model.addAttribute("lName", currentUser.getLname());
        model.addAttribute("email", currentUser.getEmail());
        if (currentUser.getDateOfBirth() != null) {
            model.addAttribute("dob", currentUser.getDateOfBirthString());
        }
        model.addAttribute("picturePath", currentUser.getPicturePath());
        model.addAttribute("loggedInUserId", currentUser.getId());
        model.addAttribute("id", currentUser.getId());

        return "pages/profilePage";
    }

    /**
     * Gets the thymeleaf page representing the profile page of the given user id, displaying that
     * user's account details. Will only work logged in as that user or as that user's friend.
     *
     * @return thymeleaf profilePage
     */
    @GetMapping("/user/{id}/profile")
    public String getProfilePage(Model model, @PathVariable("id") Long id, @ModelAttribute("profilePictureForm") PictureForm profilePictureForm) {
        logger.info("GET /profile");
        User loggedInUser = userService.getLoggedInUser();

        if (id.equals(loggedInUser.getId())) {
            return "redirect:/profile";
        }

        User currentUser = tryToViewUserProfile(id);

        model.addAttribute("fName", currentUser.getFname());
        model.addAttribute("lName", currentUser.getLname());
        model.addAttribute("email", currentUser.getEmail());
        if (currentUser.getDateOfBirth() != null) {
            model.addAttribute("dob", currentUser.getDateOfBirthString());
        }

        List<Garden> gardensList = gardenService.getUserGardens(currentUser.getId());

        model.addAttribute("picturePath", currentUser.getPicturePath());
        model.addAttribute("loggedInUserId", loggedInUser.getId());
        model.addAttribute("id", id);
        model.addAttribute("gardens", gardensList);

        return "pages/friendProfile";
    }

    /**
     * Handles POST requests from the /profile endpoint.
     * Specifically, this handles the uploading of a new profile picture.
     *
     * @param request            the HttpServletRequest object containing the request information
     * @param profilePictureForm the PictureForm object containing the form's user inputted image file
     * @param bindingResult      the BindingResult object for validation errors
     * @return the view to display:
     * - If there are validation errors with the image, stays on the form but render the user's actual profile picture.
     * - Else, redirect to the user's (edited) profile page with the new profile picture.
     * @throws IOException IOException
     */
    @PostMapping("/profile")
    public String handleProfilePictureUpload(HttpServletRequest request,
                                             @ModelAttribute("profilePictureForm") PictureForm profilePictureForm,
                                             BindingResult bindingResult,
                                             Model model) throws IOException {
        User currentUser = userService.getLoggedInUser();

        PictureForm.validate(profilePictureForm, bindingResult, currentUser);

        if (!profilePictureForm.getPictureFile().isEmpty() && !bindingResult.hasFieldErrors("pictureFile")) {
            Path uploadDirectoryPath = Paths.get(UPLOAD_DIRECTORY);

            if (!Files.exists(uploadDirectoryPath)) {
                try {
                    Files.createDirectories(uploadDirectoryPath);
                } catch (IOException e) {
                    throw new IOException("Failed to create upload directory", e);
                }
            }

            String filename = profilePictureForm.getPictureFile().getOriginalFilename();
            Path filePath = uploadDirectoryPath.resolve(filename);
            Files.write(filePath, profilePictureForm.getPictureFile().getBytes());
            currentUser.setPicturePath("/uploads/" + filename);
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("fName", currentUser.getFname());
            model.addAttribute("lName", currentUser.getLname());
            model.addAttribute("email", currentUser.getEmail());
            if (currentUser.getDateOfBirth() != null) {
                model.addAttribute("dob", currentUser.getDateOfBirthString());
            }
            model.addAttribute("picturePath", currentUser.getPicturePath());
            return "pages/profilePage";
        }

        userService.updateUserByEmail(currentUser.getEmail(), currentUser);
        userService.authenticateUser(authenticationManager, currentUser, request);

        return "redirect:/profile";
    }

    /**
     * This method is used to get the profile page for the user
     * 
     * @param editUserForm the form used to edit the user's profile
     * @return A string that represents the link to the profile page
     */
    @GetMapping("/editProfile")
    public String getEditProfilePage(@ModelAttribute("editUserForm") EditUserForm editUserForm) {
        logger.info("GET /editProfile");
        User currentUser = userService.getLoggedInUser();

        editUserForm.setFirstName(currentUser.getFname());
        editUserForm.setLastName(currentUser.getLname());
        editUserForm.setEmail(currentUser.getEmail());
        if (currentUser.getDateOfBirth() != null) editUserForm.setDob(currentUser.getDateOfBirthString());
        editUserForm.setNoSurnameCheckBox(editUserForm.getLastName() == null || editUserForm.getLastName().isEmpty());
        editUserForm.setPicturePath(currentUser.getPicturePath());

        return "pages/editProfilePage";
    }

    /**
     * Handles POST requests from the /editProfile endpoint.
     * Checks for errors and accordingly edits the user's details.
     *
     * @param request           the HttpServletRequest object containing the request information
     * @param editUserForm      the EditUserForm object containing the form's user inputs
     * @param bindingResult     the BindingResult object for validation errors
     * @return the view to display:
     * - If there are validation errors, stays on the 'Edit Profile' form.
     * - Else, redirect to the user's (edited) profile page.
     * @throws IOException IOException
     */
    @PostMapping("/editProfile")
    public String editProfile(HttpServletRequest request,
                              @ModelAttribute("editUserForm") EditUserForm editUserForm,
                              BindingResult bindingResult) throws IOException {
        logger.info("POST /editProfile");
        User currentUser = userService.getLoggedInUser();
        String oldEmail = currentUser.getEmail();

        EditUserForm.validate(editUserForm, bindingResult, currentUser);

        if (!bindingResult.hasFieldErrors("email") && !editUserForm.getEmail().equals(oldEmail) && userService.checkEmail(editUserForm.getEmail())) {
            bindingResult.addError(new FieldError("editUserForm", "email", editUserForm.getEmail(), false, null, null, "Email address is already in use"));
        }

        if (!editUserForm.getPictureFile().isEmpty() && !bindingResult.hasFieldErrors("pictureFile")) {
            Path uploadDirectoryPath = Paths.get(UPLOAD_DIRECTORY);
            if (!Files.exists(uploadDirectoryPath)) {
                try {
                    Files.createDirectories(uploadDirectoryPath);
                } catch (IOException e) {
                    throw new IOException("Failed to create upload directory", e);
                }
            }
            Path filePath = uploadDirectoryPath.resolve(editUserForm.getPictureFile().getOriginalFilename());
            Files.write(filePath, editUserForm.getPictureFile().getBytes());
            editUserForm.setPicturePath("/uploads/" + editUserForm.getPictureFile().getOriginalFilename());
        }

        if (bindingResult.hasErrors()) {
            if (bindingResult.hasFieldErrors("pictureFile")) editUserForm.setPicturePath(currentUser.getPicturePath());
            return "pages/editProfilePage";
        }

        currentUser.setFname(editUserForm.getFirstName());
        currentUser.setLname(editUserForm.getLastName());
        currentUser.setEmail(editUserForm.getEmail());
        currentUser.setDateOfBirth(editUserForm.getDobLocalDate());
        currentUser.setPicturePath(editUserForm.getPicturePath());

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
    public String getUpdatePassword(@ModelAttribute("updatePasswordForm") UpdatePasswordForm updatePasswordForm) {
        logger.info("GET /editProfile/updatePassword");
        return "pages/updatePasswordPage";
    }

    /**
     * Handles POST requests to the /editProfile/updatePassword endpoint.
     * Changes the password for the user, if they enter the password they currently have.
     *
     * @param request the HttpServletRequest object containing the request information
     * @param updatePasswordForm the UpdatePasswordForm object representing the password fields
     * @param bindingResult the BindingResult object for validation errors
     * @return a String representing the view to display after login:
     *         - If there are validation errors, returns the login page to display errors.
     *         - If login is successful, redirects to the application's home page.
     */
    @PostMapping("/editProfile/updatePassword")
    public String postUpdatePassword(HttpServletRequest request,
                                     @ModelAttribute("updatePasswordForm") UpdatePasswordForm updatePasswordForm,
                                     BindingResult bindingResult) {
        logger.info("POST /editProfile/updatePassword");
        User currentUser = userService.getLoggedInUser();
        updatePasswordForm.setExistingUser(currentUser);
        UpdatePasswordForm.validate(updatePasswordForm, bindingResult);

        if (!(bindingResult.hasFieldErrors("password")) && !passwordEncoder.matches(updatePasswordForm.getPassword(), currentUser.getPassword())) {
            bindingResult.addError(new FieldError("updatePasswordForm", "password", updatePasswordForm.getPassword(), false, null, null, "Your old password is incorrect"));
        }

        if (bindingResult.hasErrors()) {
            return "pages/updatePasswordPage";
        }

        currentUser.setPassword(passwordEncoder.encode(updatePasswordForm.getNewPassword()));
        userService.updateUserByEmail(currentUser.getEmail(), currentUser);
        // Send confirmation email to user
        emailService.sendSimpleMessage(currentUser.getEmail(), "Password Changed", "Your password has been changed successfully");
        return "redirect:/profile";
    }

    /**
     * Handles POST requests to the /uploads/{imageName} endpoint.
     * This endpoint allows us to display images stored in /uploads
     *
     * @param imageName the filename of the image
     * @return a ResponseEntity instance with the image data to render
     * @throws MalformedURLException MalformedURLException
     */
    @GetMapping("/uploads/{imageName}")
    public ResponseEntity<Resource> serveImage(@PathVariable String imageName) throws MalformedURLException {
        Path imagePath = Paths.get(UPLOAD_DIRECTORY).resolve(imageName).normalize();
        Resource resource = new UrlResource(imagePath.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            throw new RuntimeException("Image not found or cannot be read: " + imageName);
        }

        return ResponseEntity.ok().body(resource);
    }
}
