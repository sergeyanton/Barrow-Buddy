package nz.ac.canterbury.seng302.gardenersgrove.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;

public class HomeController {
    Logger loggerLogin = LoggerFactory.getLogger(LoginController.class);

    /**
     * Gets the thymeleaf page representing the /home page
     * @return thymeleaf homePage
     */
    @GetMapping("/home")
    public String home() {
        loggerLogin.info("GET /home");
        return "pages/homePage";
    }
}
