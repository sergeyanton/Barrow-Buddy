package nz.ac.canterbury.team1000.gardenersgrove.controller;

import java.util.List;

import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpServletRequest;
import nz.ac.canterbury.team1000.gardenersgrove.entity.Garden;
import nz.ac.canterbury.team1000.gardenersgrove.service.GardenService;

/**
 * This is the controller for the home page.
 */
@Controller
public class HomeController {
    Logger logger = LoggerFactory.getLogger(HomeController.class);

    private final GardenService gardenService;
    private final UserService userService;

    @Autowired
    public HomeController(GardenService gardenService, UserService userService) {
        this.gardenService = gardenService;
        this.userService = userService;
    }

    @ModelAttribute("currentUrl")
    private String getCurrentPath(HttpServletRequest request) {
        // the url path including query parameters
        if (request.getQueryString() == null) {
            return request.getRequestURI();
        }
        return request.getRequestURI() + "?" + request.getQueryString();
    }

    @ModelAttribute("allGardens")
    private List<Garden> getAllGardens() {
        return gardenService.getGardens();
    }

    /**
     * Renders the template.
     */
    @GetMapping("/")
    public String home() {
        return userService.isSignedIn() ? "pages/homePage" : "pages/landingPage";
    }
}
