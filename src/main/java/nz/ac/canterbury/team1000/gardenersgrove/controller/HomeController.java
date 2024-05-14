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

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Renders the template.
     */
    @GetMapping("/")
    public String getHome() {
        // If viewer not verified, redirect to verification page
        return userService.isSignedIn() ? "pages/homePage" : "pages/landingPage";
    }

    @GetMapping("/landing")
    public String getLandingPage() {
        logger.info("/GET landing");
        return "pages/landingPage";
    }

    @GetMapping("/home")
    public String getHomePage() {
        logger.info("/GET home");
        return "pages/homePage";
    }

}
