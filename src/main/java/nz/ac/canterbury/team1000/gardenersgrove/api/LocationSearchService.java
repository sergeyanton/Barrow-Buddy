package nz.ac.canterbury.team1000.gardenersgrove.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nz.ac.canterbury.team1000.gardenersgrove.service.TokenBucketService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import nz.ac.canterbury.team1000.gardenersgrove.entity.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This service contains the handling of getting matching location results entered in address fields in both create and edit
 * garden form. Contains a class searchLocation that searches for matching locations and locationsIntoList that parses a JSON response
 * in String format into a list containing Location entities.
 */
@Service
public class LocationSearchService {
    private final String API_KEY = System.getenv("API_KEY");
    private final String URL = "https://api.locationiq.com/v1/";

    private RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * This is a rate limiter that uses the TokenBucket class. LocationIQ has a request limit rate of 2 requests
     * per second so if more than 2 requests are made in a second, then calling consumeToken() would return false,
     * meaning the rate limit has been exceeded.
     *
     * Rate Limit Duration of 1000 is how many milliseconds there are in a second.
     */
    private final TokenBucketService TWO_REQUESTS_PER_SECOND_RATE_LIMITER = new TokenBucketService(10, 2, 1000);

    /**
     * This is a rate limiter that uses the TokenBucket class. LocationIQ has a request limit rate of 60 requests
     * per minute so if more than 60 requests are made in a minute, then calling consumeToken() would return false,
     * meaning the rate limit has been exceeded.
     *
     * Rate Limit Duration of 60000 is how many milliseconds there are in a minute.
     */
    private final TokenBucketService SIXTY_REQUESTS_PER_MINUTE_RATE_LIMITER = new TokenBucketService(120, 60, 60000);

