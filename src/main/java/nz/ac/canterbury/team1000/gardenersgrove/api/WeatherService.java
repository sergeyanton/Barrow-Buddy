package nz.ac.canterbury.team1000.gardenersgrove.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import nz.ac.canterbury.team1000.gardenersgrove.entity.WeatherData;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {
  private final String URL = "https://api.open-meteo.com/v1/forecast?latitude=-43.5333&longitude=172.6333&hourly=weather_code";
  private RestTemplate restTemplate = new RestTemplate();
  private final ObjectMapper objectMapper = new ObjectMapper();

  public WeatherService() {}

  public WeatherService(RestTemplate restTemplate) {this.restTemplate = restTemplate;}

  public WeatherData getWeatherData() {
    String jsonResponse = restTemplate.getForObject(URL, String.class);
    WeatherData weatherData = new WeatherData(jsonResponse);
    return weatherData;
  }
}
