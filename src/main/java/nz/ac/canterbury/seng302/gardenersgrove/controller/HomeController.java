package nz.ac.canterbury.seng302.gardenersgrove.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    Logger logger = LoggerFactory.getLogger(HomeController.class);

    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Gets the thymeleaf page representing the / page
     * If the user is logged in, this is the home page,
     * otherwise this is the landing page.
     * @return thymeleaf homePage or landingPage
     */
    @GetMapping("/")
    public String getHome() {
        logger.info("GET /");
        return userService.isSignedIn() ? "pages/homePage" : "pages/landingPage";
    }
}
