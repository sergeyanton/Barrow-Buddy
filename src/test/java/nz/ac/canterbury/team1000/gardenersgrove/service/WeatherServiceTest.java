package nz.ac.canterbury.team1000.gardenersgrove.service;

import static org.mockito.ArgumentMatchers.any;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import nz.ac.canterbury.team1000.gardenersgrove.api.LocationSearchService;
import nz.ac.canterbury.team1000.gardenersgrove.entity.Weather;
import nz.ac.canterbury.team1000.gardenersgrove.repository.GardenRepository;
import nz.ac.canterbury.team1000.gardenersgrove.repository.WeatherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

@DataJpaTest
public class WeatherServiceTest {
	private WeatherService weatherService;
	@Mock
	private WeatherRepository weatherRepository;
	@Mock
	private RestTemplate restTemplate;
	@Mock
	private ObjectMapper objectMapper;

	private Long gardenId;
	private List<Weather> weatherList;

	@BeforeEach
	void setUp() {
		weatherRepository = Mockito.mock(WeatherRepository.class);
		restTemplate = Mockito.mock(RestTemplate.class);
		weatherService = new WeatherService(weatherRepository, restTemplate, objectMapper);

		gardenId = 1L;
		weatherList = new ArrayList<>();
		Mockito.when(weatherRepository.findByGardenId(gardenId)).thenReturn(Optional.of(weatherList));
		Mockito.when(weatherRepository.save(Mockito.any())).thenReturn(null);
	}

	@Test
	public void testGetWeatherByGardenId() {
		// Mock the response from the API
		String mockJsonResponse = "{\"daily\": {\"weather_code\": [1, 1, 1]}, \"hourly\": "
			+ "{\"temperature_2m\": [20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, "
			+ "20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0]}}";
		Mockito.when(restTemplate.getForObject(Mockito.any(String.class), Mockito.any(Class.class))).thenReturn(mockJsonResponse);
		System.out.println(restTemplate.getForObject("",String.class));
		List<Weather> result = weatherService.getWeatherByGardenId(gardenId);
		System.out.println(result);
	}
}
