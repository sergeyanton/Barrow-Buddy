package nz.ac.canterbury.team1000.gardenersgrove.entity;

public class Location {
    private String houseNumber;
    private String street;
    private String suburb;
    private String city;
    private String postCode;
    private String country;

    public Location(String houseNumber, String street, String suburb, String city, String postCode, String country) {
        this.houseNumber = houseNumber;
        this.street = street;
        this.suburb = suburb;
        this.city = city;
        this.postCode = postCode;
        this.country = country;
    }

    public Location() {}

    public String getHouseNumber() {
        return this.houseNumber;
    }

    public String getStreet() {
        return this.street;
    }

    public String getSuburb() {
        return this.suburb;
    }

    public String getCity() {
        return this.city;
    }

    public String getPostcode() {
        return this.postCode;
    }

    public String getCountry() {
        return this.country;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "Location{" +
                "houseNumber='" + houseNumber + '\'' +
                ", street='" + street + '\'' +
                ", suburb='" + suburb + '\'' +
                ", city='" + city + '\'' +
                ", postCode='" + postCode + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
