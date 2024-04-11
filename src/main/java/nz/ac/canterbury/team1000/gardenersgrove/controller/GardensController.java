package nz.ac.canterbury.team1000.gardenersgrove.controller;

import java.util.List;
import java.util.Optional;

import nz.ac.canterbury.team1000.gardenersgrove.form.GardenForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpServletRequest;
import nz.ac.canterbury.team1000.gardenersgrove.classes.ValidityCheck;
import nz.ac.canterbury.team1000.gardenersgrove.entity.Garden;
import nz.ac.canterbury.team1000.gardenersgrove.entity.Plant;
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

    @Autowired
    public GardensController(GardenService gardenService, PlantService plantService,
                             UserService userService) {
        this.gardenService = gardenService;
        this.plantService = plantService;
        this.userService = userService;
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
     * Handles GET requests from the /gardens/create endpoint.
     * Displays results of previous form when linked to from POST request
     *
     * @param createGardenForm the GardenForm object representing the new garden's details,
     *                         useful for seeing erroneous inputs of a failed POST request
     * @return the view to display, 'createGarden', which contains a form
     */
    @GetMapping("/gardens/create")
    public String gardenCreateGet(@ModelAttribute("createGardenForm") GardenForm createGardenForm) {
        logger.info("GET /gardens/create");
        return "createGarden";
    }

    /**
     * Handles POST requests from the /gardens/create endpoint.
     * Handles creation of new gardens
     *
     * @param request           the HttpServletRequest object containing the request information
     * @param createGardenForm  the GardenForm object representing the new garden's details
     * @param bindingResult     the BindingResult object for validation errors
     * @return the view to display:
     *         - If there are validation errors, stays on the 'Create Garden' form.
     *         - Else, redirect to the newly created garden's profile page.
     */
    @PostMapping("/gardens/create")
    public String gardenCreatePost(HttpServletRequest request,
                                   @ModelAttribute("createGardenForm") GardenForm createGardenForm,
                                   BindingResult bindingResult) {
        logger.info("POST /gardens/create");
        GardenForm.validate(createGardenForm, bindingResult);
        if (bindingResult.hasErrors()) {
            return "createGarden";
        }

        Garden newGarden = createGardenForm.getGarden();
        gardenService.addGarden(newGarden);
        logger.info("Garden created: " + newGarden);
        return "redirect:/gardens/" + newGarden.getId();
    }

    /**
     * Handles GET requests from the /gardens endpoint.
     * Displays all gardens that the user owns.
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
     * Handles GET requests from the /gardens/{gardenId} endpoint.
     * Displays details of the garden with the given id
     *
     * @param model (map-like) representation of results to be used by thymeleaf
     * @return thymeleaf gardenProfile
     */
    @GetMapping("/gardens/{gardenId}")
    public String viewGarden(@PathVariable("gardenId") Long gardenId, Model model) {
        logger.info("GET /gardens/" + gardenId);
        model.addAttribute("garden", gardenService.getGardenById(gardenId));
        return "gardenProfile";
    }

    /**
     * Handles GET requests from the /gardens/{gardenId}/edit endpoint.
     * Displays the 'Edit Garden' form.
     *
     * @param gardenId the id of the garden being got
     * @param editGardenForm the GardenForm object representing the edited garden's details,
     *                       useful for seeing erroneous inputs of a failed POST request
     * @return thymeleaf editGarden
     */
    @GetMapping("/gardens/{gardenId}/edit")
    public String gardenEditGet(@PathVariable("gardenId") Long gardenId,
                                @ModelAttribute("editGardenForm") GardenForm editGardenForm) {
        logger.info("GET /gardens/" + gardenId + "/edit");
        Garden garden = gardenService.getGardenById(gardenId);
        editGardenForm.setName(garden.getName());
        editGardenForm.setLocation(garden.getLocation());
        if (garden.getSize() != null) editGardenForm.setSize(garden.getSize().toString());
        return "editGarden";
    }

    /**
     * Handles POST requests from the /gardens/{gardenId}/edit endpoint.
     * Handles editing of gardens
     *
     * @param request           the HttpServletRequest object containing the request information
     * @param editGardenForm    the GardenForm object representing the garden's new details
     * @param bindingResult     the BindingResult object for validation errors
     * @param gardenId          the id of the garden being edited
     * @return the view to display:
     *         - If there are validation errors, stays on the 'Edit Garden' form.
     *         - Else, redirect to the edited garden's profile page.
     */
    @PostMapping("/gardens/{gardenId}/edit")
    public String gardenEditPost(HttpServletRequest request,
                                 @ModelAttribute("editGardenForm") GardenForm editGardenForm,
                                 BindingResult bindingResult,
                                 @PathVariable("gardenId") Long gardenId) {
        logger.info("POST /gardens/" + gardenId + "/edit");
        GardenForm.validate(editGardenForm, bindingResult);
        if (bindingResult.hasErrors()) {
            return "editGarden";
        }

        Garden garden = gardenService.getGardenById(gardenId);
        Garden edit = editGardenForm.getGarden();
        garden.setName(edit.getName());
        garden.setLocation(edit.getLocation());
        garden.setSize(edit.getSize());

        gardenService.updateGarden(garden);

        logger.info("Garden edited: " + garden);
        return "redirect:/gardens/" + garden.getId();
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

        // TODO handle when the gardenId is not for an existing garden (.getGardenById)

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