    /**
     * This is a rate limiter that uses the TokenBucket class. LocationIQ has a request limit rate of 5000 requests
     * per day so if more than 5000 requests are made in a day, then calling consumeToken() would return false,
     * meaning the rate limit has been exceeded.
     *
     * Rate Limit Duration of 86400000 is how many milliseconds there are in a day.
     */
    private final TokenBucketService FIVE_THOUSAND_REQUESTS_PER_DAY_RATE_LIMITER = new TokenBucketService(10000, 5000, 86400000);

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
    public List<Location> searchLocations(String query, String[] fullAddress, String addressField) {
        // If the number of requests exceed the rate limit (2 requests per second, 60 requests per minute, or 5000 requests per day),
        // then it will not continue with sending the request and instead return an empty array list.
        if (!TWO_REQUESTS_PER_SECOND_RATE_LIMITER.consumeToken() || !SIXTY_REQUESTS_PER_MINUTE_RATE_LIMITER.consumeToken() || !FIVE_THOUSAND_REQUESTS_PER_DAY_RATE_LIMITER.consumeToken()) {
            return new ArrayList<>();
        }

        try {
            // Construct URL for sending requests to the LocationIQ API
            String url = "";
            String fullQuery = "";
            // address = 0, suburb = 1, city = 2, postcode = 3, country = 4

            switch (addressField) {
                case "country":
                    url = URL + "autocomplete?q=" + query + "&key=" + API_KEY + "&tag=place:country";
                    break;
                case "city":
                    url = URL + "autocomplete?q=" + query + "&key=" + API_KEY + "&tag=place&normalizecity=1";
                    break;
                case "postcode":
                    url = URL + "autocomplete?q=" + query + "&key=" + API_KEY + "&tag=place:postcode&normalizecity=1";
                    break;
                case "suburb":
                    url = URL + "autocomplete?q=" + query + "&key=" + API_KEY + "&tag=place:suburb&normalizecity=1";
                    break;
                default:
                    fullQuery += query;
                    if (!fullAddress[1].isEmpty()) {
                        fullQuery += ", " + fullAddress[1];
                    }
                    if (!fullAddress[2].isEmpty()) {
                        fullQuery += ", " + fullAddress[2];
                    }
                    if (!fullAddress[3].isEmpty()) {
                        fullQuery += ", " + fullAddress[3];
                    }
                    if (!fullAddress[4].isEmpty()) {
                        fullQuery += ", " + fullAddress[4];
                    }
                    url = URL + "autocomplete?q=" + fullQuery + "&key=" + API_KEY + "&normalizecity=1";
            }

            // Sending a request to the LocationIQ API endpoint and returns a JSON response in string form
            String jsonResponse = restTemplate.getForObject(url, String.class);

            // Calls locationsIntoList() method given the JSON response in String format, the query and the address field
            return locationsIntoList(jsonResponse, query, fullAddress, addressField);
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
    public List<Location> locationsIntoList(String jsonResponse, String query, String[] fullAddress, String addressField) throws JsonProcessingException {
        List<Location> locationAddresses = new ArrayList<>();

        List<Map<String, Object>> locations = objectMapper.readValue(jsonResponse, List.class);

        // Obtain the type of the location, the display format of the location, and the address map of the location
        for (Map<String, Object> location : locations) {
            Double latitude = Double.parseDouble(location.get("lat").toString());
            Double longitude = Double.parseDouble(location.get("lon").toString());
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

            // IF INPUT IS DONE IN THE COUNTRY FIELD
            if (addressField.equals("country") && locationType.equals("country")) {
                country = addressMap.get("name").toString();

                locationAddresses.add(new Location(address, suburb, city, postcode, country, latitude, longitude, displayPlace));
            }
            // IF INPUT IS DONE IN THE POSTCODE FIELD
            else if (addressField.equals("postcode") && locationType.equals("postcode")) {
                postcode = addressMap.get("name").toString();
                if (addressMap.containsKey("city")) city = addressMap.get("city").toString();
                if (addressMap.containsKey("country")) country = addressMap.get("country").toString();

                if (!city.isEmpty() && !country.isEmpty()) {
                    locationAddresses.add(new Location(address, suburb, city, postcode, country, latitude, longitude, displayPlace));
                }
            }
            // IF INPUT IS DONE IN THE CITY FIELD
            else if (addressField.equals("city") && locationType.equals("city")) {
                city = addressMap.get("name").toString();
                if (addressMap.containsKey("country")) country = addressMap.get("country").toString();

                if (!city.isEmpty() && !country.isEmpty()) {
                    locationAddresses.add(new Location(address, suburb, city, postcode, country, latitude, longitude, displayPlace));
                }
            }
            // IF INPUT IS DONE IN THE SUBURB FIELD
            else if (addressField.equals("suburb") && locationType.equals("suburb")) {
                suburb = addressMap.get("name").toString();
                if (addressMap.containsKey("city")) city = addressMap.get("city").toString();
                if (addressMap.containsKey("country")) country = addressMap.get("country").toString();

                if (!city.isEmpty() && !country.isEmpty()) {
                    locationAddresses.add(new Location(address, suburb, city, postcode, country, latitude, longitude, displayPlace));
                }
            }
            // IF INPUT IS DONE IN THE ADDRESS FIELD
            else if (addressField.equals("address")) {
                boolean validLocation = true;

                // CHECK IF THE LOCATION IS A HOUSE ADDRESS
                if (addressMap.containsKey("house_number")) {
                    String addressCombined = addressMap.get("house_number").toString() + " " + addressMap.get("road").toString();
                    if (addressCombined.startsWith(query)) {
                        displayPlace = addressCombined;
                        address = addressMap.get("house_number").toString() + " " + addressMap.get("road").toString();
                    }
                } else { // OTHERWISE IT WOULD BE A STREET
                    address = addressMap.get("name").toString();
                }

                if (addressMap.containsKey("suburb")) {
                    if (!fullAddress[1].isEmpty()) {
                        if (!addressMap.get("suburb").toString().equals(fullAddress[1])) {
                            validLocation = false;
                        }
                    }
                    suburb = addressMap.get("suburb").toString();
                }

                if (addressMap.containsKey("city")) {
                    if (!fullAddress[2].isEmpty()) {
                        if (!addressMap.get("city").toString().equals(fullAddress[2])) {
                            validLocation = false;
                        }
                    }
                    city = addressMap.get("city").toString();
                }

                if (addressMap.containsKey("postcode")) {
                    if (!fullAddress[3].isEmpty()) {
                        if (!addressMap.get("postcode").toString().equals(fullAddress[3])) {
                            validLocation = false;
                        }
                    }
                    postcode = addressMap.get("postcode").toString();
                }

                if (addressMap.containsKey("country")) {
                    if (!fullAddress[4].isEmpty()) {
                        if (!addressMap.get("country").toString().equals(fullAddress[4])) {
                            validLocation = false;
                        }
                    }
                    country = addressMap.get("country").toString();
                }

                if (!address.isEmpty() && !city.isEmpty() && !country.isEmpty() && address.startsWith(query) && validLocation) {
                    locationAddresses.add(new Location(address, suburb, city, postcode, country, latitude, longitude, displayPlace));
                }
            }
        }
        return locationAddresses;
    }
}