package nz.ac.canterbury.team1000.gardenersgrove.controller;

import java.io.IOException;
import java.util.Objects;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import nz.ac.canterbury.team1000.gardenersgrove.form.GardenForm;
import nz.ac.canterbury.team1000.gardenersgrove.form.PictureForm;
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
import org.springframework.web.server.ResponseStatusException;

import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;

/**
 * This is the controller for the gardens page.
 */
@Controller
public class GardensController {
    final Logger logger = LoggerFactory.getLogger(GardensController.class);
    private final GardenService gardenService;
    private final PlantService plantService;
    private final UserService userService;


    //TODO make a controller dedicated to uploading files.
    private final static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";

    
    public GardensController(GardenService gardenService, PlantService plantService,
                                 UserService userService) {
        this.gardenService = gardenService;
        this.plantService = plantService;
        this.userService = userService;
    }

    /**
     * Gets the garden with the given id, if the garden can be accessed.
     * Otherwise, throws an HTTP response exception like 403 or 404
     * @param gardenId The id of the garden to access
     * @return The garden object accessed
     * @throws ResponseStatusException An exception that has occurred when trying to access the garden
     */
    private Garden tryToAccessGarden(Long gardenId) throws ResponseStatusException {
        Garden garden = null;
        // Make sure the garden exists
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

        return garden;
    }

    /**
     * Gets the plant with the given id, if the plant can be accessed.
     * Otherwise, throws an HTTP response exception like 403 or 404
     * @param plantId The id of the plant to access
     * @return The plant object accessed
     * @throws ResponseStatusException An exception that has occurred when trying to access the plant
     */
    private Plant tryToAccessPlant(Long plantId) throws ResponseStatusException {
        Plant plant = null;
        // Make sure the plant exists
        try {
            plant = plantService.getPlantById(plantId);
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Plant not found"
            );
        }

