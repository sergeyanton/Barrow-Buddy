package nz.ac.canterbury.team1000.gardenersgrove.controller;

import jakarta.servlet.http.HttpServletRequest;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.form.EditUserForm;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;
import nz.ac.canterbury.team1000.gardenersgrove.util.Password;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User currentUser = userService.findEmail(currentPrincipalName);
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
     * Handles POST requests from the /gardens/create endpoint.
     * Handles creation of new gardens
     *
     * @param request          the HttpServletRequest object containing the request information
     * @param createGardenForm the GardenForm object representing the new garden's details
     * @param bindingResult    the BindingResult object for validation errors
     * @return the view to display:
     * - If there are validation errors, stays on the 'Create Garden' form.
     * - Else, redirect to the newly created garden's profile page.
     */


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
                                             @RequestParam("newProfilePicture") MultipartFile profilePicture) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User currentUser = userService.findEmail(currentPrincipalName);

        if (!profilePicture.isEmpty()) {
            Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, profilePicture.getOriginalFilename());
            String filename = profilePicture.getOriginalFilename();
            Files.write(fileNameAndPath, profilePicture.getBytes());
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
    public String getEditProfilePage(EditUserForm editUserForm) {
        logger.info("GET /editProfile");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User currentUser = userService.findEmail(currentPrincipalName);

        editUserForm.setFromUser(currentUser);

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
                              EditUserForm editUserForm,
                              BindingResult bindingResult,
                              @RequestParam("profilePicture") MultipartFile profilePicture) throws IOException {
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
            currentUser.setPassword(Password.hashPassword(editUserForm.getPassword()));
        }

        // Profile pic handling
        if (!profilePicture.isEmpty()) {
            Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, profilePicture.getOriginalFilename());
            String filename = profilePicture.getOriginalFilename();
            Files.write(fileNameAndPath, profilePicture.getBytes());
            currentUser.setProfilePicturePath("/uploads/" + filename);
        }

        userService.updateUserByEmail(oldEmail, currentUser);
        userService.authenticateUser(authenticationManager, currentUser, request);

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
