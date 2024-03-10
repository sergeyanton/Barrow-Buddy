package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.classes.ValidityCheck;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Plant;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import java.util.List;
import java.util.Optional;

import nz.ac.canterbury.seng302.gardenersgrove.service.PlantService;
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
    private final PlantService plantService;

    @Autowired
    public GardensController(GardenService gardenService, PlantService plantService) {
        this.gardenService = gardenService;
        this.plantService = plantService;
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

        model.addAttribute("actionLabel", "Create Garden");
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

        if (ValidityCheck.validGardenForm(gardenName, gardenLocation, gardenSize)) {
            Garden addedGarden =
                    gardenService.addGarden(new Garden(gardenName, gardenLocation, gardenSize));
            logger.info("Garden created: " + addedGarden);
            return "redirect:/gardens/" + addedGarden.getId();
        }

        model.addAttribute("actionLabel", "Create Garden");
        String nextDestination = Optional.ofNullable(request.getParameter("next")).orElse("/");
        model.addAttribute("nextDestination", nextDestination);

        displayGardenFormErrors(gardenName, gardenLocation, gardenSize, model);

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

        model.addAttribute("actionLabel", "Edit Garden");
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

        // TODO Handle error gracefully when user puts invalid id in url (do for each
        // gardenService.getGardenById)
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
        model.addAttribute("actionLabel", "Edit Garden");
        displayGardenFormErrors(gardenName, gardenLocation, gardenSize, model);
        return "editGarden";
    }

    @GetMapping("/gardens/{gardenId}/plants/create")
    public String gardenCreatePlantGet(@PathVariable("gardenId") Long gardenId,
                                       @RequestParam(name = "plantName", required = false,
                                               defaultValue = "") String plantName,
                                       @RequestParam(name = "plantCount", required = false,
                                               defaultValue = "") String plantCount,
                                       @RequestParam(name = "plantDescription", required = false,
                                               defaultValue = "") String plantDescription,
                                       @RequestParam(name = "plantedOnDate", required = false,
                                               defaultValue = "") String plantedOnDate,
                                       Model model) {
        logger.info("GET /gardens/" + gardenId + "/plants/create");

        model.addAttribute("gardenId", gardenId);
        model.addAttribute("plantName", plantName);
        model.addAttribute("plantCount", plantCount);
        model.addAttribute("plantDescription", plantDescription);
        model.addAttribute("plantedOnDate", plantedOnDate);

        model.addAttribute("actionLabel", "Create Plant");
        return "createPlant";
    }

    @PostMapping("/gardens/{gardenId}/plants/create")
    public String gardenCreatePlantPost(@PathVariable("gardenId") Long gardenId,
                                       @RequestParam(name = "plantName") String plantName,
                                       @RequestParam(name = "plantCount") String plantCount,
                                       @RequestParam(name = "plantDescription") String plantDescription,
                                       @RequestParam(name = "plantedOnDate") String plantedOnDate,
                                       Model model) {
        logger.info("POST /gardens/" + gardenId + "/plants/create");

        if (ValidityCheck.validPlantForm(plantName, plantCount, plantDescription, plantedOnDate)) {
            plantedOnDate = plantedOnDate.split("-")[2] + "/" + plantedOnDate.split("-")[1] + "/" + plantedOnDate.split("-")[0]; // maybe not necessary
            Plant addedPlant = plantService.addPlant(new Plant(plantName, plantCount, plantDescription, plantedOnDate, gardenId));
            logger.info("Plant created: " + addedPlant);
            return "redirect:/gardens/" + gardenId;
        }

        displayPlantFormErrors(plantName, plantCount, plantDescription, plantedOnDate, model);

        model.addAttribute("actionLabel", "Create Plant");

        return "createPlant";
    }

    /**
     * A helper function to avoid duplication of code. Both the create & edit forms for a garden
     * have the exact same error messages so this code is used in both POSTs.
     *
     * @param gardenName name of the garden
     * @param gardenLocation location of the garden
     * @param gardenSize size of the garden
     * @param model (map-like) representation of name for use in thymeleaf, with values being set to
     *        relevant parameters provided
     */
    private void displayGardenFormErrors(String gardenName, String gardenLocation, String gardenSize,
            Model model) {
        model.addAttribute("gardenName", gardenName);
        model.addAttribute("gardenLocation", gardenLocation);
        model.addAttribute("gardenSize", gardenSize);

        Optional<String> validGardenNameCheck = ValidityCheck.validGardenName(gardenName);
        Optional<String> validGardenLocationCheck =
                ValidityCheck.validGardenLocation(gardenLocation);
        Optional<String> validGardenSizeCheck = ValidityCheck.validateGardenSize(gardenSize);

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
    }

    /**
     * A helper function to avoid duplication of code.
     *
     * @param plantName the name of the plant
     * @param plantCount the amount of plants in the garden
     * @param plantDescription a short description of the plant
     * @param plantedOnDate the date that the plant was planted
     * @param model (map-like) representation of name for use in thymeleaf, with values being set to
     *      relevant parameters provided
     */
    private void displayPlantFormErrors(String plantName, String plantCount, String plantDescription, String plantedOnDate,
                                         Model model) {
        model.addAttribute("plantName", plantName);
        model.addAttribute("plantCount", plantCount);
        model.addAttribute("plantDescription", plantDescription);
        model.addAttribute("plantedOnDate", plantedOnDate);

        Optional<String> validPlantNameCheck = ValidityCheck.validatePlantName(plantName);
        Optional<String> validPlantCountCheck = ValidityCheck.validatePlantCount(plantCount);
        Optional<String> validPlantDescription = ValidityCheck.validatePlantDescription(plantDescription);
        Optional<String> validPlantedOnDate = ValidityCheck.validateDate(plantedOnDate);

        if (validPlantNameCheck.isPresent()) {
            model.addAttribute("plantNameError", validPlantNameCheck.get());
        } else {
            model.addAttribute("plantNameError", "");
        }
        if (validPlantCountCheck.isPresent()) {
            model.addAttribute("plantCountError", validPlantCountCheck.get());
        } else {
            model.addAttribute("plantCountError", "");
        }
        if (validPlantDescription.isPresent()) {
            model.addAttribute("plantDescriptionError", validPlantDescription.get());
        } else {
            model.addAttribute("plantDescriptionError", "");
        }
        if (validPlantedOnDate.isPresent()) {
            model.addAttribute("plantedOnDateError", validPlantedOnDate.get());
        } else {
            model.addAttribute("plantedOnDateError", "");
        }
    }
}
