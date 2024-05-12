package nz.ac.canterbury.team1000.gardenersgrove.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import nz.ac.canterbury.team1000.gardenersgrove.entity.WeatherData;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {
    private final String URL = "https://api.open-meteo.com/v1/forecast?latitude=-43.5333&longitude=172.6333&daily=weather_code&forecast_days=5";
    private RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public WeatherService() {}

    public WeatherService(RestTemplate restTemplate) {this.restTemplate = restTemplate;}

    public List<WeatherData> getWeatherData() {
        String jsonResponse = restTemplate.getForObject(URL, String.class);
        try {
            Map<String, Object> weather = objectMapper.readValue(jsonResponse, Map.class);
            List<Integer> weatherCodes = (List) ((Map<String, Object>) weather.get("daily")).get("weather_code");
            System.out.println(weatherCodes);

            List<WeatherData> weatherDataList = new ArrayList<>();
            for (Integer code : weatherCodes) {
                weatherDataList.add(new WeatherData(WeatherType.getByCode(code)));
            }

			return weatherDataList;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
