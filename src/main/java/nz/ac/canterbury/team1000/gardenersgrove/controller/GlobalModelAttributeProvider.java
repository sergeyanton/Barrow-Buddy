package nz.ac.canterbury.team1000.gardenersgrove.controller;

import java.util.Arrays;
import java.util.List;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import jakarta.servlet.http.HttpServletRequest;
import nz.ac.canterbury.team1000.gardenersgrove.entity.Garden;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.service.GardenService;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;

/**
 * Controller advice is applied to every controller and is used to provide model attributes the need to be access globally.
 */
@ControllerAdvice
public class GlobalModelAttributeProvider {
    private final GardenService gardenService;
    private final UserService userService;

    public GlobalModelAttributeProvider(GardenService gardenService, UserService userService) {
        this.gardenService = gardenService;
        this.userService = userService;
    }

    /**
     * Provide the currentUrl to every model/controller
     * @param request the current request
     * @return the current url
     */
    @ModelAttribute("currentUrl")
    private String getCurrentPath(HttpServletRequest request) {
        // the url path including query parameters
        if (request.getQueryString() == null) {
            return request.getRequestURI();
        }
        return request.getRequestURI() + "?" + request.getQueryString();
    }

    /**
     * Provide the paginationPath, this is the current url without the page number or base url,
     * So /test and /prod get stripped from /test?page=1 and /prod?page=2
     * @param request the current request
     * @return the current url without the page number or base url
     */
    @ModelAttribute("paginationPath")
    public String getPaginationPath(HttpServletRequest request) {
        String currentPath = request.getRequestURI();
        String baseUrl = request.getContextPath(); // Get the base URL, e.g., /test
        String queryString = request.getQueryString();

        // Remove the base URL from the path
        if (currentPath.startsWith(baseUrl)) {
            currentPath = currentPath.substring(baseUrl.length());
        }

        if (queryString != null) {
            currentPath += "?" + queryString.replaceAll("(^|&)page=\\d+", "");
        }

        // Remove floating question mark (this happens when page is the only query parameter left)
        if (currentPath.endsWith("?")) {
            currentPath = currentPath.substring(0, currentPath.length() - 1);
        }

        return currentPath;
    }


    /**
     * Necessary for being able to display each garden in the nav bar
     * 
     * @return all gardens currently in the database
     */
    @ModelAttribute("allGardens")
    private List<Garden> getAllGardens() {
        User user = userService.getLoggedInUser();
        if (user == null) {
            return Arrays.asList();
        }
        return gardenService.getUserGardens(user.getId());
    }
}
