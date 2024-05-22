package nz.ac.canterbury.team1000.gardenersgrove.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Build Properties Controller
 * Used as a way to implement global config variables.
 * Allows for adding common variables to the Model,
 * which will then get passed through to ThymeLeaf pages.
 *
 * E.g. The "debug" variable can be accessed in the "error.html" page,
 * without setting it manually from an ErrorController.
 */
@ControllerAdvice
public class BuildPropertiesController {
    /**
     * Set a global "debug" variable,
     * which is true if an only if the DEBUG environment variable is set to "true"
     * @return Whether or not the application is in debug mode
     */
    @ModelAttribute("debug")
    public boolean isDev() {
        return true;
        // String debugFlag = System.getenv("DEBUG");
        // return debugFlag != null && debugFlag.equals("true");
    }
}