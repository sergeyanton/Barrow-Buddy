package nz.ac.canterbury.team1000.gardenersgrove.controller;

import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;
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
        return userService.isSignedIn() ? "pages/homePage" : "pages/landingPage";
    }
}
