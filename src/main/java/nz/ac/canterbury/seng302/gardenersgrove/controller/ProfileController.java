package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProfileController {
    Logger logger = LoggerFactory.getLogger(HomeController.class);

    @GetMapping("/editProfile")
    public String getEditProfilePage() {
        logger.info("GET /editProfile");
        return "pages/editProfilePage";
    }
    @PostMapping
    public String editProfile(User currentuser, Model model) {
        logger.info("POST /editProfile");
        
        return "redirect:/profile";
    }
}
