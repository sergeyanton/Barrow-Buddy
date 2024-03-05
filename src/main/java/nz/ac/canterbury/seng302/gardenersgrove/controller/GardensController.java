package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.classes.ValidityCheck;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpServletRequest;

/**
 * This is the controller for the gardens page.
 */
@Controller
public class GardensController {
    Logger logger = LoggerFactory.getLogger(GardensController.class);
    private final GardenService gardenService;

    @Autowired
    public GardensController(GardenService gardenService) {
        this.gardenService = gardenService;
    }

    @ModelAttribute("currentUrl")
    private String getCurrentPath(HttpServletRequest request) {
        // the url path including query parameters
        if (request.getQueryString() == null) {
            return request.getRequestURI();
        }
        return request.getRequestURI() + "?" + request.getQueryString();
    }

    @ModelAttribute("allGardens")
    private List<Garden> getAllGardens() {
        return gardenService.getGardens();
    }

    /**
     * Gets form to be displayed, includes the ability to display results of previous form when
     * linked to from POST form
     *
     * @param gardenName previous garden name entered into form to be displayed
     * @param gardenLocation previous garden location entered into form to be displayed
     * @param gardenSize previous garden size entered into form to be displayed
     * @param model (map-like) representation of gardenName, gardenLocation, garden Size and
     *        isValidName & isValidLocation boolean for use in thymeleaf
     * @return thymeleaf demoFormTemplate
     */
    @GetMapping("/gardens/create")
    public String gardenCreateGet(HttpServletRequest request,
            @RequestParam(name = "gardenName", required = false,
                    defaultValue = "") String gardenName,
            @RequestParam(name = "gardenLocation", required = false,
                    defaultValue = "") String gardenLocation,
            @RequestParam(name = "gardenSize", required = false,
                    defaultValue = "") String gardenSize,
            Model model) {
        logger.info("GET /gardens/create");
        model.addAttribute("gardenName", gardenName);
        model.addAttribute("gardenLocation", gardenLocation);
        model.addAttribute("gardenSize", gardenSize);
        String nextDestination = Optional.ofNullable(request.getParameter("next")).orElse("/");
        model.addAttribute("nextDestination", nextDestination);

        return "createGarden";
    }

    /**
     * Posts a form response with name, location, and size of the garden
     *
     * @param gardenName name of garden
     * @param gardenLocation location of garden
     * @param gardenSize size of garden
     * @param model (map-like) representation of name for use in thymeleaf, with values being set to
     *        relevant parameters provided
     * @return thymeleaf demoFormTemplate
     *
     */
    @PostMapping("/gardens/create")
    public String gardenCreatePost(HttpServletRequest request,
            @RequestParam(name = "gardenName") String gardenName,
            @RequestParam(name = "gardenLocation") String gardenLocation,
            @RequestParam(name = "gardenSize") String gardenSize, Model model) {
        logger.info("POST /gardens/create");

        model.addAttribute("gardenName", gardenName);
        model.addAttribute("gardenLocation", gardenLocation);
        model.addAttribute("gardenSize", gardenSize);

        String nextDestination = Optional.ofNullable(request.getParameter("next")).orElse("/");
        model.addAttribute("nextDestination", nextDestination);

        Optional<String> validGardenSizeCheck = ValidityCheck.validateGardenSize(gardenSize);
        Optional<String> validGardenNameCheck = ValidityCheck.validGardenName(gardenName);
        Optional<String> validGardenLocationCheck =
                ValidityCheck.validGardenLocation(gardenLocation);

        if (validGardenNameCheck.isPresent()) {
            model.addAttribute("gardenNameError", validGardenNameCheck.get());
        } else {
            // clear any previous error message
            model.addAttribute("gardenNameError", "");
        }
        if (validGardenLocationCheck.isPresent()) {
            model.addAttribute("gardenLocationError", validGardenLocationCheck.get());
        } else {
            model.addAttribute("gardenLocationError", "");
        }
        if (validGardenSizeCheck.isPresent()) {
            model.addAttribute("gardenSizeError", validGardenSizeCheck.get());
        } else {
            model.addAttribute("gardenSizeError", "");
        }

        if (ValidityCheck.validGardenForm(gardenName, gardenLocation, gardenSize)) {
            Garden addedGarden =
                    gardenService.addGarden(new Garden(gardenName, gardenLocation, gardenSize));
            logger.info("Garden created: " + addedGarden);
            return "redirect:/gardens/" + addedGarden.getId();
        }

        return "createGarden";
    }

