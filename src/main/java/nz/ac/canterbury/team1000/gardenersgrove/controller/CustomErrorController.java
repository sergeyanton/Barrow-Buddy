package nz.ac.canterbury.team1000.gardenersgrove.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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
        model.addAttribute("debug", true);
        if (status == null) {
            model.addAttribute("errorMessage", "Something went wrong");
            return "error/errorPage";
        }
        HttpStatus statusCode = HttpStatus.valueOf(Integer.parseInt(status.toString()));
        model.addAttribute("errorMessage", "%s - %s".formatted(statusCode.value(), statusCode.getReasonPhrase()));
        System.out.println(model.asMap().entrySet().toString());
        return "error/errorPage";
    }

    public String getErrorPath() {
        return "/error";
    }
}