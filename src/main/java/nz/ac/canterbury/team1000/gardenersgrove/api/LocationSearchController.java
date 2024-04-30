package nz.ac.canterbury.team1000.gardenersgrove.api;

import nz.ac.canterbury.team1000.gardenersgrove.entity.Location;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LocationSearchController {
    private final LocationSearchService locationSearchService;

    public LocationSearchController(LocationSearchService locationSearchService) {
        this.locationSearchService = locationSearchService;
    }

    @GetMapping("/locationSearch")
    public List<Location> searchLocations(@RequestParam(name = "query") String query, @RequestParam(name = "addressField") String addressField) {
        return locationSearchService.searchLocations(query, addressField);
    }
}
