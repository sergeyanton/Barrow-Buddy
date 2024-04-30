package nz.ac.canterbury.team1000.gardenersgrove.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class BuildPropertiesController {
    @ModelAttribute("debug")
    public boolean isDev() {
        String debugFlag = System.getenv("DEBUG");
        return debugFlag != null && debugFlag.equals("true");
    }
}