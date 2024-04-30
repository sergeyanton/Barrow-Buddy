package nz.ac.canterbury.team1000.gardenersgrove.entity;

import java.util.ArrayList;
import java.util.List;

public class Location {
    public String address;
    public String suburb;
    public String city;
    public String postCode;
    public String country;
    public String displayPlace;
    public String displayAddress;

    public Location(String address, String suburb, String city, String postCode, String country, String displayPlace) {
        this.address = address;
        this.suburb = suburb;
        this.city = city;
        this.postCode = postCode;
        this.country = country;
        this.displayPlace = displayPlace;
        this.displayAddress = displayAddress();
    }

    public String displayAddress() {
        List<String> fields = new ArrayList<>();
        if (!address.isEmpty()) fields.add(address);
        if (!suburb.isEmpty()) fields.add(suburb);
        if (!city.isEmpty()) fields.add(city);
        if (!postCode.isEmpty()) fields.add(postCode);
        if (!country.isEmpty()) fields.add(country);
        return String.join(", ", fields);
    }
}
