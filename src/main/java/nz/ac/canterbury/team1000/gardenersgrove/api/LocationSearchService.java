package nz.ac.canterbury.team1000.gardenersgrove.api;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import nz.ac.canterbury.team1000.gardenersgrove.service.TokenBucketService;
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
    private final TokenBucketService secondTokenBucket = new TokenBucketService(10, 2, 1000);

    private final TokenBucketService minuteTokenBucket = new TokenBucketService(120, 60, 60000);

    private final TokenBucketService dailyTokenBucket = new TokenBucketService(10000, 5000, 86400000);

    public LocationSearchService() {}

    public List<Location> searchLocations(String query, String addressField) {
        if (!secondTokenBucket.consumeToken() || !minuteTokenBucket.consumeToken() || !dailyTokenBucket.consumeToken()) {
            return new ArrayList<>();
        }

        // CONSTRUCT URL
        String encodedQuery = query.replaceAll(" ", "%");
        String url = URL + "?q=" + encodedQuery + "&limit=5&key=" + API_KEY;

        // SEND GET REQUEST TO API ENDPOINT
        String jsonResponse = restTemplate.getForObject(url, String.class);

        List<Location> locationAddresses = new ArrayList<>();

        // PARSE THE JSON RESPONSE WITH A TRY-CATCH
        // RETURN LOCATION LIST IF THERE ARE RESULTS, RETURN EMPTY LIST OTHERWISE
        try {
            List<Map<String, Object>> locations = objectMapper.readValue(jsonResponse, List.class);

            for (Map<String, Object> location : locations) {
                String locationType = (String) location.get("type");
                Map<String, Object> address = (Map<String, Object>) location.get("address");
                Location newLocation = new Location();
                if (addressField.equals("city") && locationType.equals("city")) {
                    newLocation.setCity(address.get("name").toString());
                    newLocation.setCountry(address.get("country").toString());
                } else if (addressField.equals("suburb") && locationType.equals("suburb")) {
                    newLocation.setSuburb(address.get("name").toString());
                    newLocation.setCity(address.get("city").toString());
                    newLocation.setCountry(address.get("country").toString());
                } else if (addressField.equals("street address")) {
                    if (locationType.equals("road") || locationType.equals("secondary")) {
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
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static void main(String[] args) {
        LocationSearchService locationSearchService = new LocationSearchService();
        String query = "Ilam Road";
        String addressField = "street address";

        List<Location> location = locationSearchService.searchLocations(query, addressField);

        for (Location loc : location) {
            switch (addressField) {
                case "street address":
                    if (loc.getHouseNumber() != null) {
                        System.out.println(loc.getHouseNumber() + " " + loc.getStreet() + ", " + loc.getSuburb() + ", " + loc.getCity() +
                                " " + loc.getPostcode() + ", " + loc.getCountry());
                    } else {
                        System.out.println(loc.getStreet() + ", " + loc.getSuburb() + ", " + loc.getCity() +
                                " " + loc.getPostcode() + ", " + loc.getCountry());
                    }
                    break;
                case "suburb":
                    System.out.println(loc.getSuburb() + ", " + loc.getCity() + ", " + loc.getCountry());
                    break;
                case "city":
                    System.out.println(loc.getCity() + ", " + loc.getCountry());
                    break;
            }
        }
    }
}