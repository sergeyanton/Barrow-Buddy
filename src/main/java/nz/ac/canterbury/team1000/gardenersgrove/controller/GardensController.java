package nz.ac.canterbury.team1000.gardenersgrove.controller;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import nz.ac.canterbury.team1000.gardenersgrove.service.ModerationService;
import nz.ac.canterbury.team1000.gardenersgrove.service.WeatherService;
import nz.ac.canterbury.team1000.gardenersgrove.entity.Weather;
import nz.ac.canterbury.team1000.gardenersgrove.form.GardenForm;
import nz.ac.canterbury.team1000.gardenersgrove.form.PictureForm;
import nz.ac.canterbury.team1000.gardenersgrove.form.PlantForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import org.springframework.web.bind.annotation.RequestParam;
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
	private final WeatherService weatherService;
	private final ModerationService moderationService;

	//TODO make a controller dedicated to uploading files.
	private final static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";


	public GardensController(GardenService gardenService, PlantService plantService,
		UserService userService, WeatherService weatherService,
		ModerationService moderationService) {
		this.gardenService = gardenService;
		this.plantService = plantService;
		this.userService = userService;
		this.weatherService = weatherService;
		this.moderationService = moderationService;
	}

	/**
	 * Gets the garden with the given id, if the garden can be accessed. Otherwise, throws an HTTP
	 * response exception like 403 or 404
	 *
	 * @param gardenId The id of the garden to access
	 * @return The garden object accessed
	 * @throws ResponseStatusException An exception that has occurred when trying to access the
	 *                                 garden
	 */
	private Garden tryToAccessGarden(Long gardenId) throws ResponseStatusException {
		Garden garden;
		// Make sure the garden exists
		try {
			garden = gardenService.getGardenById(gardenId);
		} catch (IllegalArgumentException exception) {
			throw new ResponseStatusException(
				HttpStatus.NOT_FOUND,
				"Garden not found"
			);
		}

        // Garden must be public for any user to view it
        // If private, user must own the garden to view it
        boolean gardenIsPrivate = !garden.getIsPublic();
        boolean userOwnsGarden = doesUserOwnGarden(garden);
        if (gardenIsPrivate && !userOwnsGarden) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "This garden is private. Only the owner can view it."
            );
        }

        return garden;
    }

    /**
     * Gets the garden with the given id, if the garden can be accessed and edited.
     * Otherwise, throws an HTTP response exception like 403 or 404
     * User must be own the garden to edit it.
     *
     * @param gardenId The id of the garden to be edited.
     * @return The garden object accessed.
     * @throws ResponseStatusException An exception that has occurred when trying to access the garden
     */
    private Garden tryToEditGarden(Long gardenId) throws ResponseStatusException {
        Garden garden;
        // Make sure the garden exists
        try {
            garden = gardenService.getGardenById(gardenId);
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Garden not found"
            );
        }

        // User must own the garden to edit it
        boolean userOwnsGarden = doesUserOwnGarden(garden);
        if (!userOwnsGarden) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "This garden does not belong to you. Only the owner can edit their garden."
            );
        }
        return garden;
    }

    /**
     * Gets the plant with the given id, if the plant can be accessed.
     * Otherwise, throws an HTTP response exception like 403 or 404
     * User must own the garden to edit any of its plants.
     *
     * @param plantId The id of the plant to access
     * @return The plant object accessed
     * @throws ResponseStatusException An exception that has occurred when trying to access the plant
     */
    private Plant tryToEditPlant(Long plantId, Long gardenId) throws ResponseStatusException {
        Plant plant;
        // Make sure the plant exists
        try {
            plant = plantService.getPlantById(plantId);
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Plant not found"
            );
        }
        // User must own the garden in order to edit the plant
        boolean userOwnsGarden = doesUserOwnGarden(gardenService.getGardenById(gardenId));
        if (!userOwnsGarden) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "This plant does not belong to you. Only the owner of the garden can edit the plants."
            );
        }
        return plant;
    }

    /**
     * Helper method to check if the logged-in user owns the given garden.
     *
     * @param garden Garden object that is trying to be accessed.
     * @return boolean if the user currently logged-in owns the garden or not.
     */
    public boolean doesUserOwnGarden(Garden garden) {
        return Objects.equals(garden.getOwner().getId(), userService.getLoggedInUser().getId());
    }

	/**
	 * Handles GET requests from the /gardens/create endpoint. Displays results of previous form
	 * when linked to from POST request
	 *
	 * @param createGardenForm the GardenForm object representing the new garden's details, useful
	 *                         for seeing erroneous inputs of a failed POST request
	 * @return the view to display, 'pages/createGardenPage', which contains a form
	 */
	@GetMapping("/gardens/create")
	public String gardenCreateGet(@ModelAttribute("createGardenForm") GardenForm createGardenForm) {
		logger.info("GET /gardens/create");
		return "pages/createGardenPage";
	}

	/**
	 * Handles POST requests from the /gardens/create endpoint. Handles creation of new gardens
	 *
	 * @param request          the HttpServletRequest object containing the request information
	 * @param createGardenForm the GardenForm object representing the new garden's details
	 * @param bindingResult    the BindingResult object for validation errors
	 * @return the view to display: - If there are validation errors, stays on the 'Create Garden'
	 * 			form. - Else, redirect to the newly created garden's profile page.
	 */
	@PostMapping("/gardens/create")
	public String gardenCreatePost(HttpServletRequest request,
		@ModelAttribute("createGardenForm") GardenForm createGardenForm,
		BindingResult bindingResult) {
		logger.info("POST /gardens/create");

		GardenForm.validate(createGardenForm, moderationService, bindingResult);
		if (bindingResult.hasErrors()) {
			return "pages/createGardenPage";
		}

		User loggedInUser = userService.getLoggedInUser();
		Garden newGarden = createGardenForm.getGarden(loggedInUser);
		logger.info(newGarden.getLatitude() + " " + newGarden.getLongitude());
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
        long loggedInUserId = userService.getLoggedInUser().getId();
        model.addAttribute("loggedInUserId", loggedInUserId);
        // This is when the weather info is actual retrieved
        // TODO: improve getWeatherByGardenId to make it actually search the location of the garden
        // TODO: also for the purpose of the spike, the parsing was somewhat rushed, i didn't actually parse the humidity
        // This function is for getting the current and future weather
        // Stephen has a plan for the previous day's weather so pls talk to him abt that if u are doing that task
        List<Weather> weather = weatherService.getCurrentWeatherByGardenId(gardenId);
		List<Weather> futureWeather = weatherService.getFutureWeatherByGardenId(gardenId);

		Weather currentWeather = weather.get(0);
		Weather nextWeather = futureWeather.get(0);
		LocalTime currentHour = LocalTime.now();

		String sunSetTime = nextWeather.sunSet.split("T")[1];
		String sunRiseTime = nextWeather.sunRise.split("T")[1];

		boolean isClearWeatherAtNight = currentWeather.getType().getText().equals("Clear")
			&& (currentHour.isAfter(LocalTime.parse(sunSetTime))
			|| currentHour.isBefore(LocalTime.parse(sunRiseTime)));

		String currentWeatherIconPath;
		if (isClearWeatherAtNight) {
			currentWeatherIconPath = "/images/weather/moon.png";
		} else {
			currentWeatherIconPath = currentWeather.getType().getPicturePath();
		}

		futureWeather.remove(0);

        model.addAttribute("weather", weather);
		model.addAttribute("futureWeather", futureWeather);
		model.addAttribute("currentWeatherIconPath", currentWeatherIconPath);
        model.addAttribute("garden", garden);
        model.addAttribute("plants", plantService.getPlantsByGardenId(garden.getId()));

        return "pages/gardenProfilePage";
    }

	/**
	 * Handles POST requests from the /gardens/{gardenId}/plants/{plantId} endpoint. Particularly it
	 * handles the uploading of images for a plant's picture.
	 *
	 * @param request          the HttpServletRequest object containing the request information
	 * @param gardenId         the id of the garden that is being viewed
	 * @param plantId          the id of the plant that is being edited
	 * @param plantPictureForm the PictureForm object representing a form with the uploaded image
	 *                         file
	 * @param bindingResult    the BindingResult object for validation errors
	 * @param model            (map-like) representation of results to be used by thymeleaf
	 * @return the view to display: - If there are validation errors with the image, stays on the
	 * form but render the plant's actual picture. - Else, redirect to the edited garden page with
	 * the new profile picture for the plant.
	 * @throws IOException IOException
	 */
	@PostMapping("/gardens/{gardenId}/plants/{plantId}")
	public String changePlantPictureFromGardenPage(HttpServletRequest request,
													@PathVariable("gardenId") Long gardenId,
													@PathVariable("plantId") Long plantId,
													@ModelAttribute("plantPictureForm") PictureForm plantPictureForm,
													BindingResult bindingResult, Model model) throws IOException {
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
	 * Handles GET requests from the /gardens/{gardenId}/edit endpoint. Displays the 'Edit Garden'
	 * form.
	 *
	 * @param gardenId       the id of the garden being got
	 * @param editGardenForm the GardenForm object representing the edited garden's details, useful
	 *                       for seeing erroneous inputs of a failed POST request
	 * @return thymeleaf pages/editGardenPage
	 */
	@GetMapping("/gardens/{gardenId}/edit")
	public String gardenEditGet(@PathVariable("gardenId") Long gardenId,
		@ModelAttribute("editGardenForm") GardenForm editGardenForm) {
		logger.info("GET /gardens/" + gardenId + "/edit");

        Garden garden = tryToEditGarden(gardenId);

		editGardenForm.setName(garden.getName());

		editGardenForm.setAddress(garden.getAddress());
		editGardenForm.setSuburb(garden.getSuburb());
		editGardenForm.setCity(garden.getCity());
		editGardenForm.setPostcode(garden.getPostcode());
		editGardenForm.setCountry(garden.getCountry());
		editGardenForm.setDescription(garden.getDescription());

		if (garden.getSize() != null) {
			editGardenForm.setSize(garden.getSize().toString());
		}

		return "pages/editGardenPage";
	}

	/**
	 * Handles POST requests from the /gardens/{gardenId}/edit endpoint. Handles editing of gardens
	 *
	 * @param request        the HttpServletRequest object containing the request information
	 * @param editGardenForm the GardenForm object representing the garden's new details
	 * @param bindingResult  the BindingResult object for validation errors
	 * @param gardenId       the id of the garden being edited
	 * @return the view to display: - If there are validation errors, stays on the 'Edit Garden'
	 * form. - Else, redirect to the edited garden's profile page.
	 */
	@PostMapping("/gardens/{gardenId}/edit")
	public String gardenEditPost(HttpServletRequest request,
		@ModelAttribute("editGardenForm") GardenForm editGardenForm,
		BindingResult bindingResult,
		@PathVariable("gardenId") Long gardenId) {
		logger.info("POST /gardens/" + gardenId + "/edit");

        Garden garden = tryToEditGarden(gardenId);
        GardenForm.validate(editGardenForm, moderationService, bindingResult);
        if (bindingResult.hasErrors()) {
            return "pages/editGardenPage";
        }

		User loggedInUser = userService.getLoggedInUser();
		Garden updatedGarden = editGardenForm.getGarden(loggedInUser);

        if (updatedGarden.getLocationString().equals(garden.getLocationString())) {
            updatedGarden.setLatitude(garden.getLatitude());
            updatedGarden.setLongitude(garden.getLongitude());
        }

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
		Garden existingGarden = tryToEditGarden(gardenId);
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
        								@ModelAttribute("createPlantForm") PlantForm createPlantForm, Model model,
        								BindingResult bindingResult, @PathVariable("gardenId") Long gardenId) throws IOException {
        logger.info("POST /gardens/" + gardenId + "/plants/create");

        Garden existingGarden = tryToEditGarden(gardenId);
        model.addAttribute("garden", existingGarden);

        PlantForm.validate(createPlantForm, bindingResult);

		if (!createPlantForm.getPictureFile().isEmpty() && !bindingResult.hasFieldErrors(
			"pictureFile")) {
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
			if (bindingResult.hasFieldErrors("pictureFile")) {
				createPlantForm.setPicturePath("/images/defaultPlantPic.png");
			}
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
     * @param editPlantForm the PlantForm object representing the plant's details, useful for
     *                      seeing erroneous inputs of a failed POST request
     * @param model the model to be used by thymeleaf
     * @return thymeleaf pages/editPlantPage
     */
    @GetMapping("/gardens/{gardenId}/plants/{plantId}/edit")
    public String gardenEditPlant(@PathVariable("gardenId") Long gardenId,
									@PathVariable("plantId") Long plantId,
									@ModelAttribute("editPlantForm") PlantForm editPlantForm,
									Model model){
        logger.info("GET /gardens/" + gardenId + "/plants/" + plantId + "/edit");
        Garden existingGarden = tryToEditGarden(gardenId);
        Plant existingPlant = tryToEditPlant(plantId, gardenId);
        String plantPicturePath = plantService.getPlantById(plantId).getPicturePath();
        editPlantForm.setPicturePath(plantPicturePath);

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
										BindingResult bindingResult, Model model) throws IOException{
		logger.info("POST /gardens/" + gardenId + "/plants/" + plantId + "/edit");

        Garden existingGarden = tryToEditGarden(gardenId);
        Plant existingPlant = tryToEditPlant(plantId, existingGarden.getId());
        model.addAttribute("garden", existingGarden);
        model.addAttribute("plant", existingPlant);
        PlantForm.validate(editPlantForm, bindingResult);

		if (editPlantForm.getPictureFile() != null && !editPlantForm.getPictureFile().isEmpty()
			&& !bindingResult.hasFieldErrors("pictureFile")) {
			Path uploadDirectoryPath = Paths.get(UPLOAD_DIRECTORY);
			if (!Files.exists(uploadDirectoryPath)) {
				try {
					Files.createDirectories(uploadDirectoryPath);
				} catch (IOException e) {
					throw new IOException("Failed to create upload directory", e);
				}
			}
			Path filePath = uploadDirectoryPath.resolve(editPlantForm.getPictureFile().getOriginalFilename());
			Files.write(filePath, editPlantForm.getPictureFile().getBytes());
			editPlantForm.setPicturePath("/uploads/" + editPlantForm.getPictureFile().getOriginalFilename());
		}

		if (bindingResult.hasErrors()) {
			if (bindingResult.hasFieldErrors("pictureFile")) {
				editPlantForm.setPicturePath("/images/defaultPlantPic.png");
			}
			return "pages/editPlantPage";
		}

        editPlantForm.updatePlant(existingPlant);
        plantService.updatePlant(existingPlant);
        logger.info("Plant updated: " + existingPlant);
        return "redirect:/gardens/" + gardenId;
    }

	/**
	 * Handles GET requests from the /updateGardenPublicity endpoint.
	 * This changes the publicity of the garden depending on the state of the checkbox
	 *
	 * @param gardenId The id of the garden
	 * @param isPublic The state of the checkbox - checked means garden is public, unchecked means
	 *                 garden is private
	 */
	@GetMapping("/updateGardenPublicity")
	public String updateGardenPublicity(@RequestParam(name = "gardenId") Long gardenId,
										@RequestParam(name = "gardenPublicity") boolean isPublic) {
		Garden garden = tryToAccessGarden(gardenId);
		garden.setIsPublic(isPublic);

		gardenService.updateGardenById(garden.getId(), garden);
		logger.info(
			"Garden " + gardenId + "'s publicity has been updated to " + (isPublic ? "Public"
				: "Private"));

        return "redirect:/gardens/" + gardenId;
    }

    /**
     * Handles GET requests for the /browseGardens endpoint.
     * Displays the 10 most recently made public gardens and a garden search bar.
     * If a query is present, searches and displays for gardens with matching string.
     *
     * @param model (map-like) representation of results to be used by thymeleaf
     * @param query the search query to search for gardens
     * @return thymeleaf pages/browseGardensPage
     */
    @GetMapping("/browseGardens")
    public String browseGardens(@RequestParam(name = "query", required = false, defaultValue = "") String query, @RequestParam(name = "page", defaultValue = "1") int page, Model model) {
        logger.info("GET /browseGardens");
        int pageSize = 10;
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("createdAt").descending());
        Page<Garden> gardens;
        // if query search results
        if (!query.isBlank()) {
            gardens = gardenService.searchGardens(query, pageable);
        } else {
            gardens = gardenService.getPublicGardens(pageable);
        }
        model.addAttribute("query", query);
        model.addAttribute("gardens", gardens);
        return "pages/browseGardensPage";
    }

}
