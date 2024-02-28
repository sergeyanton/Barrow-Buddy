package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.entity.FormResult;
import nz.ac.canterbury.seng302.gardenersgrove.service.FormService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationController {
    Logger logger = LoggerFactory.getLogger(LandingController.class);

    private String errorMessage = "";

    /**
     * Gets the thymeleaf page representing the /register page (a basic welcome screen with some links)
     * @return thymeleaf registrationPage
     */
    @GetMapping("/register")
    public String registration(Model model) {
        logger.info("GET /register");
        model.addAttribute("errorMessage", errorMessage);
        return "pages/registrationPage";
    }
}
