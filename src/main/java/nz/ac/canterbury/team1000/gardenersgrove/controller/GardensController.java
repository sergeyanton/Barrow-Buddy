package nz.ac.canterbury.team1000.gardenersgrove.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import nz.ac.canterbury.team1000.gardenersgrove.form.GardenForm;
import nz.ac.canterbury.team1000.gardenersgrove.form.PlantForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.servlet.http.HttpServletRequest;
import nz.ac.canterbury.team1000.gardenersgrove.entity.Garden;
import nz.ac.canterbury.team1000.gardenersgrove.entity.Plant;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.service.GardenService;
import nz.ac.canterbury.team1000.gardenersgrove.service.PlantService;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;
import org.springframework.web.server.ResponseStatusException;

/**
 * This is the controller for the gardens page.
 */
@Controller
public class GardensController {
    final Logger logger = LoggerFactory.getLogger(GardensController.class);
    private final GardenService gardenService;
    private final PlantService plantService;
    private final UserService userService;


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


    /**
     * Necessary for being able to display each garden in the nav bar
     * @return all gardens currently in the database
     */
    @ModelAttribute("allGardens")
    private List<Garden> getAllGardens() {
        User loggedInUser = userService.getLoggedInUser();
        if (loggedInUser == null) {
            return Arrays.asList();
        }
        return gardenService.getUserGardens(loggedInUser.getId());
    }

    /**
     * Handles GET requests from the /gardens/create endpoint.
     * Displays results of previous form when linked to from POST request
     *
     * @param createGardenForm the GardenForm object representing the new garden's details,
     *                         useful for seeing erroneous inputs of a failed POST request
     * @return the view to display, 'pages/createGardenPage', which contains a form
     */
    @GetMapping("/gardens/create")
    public String gardenCreateGet(@ModelAttribute("createGardenForm") GardenForm createGardenForm) {
        logger.info("GET /gardens/create");
        return "pages/createGardenPage";
    }

    /**
     * Handles POST requests from the /gardens/create endpoint.
     * Handles creation of new gardens
     *
     * @param request          the HttpServletRequest object containing the request information
     * @param createGardenForm the GardenForm object representing the new garden's details
     * @param bindingResult    the BindingResult object for validation errors
     * @return the view to display:
     * - If there are validation errors, stays on the 'Create Garden' form.
     * - Else, redirect to the newly created garden's profile page.
     */
    @PostMapping("/gardens/create")
    public String gardenCreatePost(HttpServletRequest request,
                                   @ModelAttribute("createGardenForm") GardenForm createGardenForm,
                                   BindingResult bindingResult) {
        logger.info("POST /gardens/create");

        GardenForm.validate(createGardenForm, bindingResult);
        if (bindingResult.hasErrors()) {
            return "pages/createGardenPage";
        }

        User loggedInUser = userService.getLoggedInUser();
        Garden newGarden = createGardenForm.getGarden(loggedInUser);
        gardenService.addGarden(newGarden);
        logger.info("Garden created: " + newGarden);
        return "redirect:/gardens/" + newGarden.getId();
    }

    /**
     * Handles GET requests from the /gardens endpoint.
     * Displays all gardens that the user owns.
     *
     * @param model (map-like) representation of results to be used by thymeleaf
     * @return thymeleaf pages/gardensPage
     */
    @GetMapping("/gardens")
    public String viewGardens(Model model) {
        logger.info("GET /gardens");
        User loggedInUser = userService.getLoggedInUser();
        model.addAttribute("gardens", gardenService.getUserGardens(loggedInUser.getId()));
        return "pages/gardensPage";
    }

