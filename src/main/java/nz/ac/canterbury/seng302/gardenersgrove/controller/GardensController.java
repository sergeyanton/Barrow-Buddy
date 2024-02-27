package nz.ac.canterbury.seng302.gardenersgrove.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * This is the controller for the home page.
 */
@Controller
public class GardensController {
    Logger logger = LoggerFactory.getLogger(GardensController.class);

    /**
     * Renders the template.
     */
    @GetMapping("/gardens/create")
    public String gardenCreate() {
        return "createGarden";
    }
}
