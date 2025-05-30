package nz.ac.canterbury.team1000.gardenersgrove.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity class reflecting an entry of address, suburb, city, postcode, country, displayPlace and
 * displayAddress of a location
 */
public class Location {
    public String address;
    public String suburb;
    public String city;
    public String postcode;
    public String country;
    public Double latitude;
    public Double longitude;
    public String displayPlace;
    public String displayAddress;

    public Location(String address, String suburb, String city, String postcode, String country, Double latitude, Double longitude, String displayPlace) {
        this.address = address;
        this.suburb = suburb;
        this.city = city;
        this.postcode = postcode;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
        this.displayPlace = displayPlace;
        this.displayAddress = displayAddress();
    }

    /**
     * Displays the full address information in the format: address, suburb, city, postcode, country (and ignores any that
     * is empty or blank)
     * @return full address information in the format specified above
     */
    public String displayAddress() {
        List<String> fields = new ArrayList<>();
        if (!address.isEmpty()) fields.add(address);
        if (!suburb.isEmpty()) fields.add(suburb);
        if (!city.isEmpty()) fields.add(city);
        if (!postcode.isEmpty()) fields.add(postcode);
        if (!country.isEmpty()) fields.add(country);
        return String.join(", ", fields);
    }
}
