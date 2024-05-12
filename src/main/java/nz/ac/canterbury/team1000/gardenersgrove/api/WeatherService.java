package nz.ac.canterbury.team1000.gardenersgrove.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import nz.ac.canterbury.team1000.gardenersgrove.entity.Plant;
import nz.ac.canterbury.team1000.gardenersgrove.entity.WeatherData;
import nz.ac.canterbury.team1000.gardenersgrove.repository.PlantRepository;
import nz.ac.canterbury.team1000.gardenersgrove.repository.WeatherRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {
    private final WeatherRepository weatherRepository;

    private final String URL = "https://api.open-meteo.com/v1/forecast?latitude=-43.5333&longitude=172.6333&daily=weather_code&forecast_days=5";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    public WeatherService(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    public WeatherData getWeatherById(long id) {
        return weatherRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid id: " + id));
    }

    public List<WeatherData> getWeatherByGardenId(long id) {
        return weatherRepository.findByGardenId(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid id: " + id));
    }

    public WeatherData addWeatherData(WeatherData weatherData) {
        return weatherRepository.save(weatherData);
    }


    public List<WeatherData> getWeatherData(Long gardenId) {
        String jsonResponse = restTemplate.getForObject(URL, String.class);
        try {
            Map<String, Object> weather = objectMapper.readValue(jsonResponse, Map.class);
            List<Integer> weatherCodes = (List) ((Map<String, Object>) weather.get("daily")).get("weather_code");
            System.out.println(weatherCodes);

            List<WeatherData> weatherDataList = new ArrayList<>();
            for (Integer code : weatherCodes) {
                weatherDataList.add(new WeatherData(gardenId, WeatherType.getByCode(code)));
            }

			return weatherDataList;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
