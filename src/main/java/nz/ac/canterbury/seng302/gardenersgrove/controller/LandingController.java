package nz.ac.canterbury.seng302.gardenersgrove.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * This is a basic spring boot controller, note the @link{Controller} annotation which defines this.
 * This controller defines endpoints as functions with specific HTTP mappings
 */
@Controller
public class LandingController {
    Logger logger = LoggerFactory.getLogger(LandingController.class);

    /**
     * Gets the thymeleaf page representing the /welcome page
     * @return thymeleaf landingPage
     */
    @GetMapping("/")
    public String getTemplate() {
        logger.info("GET /");
        return "pages/landingPage";
    }
}
