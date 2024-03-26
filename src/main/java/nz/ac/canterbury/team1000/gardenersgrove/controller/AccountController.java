package nz.ac.canterbury.team1000.gardenersgrove.controller;

import jakarta.servlet.http.HttpServletRequest;
import nz.ac.canterbury.team1000.gardenersgrove.controller.dataCollection.LogInData;
import nz.ac.canterbury.team1000.gardenersgrove.controller.dataCollection.RegistrationData;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;
import nz.ac.canterbury.team1000.gardenersgrove.validation.InputValidation;
import nz.ac.canterbury.team1000.gardenersgrove.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import static nz.ac.canterbury.team1000.gardenersgrove.controller.dataCollection.RegistrationData.createNewUser;
import static nz.ac.canterbury.team1000.gardenersgrove.util.PageUtils.pageWithError;
import static nz.ac.canterbury.team1000.gardenersgrove.validation.InputValidation.checkLoginData;
import static nz.ac.canterbury.team1000.gardenersgrove.validation.InputValidation.verifyPassword;
import java.time.format.DateTimeFormatter;

@Controller
public class AccountController {
    Logger logger = LoggerFactory.getLogger(AccountController.class);
    private final UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    public AccountController(UserService userService) {
        this.userService = userService;
    }


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
        User u = userService.findEmail(currentPrincipalName);
        model.addAttribute("fName", u.getFname());
        model.addAttribute("lName", u.getLname());
        model.addAttribute("email", u.getEmail());
        if (u.getDateOfBirth() != null) {
            model.addAttribute("dob",
                    u.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }


        return "pages/profilePage";
    }


    /**
     * Gets the thymeleaf page representing the /register page Will only work if the user is not
     * logged in, otherwise it will redirect to the home page
     * 
     * @return thymeleaf registrationPage
     */
    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        logger.info("GET /register");

        return userService.isSignedIn() ? "redirect:/" : "pages/registrationPage";
    }


    /**
     * Handles POST requests to the /register endpoint. registers the user, or shows an error
     * message if the user details are invalid.
     * 
     * @param request The HTTP request being made
     * @param newUser The user registration data sent in the request
     * @param model The request model
     * @return A redirect to the profile page, or the registration page with an error message if
     *         unsuccessful
     */
    @PostMapping("/register")
    public String register(HttpServletRequest request, RegistrationData newUser, Model model) {
        logger.info(String.format("Registering new user '%s %s'", newUser.getfName(),
                newUser.getlName()));

        InputValidation inputValidation = new InputValidation(userService);

        Validator error = inputValidation.checkRegistrationData(newUser, false);
        if (!error.getStatus()) {
            model.addAttribute("fName", newUser.getfName());
            model.addAttribute("lName", newUser.getlName());
            model.addAttribute("noSurnameCheckBox", newUser.getNoSurnameCheckBox());
            model.addAttribute("email", newUser.getEmail());
            model.addAttribute("password", newUser.getPassword());
            model.addAttribute("retypePassword", newUser.getRetypePassword());
            model.addAttribute("dob", newUser.getDob());
            return pageWithError("pages/registrationPage", model, error.getMessage());
        }

        User user = createNewUser(newUser);
        user.setProfilePicturePath("/resources/images/default_pic.jpg");
        user.grantAuthority("ROLE_USER");
        userService.registerUser(user);

        // Auto-login when registering
        userService.authenticateUser(authenticationManager, user, request);

        ResponseEntity.ok();

        return "redirect:/profile";
    }


    /**
     * Gets the thymeleaf page representing the /login page Will only work if the user is not logged
     * in, otherwise it will redirect to the home page
     * 
     * @return thymeleaf loginPage
     */
    @GetMapping("/login")
    public String getLoginPage() {
        logger.info("GET /login");
        return userService.isSignedIn() ? "redirect:/" : "pages/loginPage";
    }

    /**
     * Handles POST requests to the /login endpoint. Logs in the user, or shows an error message if
     * the login details are invalid.
     * 
     * @param request The HTTP request being made
     * @param newUser The login data sent in the request
     * @param model The request model
     * @return A redirect to the profile page, or the login page with an error message if
     *         unsuccessful
     */
    @PostMapping("/login")
    public String login(HttpServletRequest request, LogInData newUser, Model model) {
        if (userService.isSignedIn()) {
            return "redirect:/";
        }

        Validator error = checkLoginData(newUser);
        if (!error.getStatus()) {
            return pageWithError("pages/loginPage", model, error.getMessage());
        }

        User user = userService.findEmail(newUser.getEmail());

        if (user == null) {
            String errorMessage = "The email address is unknown, or the password is invalid";
            return pageWithError("pages/loginPage", model, errorMessage);
        }

        if (!verifyPassword(newUser.getPassword(), user.getPassword())) {
            String errorMessage = "Wrong password.";
            return pageWithError("pages/loginPage", model, errorMessage);
        }

        userService.authenticateUser(authenticationManager, user, request);

        return "redirect:/";
    }
}
