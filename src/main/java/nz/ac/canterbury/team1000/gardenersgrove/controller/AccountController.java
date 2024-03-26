package nz.ac.canterbury.team1000.gardenersgrove.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import nz.ac.canterbury.team1000.gardenersgrove.controller.dataCollection.LogInData;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.form.FormValidator;
import nz.ac.canterbury.team1000.gardenersgrove.form.RegistrationForm;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;
import nz.ac.canterbury.team1000.gardenersgrove.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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
    public String getRegisterPage(RegistrationForm registrationForm) {
//        logger.info("GET /register");

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
    public String register(HttpServletRequest request, RegistrationForm registrationForm, BindingResult bindingResult) {
        FormValidator.validateRegistrationForm(registrationForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "pages/registrationPage";
        }

        // form was submitted with valid data
        // create the user and log them in
        User newUser = new User(
            registrationForm.getFirstName(),
            registrationForm.getLastName(),
            registrationForm.getEmail(),
            registrationForm.getPassword(),
            registrationForm.getDobLocalDate()
        );
        userService.registerUser(newUser);
        userService.authenticateUser(authenticationManager, newUser, request);

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
