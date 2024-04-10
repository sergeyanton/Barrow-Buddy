package nz.ac.canterbury.team1000.gardenersgrove.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpServletRequest;
import nz.ac.canterbury.team1000.gardenersgrove.classes.ValidityCheck;
import nz.ac.canterbury.team1000.gardenersgrove.entity.Garden;
import nz.ac.canterbury.team1000.gardenersgrove.entity.Plant;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.service.GardenService;
import nz.ac.canterbury.team1000.gardenersgrove.service.PlantService;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;

/**
 * This is the controller for the gardens page.
 */
@Controller
public class GardensController {
    Logger logger = LoggerFactory.getLogger(GardensController.class);
    private final GardenService gardenService;
    private final PlantService plantService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    
    public GardensController(GardenService gardenService, PlantService plantService,
                             UserService userService, AuthenticationManager authenticationManager) {
        this.gardenService = gardenService;
        this.plantService = plantService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
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
        User loggedInUser = userService.getLoggedInUser();
        if (loggedInUser == null) {
            return Arrays.asList();
        }
        return gardenService.getUserGardens(loggedInUser.getId());
    }

    /**
     * Gets form to be displayed, includes the ability to display results of previous form when
     * linked to from POST form
     *
     * @param gardenName     previous garden name entered into form to be displayed
     * @param gardenLocation previous garden location entered into form to be displayed
     * @param gardenSize     previous garden size entered into form to be displayed
     * @param model          (map-like) representation of gardenName, gardenLocation, garden Size and
     *                       isValidName & isValidLocation boolean for use in thymeleaf
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
     * @param gardenName     name of garden
     * @param gardenLocation location of garden
     * @param gardenSize     size of garden
     * @param model          (map-like) representation of name for use in thymeleaf, with values being set to
     *                       relevant parameters provided
     * @return thymeleaf demoFormTemplate
     */
    @PostMapping("/gardens/create")
    public String gardenCreatePost(HttpServletRequest request,
                                   @RequestParam(name = "gardenName") String gardenName,
                                   @RequestParam(name = "gardenLocation") String gardenLocation,
                                   @RequestParam(name = "gardenSize") String gardenSize, Model model) {
        logger.info("POST /gardens/create");

        User loggedInUser = userService.getLoggedInUser();

        if (ValidityCheck.validGardenForm(gardenName, gardenLocation, gardenSize)) {
            Garden addedGarden =
                    gardenService.addGarden(new Garden(gardenName, gardenLocation, gardenSize, loggedInUser));
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
        User loggedInUser = userService.getLoggedInUser();
        model.addAttribute("gardens", gardenService.getUserGardens(loggedInUser.getId()));
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
        Garden garden = gardenService.getGardenById(gardenId);
        User loggedInUser = userService.getLoggedInUser();
        // check that the garden belongs to the logged in user
        if (garden.getOwner().getId() != loggedInUser.getId()) {
            // respond with 403 Forbidden
            return "error/403";
        }
        model.addAttribute("garden", garden);
        return "gardenProfile";
    }

    /**
     * Gets form to be displayed, includes the ability to display results of previous form when
     * linked to from POST form
     *
     * @param model (map-like) representation of garden for use in thymeleaf
     * @return thymeleaf editGarden
     */
    @GetMapping("/gardens/{gardenId}/edit")
    public String gardenEditGet(@PathVariable("gardenId") Long gardenId, Model model) {
        logger.info("GET /gardens/" + gardenId + "/edit");

        User loggedInUser = userService.getLoggedInUser();
        Garden garden = gardenService.getGardenById(gardenId);

        if (garden.getOwner().getId() != loggedInUser.getId()) {
            // respond with 403 Forbidden
            return "error/403";
        }

        model.addAttribute("gardenId", gardenId);
        model.addAttribute("gardenName", garden.getName());
        model.addAttribute("gardenLocation", garden.getLocation());
        model.addAttribute("gardenSize", garden.getSize());
        model.addAttribute("actionLabel", "Edit Garden");
        return "editGarden";
    }

    /**
     * Posts a form response with name, location, and size of the garden
     *
     * @param gardenName     name of garden
     * @param gardenLocation location of garden
     * @param gardenSize     size of garden
     * @param model          (map-like) representation of values for use in thymeleaf, with values being set
     *                       to relevant parameters provided
     * @return thymeleaf editGarden
     */
    @PostMapping("/gardens/{gardenId}/edit")
    public String gardenEditPost(@PathVariable("gardenId") Long gardenId,
                                 @RequestParam(name = "gardenName") String gardenName,
                                 @RequestParam(name = "gardenLocation") String gardenLocation,
                                 @RequestParam(name = "gardenSize") String gardenSize, Model model) {
        logger.info("POST /gardens/" + gardenId + "/edit");

        User loggedInUser = userService.getLoggedInUser();
        Garden garden = gardenService.getGardenById(gardenId);

        if (garden.getOwner().getId() != loggedInUser.getId()) {
            // respond with 403 Forbidden
            return "error/403";
        }

        if (ValidityCheck.validGardenForm(gardenName, gardenLocation, gardenSize)) {
            garden.setName(gardenName);
            garden.setLocation(gardenLocation);
            garden.setSize(gardenSize);

            gardenService.updateGarden(garden);
            logger.info("Garden updated: " + garden);
            return "redirect:/gardens/" + garden.getId();
        }
        model.addAttribute("actionLabel", "Edit Garden");
        model.addAttribute("gardenId", gardenId);
        model.addAttribute("gardenSize", gardenSize);
        model.addAttribute("gardenLocation", gardenLocation);
        model.addAttribute("gardenName", gardenName);

        displayGardenFormErrors(gardenName, gardenLocation, gardenSize, model);
        return "editGarden";
    }


    /**
     * Gets form to be displayed, includes the ability to display results of previous form when
     * linked to from POST form
     *
     * @param gardenId         id of garden that this plant belongs to
     * @param plantName        the name of the plant
     * @param plantCount       the amount of this plant in the garden
     * @param plantDescription a short description of the plant
     * @param plantedOnDate    the date that the plant was planted on
     * @param model            (map-like) representation of values for use in thymeleaf, with values being set
     *                         to relevant parameters provided
     * @return thymeleaf createPlant
     */
    @GetMapping("/gardens/{gardenId}/plants/create")
    public String gardenCreatePlantGet(@PathVariable("gardenId") Long gardenId,
                                       @RequestParam(name = "plantName", required = false, defaultValue = "") String plantName,
                                       @RequestParam(name = "plantCount", required = false,
                                               defaultValue = "") String plantCount,
                                       @RequestParam(name = "plantDescription", required = false,
                                               defaultValue = "") String plantDescription,
                                       @RequestParam(name = "plantedOnDate", required = false,
                                               defaultValue = "") String plantedOnDate,
                                       Model model) {
        logger.info("GET /gardens/" + gardenId + "/plants/create");

        User loggedInUser = userService.getLoggedInUser();
        Garden garden = gardenService.getGardenById(gardenId);

        if (garden.getOwner().getId() != loggedInUser.getId()) {
            // respond with 403 Forbidden
            return "error/403";
        }

        model.addAttribute("gardenId", gardenId);
        model.addAttribute("plantName", plantName);
        model.addAttribute("plantCount", plantCount);
        model.addAttribute("plantDescription", plantDescription);
        model.addAttribute("plantedOnDate", plantedOnDate);

        model.addAttribute("actionLabel", "Create Plant");
        return "createPlant";
    }

    /**
     * Posts a form response with name, location, and size of the garden
     *
     * @param gardenId         id of garden that this plant belongs to
     * @param plantName        the name of the plant
     * @param plantCount       the amount of this plant in the garden
     * @param plantDescription a short description of the plant
     * @param plantedOnDate    the date that the plant was planted on
     * @param model            (map-like) representation of values for use in thymeleaf, with values being set
     *                         to relevant parameters provided
     * @return thymeleaf createPlant if invalid form, gardens/{gardenId} if valid
     */
    @PostMapping("/gardens/{gardenId}/plants/create")
    public String gardenCreatePlantPost(@PathVariable("gardenId") Long gardenId,
                                        @RequestParam(name = "plantName") String plantName,
                                        @RequestParam(name = "plantCount") String plantCount,
                                        @RequestParam(name = "plantDescription") String plantDescription,
                                        @RequestParam(name = "plantedOnDate") String plantedOnDate, Model model) {
        logger.info("POST /gardens/" + gardenId + "/plants/create");

        User loggedInUser = userService.getLoggedInUser();
        Garden garden = gardenService.getGardenById(gardenId);

        if (garden.getOwner().getId() != loggedInUser.getId()) {
            // respond with 403 Forbidden
            return "error/403";
        }

        if (ValidityCheck.validPlantForm(plantName, plantCount, plantDescription, plantedOnDate)) {
            plantedOnDate = plantedOnDate.isBlank() ? ""
                    : plantedOnDate.split("-")[2] + "/" + plantedOnDate.split("-")[1] + "/"
                    + plantedOnDate.split("-")[0]; // maybe not necessary
            Plant addedPlant = plantService.addPlant(
                    new Plant(plantName, plantCount, plantDescription, plantedOnDate, gardenId));
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
     * @param gardenName     name of the garden
     * @param gardenLocation location of the garden
     * @param gardenSize     size of the garden
     * @param model          (map-like) representation of values for use in thymeleaf, with values being set
     *                       to relevant parameters provided
     */
    private void displayGardenFormErrors(String gardenName, String gardenLocation,
                                         String gardenSize, Model model) {
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
     * @param plantName        the name of the plant
     * @param plantCount       the amount of plants in the garden
     * @param plantDescription a short description of the plant
     * @param plantedOnDate    the date that the plant was planted
     * @param model            (map-like) representation of values for use in thymeleaf, with values being set
     *                         to relevant parameters provided
     */
    private void displayPlantFormErrors(String plantName, String plantCount,
                                        String plantDescription, String plantedOnDate, Model model) {
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
