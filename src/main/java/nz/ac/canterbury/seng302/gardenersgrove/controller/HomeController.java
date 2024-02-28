package nz.ac.canterbury.seng302.gardenersgrove.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {
    Logger logger = LoggerFactory.getLogger(HomeController.class);

    /**
     * Gets the thymeleaf page representing the /home page
     * @return thymeleaf homePage
     */

    @GetMapping("/home")
    public String getHome() {
        logger.info("GET /home");
        return "pages/homePage";
    }
}
