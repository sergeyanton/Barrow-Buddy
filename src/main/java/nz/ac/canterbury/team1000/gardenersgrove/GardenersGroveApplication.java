package nz.ac.canterbury.team1000.gardenersgrove;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * Gardener's Grove entry-point
 */
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class GardenersGroveApplication {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	/**
	 * Main entry point, runs the Spring application
	 * 
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(GardenersGroveApplication.class, args);
	}
}
