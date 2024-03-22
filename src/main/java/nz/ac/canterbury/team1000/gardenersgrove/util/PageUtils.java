package nz.ac.canterbury.team1000.gardenersgrove.util;

import org.springframework.ui.Model;

/**
 * A collection of common utilities used in interacting with or returning pages.
 */
public class PageUtils {
    /**
     * Adds an error message to a given page. Requires the user of errorMessageFragment in the page
     * HTML.
     * 
     * @param pagePath The path to the HTML/ThymeLeaf page to add the error message to
     * @param model The request Model
     * @param errorMessage The error message/text to be displayed
     * @return The given pagePath
     */
    public static String pageWithError(String pagePath, Model model, String errorMessage) {
        model.addAttribute("errorMessage", errorMessage);
        return pagePath;
    }
}
