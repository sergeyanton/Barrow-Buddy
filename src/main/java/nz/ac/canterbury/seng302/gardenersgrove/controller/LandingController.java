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
     * Redirects GET default url '/' to '/welcome'
     * @return redirect to /welcome
     */
    @GetMapping("/")
    public String landing() {
        logger.info("GET /");
        return "redirect:./welcome";
    }

    /**
     * Gets the thymeleaf page representing the /demo page (a basic welcome screen with some links)
     * @param name url query parameter of user's name
     * @param model (map-like) representation of data to be used in thymeleaf display
     * @return thymeleaf demoTemplate
     */
    @GetMapping("/welcome")
    public String getTemplate() {
        logger.info("GET /welcome");
        return "pages/landingPage";
    }

}
