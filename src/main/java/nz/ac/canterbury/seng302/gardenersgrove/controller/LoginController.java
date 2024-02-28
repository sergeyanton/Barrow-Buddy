package nz.ac.canterbury.seng302.gardenersgrove.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    Logger logger = LoggerFactory.getLogger(LandingController.class);

    /**
     * Gets the thymeleaf page representing the /login page (with an eamil and the password to log in)
     * @return thymeleaf loginPage
     */
    @GetMapping("/login")
    public String login() {
        logger.info("GET /login");
        return "pages/loginPage";
    }
}
