package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * This is the controller for the home page.
 */
@Controller
public class GardensController {
    Logger logger = LoggerFactory.getLogger(GardensController.class);
    private final GardenService gardenService;
    @Autowired
    public GardensController(GardenService gardenService) {
        this.gardenService = gardenService;
    }

    /**
     * Renders the template.
     */
    @GetMapping("/gardens/create")
    public String gardenCreate() {
        return "createGarden";
    }

    @GetMapping("/gardens/gardenForm")
    public String form(@RequestParam(name="gardenName", required = false, defaultValue = "") String gardenName,
                       @RequestParam(name="gardenLocation", required = false, defaultValue = "") String gardenLocation,
                       @RequestParam(name="gardenSize", required = false, defaultValue = "") double gardenSize,
                       Model model) {
//        logger.info("GET /form");
        model.addAttribute("gardenName", gardenName);
        model.addAttribute("gardenLocation", gardenLocation);
        model.addAttribute("gardenSize", gardenSize);
        return "createGarden";
    }

    /**
     * Posts a form response with name
     * @param gardenName name of garden
     * @param gardenLocation location of garden
     * @param gardenSize size of garden
     * @param model (map-like) representation of name for use in thymeleaf,
     *              with values being set to relevant parameters provided
     * @return thymeleaf demoFormTemplate
     */
    @PostMapping("/gardens/gardenForm")
    public String submitForm( @RequestParam(name="gardenName") String gardenName,
                              @RequestParam(name="gardenLocation") String gardenLocation,
                              @RequestParam(name="gardenSize") double gardenSize,
                              Model model) {
//        logger.info("POST /form");
        gardenService.addFormResult(new Garden(gardenName, gardenLocation, gardenSize));
        model.addAttribute("gardenName", gardenName);
        model.addAttribute("gardenLocation", gardenLocation);
        model.addAttribute("gardenSize", gardenSize);
        return "createGarden";
    }

    /**
     * Gets all form responses
     * @param model (map-like) representation of results to be used by thymeleaf
     * @return thymeleaf demoResponseTemplate
     */
    @GetMapping("/gardens/createdGardens") //TODO improve pathnames
    public String responses(Model model) {
//        logger.info("GET /form/responses"); //TODO logging
        model.addAttribute("gardens", gardenService.getGardens());
        return "createdGardens";
    }
}
