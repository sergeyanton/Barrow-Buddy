package nz.ac.canterbury.team1000.gardenersgrove.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class BuildPropertiesController {
    @ModelAttribute("debug")
    public boolean isDev() {
        return System.getenv("DEBUG").equals("true");
    }
}