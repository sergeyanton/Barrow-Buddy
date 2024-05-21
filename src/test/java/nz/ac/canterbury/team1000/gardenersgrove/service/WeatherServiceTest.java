package nz.ac.canterbury.team1000.gardenersgrove.service;

import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import nz.ac.canterbury.team1000.gardenersgrove.api.LocationSearchService;
import nz.ac.canterbury.team1000.gardenersgrove.entity.Garden;
import nz.ac.canterbury.team1000.gardenersgrove.entity.Weather;
import nz.ac.canterbury.team1000.gardenersgrove.entity.WeatherType;
import nz.ac.canterbury.team1000.gardenersgrove.repository.WeatherRepository;
import nz.ac.canterbury.team1000.gardenersgrove.service.GardenService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.web.client.RestTemplate;

public class WeatherServiceTest {
	private WeatherService weatherService;
	@Mock
	private WeatherRepository weatherRepository;
	@Mock
	private RestTemplate restTemplate;
	@Mock
	private GardenService gardenService;
	@Mock
	private LocationSearchService locationSearchService;
	private Long gardenId;
	private List<Weather> weatherList;

	@BeforeEach
	void setUp() {
		weatherRepository = mock(WeatherRepository.class);
		restTemplate = mock(RestTemplate.class);
		gardenService = mock(GardenService.class);
		locationSearchService = mock(LocationSearchService.class);
		weatherService = new WeatherService(weatherRepository, restTemplate, new ObjectMapper(), gardenService, locationSearchService);
		gardenId = 1L;
		weatherList = new ArrayList<>();
		when(weatherRepository.findByGardenId(gardenId)).thenReturn(weatherList);
		when(weatherRepository.save(any())).thenReturn(null);
		when(gardenService.getGardenById(gardenId)).thenReturn(new Garden("Name", "Address",
<<<<<<< HEAD
			"Suburb", "City", "Postcode", "Country", false, 1.0, "", null, true));
		when(locationSearchService.getCoordinates(any())).thenReturn(Arrays.asList(-1.0, -1.0));
=======
			"Suburb", "City", "Postcode", "Country", 0.0, 0.0, 1.0, null, true));
>>>>>>> 7fc08cc3510fed12b8d1fa09d120a1a65cb77c52
	}

	/**
	 * Helper function to make testing smoother regarding mocking the API.
	 * @param codes int array of weather codes
	 * @param temps double array of hourly temperature codes
	 * @return JSON string with the given values, essentially mocking the Open-Meteo API call
	 */
	public String generateResponse(int[] codes, double[] temps) {
		double[] fullTemps = new double[temps.length * 24];
		for (int i = 0; i < temps.length; i++) {
			for (int j = 0; j < 24; j++) {
				fullTemps[i * 24 + j] = temps[i];
			}
		}

		// Stephen Hockey (sho151) ChatGPT attribution: the idea to use Arrays.toString
		return "{\"daily\": {\"weather_code\": " + Arrays.toString(codes) +
			"}, \"hourly\": {\"temperature_2m\": " + Arrays.toString(fullTemps) + "}}";
	}


	@Test
	public void GetWeatherForGarden_FirstGet_PersistsAndReturns() {
		int[] codes = new int[]{1, 1, 1};
		double[] temps = new double[]{20.0, 18.0, 22.5};
		when(restTemplate.getForObject(anyString(), any())).thenReturn(generateResponse(codes, temps));

		List<Weather> result = weatherService.getWeatherByGardenId(gardenId);

		verify(weatherRepository, times(3)).save(any());

		for (int i = 0; i < result.size(); i++) {
			Weather weather = result.get(i);
			Assertions.assertEquals(WeatherType.getByCode(codes[i]), weather.getType());
			Assertions.assertEquals(temps[i], weather.getTemperature());
		}
	}
}
