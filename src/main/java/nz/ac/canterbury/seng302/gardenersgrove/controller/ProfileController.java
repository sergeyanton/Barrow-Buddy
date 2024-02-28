package nz.ac.canterbury.seng302.gardenersgrove.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProfileController {

    Logger logger = LoggerFactory.getLogger(HomeController.class);

    /**
     * Gets the thymeleaf page representing the /profile page (displaying user's information)
     * @return thymeleaf profilePage
     */
    @GetMapping("/profile")
    public String login() {
        logger.info("GET /profile");
        return "pages/profilePage";
    }


}
