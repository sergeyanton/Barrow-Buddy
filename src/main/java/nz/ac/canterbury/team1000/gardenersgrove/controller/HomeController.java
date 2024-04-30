package nz.ac.canterbury.team1000.gardenersgrove.controller;

import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;
import nz.ac.canterbury.team1000.gardenersgrove.service.VerificationTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * This is the controller for the home page.
 */
@Controller
public class HomeController {
    Logger logger = LoggerFactory.getLogger(HomeController.class);

    private final UserService userService;
    private final VerificationTokenService verificationTokenService;

    public HomeController(UserService userService, VerificationTokenService verificationTokenService) {
        this.userService = userService;
        this.verificationTokenService = verificationTokenService;
    }

    /**
     * Renders the template.
     */
    @GetMapping("/")
    public String getHome() {
        // If viewer not verified, redirect to verification page
        if (userService.getLoggedInUser() != null && verificationTokenService.getVerificationTokenByUserId(userService.getLoggedInUser().getId()) != null) {
            return "pages/landingPage";
        }
        return userService.isSignedIn() ? "pages/homePage" : "pages/landingPage";
    }

    @GetMapping("/landing")
    public String getLandingPage() {
        logger.info("/GET landing");
        return "pages/landingPage";
    }

}
