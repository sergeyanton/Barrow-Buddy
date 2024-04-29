package nz.ac.canterbury.team1000.gardenersgrove.api;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import nz.ac.canterbury.team1000.gardenersgrove.entity.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class LocationSearchService {
    private final String API_KEY = "pk.73082d5d87fb6091cebd2b86194b5b79"; // Right now this is using my personal account, create a new account for Team 1000
    private final String URL = "https://api.locationiq.com/v1/autocomplete";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public LocationSearchService() {}

    public List<Location> searchLocations(String query, String type) {
        // CONSTRUCT URL
        String url = URL + "?q=" + query + "&limit=5&key=" + API_KEY;

        // SEND GET REQUEST TO API ENDPOINT
        String jsonResponse = restTemplate.getForObject(url, String.class);

        List<Location> locationAddresses = new ArrayList<>();

        // PARSE THE JSON RESPONSE WITH A TRY-CATCH
        // RETURN LOCATION LIST IF THERE ARE RESULTS, RETURN EMPTY LIST OTHERWISE
        try {
            List<Map<String, Object>> locations = objectMapper.readValue(jsonResponse, List.class);

            for (Map<String, Object> location : locations) {
                Map<String, Object> address = (Map<String, Object>) location.get("address");
                Location newLocation = new Location();
                if (type.equals("city") && location.get("type").equals("city")) {
                    newLocation.setCity(address.get("name").toString());
                    newLocation.setCountry(address.get("country").toString());
                } else if (type.equals("suburb") && location.get("type").equals("suburb")) {
                    newLocation.setSuburb(address.get("name").toString());
                    newLocation.setCity(address.get("city").toString());
                    newLocation.setCountry(address.get("country").toString());
                } else if (type.equals("street address")) {
                    if (address.get("type").equals("road")) {
                        newLocation.setStreet(address.get("name").toString());
                        newLocation.setSuburb(address.get("suburb").toString());
                        newLocation.setCity(address.get("city").toString());
                        newLocation.setPostCode(address.get("postcode").toString());
                        newLocation.setCountry(address.get("country").toString());
                    } else {
                        newLocation.setHouseNumber(address.get("house_number").toString());
                        newLocation.setStreet(address.get("road").toString());
                        newLocation.setSuburb(address.get("suburb").toString());
                        newLocation.setCity(address.get("city").toString());
                        newLocation.setPostCode(address.get("postcode").toString());
                        newLocation.setCountry(address.get("country").toString());
                    }
                }
                if (newLocation.getCity() != null && newLocation.getCountry() != null) {
                    locationAddresses.add(newLocation);
                }
            }
            return locationAddresses;
        } catch (JsonProcessingException e) {
            return new ArrayList<>();
        }
    }

    public static void main(String[] args) {
        LocationSearchService locationSearchService = new LocationSearchService();

        List<Location> location = locationSearchService.searchLocations("Chr", "city");

        if (location.isEmpty()) {
            System.out.println("No matching location found");
        }

        for (Location loc : location) {
            System.out.println(loc);
        }
    }
}
