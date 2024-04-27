package nz.ac.canterbury.team1000.gardenersgrove.controller;

import jakarta.servlet.http.HttpServletRequest;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.entity.VerificationToken;
import nz.ac.canterbury.team1000.gardenersgrove.form.LoginForm;
import nz.ac.canterbury.team1000.gardenersgrove.form.RegistrationForm;
import nz.ac.canterbury.team1000.gardenersgrove.form.VerificationTokenForm;
import nz.ac.canterbury.team1000.gardenersgrove.service.EmailService;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;
import nz.ac.canterbury.team1000.gardenersgrove.service.VerificationTokenService;
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

import java.time.format.DateTimeFormatter;

@Controller
public class AccountController {
    Logger logger = LoggerFactory.getLogger(AccountController.class);
    private final UserService userService;
    private final VerificationTokenService verificationTokenService;
    private final EmailService emailService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    public AccountController(UserService userService, VerificationTokenService verificationTokenService, EmailService emailService) {
        this.userService = userService;
        this.verificationTokenService = verificationTokenService;
        this.emailService = emailService;
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
     * - If the user is signed in, redirect to the home page
     * - Else, go to the registration page, where the user can create a new account
     */
    @GetMapping("/register")
    public String getRegisterPage(@ModelAttribute RegistrationForm registrationForm) {
        logger.info("GET /register");
        return userService.isSignedIn() ? "redirect:/" : "pages/registrationPage";
    }


    /**
     * Handles POST requests to the /register endpoint.
     * Handles the registration process for new users.
     *
     * @param request          the HttpServletRequest object containing the request information
     * @param registrationForm the RegistrationForm object representing the user's registration data
     * @param bindingResult    the BindingResult object for validation errors
     * @return the view to display after registration:
     * - If there are validation errors, returns the registration page to display errors.
     * - If registration is successful, redirects to the registrationVerification page to enter the verification token.
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
        sendVerificationEmail(newUser);
        return "redirect:/register/verification";
    }

    @GetMapping("/register/verification")
    public String getRegisterVerificationPage(@ModelAttribute("verificationTokenForm") VerificationTokenForm verificationTokenForm) {
        logger.info("GET /register/verification");
        return "pages/verificationPage";
    }

    /**
     * Handles POST requests to the /register/verification endpoint.
     * Handles the registration verification process for new users.
     *
     * @param verificationTokenForm the VerificationTokenForm object representing the user's verification token data
     * @param bindingResult         the BindingResult object for validation errors
     * @return the view to display after registration:
     * - Either if there are validation errors or not, returns the registrationVerification page to display errors/ not.
     */
    @PostMapping("/register/verification")
    public String registerVerification(@ModelAttribute("verificationTokenForm") VerificationTokenForm verificationTokenForm, BindingResult bindingResult) {
        VerificationTokenForm.validate(verificationTokenForm, bindingResult);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        long userId = userService.findEmail(authentication.getName()).getId();

        if (!bindingResult.hasFieldErrors("verificationToken") && !validateToken(verificationTokenForm.getVerificationToken(), userId)) {
            bindingResult.addError(new FieldError("verificationTokenForm", "verificationToken", verificationTokenForm.getVerificationToken(), false, null, null, "Signup code invalid"));
        }

        if (bindingResult.hasErrors()) {
            return "pages/verificationPage";
        }
        verificationTokenService.getVerificationTokenByUserId(userId).verifyUser();
        return "redirect:/login";
    }

    /**
     * Validates the user's inputted token against the stored token in the database.
     *
     * @param userInputToken the user's inputted token as a string
     * @param userId         the user's id
     * @return boolean value whether the token is verified(true) or not(false)
     */
    private boolean validateToken(String userInputToken, long userId) {
        return verifyPassword(userInputToken, verificationTokenService.getVerificationTokenByUserId(userId).getHashedToken());
    }

    /**
     * Sends a verification email to the user.
     *
     * @param user the user to send the verification email to
     */
    private void sendVerificationEmail(User user) {
        logger.info("Sending verification email to " + user.getEmail());
        VerificationToken token = new VerificationToken(user.getId());
        verificationTokenService.addVerificationToken(token);
        String body = "Please verify your account by copying the following code into the prompted field: \n\n" + token.getPlainToken()
                + "\n\nIf this was not you, you can ignore this message and the account will be deleted after 10 minutes";
        emailService.sendSimpleMessage(user.getEmail(), "Gardeners Grove Account Verification", body);
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
//        if (verificationTokenService.getVerificationTokenByUserId(userService.getLoggedInUser().getId()).getVerified() == true) {
//
//        }
        return userService.isSignedIn() ? "redirect:/" : "pages/loginPage";
    }

    /**
     * Handles POST requests to the /login endpoint.
     * Logs in the user, or shows an error message if the login details are invalid.
     *
     * @param request       the HttpServletRequest object containing the request information
     * @param loginForm     the LoginForm object representing the user's login data
     * @param bindingResult the BindingResult object for validation errors
     * @return a String representing the view to display after login:
     * *  *         - If there are validation errors, returns the login page to display errors.
     * *  *         - If login is successful, redirects to the application's home page.
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
}
