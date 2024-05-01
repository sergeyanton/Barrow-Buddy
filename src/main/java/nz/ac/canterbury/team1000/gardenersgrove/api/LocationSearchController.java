package nz.ac.canterbury.team1000.gardenersgrove.api;

import nz.ac.canterbury.team1000.gardenersgrove.entity.Location;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for handling LocationSearchService method calls between JavaScript and Java
 */
@RestController
public class LocationSearchController {
    private final LocationSearchService locationSearchService;

    public LocationSearchController(LocationSearchService locationSearchService) {
        this.locationSearchService = locationSearchService;
    }

    /**
     * Handles GET requests from the /locationSearch endpoint.
     *
     * Calls the searchLocations() method in LocationSearchService using the requested parameters:
     * @param query input that user has typed in the address field
     * @param addressField the input field in the address part of the garden forms where user input occurred
     * @return a list containing Location entities that match the query and type of address - empty list if no matching
     * results found
     */
    @GetMapping("/locationSearch")
    public List<Location> searchLocations(@RequestParam(name = "query") String query, @RequestParam(name = "addressField") String addressField) {
        return locationSearchService.searchLocations(query, addressField);
    }
}
