package nz.ac.canterbury.seng302.gardenersgrove.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {
    Logger logger = LoggerFactory.getLogger(HomeController.class);

    @GetMapping("/editProfile")
    public String getHome() {
        logger.info("GET /editProfile");
        return "pages/editProfilePage";
    }
}
