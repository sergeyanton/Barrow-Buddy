package nz.ac.canterbury.seng302.gardenersgrove.controller;

import jakarta.servlet.http.HttpServletRequest;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
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
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AccountController {
    Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String getRegisterPage() {
        logger.info("GET /register");
        return "pages/registrationPage";
    }

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public String register(HttpServletRequest request,
                           @RequestParam(name = "email") String email,
                           @RequestParam(name = "fname") String fname,
                           @RequestParam(name = "lname") String lname,
                           //@RequestParam(name = "dob") String dateOfBirth,
                           @RequestParam(name = "password") String password) {

        logger.info("registering user");

        // TODO: add validation here

        String dateOfBirth = "";


        User user = new User(fname, lname, password, email, dateOfBirth);
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
        model.addAttribute("email", u.getEmail());
        model.addAttribute("fname", u.getFname());
        model.addAttribute("authorities", u.getAuthorities());
        return "pages/profilePage";
    }
}
