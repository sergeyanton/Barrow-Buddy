package nz.ac.canterbury.seng302.gardenersgrove.controller;

import jakarta.servlet.http.HttpServletRequest;
import nz.ac.canterbury.seng302.gardenersgrove.controller.dataCollection.LogInData;
import nz.ac.canterbury.seng302.gardenersgrove.controller.dataCollection.RegistrationData;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Validator;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static nz.ac.canterbury.seng302.gardenersgrove.Validation.InputValidation.*;
import static nz.ac.canterbury.seng302.gardenersgrove.Validation.InputValidation.checkDob;
import static nz.ac.canterbury.seng302.gardenersgrove.controller.dataCollection.RegistrationData.createNewUser;

@Controller
public class AccountController {
    Logger logger = LoggerFactory.getLogger(AccountController.class);
    private final UserService userService;

    @Autowired
    public AccountController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        logger.info("GET /register");
        model.addAttribute("errorMessage", "");
        return "pages/registrationPage";
    }

    @Autowired
    private AuthenticationManager authenticationManager;

    private void authenticateUser(User user, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), user.getAuthorities());
        Authentication authentication = authenticationManager.authenticate(token);

        if (authentication.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
        }
    }

    private String pageWithError(String pagePath, Model model, String errorMessage) {
        model.addAttribute("errorMessage", errorMessage);
        return pagePath;
    }

    @PostMapping("/register")
    public String register(HttpServletRequest request, RegistrationData newUser, Model model) {
        logger.info(String.format("Registering new user '%s %s'", newUser.getfName(), newUser.getlName()));

        Validator error = dataCheck(newUser);
        if (!error.getStatus()) {
            return pageWithError("pages/registrationPage", model, error.getMessage());
        }

        User user = createNewUser(newUser);
        user.grantAuthority("ROLE_USER");
        userService.registerUser(user);

        // Auto-login when registering
        authenticateUser(user, request);

        return "redirect:/profile";
    }


    @GetMapping("/login")
    public String getLoginPage() {
        return "pages/loginPage";
    }


    @GetMapping("/profile")
    public String getProfilePage(Model model) {
        logger.info("GET /profile");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User u = userService.getUserByEmail(currentPrincipalName);
        model.addAttribute("fName", u.getFname());
        model.addAttribute("lName", u.getLname());
        model.addAttribute("email", u.getEmail());
        model.addAttribute("dob", u.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        return "pages/profilePage";
    }


    private Validator dataCheck(RegistrationData newUser){
        Validator nameCheck = checkName(newUser.getfName());
        if (!nameCheck.getStatus()) return nameCheck;

        if (!newUser.getNoSurnameCheckBox()) {
            Validator surnameCheck = checkName(newUser.getlName());
            if (!surnameCheck.getStatus()) return surnameCheck;
        }

        Validator emailCheck = checkEmailSignup(newUser.getEmail(),  userService);
        if (!emailCheck.getStatus()) return emailCheck;

        if(!Objects.equals(newUser.getPassword(), newUser.getRetypePassword())){
            return new Validator(false, "Passwords do not match");
        }

        Validator passwordCheck = checkPassword(newUser.getPassword());
        if (!passwordCheck.getStatus()) return passwordCheck;

        Validator dobCheck = checkDob(newUser.getDob());
        if (!dobCheck.getStatus()){return dobCheck;}

        return new Validator(true, "");
    }

    private Validator loginInputCheck(LogInData newUser){

        Validator emailCheck = checkEmailLogin(newUser.getEmail());
        if (!emailCheck.getStatus()) return emailCheck;

        Validator passwordCheck = checkPasswordEmpty(newUser.getPassword());
        if (!passwordCheck.getStatus()) return passwordCheck;

        return new Validator(true, "");
    }

    @PostMapping("/login")
    public String login(HttpServletRequest request, LogInData newUser, Model model) {
        logger.info("in here");

        Validator error = loginInputCheck(newUser);
        if (!error.getStatus()) {
            return pageWithError("pages/loginPage", model, error.getMessage());
        }

        User user = userService.findEmail(newUser.getEmail());

        if (user == null) {
            String errorMessage = String.format("No user with the email '%s' exists.", newUser.getEmail());
            return pageWithError("pages/loginPage", model, errorMessage);
        }

        if (!newUser.getPassword().equals(user.getPassword())) {
            String errorMessage = "Wrong password.";
            return pageWithError("pages/loginPage", model, errorMessage);
        }

        authenticateUser(user, request);

        return "redirect:/profile";
    }
}
