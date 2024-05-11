package nz.ac.canterbury.team1000.gardenersgrove.api;

import java.util.List;
import nz.ac.canterbury.team1000.gardenersgrove.entity.Location;
import nz.ac.canterbury.team1000.gardenersgrove.entity.WeatherData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class WeatherController {
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weatherSearch")
    public WeatherData searchLocations() {
        return weatherService.getWeatherData();
    }
}
