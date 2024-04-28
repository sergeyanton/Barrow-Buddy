package nz.ac.canterbury.team1000.gardenersgrove.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import nz.ac.canterbury.team1000.gardenersgrove.entity.Location;

import java.util.*;

@Service
public class LocationSearchService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public LocationSearchService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * This function searches for exact locations of the address entered by the user
     * @param address the string address entered by user (e.g. Ilam Road)
     * @param type the type of address field the address is (e.g. so it does not search for cities named Ilam Road, etc.)
     * @return a list of geographic coordinates of locations that match the address + type
     */
    public List<List<String>> searchAddress(String address, String type) {
        String url = String.format("https://nominatim.openstreetmap.org/search?q=%s&format=json", address);
        String jsonResponse = restTemplate.getForObject(url, String.class);

        List<List<String>> coordinates = new ArrayList<>();

        try {
            List<Map<String, Object>> locations = objectMapper.readValue(jsonResponse, List.class);
            Iterator<Map<String, Object>> iterator = locations.iterator();

            while (iterator.hasNext()) {
                Map<String, Object> location = iterator.next();
                if (type.equals(location.get("addresstype"))) {
                    List<String> newCoordinate = new ArrayList<>();
                    newCoordinate.add(location.get("lat").toString());
                    newCoordinate.add(location.get("lon").toString());
                    coordinates.add(newCoordinate);
                }
            }
            return coordinates;
        } catch (Exception e) {
            System.out.println("yo this a problem");
            return null;
        }
    }

    /**
     * This function searches for the exact location by given latitude and longitude coordinates
     * @param coordinates the coordinates (latitude, longitude) used to locate the exact location
     * @return a Location object that contains values of the exact location
     */
    public Location getLocationByCoordinates(List<String> coordinates) {
        String latitude = coordinates.get(0);
        String longitude = coordinates.get(1);

        String url = String.format("https://nominatim.openstreetmap.org/reverse?lat=%s&lon=%s&format=json", latitude, longitude);
        String jsonResponse = restTemplate.getForObject(url, String.class);

        try {
            Map locations = objectMapper.readValue(jsonResponse, Map.class);

            Map<String, Object> address = (Map<String, Object>) locations.get("address");

            Location location = new Location();
            if (address.containsKey("house_number")) {
                location.setHouseNumber((String) address.get("house_number"));
            }

            if (address.containsKey("road")) {
                location.setStreet((String) address.get("road"));
            }

            if (address.containsKey("suburb")) {
                location.setSuburb((String) address.get("suburb"));
            }

            if (address.containsKey("city")) {
                location.setCity((String) address.get("city"));
            } else {
                return null;
            }

            if (address.containsKey("postcode")) {
                location.setPostCode((String) address.get("postcode"));
            }

            if (address.containsKey("country")) {
                location.setCountry((String) address.get("country"));
            } else {
                return null;
            }

            return location;

        } catch (JsonMappingException e) {
            System.out.println("yo this a problem");
        } catch (JsonProcessingException e) {
            System.out.println("yo this a problem");
        }

        return null;
    }

    /**
     * This function obtains coordinates of multiple locations in a given list and uses the
     * getLocationByCoordinates() function to find the exact locations of the coordinates
     * @param coordinatesList list of coordinates of locations
     * @return list of Location objects that hold values of the searched locations
     */
    public List<Location> getAllLocations(List<List<String>> coordinatesList) {
        List<Location> allLocations = new ArrayList<>();

        for (int i = 0; i < coordinatesList.size(); i++) {
            Location location = getLocationByCoordinates(coordinatesList.get(i));

            if (location != null) {
                allLocations.add(location);
            }
        }
        return allLocations;
    }

    // temporary main function to experiment
    public static void main(String[] args) {
        LocationSearchService locationSearchService = new LocationSearchService();

        /**
         * Types for type:
         * Street Number: house_number
         * Street: road
         * Suburb: suburb
         * City: city
         * Postcode: postcode
         * Country: country
         */

        List<List<String>> addresses = locationSearchService.searchAddress("Christchurch", "city");

        List<Location> allAddresses = locationSearchService.getAllLocations(addresses);

        for (Location address : allAddresses) {

            if (address.getHouseNumber() != null) {
                System.out.println("House Number: " + address.getHouseNumber());
            }

            if (address.getStreet() != null) {
                System.out.println("Street: " + address.getStreet());
            }

            if (address.getSuburb() != null) {
                System.out.println("Suburb: " + address.getSuburb());
            }

            if (address.getCity() != null) {
                System.out.println("City: " + address.getCity());
            }

            if (address.getPostcode() != null) {
                System.out.println("Postcode: " + address.getPostcode());
            }

            if (address.getCountry() != null) {
                System.out.println("Country: " + address.getCountry());
            }

            System.out.println();

        }
    }
}