        return plant;
    }


    /**
     * Handles GET requests from the /gardens/create endpoint. Displays results of previous form
     * when linked to from POST request
     *
     * @param createGardenForm the GardenForm object representing the new garden's details, useful
     *        for seeing erroneous inputs of a failed POST request
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
     * @param request the HttpServletRequest object containing the request information
     * @param createGardenForm the GardenForm object representing the new garden's details
     * @param bindingResult the BindingResult object for validation errors
     * @return the view to display: - If there are validation errors, stays on the 'Create Garden'
     *         form. - Else, redirect to the newly created garden's profile page.
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
     * @param gardenId the id of the garden that is being viewed
     * @param plantPictureForm the PictureForm object representing a form with the uploaded image file
     * @param model (map-like) representation of results to be used by thymeleaf
     * @return thymeleaf pages/gardenProfilePage
     */
    @GetMapping("/gardens/{gardenId}")
    public String viewGarden(@PathVariable("gardenId") Long gardenId,
                             @ModelAttribute("plantPictureForm") PictureForm plantPictureForm,
                             Model model) {
        logger.info("GET /gardens/" + gardenId);
        Garden garden = tryToAccessGarden(gardenId);
        model.addAttribute("garden", garden);
        model.addAttribute("plants", plantService.getPlantsByGardenId(garden.getId()));
        return "pages/gardenProfilePage";
    }

    /**
     * Handles POST requests from the /gardens/{gardenId}/plants/{plantId} endpoint.
     * Particularly it handles the uploading of images for a plant's picture.
     *
     * @param request           the HttpServletRequest object containing the request information
     * @param gardenId          the id of the garden that is being viewed
     * @param plantId the id of the plant that is being edited
     * @param plantPictureForm  the PictureForm object representing a form with the uploaded image file
     * @param bindingResult     the BindingResult object for validation errors
     * @param model             (map-like) representation of results to be used by thymeleaf
     * @return the view to display:
     * - If there are validation errors with the image, stays on the form but render the plant's actual picture.
     * - Else, redirect to the edited garden page with the new profile picture for the plant.
     * @throws IOException IOException
     */
    @PostMapping("/gardens/{gardenId}/plants/{plantId}")
    public String changePlantPictureFromGardenPage(HttpServletRequest request,
                                                   @PathVariable("gardenId") Long gardenId,
                                                   @PathVariable("plantId") Long plantId,
                                                   @ModelAttribute("plantPictureForm") PictureForm plantPictureForm,
                                                   BindingResult bindingResult,
                                                   Model model) throws IOException {
        logger.info("POST /gardens/" + gardenId);
        Plant plant = plantService.getPlantById(plantId);

        PictureForm.validate(plantPictureForm, bindingResult, null); // TODO is there a reason that .validate has a User parameter?

        if (!plantPictureForm.getPictureFile().isEmpty() && !bindingResult.hasFieldErrors("pictureFile")) {
            Path uploadDirectoryPath = Paths.get(UPLOAD_DIRECTORY);

            if (!Files.exists(uploadDirectoryPath)) {
                try {
                    Files.createDirectories(uploadDirectoryPath);
                } catch (IOException e) {
                    throw new IOException("Failed to create upload directory", e);
                }
            }

            String filename = plantPictureForm.getPictureFile().getOriginalFilename();
            Path filePath = uploadDirectoryPath.resolve(filename);
            Files.write(filePath, plantPictureForm.getPictureFile().getBytes());
            plant.setPicturePath("/uploads/" + filename);
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("garden", gardenService.getGardenById(gardenId));
            model.addAttribute("plants", plantService.getPlantsByGardenId(gardenId));
            return "pages/gardenProfilePage";
        }

        plantService.updatePlant(plant);

        return "redirect:/gardens/" + gardenId;
    }

    /**
     * Handles GET requests from the /gardens/{gardenId}/edit endpoint.
     * Displays the 'Edit Garden' form.
     *
     * @param gardenId the id of the garden being got
     * @param editGardenForm the GardenForm object representing the edited garden's details, useful
     *        for seeing erroneous inputs of a failed POST request
     * @return thymeleaf pages/editGardenPage
     */
    @GetMapping("/gardens/{gardenId}/edit")
    public String gardenEditGet(@PathVariable("gardenId") Long gardenId,
            @ModelAttribute("editGardenForm") GardenForm editGardenForm) {
        logger.info("GET /gardens/" + gardenId + "/edit");

        Garden garden = tryToAccessGarden(gardenId);

        editGardenForm.setName(garden.getName());

        editGardenForm.setAddress(garden.getAddress());
        editGardenForm.setSuburb(garden.getSuburb());
        editGardenForm.setCity(garden.getCity());
        editGardenForm.setPostcode(garden.getPostcode());
        editGardenForm.setCountry(garden.getCountry());

        if (garden.getSize() != null) editGardenForm.setSize(garden.getSize().toString());

        return "pages/editGardenPage";
    }

    /**
     * Handles POST requests from the /gardens/{gardenId}/edit endpoint.
     * Handles editing of gardens
     *
     * @param request the HttpServletRequest object containing the request information
     * @param editGardenForm the GardenForm object representing the garden's new details
     * @param bindingResult the BindingResult object for validation errors
     * @param gardenId the id of the garden being edited
     * @return the view to display: - If there are validation errors, stays on the 'Edit Garden'
     *         form. - Else, redirect to the edited garden's profile page.
     */
    @PostMapping("/gardens/{gardenId}/edit")
    public String gardenEditPost(HttpServletRequest request,
            @ModelAttribute("editGardenForm") GardenForm editGardenForm,
            BindingResult bindingResult,
            @PathVariable("gardenId") Long gardenId) {
        logger.info("POST /gardens/" + gardenId + "/edit");

        Garden garden = tryToAccessGarden(gardenId);
        GardenForm.validate(editGardenForm, bindingResult);
        if (bindingResult.hasErrors()) {
            return "pages/editGardenPage";
        }

        User loggedInUser = userService.getLoggedInUser();
        Garden updatedGarden = editGardenForm.getGarden(loggedInUser);

        gardenService.updateGardenById(garden.getId(), updatedGarden);

        logger.info("Garden edited: " + garden);
        return "redirect:/gardens/" + garden.getId();
    }

    /**
     * Handles GET requests from the /gardens/{gardenId}/edit endpoint.
     * Displays the 'Edit Garden' form.
     *
     * @param gardenId id of garden that this plant belongs to
     * @param createPlantForm the PlantForm object representing the plant's details, useful for
     *        seeing erroneous inputs of a failed POST request
     * @return thymeleaf pages/createPlantPage
     */
    @GetMapping("/gardens/{gardenId}/plants/create")
    public String gardenCreatePlantGet(@PathVariable("gardenId") Long gardenId,
            @ModelAttribute("createPlantForm") PlantForm createPlantForm, Model model) {
        logger.info("GET /gardens/" + gardenId + "/plants/create");

        Garden existingGarden = tryToAccessGarden(gardenId);
        createPlantForm.setPicturePath("/images/defaultPlantPic.png");
        model.addAttribute("garden", existingGarden);
        return "pages/createPlantPage";
    }

    /**
     * Handles POST requests from the /gardens/{gardenId}/plants/create endpoint.
     * Handles creation of plants
     *
     * @param request the HttpServletRequest object containing the request information
     * @param createPlantForm the PlantForm object representing the garden's new details
     * @param bindingResult the BindingResult object for validation errors
     * @param gardenId the id of the garden that the plant is being added to
     * @return the view to display:
     *         - If there are validation errors, stays on the 'Create Plant' form.
     *         - Else, redirect to the plant's garden's profile page.
     */
    @PostMapping("/gardens/{gardenId}/plants/create")
    public String gardenCreatePlantPost(HttpServletRequest request,
            @ModelAttribute("createPlantForm") PlantForm createPlantForm,
            BindingResult bindingResult,
            @PathVariable("gardenId") Long gardenId, Model model) throws IOException{
        logger.info("POST /gardens/" + gardenId + "/plants/create");

        Garden existingGarden = tryToAccessGarden(gardenId);
        model.addAttribute("garden", existingGarden);

        PlantForm.validate(createPlantForm, bindingResult);

        if (!createPlantForm.getPictureFile().isEmpty() && !bindingResult.hasFieldErrors("pictureFile")) {
            Path uploadDirectoryPath = Paths.get(UPLOAD_DIRECTORY);
            if (!Files.exists(uploadDirectoryPath)) {
                try {
                    Files.createDirectories(uploadDirectoryPath);
                } catch (IOException e) {
                    throw new IOException("Failed to create upload directory", e);
                }
            }
            Path filePath = uploadDirectoryPath.resolve(createPlantForm.getPictureFile().getOriginalFilename());
            Files.write(filePath, createPlantForm.getPictureFile().getBytes());
            createPlantForm.setPicturePath("/uploads/" + createPlantForm.getPictureFile().getOriginalFilename());
        }


        if (bindingResult.hasErrors()) {
            if (bindingResult.hasFieldErrors("pictureFile")) createPlantForm.setPicturePath("/images/defaultPlantPic.png");
            return "pages/createPlantPage";
        }

        // this line is actually not strictly needed as spring MVC does this implicitly, but I have
        // left it for the sake
        // of understanding how the plant 'knows' what garden it belongs to. It appears to be magic
        // otherwise
        createPlantForm.setGardenId(gardenId);

        Plant plant = createPlantForm.getPlant();
        plantService.addPlant(plant);

        logger.info("Plant created: " + plant);
        return "redirect:/gardens/" + gardenId;
    }

    /**
     * Handles GET requests from the /gardens/{gardenId}/plants/{plantId}/edit endpoint.
     * It displays the 'Edit Plant' form.
     *
     * @param gardenId Id of the garden that this plant belongs to
     * @param plantId Id of the plant to edit
     * @param model the model to be used by thymeleaf
     * @return thymeleaf pages/editPlantPage
     */
    @GetMapping("/gardens/{gardenId}/plants/{plantId}/edit")
    public String gardenEditPlant(@PathVariable("gardenId") Long gardenId,
            @PathVariable("plantId") Long plantId,
            Model model){

        logger.info("GET /gardens/" + gardenId + "/plants/" + plantId + "/edit");

        Garden existingGarden = tryToAccessGarden(gardenId);
        Plant existingPlant = tryToAccessPlant(plantId);

        model.addAttribute("garden", existingGarden);
        model.addAttribute("plant", existingPlant);
        model.addAttribute("editPlantForm", PlantForm.fromPlant(existingPlant));

        return "pages/editPlantPage";
    }

    /**
     * Handles POST requests from the /gardens/{gardenId}/plants/{plantId}/edit endpoint.
     *
     * @param request the HttpServletRequest object containing the request information
     * @param gardenId id of the garden that this plant belongs to
     * @param plantId id of the plant to edit
     * @param editPlantForm the PlantForm object representing the plant's new details
     * @param bindingResult the BindingResult object for validation errors
     * @return the view to display:
     *         - If there are validation errors, stays on the 'Edit Plant'
     *         - Else, redirect to the plant's garden's profile page.
     */
    @PostMapping("/gardens/{gardenId}/plants/{plantId}/edit")
    public String gardenEditPlantPost(HttpServletRequest request,
            @PathVariable("gardenId") Long gardenId,
            @PathVariable("plantId") Long plantId,
            @ModelAttribute("editPlantForm") PlantForm editPlantForm,
            BindingResult bindingResult, Model model) {

        logger.info("POST /gardens/" + gardenId + "/plants/" + plantId + "/edit");

        Garden existingGarden = tryToAccessGarden(gardenId);
        Plant existingPlant = tryToAccessPlant(plantId);

        model.addAttribute("garden", existingGarden);
        model.addAttribute("plant", existingPlant);

        PlantForm.validate(editPlantForm, bindingResult);
        if (bindingResult.hasErrors()) {
            return "pages/editPlantPage";
        }

        editPlantForm.updatePlant(existingPlant);
        plantService.updatePlant(existingPlant);

        return "redirect:/gardens/" + gardenId;
    }
}
