package nz.ac.canterbury.team1000.gardenersgrove.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import nz.ac.canterbury.team1000.gardenersgrove.entity.Garden;
import nz.ac.canterbury.team1000.gardenersgrove.entity.WeatherType;
import nz.ac.canterbury.team1000.gardenersgrove.entity.Weather;
import nz.ac.canterbury.team1000.gardenersgrove.repository.WeatherRepository;
import nz.ac.canterbury.team1000.gardenersgrove.service.GardenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Service class for Weather entities, defined by the @link{Service} annotation. This clas links
 * automatically with @link{WeatherRepository}.
 * Also is responsible for calling the Open-Meteo API and parsing the response.
 */
@Service
public class WeatherService {
    final Logger logger = LoggerFactory.getLogger(WeatherService.class);
    private final WeatherRepository weatherRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final GardenService gardenService;
    LocalDate dateTwoDaysAgo = LocalDate.now().minusDays(2);
    LocalDate dateFiveDaysLater = LocalDate.now().plusDays(5);
    private final String URL =
        "https://api.open-meteo.com/v1/forecast?hourly=temperature_2m,relative_humidity_2m,weather_code&start_date="
            + dateTwoDaysAgo + "&end_date=" + dateFiveDaysLater;

    @Autowired
    public WeatherService(WeatherRepository weatherRepository, RestTemplate restTemplate,
        ObjectMapper objectMapper, GardenService gardenService) {
        this.weatherRepository = weatherRepository;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.gardenService = gardenService;
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

    /**
     * Retrieves the current weather and the weather immediately before or after the current time for a given garden.
     * This method checks the list of weather data for a garden and identifies the weather condition that is
     * currently in effect.
     *
     * @param gardenId the ID of the garden for which to retrieve the weather information
     * @return a list of Weather objects containing the current weather - the weather for previous hour and the next hour
     */
    public List<Weather> getCurrentWeatherByGardenId(Long gardenId) {
        List<Weather> beforeAndAfterWeather = new ArrayList<>();
        List<Weather> weathers = getWeather(gardenId);
        for (int i = 0; i < weathers.size(); i++) {
            LocalDateTime weatherDateTime = weathers.get(i).getDateTime();
            LocalDateTime nextWeatherHour = weatherDateTime.plusHours(1);

            LocalDateTime timeRightNow = LocalDateTime.now();

            if (weatherDateTime.isBefore(timeRightNow.plusMinutes(1)) && nextWeatherHour.isAfter(timeRightNow)) {
                if (i == 0) {
                    beforeAndAfterWeather.add(weathers.get(i));
                } else {
                    int prev = i - 1;
                    while (weathers.get(i-1).getType() == WeatherType.SAME) {
                        weathers.get(i-1).setType(weathers.get(prev-1).getType());
                        prev--;
                    }
                    if (weathers.get(i-1).getType() != WeatherType.SAME) {
                        beforeAndAfterWeather.add(weathers.get(i-1));
                    }
                    if (weathers.get(i).getType() == WeatherType.SAME) {
                        weathers.get(i).setType(weathers.get(i-1).getType());
                    }
                    if (weathers.get(i+1).getType() == WeatherType.SAME) {
                        weathers.get(i+1).setType(weathers.get(i).getType());
                    }
                }
                beforeAndAfterWeather.add(weathers.get(i));
                beforeAndAfterWeather.add(weathers.get(i+1));

                break;
            }

        }
        return beforeAndAfterWeather;
    }

    /**
     * Persists a list of Weather entities into the database
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
     * Retrieves the weather data for a specified garden by its ID, including weather codes, temperatures,
     * humidity, and timestamps. The weather data is fetched from an API and parsed into a list of
     * Weather objects.
     *
     * @param gardenId the ID of the garden for which to retrieve the weather data
     * @return a list of Weather objects containing the weather data for the garden
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

            String url = URL + "&latitude=" + latitude + "&longitude=" + longitude + "&timezone=Pacific/Auckland";
            String jsonResponse = restTemplate.getForObject(url, String.class);
            Map<String, Object> weather = objectMapper.readValue(jsonResponse, Map.class);
            List<Integer> weatherCodes = (ArrayList) ((Map<String, Object>) weather.get("hourly")).get("weather_code");
            List<Double> hourlyTemps = (ArrayList) ((Map<String, Object>) weather.get("hourly")).get("temperature_2m");
            List<Integer> hourlyHumidity = (ArrayList) ((Map<String, Object>) weather.get("hourly")).get("relative_humidity_2m");
            List<String> hourlyTime = (ArrayList) ((Map<String, Object>) weather.get("hourly")).get("time");
            List<LocalDateTime> hourlyTimeParsed = new ArrayList<>();

			for (String s : hourlyTime) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
				LocalDateTime localDateTime = LocalDateTime.parse(s, formatter);
				hourlyTimeParsed.add(localDateTime);
			}

			List<Double> dailyTemps = new ArrayList<>(hourlyTemps);
            List<Integer> dailyHumidity = new ArrayList<>(hourlyHumidity);

            List<Weather> weatherList = new ArrayList<>();
            for (int i = 0; i < weatherCodes.size(); i++) {
                weatherList.add(new Weather(gardenId, hourlyTimeParsed.get(i), WeatherType.getByCode(weatherCodes.get(i)),
                    dailyTemps.get(i), dailyHumidity.get(i), hourlyTimeParsed.get(i).getDayOfWeek().toString()));
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
