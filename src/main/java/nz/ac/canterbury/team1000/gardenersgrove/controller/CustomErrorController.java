package nz.ac.canterbury.team1000.gardenersgrove.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status == null) return "error";
        int statusCode = Integer.parseInt(status.toString());

        switch (statusCode) {
            case HttpStatus.NOT_FOUND.value():
                model.addAttribute("errorMessage", "This is not what you're looking for");
                return "error-404";
            case HttpStatus.FORBIDDEN.value():
                model.addAttribute("errorMessage", "Go back to where you came from! Morgan.");
                return "error-403";
        }
        return "error";

    }
}