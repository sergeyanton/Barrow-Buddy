package nz.ac.canterbury.team1000.gardenersgrove.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import nz.ac.canterbury.team1000.gardenersgrove.entity.Garden;
import nz.ac.canterbury.team1000.gardenersgrove.entity.WeatherType;
import nz.ac.canterbury.team1000.gardenersgrove.entity.Weather;
import nz.ac.canterbury.team1000.gardenersgrove.repository.WeatherRepository;
import nz.ac.canterbury.team1000.gardenersgrove.api.LocationSearchService;
import nz.ac.canterbury.team1000.gardenersgrove.service.GardenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Service class for Weather entities, defined by the @link{Service} annotation. This clas links
 * automatically with @link{WeatherRepository}.
 * <p>
 * Also is responsible for calling the Open-Meteo API and parsing the response.
 */
@Service
public class WeatherService {

	final Logger logger = LoggerFactory.getLogger(WeatherService.class);
	private final WeatherRepository weatherRepository;
	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;
	private final GardenService gardenService;
	private final LocationSearchService locationSearchService;
	//TODO actually make this URL the correct URL, not just to prove the spike
	private final String URL = "https://api.open-meteo.com/v1/forecast?hourly=temperature_2m,relative_humidity_2m&daily=weather_code";

	@Autowired
	public WeatherService(WeatherRepository weatherRepository, RestTemplate restTemplate,
		ObjectMapper objectMapper, GardenService gardenService,
		LocationSearchService locationSearchService) {
		this.weatherRepository = weatherRepository;
		this.restTemplate = restTemplate;
		this.objectMapper = objectMapper;
		this.gardenService = gardenService;
		this.locationSearchService = locationSearchService;
	}

	/**
	 * Gets a list of Weather entities describing the type of weather, temperature, and humidity of
	 * the current day and the future 5 days for a garden.
	 * If there is no relevant and up-to-date Weather entities persisted in the database already,
	 * then weather data is fetched from Open-Meteo, and then persisted.
	 *
	 * @param gardenId ID of the garden that this weather information is relevant for
	 * @return A list Weather entities regarding the current day and future 5 days.
	 */
    public List<Weather> getWeatherByGardenId(long gardenId) {
        List<Weather> persistedWeatherList = weatherRepository.findByGardenId(gardenId);
        if (persistedWeatherList.isEmpty()) {
            logger.info("Weather for garden " + gardenId + " has not been persisted yet");
            return persistWeather(getWeather(gardenId));
        }
        logger.info("Weather for garden " + gardenId + " has been persisted before");
        if (persistedWeatherList.get(0).getExpiry().isBefore(LocalDateTime.now())) {
            logger.info("But it has since expired");
            List<Weather> newWeatherList = getWeather(gardenId);
            for (int i = 0; i < persistedWeatherList.size(); i++) {
                persistedWeatherList.get(i).setTo(newWeatherList.get(i));
            }
            return persistWeather(persistedWeatherList);
        }
		return persistedWeatherList;
    }


	/**
	 * Persists a list of Weather entities into the database
	 *
	 * @param weatherList The list of weather entities to persist
	 * @return The list of weather entities that are being persisted
	 */
	private List<Weather> persistWeather(List<Weather> weatherList) {
		logger.info("...Persisting...");

		for (Weather weather : weatherList) {
			weatherRepository.save(weather);
		}
		return weatherList;
	}

	/**
	 * Calls the API and parses the response into a list of Weather entities
	 *
	 * @param gardenId ID of the garden that this weather information is relevant for
	 * @return A list of Weather entities with data on the current and future weather.
	 */
	public List<Weather> getWeather(Long gardenId) {
		// TODO add humidity & temperature properly.
		// NOTE: temperature has daily values for the min and max temp, but not the average temperature.
		// temperature has hourly numerical temperature values that perhaps could be more usefully manipulated than the daily min and max
		// humidity has NO DAILY VALUE, must do something smart with the hourly variable. Perhaps just
		// average it but I mean, based on the AC, we have a lot of creative
		// freedom as to how the data is presented.

		Garden garden = gardenService.getGardenById(gardenId);

		try {
			String latitude = garden.getLatitude().toString();
			String longitude = garden.getLongitude().toString();

			String url = URL + "&latitude=" + latitude + "&longitude=" + longitude;
			String jsonResponse = restTemplate.getForObject(url, String.class);
			Map<String, Object> weather = objectMapper.readValue(jsonResponse, Map.class);
			List<Integer> weatherCodes = (ArrayList) ((Map<String, Object>) weather.get(
				"daily")).get("weather_code");
			List<Double> hourlyTemps = (ArrayList) ((Map<String, Object>) weather.get(
				"hourly")).get("temperature_2m");
			List<Double> dailyTemps = new ArrayList<>();
			for (int i = 0; i < hourlyTemps.size(); i += 24) {
				dailyTemps.add(hourlyTemps.get(i));
			}
			List<Weather> weatherList = new ArrayList<>();
			for (int i = 0; i < weatherCodes.size(); i++) {
				weatherList.add(new Weather(gardenId, LocalDate.now(),
					WeatherType.getByCode(weatherCodes.get(i)),
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