    /**
     * Gets all form responses (gardens)
     *
     * @param model (map-like) representation of results to be used by thymeleaf
     * @return thymeleaf createdGardens
     */
    @GetMapping("/gardens")
    public String viewGardens(Model model) {
        logger.info("GET /gardens");
        model.addAttribute("gardens", gardenService.getGardens());
        return "createdGardens";
    }

    /**
     * Gets name of garden that was clicked on.
     * 
     * @param model (map-like) representation of results to be used by thymeleaf
     * @return thymeleaf demoResponseTemplate
     */
    @GetMapping("/gardens/{gardenId}")
    public String viewGarden(@PathVariable("gardenId") Long gardenId, Model model) {
        logger.info("GET /gardens/" + gardenId);
        model.addAttribute("garden", gardenService.getGardenById(gardenId));
        return "gardenProfile";
    }

    /**
     * Gets form to be displayed, includes the ability to display results of previous form when
     * linked to from POST form
     * 
     * @param model (map-like) representation of garden for use in thymeleaf
     * @return thymeleaf demoFormTemplate
     */
    @GetMapping("/gardens/{gardenId}/edit")
    public String gardenEditGet(@PathVariable("gardenId") Long gardenId, Model model) {
        logger.info("GET /gardens/" + gardenId + "/edit");
        Garden garden = gardenService.getGardenById(gardenId);
        model.addAttribute("garden", garden);

        return "editGarden";
    }

    /**
     * Posts a form response with name, location, and size of the garden
     *
     * @param gardenName name of garden
     * @param gardenLocation location of garden
     * @param gardenSize size of garden
     * @param model (map-like) representation of name for use in thymeleaf, with values being set to
     *        relevant parameters provided
     * @return thymeleaf demoFormTemplate
     *
     */
    @PostMapping("/gardens/{gardenId}/edit")
    public String gardenEditPost(@PathVariable("gardenId") Long gardenId,
            @RequestParam(name = "gardenName") String gardenName,
            @RequestParam(name = "gardenLocation") String gardenLocation,
            @RequestParam(name = "gardenSize") String gardenSize, Model model) {
        logger.info("POST /gardens/" + gardenId + "/edit");

        // TODO get rid of code duplication (write a function)

        model.addAttribute("gardenName", gardenName);
        model.addAttribute("gardenLocation", gardenLocation);
        model.addAttribute("gardenSize", gardenSize);

        Optional<String> validGardenSizeCheck = ValidityCheck.validateGardenSize(gardenSize);
        Optional<String> validGardenNameCheck = ValidityCheck.validGardenName(gardenName);
        Optional<String> validGardenLocationCheck =
                ValidityCheck.validGardenLocation(gardenLocation);

        if (validGardenNameCheck.isPresent()) {
            model.addAttribute("gardenNameError", validGardenNameCheck.get());
        } else {
            model.addAttribute("gardenNameError", "");
        }
        if (validGardenLocationCheck.isPresent()) {
            model.addAttribute("gardenLocationError", validGardenLocationCheck.get());
        } else {
            model.addAttribute("gardenLocationError", "");
        }
        if (validGardenSizeCheck.isPresent()) {
            model.addAttribute("gardenSizeError", validGardenSizeCheck.get());
        } else {
            model.addAttribute("gardenSizeError", "");
        }

        Garden garden = gardenService.getGardenById(gardenId);

        if (ValidityCheck.validGardenForm(gardenName, gardenLocation, gardenSize)) {
            garden.setName(gardenName);
            garden.setLocation(gardenLocation);
            garden.setSize(gardenSize);

            gardenService.updateGarden(garden);
            logger.info("Garden updated: " + garden);
            return "redirect:/gardens/" + garden.getId();
        }

        model.addAttribute(garden); // so that editGarden.html knows the id of garden being edited.
        return "editGarden";
    }
}
