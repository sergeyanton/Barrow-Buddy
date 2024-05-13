package nz.ac.canterbury.team1000.gardenersgrove.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import nz.ac.canterbury.team1000.gardenersgrove.entity.WeatherType;
import nz.ac.canterbury.team1000.gardenersgrove.entity.Weather;
import nz.ac.canterbury.team1000.gardenersgrove.repository.WeatherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {
    final Logger logger = LoggerFactory.getLogger(WeatherService.class);
    private final WeatherRepository weatherRepository;
    private final String URL = "https://api.open-meteo.com/v1/forecast?latitude=-43.5333&longitude=172.6333&hourly=temperature_2m,relative_humidity_2m&daily=weather_code";
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    public WeatherService(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    public List<Weather> getWeatherByGardenId(long gardenId) {
        List<Weather> persistedWeatherList = weatherRepository.findByGardenId(gardenId).get();
        if (persistedWeatherList.isEmpty()) {
            logger.info("Weather for garden " + gardenId + " has not been persisted yet");
            return persistWeather(getWeather(gardenId));
        }
        logger.info("Weather for garden " + gardenId + " has been persisted before");
        if (persistedWeatherList.getFirst().getExpiry().isBefore(LocalDateTime.now())) {
            logger.info("But it has since expired");
            List<Weather> newWeatherList = getWeather(gardenId);
            for (int i = 0; i < persistedWeatherList.size(); i++) {
                persistedWeatherList.get(i).setTo(newWeatherList.get(i));
            }
            return persistWeather(persistedWeatherList);
        }
        return persistedWeatherList;
    }

    private List<Weather> persistWeather(List<Weather> weatherList) {
        logger.info("...Persisting...");

        for (Weather weather : weatherList) {
            weatherRepository.save(weather);
        }
        return weatherList;
    }

    public List<Weather> getWeather(Long gardenId) {
        String jsonResponse = restTemplate.getForObject(URL, String.class);
        try {
            Map<String, Object> weather = objectMapper.readValue(jsonResponse, Map.class);
            List<Integer> weatherCodes = (ArrayList) ((Map<String, Object>) weather.get("daily")).get("weather_code");
            List<Double> hourlyTemps = (ArrayList) ((Map<String, Object>) weather.get("hourly")).get("temperature_2m");
            List<Double> dailyTemps = new ArrayList<>();
            for (int i = 0; i < hourlyTemps.size(); i += 24) {
                dailyTemps.add(hourlyTemps.get(i));
            }

            List<Weather> weatherList = new ArrayList<>();
            for (int i = 0; i < weatherCodes.size(); i++) {
                weatherList.add(new Weather(gardenId, LocalDate.now(), WeatherType.getByCode(weatherCodes.get(i)),
                    dailyTemps.get(i), 0.0));
            }

            for (Weather w : weatherList) {
                logger.info(w.toString());
            }

			return weatherList;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ArrayList<>();
        }
    }
}
