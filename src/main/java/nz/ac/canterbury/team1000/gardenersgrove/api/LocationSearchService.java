package nz.ac.canterbury.team1000.gardenersgrove.api;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nz.ac.canterbury.team1000.gardenersgrove.service.TokenBucketService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import nz.ac.canterbury.team1000.gardenersgrove.entity.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class LocationSearchService {
    private final String API_KEY = "pk.dc3a28922b1f87600c4896d50b3aac8c"; // Store here for now. TODO: environment variable
    private final String URL = "https://api.locationiq.com/v1/autocomplete";

    private RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final TokenBucketService secondTokenBucket = new TokenBucketService(10, 2, 1000);

    private final TokenBucketService minuteTokenBucket = new TokenBucketService(120, 60, 60000);

    private final TokenBucketService dailyTokenBucket = new TokenBucketService(10000, 5000, 86400000);

    public LocationSearchService() {}

    public LocationSearchService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Using user input and specifying the address field, a request is sent to the LocationIQ API to find a location
     * that matches or contains query with a given tag that corresponds to the address field where user input occurred
     *
     * @param query user input in an address field
     * @param addressField the field where user input occurred
     * @return List containing Location entities that match the query and address field
     */
    public List<Location> searchLocations(String query, String addressField) {
        // If the number of requests exceed the rate limit (2 requests per second, 60 requests per minute, or 5000 requests per day),
        // then it will not continue with sending the request and instead return an empty array list.
        if (!secondTokenBucket.consumeToken() || !minuteTokenBucket.consumeToken() || !dailyTokenBucket.consumeToken()) {
            return new ArrayList<>();
        }

        try {
            // Construct URL for sending requests to the LocationIQ API
            String url;
            if (addressField.equals("country")) {
                url = URL + "?q=" + query + "&limit=5&key=" + API_KEY + "&tag=place:country";
            } else if (addressField.equals("city")) {
                url = URL + "?q=" + query + "&limit=5&key=" + API_KEY + "&tag=place:city";
            } else if (addressField.equals("postcode")) {
                url = URL + "?q=" + query + "&limit=5&key=" + API_KEY + "&tag=place:postcode";
            } else if (addressField.equals("suburb")) {
                url = URL + "?q=" + query + "&limit=5&key=" + API_KEY + "&tag=place:suburb";
            } else {
                url = URL + "?q=" + query + "&limit=5&key=" + API_KEY;
            }

            // Sending a request to the LocationIQ API endpoint and returns a JSON response in string form
            String jsonResponse = restTemplate.getForObject(url, String.class);

            // Calls locationsIntoList() method given the JSON response in String format, the query and the address field
            return locationsIntoList(jsonResponse, query, addressField);
        } catch (Exception e) {
            // Return an array list if any exception has occurred
            return new ArrayList<>();
        }
    }

    /**
     * Using the JSON response, query and address field, it maps the String formatted JSON response into a Map format then
     * checks each location found by LocationIQ API and if the type of location returned matches the address field
     * type, then add the location into a list that gets returned
     *
     * @param jsonResponse the received JSON Response from the LocationIQ API endpoint
     * @param query user input in an address field
     * @param addressField the field where user input occurred
     * @return
     * @throws JsonProcessingException if a JSON process exception has occurred, it makes sure to throw an JsonProcessingException
     */
    public List<Location> locationsIntoList(String jsonResponse, String query, String addressField) throws JsonProcessingException {
        List<Location> locationAddresses = new ArrayList<>();

        List<Map<String, Object>> locations = objectMapper.readValue(jsonResponse, List.class);

        // Obtain the type of the location, the display format of the location, and the address map of the location
        for (Map<String, Object> location : locations) {
            String locationType = (String) location.get("type");
            String displayPlace = (String) location.get("display_place");
            Map<String, Object> addressMap = (Map<String, Object>) location.get("address");

            String address = "";
            String suburb = "";
            String city = "";
            String postcode = "";
            String country = "";

            // Checks that the location matches the address field then creates a Location entity which then gets appended
            // to the location list that gets returned at the end of the method
            // Besides the country address field, it makes sure that the location must have a city and country in its
            // address map to deal with the requirement of the form submission containing a non-empty city and country
            if (addressField.equals("country") && locationType.equals("country")) {
                country = addressMap.get("name").toString();

                locationAddresses.add(new Location(address, suburb, city, postcode, country, displayPlace));
            } else if (addressField.equals("postcode") && locationType.equals("postcode")) {
                postcode = addressMap.get("name").toString();
                if (addressMap.containsKey("city")) city = addressMap.get("city").toString();
                if (addressMap.containsKey("country")) country = addressMap.get("country").toString();

                if (!city.isEmpty() && !country.isEmpty()) {
                    locationAddresses.add(new Location(address, suburb, city, postcode, country, displayPlace));
                }
            }
            if (addressField.equals("city") && locationType.equals("city")) {
                city = addressMap.get("name").toString();
                if (addressMap.containsKey("country")) country = addressMap.get("country").toString();

                if (!city.isEmpty() && !country.isEmpty()) {
                    locationAddresses.add(new Location(address, suburb, city, postcode, country, displayPlace));
                }
            } else if (addressField.equals("suburb") && locationType.equals("suburb")) {
                suburb = addressMap.get("name").toString();
                if (addressMap.containsKey("city")) city = addressMap.get("city").toString();
                if (addressMap.containsKey("country")) country = addressMap.get("country").toString();

                if (!city.isEmpty() && !country.isEmpty()) {
                    locationAddresses.add(new Location(address, suburb, city, postcode, country, displayPlace));
                }
            } else if (addressField.equals("address")) {
                if (addressMap.containsKey("house_number")) {
                    String addressCombined = addressMap.get("house_number").toString() + " " + addressMap.get("road").toString();
                    if (addressCombined.startsWith(query)) {
                        address = addressMap.get("house_number").toString() + " " + addressMap.get("road").toString();
                    }
                } else {
                    address = addressMap.get("name").toString();
                }
                if (addressMap.containsKey("suburb")) suburb = addressMap.get("suburb").toString();
                if (addressMap.containsKey("city")) city = addressMap.get("city").toString();
                if (addressMap.containsKey("postcode")) postcode = addressMap.get("postcode").toString();
                if (addressMap.containsKey("country")) country = addressMap.get("country").toString();

                if (!address.isEmpty() && !city.isEmpty() && !country.isEmpty()) {
                    locationAddresses.add(new Location(address, suburb, city, postcode, country, displayPlace));
                }
            }
        }
        return locationAddresses;
    }
}