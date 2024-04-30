package nz.ac.canterbury.team1000.gardenersgrove.controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import nz.ac.canterbury.team1000.gardenersgrove.entity.ResetToken;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.entity.VerificationToken;
import nz.ac.canterbury.team1000.gardenersgrove.form.*;
import nz.ac.canterbury.team1000.gardenersgrove.service.EmailService;
import nz.ac.canterbury.team1000.gardenersgrove.service.ResetTokenService;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;
import nz.ac.canterbury.team1000.gardenersgrove.service.VerificationTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
public class AccountController {
    final Logger logger = LoggerFactory.getLogger(AccountController.class);
    private final UserService userService;

    private final VerificationTokenService verificationTokenService;
    private final ResetTokenService resetTokenService;
    private final EmailService emailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AccountController(UserService userService, VerificationTokenService verificationTokenService,
                             EmailService emailService, ResetTokenService resetTokenService) {
        this.userService = userService;
        this.verificationTokenService = verificationTokenService;
        this.emailService = emailService;
        this.resetTokenService = resetTokenService;
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
    public String getRegisterPage(@ModelAttribute("registrationForm") RegistrationForm registrationForm) {
        logger.info("GET /register");
        return userService.isSignedIn() ? "redirect:/" : "pages/registrationPage";
    }

    /**
     * Gets the thymeleaf page representing the /forgotPassword page, for the user to enter
     * an email to send a reset password link to.
     *
     * @return thymeleaf forgotPasswordPage
     */
    @GetMapping("/forgotPassword")
    public String getForgotPasswordPage(ForgotPasswordForm forgotPasswordForm) {
        logger.info("GET /forgotPassword");
        return "pages/forgotPasswordPage";
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
    public String register(HttpServletRequest request,
                           @ModelAttribute("registrationForm") RegistrationForm registrationForm,
                           BindingResult bindingResult) {
        RegistrationForm.validate(registrationForm, bindingResult);

        if (!bindingResult.hasFieldErrors("email") && userService.checkEmail(registrationForm.getEmail())) {
            bindingResult.addError(new FieldError("registrationForm", "email", registrationForm.getEmail(), false, null, null, "Email already in use"));
        }

        if (bindingResult.hasErrors()) {
            return "pages/registrationPage";
        }

        User newUser = registrationForm.getUser(passwordEncoder);
        logger.warn(newUser.getPassword());
        // Give them the role of user
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
        User user = userService.findEmail(authentication.getName());
        if (user == null || (!bindingResult.hasFieldErrors("verificationToken") && !validateToken(verificationTokenForm.getVerificationToken(), user.getId()))) {
            bindingResult.addError(new FieldError("verificationTokenForm", "verificationToken", verificationTokenForm.getVerificationToken(), false, null, null, "Signup code invalid"));
        }

        if (bindingResult.hasErrors()) {
            return "pages/verificationPage";
        }
        verificationTokenService.updateVerifiedByUserId(user.getId());
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
        if (verificationTokenService.getVerificationTokenByUserId(userId) == null) {
            return false;
        }
        return passwordEncoder.matches(userInputToken, verificationTokenService.getVerificationTokenByUserId(userId).getHashedToken());
    }

    /**
     * Sends a verification email to the user.
     *
     * @param user the user to send the verification email to
     */
    private void sendVerificationEmail(User user) {
        logger.info("Sending verification email to " + user.getEmail());
        VerificationToken token = new VerificationToken(user.getId(), passwordEncoder);
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
    public String getLoginPage(@ModelAttribute("loginForm") LoginForm loginForm) {
        logger.info("GET /login");
        //TODO if user has been redirected from verafication page then display message “Your account has been activated, please log in”
        if (userService.getLoggedInUser() != null && !verificationTokenService.getVerificationTokenByUserId(userService.getLoggedInUser().getId()).isVerified()) {
            return "redirect:/register/verification";
        }
//        return userService.isSignedIn() ? "redirect:/" : "pages/loginPage";
        return "pages/loginPage";
    }

    /**
     * Handles POST requests to the /login endpoint.
     * Logs in the user, or shows an error message if the login details are invalid.
     *
     * @param request       the HttpServletRequest object containing the request information
     * @param loginForm     the LoginForm object representing the user's login data
     * @param bindingResult the BindingResult object for validation errors
     * @return a String representing the view to display after login:
     *         - If there are validation errors, returns the login page to display errors.
     *         - If login is successful, redirects to the application's home page.
     */
    @PostMapping("/login")
    public String login(HttpServletRequest request,
                        @ModelAttribute("loginForm") LoginForm loginForm,
                        BindingResult bindingResult) {
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
            } else if (!passwordEncoder.matches(loginForm.getPassword(), user.getPassword())) {
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

    /**
     * Sends a reset password email to the specified user.
     * This method generates a unique password reset link and sends it to the user's email address.
     * Handles any errors that might occur during the email sending process.
     *
     * @param user The User object containing the email address where the reset password email will be sent.
     */
    private void sendResetPasswordEmail(User user) {
        logger.info("Sending reset password email to " + user.getEmail());
        ResetToken token = new ResetToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiryDate(10);
        resetTokenService.addResetToken(token);

        String url = "http://localhost:8080/resetPassword?token=" + token.getToken();
        String htmlBody = "<p>Please click the below link to reset your password:</p>"
                + "<a href='" + url + "'>Reset Password Link</a>"
                + "<p>If this was not you, you can ignore this message and the account will be deleted after 10 minutes.</p>";
        try {
            emailService.sendHtmlMessage(user.getEmail(), "Gardeners Grove Account Reset Password", htmlBody);
        } catch (MessagingException e) {
            logger.error("Failed to send reset password email", e);
        }
    }

    /**
     * Handles POST requests to the /forgotPassword endpoint.
     * Let the user type an email address that they forgot the password of, and send them a reset email, or show an error message if the email address is invalid.
     *
     * @param request the HttpServletRequest object containing the request information
     * @param forgotPasswordForm the ForgotPasswordForm object representing the 'forgot password' data
     * @param bindingResult the BindingResult object for validation errors
     * @return a String representing the view to display after entering the 'forgot password email':
     *         - Whatever the result is (error or no error), returns/redirects the forgot password page.
     */
    @PostMapping("/forgotPassword")
    public String forgotPassword(HttpServletRequest request, @ModelAttribute("forgotPasswordForm") ForgotPasswordForm forgotPasswordForm, BindingResult bindingResult) {
        ForgotPasswordForm.validate(forgotPasswordForm, bindingResult);
        User user = userService.findEmail(forgotPasswordForm.getEmail());

        if (user != null) {
            sendResetPasswordEmail(user);
        }

        if (!bindingResult.hasFieldErrors("email")) {
            bindingResult.addError(new FieldError("forgotPasswordForm", "email", forgotPasswordForm.getEmail(), false, null, null, "An email was sent to the address if it was recognised"));
        }

        if (bindingResult.hasErrors()) {
            return "pages/forgotPasswordPage";
        }

        return "redirect:/forgotPasswordPage";
    }

    /**
     * Gets the thymeleaf page representing the /resetPassword page, for non-logged-in
     * users to reset their password
     *
     * @return thymeleaf resetPasswordPage
     */
    @GetMapping("/resetPassword")
    public String getResetPasswordPage(@RequestParam(value = "token") String resetToken, HttpSession session,
                                       ResetPasswordForm resetPasswordForm) {
        ResetToken token = resetTokenService.getResetToken(resetToken);
        session.setAttribute("resetToken", resetToken);
        // TODO validate token
        // if token doesn't exist or expired:
        // redirect to login page, with message "Reset password link has expired"
        logger.info("GET /resetPassword");

        return "pages/resetPasswordPage";
    }

    /**
     * Handles POST requests to the /resetPassword endpoint.
     * Resets the password for the user if they enter a valid new password.
     *
     * @param request the HttpServletRequest object containing the request information
     * @param resetPasswordForm the ResetPasswordForm object representing the password fields
     * @param bindingResult the BindingResult object for validation errors
     * @return a String representing the view to display after password reset:
     *         - If there are validation errors, returns the reset password page to display errors.
     *         - If password reset is successful, redirects to the application's login page.
     */
    @PostMapping("/resetPassword")
    public String postUpdatePassword(HttpServletRequest request,
                                     @ModelAttribute("resetPasswordForm") ResetPasswordForm resetPasswordForm,
                                     BindingResult bindingResult,
                                     HttpSession session) {
        logger.info("POST /resetPassword");
        String resetToken = (String) session.getAttribute("resetToken");
        ResetToken token = resetTokenService.getResetToken(resetToken);
        User user = token.getUser();
        // TODO check this validation of form:
        resetPasswordForm.setResetToken(resetToken);
        ResetPasswordForm.validate(resetPasswordForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "pages/resetPasswordPage";
        }

        user.setPassword(passwordEncoder.encode(resetPasswordForm.getNewPassword()));

        userService.updateUserByEmail(user.getEmail(), user);
        resetTokenService.deleteResetToken(token.getToken());
        session.removeAttribute("resetToken");

        return "redirect:/login";
    }


}
