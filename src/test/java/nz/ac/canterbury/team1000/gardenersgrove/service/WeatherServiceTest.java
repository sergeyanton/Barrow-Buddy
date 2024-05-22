package nz.ac.canterbury.team1000.gardenersgrove.service;

import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import nz.ac.canterbury.team1000.gardenersgrove.api.LocationSearchService;
import nz.ac.canterbury.team1000.gardenersgrove.entity.Garden;
import nz.ac.canterbury.team1000.gardenersgrove.entity.Weather;
import nz.ac.canterbury.team1000.gardenersgrove.entity.WeatherType;
import nz.ac.canterbury.team1000.gardenersgrove.repository.WeatherRepository;
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

	@BeforeEach
	void setUp() {
		weatherRepository = mock(WeatherRepository.class);
		restTemplate = mock(RestTemplate.class);
		gardenService = mock(GardenService.class);
		locationSearchService = mock(LocationSearchService.class);
		weatherService = new WeatherService(weatherRepository, restTemplate, new ObjectMapper(), gardenService, locationSearchService);
		gardenId = 1L;
		List<Weather> weatherList = new ArrayList<>();
		when(weatherRepository.findByGardenId(gardenId)).thenReturn(weatherList);
		when(weatherRepository.save(any())).thenReturn(null);
		when(gardenService.getGardenById(gardenId)).thenReturn(new Garden("Name", "Address", "Suburb", "City", "Postcode", "Country", 0.0, 0.0, 1.0, "", null, true));

	}

	/**
	 * Helper function to make testing smoother regarding mocking the API.
	 * @param codes int array of weather codes
	 * @param temps double array of hourly temperature codes
	 * @return JSON string with the given values, essentially mocking the Open-Meteo API call
	 */
	public String generateResponse(int[] codes, double[] temps, int[] humidity, String[] time) {
		// Stephen Hockey (sho151) ChatGPT attribution: the idea to use Arrays.toString
		StringBuilder sb = new StringBuilder();
		sb.append("{\"hourly\":{\"time\":[");
		for (int i = 0; i < time.length; i++) {
			sb.append("\"").append(time[i]).append("\"");
			if (i < time.length - 1) {
				sb.append(",");
			}
		}
		sb.append("],\"temperature_2m\":").append(Arrays.toString(temps));
		sb.append(",\"relative_humidity_2m\":").append(Arrays.toString(humidity));
		sb.append(",\"weather_code\":").append(Arrays.toString(codes)).append("}}");

		return sb.toString();
	}


	@Test
	public void GetWeatherForGarden_FirstGet_PersistsAndReturns() {
		int[] codes = new int[]{1, 1, 1};
		double[] temps = new double[]{20.0, 18.0, 22.5};
		int[] humidity = new int[]{90, 87, 88};
		String[] time = new String[]{"2020-03-28T00:00", "2020-03-28T01:00", "2020-03-28T02:00"};
		when(restTemplate.getForObject(anyString(), any())).thenReturn(generateResponse(codes, temps, humidity, time));

		List<Weather> result = weatherService.getWeatherByGardenId(gardenId);

		verify(weatherRepository, times(3)).save(any());

		for (int i = 0; i < result.size(); i++) {
			Weather weather = result.get(i);
			Assertions.assertEquals(WeatherType.getByCode(codes[i]), weather.getType());
			Assertions.assertEquals(temps[i], weather.getTemperature());
		}
	}

	@Test
	public void GetCurrentWeatherForGarden() {
		int[] codes = new int[]{1, 1, 1};
		double[] temps = new double[]{20.0, 18.0, 22.5};
		int[] humidity = new int[]{90, 87, 88};
		String[] times = new String[3];
		String date = LocalDate.now().toString();
		for (int i = 0; i < 3; i++) {
			if (i == 0) {
				String time = LocalTime.of(LocalTime.now().getHour(), 0).toString();
				times[i] = date + "T" + time;
			} else if (i == 1) {
				String time = LocalTime.of(LocalTime.now().plusHours(1).getHour(), 0).toString();
				times[i] = date + "T" + time;
			} else {
				String time = LocalTime.of(LocalTime.now().plusHours(2).getHour(), 0).toString();
				times[i] = date + "T" + time;
			}
		}

		when(restTemplate.getForObject(anyString(), any())).thenReturn(generateResponse(codes, temps, humidity, times));

		List<Weather> weatherList = weatherService.getCurrentWeatherByGardenId(gardenId);

		Assertions.assertEquals(2, weatherList.size());
	}
}
