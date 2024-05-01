package nz.ac.canterbury.team1000.gardenersgrove.controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import nz.ac.canterbury.team1000.gardenersgrove.entity.ResetToken;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.entity.VerificationToken;
import nz.ac.canterbury.team1000.gardenersgrove.form.ForgotPasswordForm;
import nz.ac.canterbury.team1000.gardenersgrove.form.LoginForm;
import nz.ac.canterbury.team1000.gardenersgrove.form.RegistrationForm;
import nz.ac.canterbury.team1000.gardenersgrove.form.VerificationTokenForm;
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
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

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
        return "pages/registrationPage";
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
        logger.info("POST /register");

        RegistrationForm.validate(registrationForm, bindingResult);

        if (!bindingResult.hasFieldErrors("email") && userService.checkEmail(registrationForm.getEmail())) {
            bindingResult.addError(new FieldError("registrationForm", "email", registrationForm.getEmail(), false, null, null, "Email already in use"));
        }

        if (bindingResult.hasErrors()) {
            return "pages/registrationPage";
        }

        User newUser = registrationForm.getUser(passwordEncoder);
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
    public String registerVerification(@ModelAttribute("verificationTokenForm") VerificationTokenForm verificationTokenForm,
                                       BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        logger.info("POST /register/verification");
        VerificationTokenForm.validate(verificationTokenForm, bindingResult);
        if (userService.getLoggedInUser() == null) {
            bindingResult.addError(new FieldError("verificationTokenForm", "verificationToken", verificationTokenForm.getVerificationToken(), false, null, null, "Account expired, please register again"));
            return "pages/verificationPage";
        }
        User user = userService.findEmail(userService.getLoggedInUser().getEmail());
        if (user == null || (!bindingResult.hasFieldErrors("verificationToken") && !validateToken(verificationTokenForm.getVerificationToken(), user.getId()))) {
            bindingResult.addError(new FieldError("verificationTokenForm", "verificationToken", verificationTokenForm.getVerificationToken(), false, null, null, "Signup code invalid"));
        }

        if (bindingResult.hasErrors()) {
            return "pages/verificationPage";
        }
        verificationTokenService.updateVerifiedByUserId(user.getId());
        redirectAttributes.addFlashAttribute("errorMessage", "Your account has been activated, please log in");
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
        logger.info("Validating token");
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
     * Handles GET requests to the /login endpoint.
     * Gets the login page.
     * If the user has not verified their account, they are redirected to verification page
     * @param loginForm the LoginForm object representing the user's login data
     */
    @GetMapping("/login")
    public String getLoginPage(@ModelAttribute("loginForm") LoginForm loginForm, Model model, RedirectAttributes redirectAttributes) {
        logger.info("GET /login");
        if (redirectAttributes.containsAttribute("errorMessage")) {
            model.addAttribute("errorMessage", redirectAttributes.getAttribute("errorMessage"));
        }
        if (userService.getLoggedInUser() != null && !verificationTokenService.getVerificationTokenByUserId(userService.getLoggedInUser().getId()).isVerified()) {
            return "redirect:/register/verification";
        }

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
        logger.info("POST /login");
        if (userService.isSignedIn()) {
            return "redirect:/home";
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
        return "redirect:/home";
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
        ResetToken token = new ResetToken(user.getId(), 10);

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
     * Sends a reset password confirmation email to the specified user.
     * Once a user's password has been reset, this email is sent to notify the user of the confirmation.
     * Handles any errors that might occur during the email sending process.
     *
     * @param user The User object containing the email address where the confirmation email will be sent.
     */
    private void sendResetConfirmation(User user) {
        logger.info("Sending reset password confirmation email to " + user.getEmail());

        String htmlBody = "<p>Dear user " + user.getFname() + " " + user.getLname() + "</p>"
                + "<p>Your account password has been successfully updated.</p>"
                + "<p>Regards,</p>"
                + "<p>Gardeners Grove Team</p>";
        try {
            emailService.sendHtmlMessage(user.getEmail(), "Gardeners Grove Reset Password Confirmation", htmlBody);
        } catch (MessagingException e) {
            logger.error("Failed to send reset password confirmation email", e);
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
     *      *  *         - Whatever the result is (error or no error), returns/redirects the forgot password page.
     */
    @PostMapping("/forgotPassword")
    public String forgotPassword(HttpServletRequest request,
                                 @ModelAttribute("forgotPasswordForm") ForgotPasswordForm forgotPasswordForm,
                                 BindingResult bindingResult) {
        logger.info("POST /forgotPassword");
        ForgotPasswordForm.validate(forgotPasswordForm, bindingResult);
        User user = userService.findEmail(forgotPasswordForm.getEmail());

        if (user != null) {
            sendResetPasswordEmail(user);
        }

        if (!bindingResult.hasFieldErrors("email")) {
            bindingResult.addError(new FieldError("forgotPasswordForm", "email", forgotPasswordForm.getEmail(), false, null, null, "An email was sent to the address if it was recognised"));

        } else if (!bindingResult.hasFieldErrors("email") && !userService.checkEmail(forgotPasswordForm.getEmail())) {
            bindingResult.addError(new FieldError("forgotPasswordForm", "email", forgotPasswordForm.getEmail(), false, null, null, "Invalid email format"));
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
                                       ResetPasswordForm resetPasswordForm, RedirectAttributes redirectAttributes) {
        ResetToken token = resetTokenService.getResetToken(resetToken);
        session.setAttribute("resetToken", resetToken);
        // if token doesn't exist or expired:
        if (token == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Reset password link has expired");
            return "redirect:/login";
        } else if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Reset password link has expired");
            resetTokenService.deleteResetToken(resetToken);
            return "redirect:/login";
        }

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
     *         - If password reset is successful, redirects to the application's login page and sends email confirmation.
     */
    @PostMapping("/resetPassword")
    public String postUpdatePassword(HttpServletRequest request,
                                     @ModelAttribute("resetPasswordForm") ResetPasswordForm resetPasswordForm,
                                     BindingResult bindingResult,
                                     HttpSession session, RedirectAttributes redirectAttributes) {
        logger.info("POST /resetPassword");
        String resetToken = (String) session.getAttribute("resetToken");
        ResetToken token = resetTokenService.getResetToken(resetToken);
        // check if token is expired yet:
        if (token == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Reset password link has expired");
            return "redirect:/login";
        } else if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Reset password link has expired");
            resetTokenService.deleteResetToken(resetToken);
            return "redirect:/login";
        }

        User user = userService.getUserById(token.getUserId());
        ResetPasswordForm.validate(resetPasswordForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "pages/resetPasswordPage";
        }

        user.setPassword(passwordEncoder.encode(resetPasswordForm.getNewPassword()));

        userService.updateUserByEmail(user.getEmail(), user);
        resetTokenService.deleteResetToken(token.getToken());
        session.removeAttribute("resetToken");

        sendResetConfirmation(user);

        return "redirect:/login";
    }


}
