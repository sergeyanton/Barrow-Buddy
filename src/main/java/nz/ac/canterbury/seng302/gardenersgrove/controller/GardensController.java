package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.classes.ValidityCheck;
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

    /**
     * Renders the template.
     */
    @GetMapping("/gardens/create")
    public String gardenCreate() {
        return "createGarden";
    }

    /**
     * Posts a form response with name, location, and size of the garden
     * @param gardenName name of garden
     * @param gardenLocation location of garden
     * @param gardenSize size of garden
     * @param model (map-like) representation of name for use in thymeleaf,
     *              with values being set to relevant parameters provided
     * @return thymeleaf demoFormTemplate
     *
     */
    @PostMapping("/gardens/create")
    public String submitForm( @RequestParam(name="gardenName") String gardenName,
                              @RequestParam(name="gardenLocation") String gardenLocation,
                              @RequestParam(name="gardenSize") String gardenSize,
                              Model model) {
        logger.info("POST /gardens/create");
        if (!ValidityCheck.validGardenName(gardenName)) {
            logger.info("ERR: invalid name");
            return "createGarden";
        }
        if (!ValidityCheck.validGardenLocation(gardenLocation)) {
            logger.info("ERR: invalid location");
            return "createGarden";
        }
        if (!ValidityCheck.validGardenSize(gardenSize)) {
            logger.info("ERR: invalid size");
            return "createGarden";
        }
        double gardenSizeDouble = Double.parseDouble(gardenSize);
        gardenService.addFormResult(new Garden(gardenName, gardenLocation, gardenSizeDouble));
        model.addAttribute("gardenName", gardenName);
        model.addAttribute("gardenLocation", gardenLocation);
        model.addAttribute("gardenSize", gardenSizeDouble);
        return "createGarden";
    }

    /**
     * Gets all form responses (gardens)
     * @param model (map-like) representation of results to be used by thymeleaf
     * @return thymeleaf demoResponseTemplate
     */
    @GetMapping("/gardens")
    public String responses(Model model) {
        logger.info("GET /gardens/createdGardens");
        model.addAttribute("gardens", gardenService.getGardens());
        return "createdGardens";
    }
}
