package nz.ac.canterbury.team1000.gardenersgrove.controller;

import jakarta.servlet.http.HttpServletRequest;
import nz.ac.canterbury.team1000.gardenersgrove.controller.dataCollection.LogInData;
import nz.ac.canterbury.team1000.gardenersgrove.controller.dataCollection.ResetPasswordData;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.form.ForgotPasswordForm;
import nz.ac.canterbury.team1000.gardenersgrove.form.LoginForm;
import nz.ac.canterbury.team1000.gardenersgrove.form.RegistrationForm;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import static nz.ac.canterbury.team1000.gardenersgrove.util.Password.verifyPassword;
// import static nz.ac.canterbury.team1000.gardenersgrove.validation.InputValidation.checkResetPasswordData;

import java.io.Console;
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
     * @param registrationForm the RegistrationForm object representing the user's registration data,
     *                         useful for seeing erroneous inputs of a failed POST request
     * @return the view to display:
     *         - If the user is signed in, redirect to the home page
     *         - Else, go to the registration page, where the user can create a new account
     */
    @GetMapping("/register")
    public String getRegisterPage(@ModelAttribute RegistrationForm registrationForm) {
        logger.info("GET /register");
        return userService.isSignedIn() ? "redirect:/" : "pages/registrationPage";
    }

    @GetMapping("/forgotPassword")
    public String getForgotPasswordPage(ForgotPasswordForm forgotPasswordForm) {
        logger.info("GET /forgotPassword");
        return "pages/forgotPasswordPage";
    }


    /**
     * Handles POST requests to the /register endpoint.
     * Handles the registration process for new users.
     *
     * @param request           the HttpServletRequest object containing the request information
     * @param registrationForm  the RegistrationForm object representing the user's registration data
     * @param bindingResult     the BindingResult object for validation errors
     * @return the view to display after registration:
     *         - If there are validation errors, returns the registration page to display errors.
     *         - If registration is successful, redirects to the user's profile page.
     */
    @PostMapping("/register")
    public String register(HttpServletRequest request, @ModelAttribute("registrationForm") RegistrationForm registrationForm, BindingResult bindingResult) {
        RegistrationForm.validate(registrationForm, bindingResult);

        if (!bindingResult.hasFieldErrors("email") && userService.checkEmail(registrationForm.getEmail())) {
            bindingResult.addError(new FieldError("registrationForm", "email", registrationForm.getEmail(), false, null, null, "Email already in use"));
        }

        if (bindingResult.hasErrors()) {
            return "pages/registrationPage";
        }

        User newUser = registrationForm.getUser();
        newUser.grantAuthority("ROLE_USER");
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
    public String getLoginPage(LoginForm loginForm) {
        logger.info("GET /login");
        return userService.isSignedIn() ? "redirect:/" : "pages/loginPage";
    }

    /**
     * Handles POST requests to the /login endpoint.
     * Logs in the user, or shows an error message if the login details are invalid.
     * 
     * @param request the HttpServletRequest object containing the request information
     * @param loginForm the LoginForm object representing the user's login data
     * @param bindingResult the BindingResult object for validation errors
     * @return a String representing the view to display after login:
     *      *  *         - If there are validation errors, returns the login page to display errors.
     *      *  *         - If login is successful, redirects to the application's home page.
     */
    @PostMapping("/login")
    public String login(HttpServletRequest request, @ModelAttribute("loginForm") LoginForm loginForm, BindingResult bindingResult) {
        if (userService.isSignedIn()) {
            return "redirect:/";
        }

        LoginForm.validate(loginForm, bindingResult);

        if (!bindingResult.hasFieldErrors()) {
            User user = userService.findEmail(loginForm.getEmail());
            // check if email exists
            String invalidUserError = "The email address is unknown, or the password is invalid";
            if (user == null) {
                bindingResult.addError(new FieldError("loginForm", "password", loginForm.getPassword(), false, null, null, invalidUserError));
            } else if (!verifyPassword(loginForm.getPassword(), user.getPassword())) {
                bindingResult.addError(new FieldError("loginForm", "password", loginForm.getPassword(), false, null, null, invalidUserError));
            }
        }

        if (bindingResult.hasErrors()) {
            return "pages/loginPage";
        }

        // form submitted with valid data
        // log in the user
        User validUser = userService.findEmail(loginForm.getEmail());
        userService.authenticateUser(authenticationManager, validUser, request);

        return "redirect:/";
    }

    @PostMapping("/forgotPassword")
    public String forgotPassword(HttpServletRequest request, @ModelAttribute("forgotPasswordForm") ForgotPasswordForm forgotPasswordForm, BindingResult bindingResult) {
        ForgotPasswordForm.validate(forgotPasswordForm, bindingResult);

        if (!bindingResult.hasFieldErrors("email") && !userService.checkEmail(forgotPasswordForm.getEmail())) {
            bindingResult.addError(new FieldError("forgotPasswordForm", "email", forgotPasswordForm.getEmail(), false, null, null, "Email does not exist"));
        }

        if (bindingResult.hasErrors()) {
            return "pages/forgotPasswordPage";
        }

        // form was submitted with valid data
        // send a reset password email to the provided email

        logger.info("SEND A RESET EMAIL TO USER!!!!!! - NOT IMPLEMENTED");


        return "redirect:/forgotPasswordPage";
    }

}
