package nz.ac.canterbury.seng302.gardenersgrove.controller;

import jakarta.servlet.http.HttpServletRequest;
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

import java.text.SimpleDateFormat;
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

    @PostMapping("/register")
    public String register(HttpServletRequest request, RegistrationData newUser, Model model) {
        logger.info(String.format("Registering new user '%s %s'", newUser.getfName(), newUser.getlName()));

        Validator error = dataCheck(newUser);
        if (!error.getStatus()){
            String errorMessage = error.getMessage();
            model.addAttribute("errorMessage", errorMessage);
            return "pages/registrationPage";
        }

        User user = createNewUser(newUser);
        user.grantAuthority("ROLE_USER");
        userService.registerUser(user);

        // Auto-login when registering
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), user.getAuthorities());
        Authentication authentication = authenticationManager.authenticate(token);

        if (authentication.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
        }

        return "redirect:/profile";
    }


    @GetMapping("/login")
    public String getLoginPage() {
        return "pages/loginPage";
    }


    @GetMapping("/profile")
    public String getUserPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User u = userService.getUserByEmail(currentPrincipalName);
        model.addAttribute("fname", u.getFname());
        model.addAttribute("lname", u.getLname());
        model.addAttribute("email", u.getEmail());
        model.addAttribute("dob", new SimpleDateFormat("yyyy/MM/dd").format(u.getDateOfBirth()));
        return "pages/profilePage";
    }


    private Validator dataCheck(RegistrationData newUser){
        Validator nameCheck = checkName(newUser.getfName());
        if (!nameCheck.getStatus()) return nameCheck;

        if (!newUser.getNoSurnameCheckBox()) {
            Validator surnameCheck = checkName(newUser.getlName());
            if (!surnameCheck.getStatus()) return surnameCheck;
        }

        Validator emailCheck = checkEmail(newUser.getEmail(),  userService);
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
}
