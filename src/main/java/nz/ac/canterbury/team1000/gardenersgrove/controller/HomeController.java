package nz.ac.canterbury.team1000.gardenersgrove.controller;

import java.util.List;

import nz.ac.canterbury.team1000.gardenersgrove.form.RegistrationForm;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;
import nz.ac.canterbury.team1000.gardenersgrove.service.VerificationTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import jakarta.servlet.http.HttpServletRequest;
import nz.ac.canterbury.team1000.gardenersgrove.entity.Garden;
import nz.ac.canterbury.team1000.gardenersgrove.service.GardenService;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * This is the controller for the home page.
 */
@Controller
public class HomeController {
    Logger logger = LoggerFactory.getLogger(HomeController.class);

    private final GardenService gardenService;
    private final UserService userService;
    private final VerificationTokenService verificationTokenService;

    @Autowired
    public HomeController(GardenService gardenService, UserService userService, VerificationTokenService verificationTokenService) {
        this.gardenService = gardenService;
        this.userService = userService;
        this.verificationTokenService = verificationTokenService;
    }

    @ModelAttribute("currentUrl")
    private String getCurrentPath(HttpServletRequest request) {
        // the url path including query parameters
        if (request.getQueryString() == null) {
            return request.getRequestURI();
        }
        return request.getRequestURI() + "?" + request.getQueryString();
    }

    /**
     * Necessary for being able to display each garden in the nav bar
     *
     * @return all gardens currently in the database
     */
    @ModelAttribute("allGardens")
    private List<Garden> getAllGardens() {
        return gardenService.getGardens();
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
