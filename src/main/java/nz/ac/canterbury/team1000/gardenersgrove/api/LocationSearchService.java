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

        String tag = "";

        if (addressField.equals("city")) {
            tag = "&tag=place:city";
        } else if (addressField.equals("suburb")) {
            tag = "&tag=place:suburb";
        } else {
            // TODO: implement tag for address - don't know how to do as address can be any class...
        }

        // CONSTRUCT URL
        String url = URL + "?q=" + query + "&limit=5&key=" + API_KEY + tag;

        try {
            // SEND GET REQUEST TO API ENDPOINT
            String jsonResponse = restTemplate.getForObject(url, String.class);

            List<Location> locationAddresses = new ArrayList<>();

            List<Map<String, Object>> locations = objectMapper.readValue(jsonResponse, List.class);

            for (Map<String, Object> location : locations) {
                String locationType = (String) location.get("type");
                String displayPlace = (String) location.get("display_place");
                Map<String, Object> addressMap = (Map<String, Object>) location.get("address");

                String address = "";
                String suburb = "";
                String city = "";
                String postcode = "";
                String country = "";

                if (addressField.equals("city") && locationType.equals("city") && addressMap.get("name").toString().contains(query)) {
                    city = addressMap.get("name").toString();
                    if (addressMap.containsKey("country")) country = addressMap.get("country").toString();

                    if (!city.isEmpty() && !country.isEmpty()) {
                        locationAddresses.add(new Location(address, suburb, city, postcode, country, displayPlace));
                    }
                } else if (addressField.equals("suburb") && locationType.equals("suburb")  && addressMap.get("name").toString().contains(query)) {
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
                    } else if (addressMap.get("name").toString().contains(query)) {
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void main(String[] args) {
        LocationSearchService locationSearchService = new LocationSearchService();
        String query = "Aida";
        String addressField = "suburb";

        List<Location> location = locationSearchService.searchLocations(query, addressField);

        if (location.isEmpty()) {
            System.out.println("No matching location found");
        }

        for (Location loc : location) {
            System.out.println(loc.displayAddress);
        }
    }
}