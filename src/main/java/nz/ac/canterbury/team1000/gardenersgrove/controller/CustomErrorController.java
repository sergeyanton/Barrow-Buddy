package nz.ac.canterbury.team1000.gardenersgrove.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

/**
 * Custom error controller class
 * Shows a custom error page for the error types 404 and 403
 * Otherwise returns a generic ThymeLeaf error page
 */
@Controller
public class CustomErrorController implements ErrorController {
    /**
     * @return A custom error page if a custom message has been specified for the request's error code,
     *          else the default ThymeLeaf error page
     */
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        model.addAttribute("status", status);
        model.addAttribute("message", message);

        final Map<Integer, String> customErrorMessages = new HashMap<>() {{
                put(HttpStatus.NOT_FOUND.value(), "Not Found");
                put(HttpStatus.FORBIDDEN.value(), "You don't have permission to access this");
        }};

        if (status != null) {
            // Show a custom error page if the status code is found in customErrorMessages
            int statusCode = Integer.parseInt(status.toString());
            if (customErrorMessages.containsKey(statusCode)) {
                model.addAttribute("customErrorMessage", customErrorMessages.get(statusCode));
                return "errorPages/custom";
            }
        }

        return "errorPages/default";

    }
}