    /**
     * Handles GET requests from the /gardens/{gardenId} endpoint.
     * Displays details of the garden with the given id
     *
     * @param model (map-like) representation of results to be used by thymeleaf
     * @return thymeleaf pages/gardenProfilePage
     */
    @GetMapping("/gardens/{gardenId}")
    public String viewGarden(@PathVariable("gardenId") Long gardenId, Model model) {
        logger.info("GET /gardens/" + gardenId);
        Garden garden = null;
        try {
            garden = gardenService.getGardenById(gardenId);
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Garden not found"
            );
        }
        User loggedInUser = userService.getLoggedInUser();
        // check that the garden belongs to the logged in user
        if (!Objects.equals(garden.getOwner().getId(), loggedInUser.getId())) {
            // respond with 403 Forbidden
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You don't own this garden"
            );
        }
        model.addAttribute("garden", garden);
        model.addAttribute("plants", plantService.getPlantsByGardenId(garden.getId()));
        return "pages/gardenProfilePage";
    }

    /**
     * Handles GET requests from the /gardens/{gardenId}/edit endpoint.
     * Displays the 'Edit Garden' form.
     *
     * @param gardenId       the id of the garden being got
     * @param editGardenForm the GardenForm object representing the edited garden's details,
     *                       useful for seeing erroneous inputs of a failed POST request
     * @return thymeleaf pages/editGardenPage
     */
    @GetMapping("/gardens/{gardenId}/edit")
    public String gardenEditGet(@PathVariable("gardenId") Long gardenId,
                                @ModelAttribute("editGardenForm") GardenForm editGardenForm) {
        logger.info("GET /gardens/" + gardenId + "/edit");

        User loggedInUser = userService.getLoggedInUser();
        Garden garden = gardenService.getGardenById(gardenId);

        if (!Objects.equals(garden.getOwner().getId(), loggedInUser.getId())) {
            // respond with 403 Forbidden
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You don't own this garden"
            );
        }

        editGardenForm.setName(garden.getName());
        editGardenForm.setLocation(garden.getLocation());
        if (garden.getSize() != null) editGardenForm.setSize(garden.getSize().toString());

        return "pages/editGardenPage";
    }

    /**
     * Handles POST requests from the /gardens/{gardenId}/edit endpoint.
     * Handles editing of gardens
     *
     * @param request        the HttpServletRequest object containing the request information
     * @param editGardenForm the GardenForm object representing the garden's new details
     * @param bindingResult  the BindingResult object for validation errors
     * @param gardenId       the id of the garden being edited
     * @return the view to display:
     * - If there are validation errors, stays on the 'Edit Garden' form.
     * - Else, redirect to the edited garden's profile page.
     */
    @PostMapping("/gardens/{gardenId}/edit")
    public String gardenEditPost(HttpServletRequest request,
                                 @ModelAttribute("editGardenForm") GardenForm editGardenForm,
                                 BindingResult bindingResult,
                                 @PathVariable("gardenId") Long gardenId) {
        logger.info("POST /gardens/" + gardenId + "/edit");

        User loggedInUser = userService.getLoggedInUser();
        Garden garden = gardenService.getGardenById(gardenId);

        if (!Objects.equals(garden.getOwner().getId(), loggedInUser.getId())) {
            // respond with 403 Forbidden
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You don't own this garden"
            );
        }
        GardenForm.validate(editGardenForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "pages/editGardenPage";
        }

        Garden edit =  editGardenForm.getGarden(loggedInUser);
        garden.setName(edit.getName());
        garden.setLocation(edit.getLocation());
        garden.setSize(edit.getSize());

        gardenService.updateGarden(garden);

        logger.info("Garden edited: " + garden);
        return "redirect:/gardens/" + garden.getId();
    }

    /**
     * Handles GET requests from the /gardens/{gardenId}/edit endpoint.
     * Displays the 'Edit Garden' form.
     *
     * @param gardenId        id of garden that this plant belongs to
     * @param createPlantForm the PlantForm object representing the plant's details,
     *                        useful for seeing erroneous inputs of a failed POST request
     * @return thymeleaf pages/createPlantPage
     */
    @GetMapping("/gardens/{gardenId}/plants/create")
    public String gardenCreatePlantGet(@PathVariable("gardenId") Long gardenId,
                                       @ModelAttribute("createPlantForm") PlantForm createPlantForm) {
        logger.info("GET /gardens/" + gardenId + "/plants/create");

        User loggedInUser = userService.getLoggedInUser();
        Garden garden = gardenService.getGardenById(gardenId);

        if (!Objects.equals(garden.getOwner().getId(), loggedInUser.getId())) {
            // respond with 403 Forbidden
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You don't own this garden"
            );
        }
        return "pages/createPlantPage";
    }

    // TODO handle when the gardenId is not for an existing garden (.getGardenById)

    /**
     * Handles POST requests from the /gardens/{gardenId}/plants/create endpoint.
     * Handles creation of plants
     *
     * @param request the HttpServletRequest object containing the request information
     * @param createPlantForm the PlantForm object representing the garden's new details
     * @param bindingResult the BindingResult object for validation errors
     * @param gardenId the id of the garden that the plant is being added to
     * @return  the view to display:
     *          - If there are validation errors, stays on the 'Create Plant' form.
     *          - Else, redirect to the plant's garden's profile page.
     */
    @PostMapping("/gardens/{gardenId}/plants/create")
    public String gardenCreatePlantPost(HttpServletRequest request,
                                        @ModelAttribute("createPlantForm") PlantForm createPlantForm,
                                        BindingResult bindingResult,
                                        @PathVariable("gardenId") Long gardenId) {
        logger.info("POST /gardens/" + gardenId + "/plants/create");

        User loggedInUser = userService.getLoggedInUser();
        Garden garden = gardenService.getGardenById(gardenId);

        if (!Objects.equals(garden.getOwner().getId(), loggedInUser.getId())) {
            // respond with 403 Forbidden
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You don't own this garden"
            );
        }
        PlantForm.validate(createPlantForm, bindingResult);
        if (bindingResult.hasErrors()) {
            return "pages/createPlantPage";
        }

        // this line is actually not strictly needed as spring MVC does this implicitly, but I have left it for the sake
        // of understanding how the plant 'knows' what garden it belongs to. It appears to be magic otherwise
        createPlantForm.setGardenId(gardenId);

        Plant plant = createPlantForm.getPlant();
        plantService.addPlant(plant);

        logger.info("Plant created: " + plant);
        return "redirect:/gardens/" + gardenId;
    }

    /**
     * Handles GET requests from the /gardens/{gardenId}/plants/{plantId}/edit endpoint.
     * Displays the 'Edit Plant' form.
     *
     * @param gardenId        id of garden that this plant belongs to
     * @param plantId         id of plant that is being edited
     * @param editPlantForm   the PlantForm object representing the plant's details,
     *                        useful for seeing erroneous inputs of a failed POST request
     * @return thymeleaf pages/editPlantPage
     */
    @GetMapping("/gardens/{gardenId}/plants/{plantId}/edit")
    public String gardenCreatePlantGet(@PathVariable("gardenId") Long gardenId,
                                       @PathVariable("gardenId") Long plantId,
                                       @ModelAttribute("editPlantForm") PlantForm editPlantForm) {
        logger.info("GET /gardens/" + gardenId + "/plants/" + plantId + "/edit");
        return "pages/editPlantPage";
    }
}