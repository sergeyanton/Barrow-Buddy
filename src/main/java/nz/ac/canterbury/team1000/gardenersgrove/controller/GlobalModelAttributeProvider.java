package nz.ac.canterbury.team1000.gardenersgrove.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import jakarta.servlet.http.HttpServletRequest;
import nz.ac.canterbury.team1000.gardenersgrove.entity.Garden;
import nz.ac.canterbury.team1000.gardenersgrove.service.GardenService;

/**
 * Controller advice is applied to every controller and is used to provide model attributes the need to be access globally.
 */
@ControllerAdvice
public class GlobalModelAttributeProvider {
    private final GardenService gardenService;

    @Autowired
    public GlobalModelAttributeProvider(GardenService gardenService) {
        this.gardenService = gardenService;
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
     * Necessary for being able to display each garden in the nav bar
     * 
     * @return all gardens currently in the database
     */
    @ModelAttribute("allGardens")
    private List<Garden> getAllGardens() {
        return gardenService.getGardens();
    }
}
