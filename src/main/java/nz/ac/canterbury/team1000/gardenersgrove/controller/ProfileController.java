package nz.ac.canterbury.team1000.gardenersgrove.controller;

import jakarta.servlet.http.HttpServletRequest;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.form.EditUserForm;
import nz.ac.canterbury.team1000.gardenersgrove.form.UpdatePasswordForm;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Controller
public class ProfileController {
    @Autowired
    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;
    private final UserService userService;
    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";
    Logger logger = LoggerFactory.getLogger(ProfileController.class);

    /**
     * Gets the thymeleaf page representing the /profile page, displaying the currently logged-in
     * user's account details. Will only work if the user is logged in.
     *
     * @return thymeleaf profilePage
     */
    @GetMapping("/profile")
    public String getProfilePage(Model model) {
        logger.info("GET /profile");
        User currentUser = userService.getLoggedInUser();

        model.addAttribute("fName", currentUser.getFname());
        model.addAttribute("lName", currentUser.getLname());
        model.addAttribute("email", currentUser.getEmail());
        model.addAttribute("profilePictureUrl", currentUser.getProfilePicturePath());
        if (currentUser.getDateOfBirth() != null) {
            model.addAttribute("dob",
                    currentUser.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }

        return "pages/profilePage";
    }

    /**
     * Handles POST requests from the /profile endpoint.
     * Specifically, this handles the uploading of a new profile picture.
     *
     * @param request           the HttpServletRequest object containing the request information
     * @param profilePicture    image (png, jpg or svg) to be saved to the file system
     * @return a redirect to the /profile endpoint (GET)
     * @throws IOException IOException
     */
    @PostMapping("/profile")
    public String handleProfilePictureUpload(HttpServletRequest request,
                                             @RequestParam("profilePicture") MultipartFile profilePicture) throws IOException {
        User currentUser = userService.getLoggedInUser();

        if (!profilePicture.isEmpty()) {
            Path uploadDirectoryPath = Paths.get(UPLOAD_DIRECTORY);

            if (!Files.exists(uploadDirectoryPath)) {
                try {
                    Files.createDirectories(uploadDirectoryPath);
                } catch (IOException e) {
                    throw new IOException("Failed to create upload directory", e);
                }
            }

            String filename = profilePicture.getOriginalFilename();
            Path filePath = uploadDirectoryPath.resolve(filename);
            Files.write(filePath, profilePicture.getBytes());
            currentUser.setProfilePicturePath("/uploads/" + filename);
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
        editUserForm.setProfilePictureUrl(currentUser.getProfilePicturePath());

        return "pages/editProfilePage";
    }

    /**
     * Handles POST requests from the /editProfile endpoint.
     * Checks for errors and accordingly edits the user's details.
     *
     * @param request           the HttpServletRequest object containing the request information
     * @param editUserForm      the EditUserForm object containing the form's user inputs
     * @param bindingResult     the BindingResult object for validation errors
     * @param profilePicture    image (png, jpg or svg) to be saved to the file system
     * @return the view to display:
     * - If there are validation errors, stays on the 'Edit Profile' form.
     * - Else, redirect to the user's (edited) profile page.
     * @throws IOException IOException
     */
    @PostMapping("/editProfile")
    public String editProfile(HttpServletRequest request,
                              @ModelAttribute("editUserForm") EditUserForm editUserForm,
                              BindingResult bindingResult,
                              @RequestParam("profilePicture") MultipartFile profilePicture) throws IOException {
        logger.info("POST /editProfile");
        User currentUser = userService.getLoggedInUser();
        String oldEmail = currentUser.getEmail();

        EditUserForm.validate(editUserForm, bindingResult, currentUser);

        if (!bindingResult.hasFieldErrors("email") && !editUserForm.getEmail().equals(oldEmail) && userService.checkEmail(editUserForm.getEmail())) {
            bindingResult.addError(new FieldError("registrationForm", "email", editUserForm.getEmail(), false, null, null, "Email address is already in use"));
        }

        if (bindingResult.hasErrors()) {
            if (!profilePicture.isEmpty()) {
                String imageToRender = "data:" + profilePicture.getContentType() + ";base64," + Base64.getEncoder().encodeToString(profilePicture.getBytes());
                System.out.println(imageToRender);
                editUserForm.setProfilePictureUrl(imageToRender);
            } else {
                editUserForm.setProfilePictureUrl(currentUser.getProfilePicturePath());
            }
            return "pages/editProfilePage";
        }

        currentUser.setFname(editUserForm.getFirstName());
        currentUser.setLname(editUserForm.getLastName());
        currentUser.setEmail(editUserForm.getEmail());
        currentUser.setDateOfBirth(editUserForm.getDobLocalDate());

        if (!profilePicture.isEmpty()) {
            Path uploadDirectoryPath = Paths.get(UPLOAD_DIRECTORY);

            if (!Files.exists(uploadDirectoryPath)) {
                try {
                    Files.createDirectories(uploadDirectoryPath);
                } catch (IOException e) {
                    throw new IOException("Failed to create upload directory", e);
                }
            }

            String filename = profilePicture.getOriginalFilename();
            Path filePath = uploadDirectoryPath.resolve(filename);
            Files.write(filePath, profilePicture.getBytes());
            currentUser.setProfilePicturePath("/uploads/" + filename);
        }

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
        UpdatePasswordForm.validate(updatePasswordForm, bindingResult);

        if (!(bindingResult.hasFieldErrors("password")) && !passwordEncoder.matches(updatePasswordForm.getPassword(), currentUser.getPassword())) {
            bindingResult.addError(new FieldError("updatePasswordForm", "password", updatePasswordForm.getPassword(), false, null, null, "Your old password is incorrect"));
        }

        if (bindingResult.hasErrors()) {
            return "pages/updatePasswordPage";
        }

        currentUser.setPassword(passwordEncoder.encode(updatePasswordForm.getNewPassword()));
        userService.updateUserByEmail(currentUser.getEmail(), currentUser);

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
