package nz.ac.canterbury.team1000.gardenersgrove.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity class reflecting an entry of name, location, and size of a garden Note the @link{Entity}
 * annotation required for declaring this as a persistence entity
 */
@Entity
public class Garden {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private String suburb;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String postcode;
    @Column(nullable = false)
    private String country;
    @Column
    private Double size;


    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(name = "is_public", nullable = false)
    private boolean isPublic = false;

    /**
     * JPA required no-args constructor
     */
    protected Garden() {
    }

    /**
     * Creates a new Garden object
     *
     * @param name     name of garden
     * @param address address name of garden's location
     * @param suburb suburb of garden's location
     * @param city city of garden's location
     * @param postcode postcode of garden's location
     * @param country country of garden's location
     * @param size     size of garden
     * @param owner    owner of garden
     */
    public Garden(String name, String address, String suburb, String city, String postcode, String country, Double size, User owner, boolean publicity) {
        this.name = name;
        this.address = address;
        this.suburb = suburb;
        this.city = city;
        this.postcode = postcode;
        this.country = country;


        if (size != null && size < 0) {
            throw new IllegalArgumentException("Garden size must be a positive number");
        }

        this.size = size;
        this.setPublicity(publicity);
        this.owner = owner;
    }

    /**
     * Creates a new garden but takes the size as a string that is then parsed to a double It can
     * either use a ',' or a '.' as a decimal separator
     *
     * @param name     name of garden
     * @param address address name of garden's location
     * @param suburb suburb of garden's location
     * @param city city of garden's location
     * @param postcode postcode of garden's location
     * @param country country of garden's location
     * @param size     size of garden
     * @param owner    owner of garden
     */
    public Garden(String name, String address, String suburb, String city, String postcode, String country, String size, User owner, boolean publicity) {
        this.name = name;
        this.address = address;
        this.suburb = suburb;
        this.city = city;
        this.postcode = postcode;
        this.country = country;
        this.setSize(size);
        this.setPublicity(publicity);

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
    public String getSuburb() {
        return suburb;
    }
    public String getCity() {
        return city;
    }
    public String getPostcode() {
        return postcode;
    }
    public String getCountry() {
        return country;
    }

    public Double getSize() {
        return size;
    }

    public boolean getPublicity() {
        return isPublic;
    }

    public void setName(String newName) {
        name = newName;
    }
    public void setAddress(String newAddress) { address = newAddress; }
    public void setSuburb(String newSuburb) {suburb = newSuburb;
    }
    public void setCity(String newCity) { city = newCity; }
    public void setPostcode(String newPostcode) { postcode = newPostcode; }
    public void setCountry(String newCountry) { country = newCountry; }

    public void setSize(Double newSize) {
        if (newSize != null && newSize < 0) {
            throw new IllegalArgumentException("Garden size must be a positive number");
        }
        size = newSize;
    }

    public void setSize(String newSize) {
        setSize((newSize.isBlank()) ? null : Double.parseDouble(newSize.replace(",", ".")));
    }

    public void setPublicity(boolean publicity) {
        isPublic = publicity;
    }

    public void setOwner(User newOwner) {
        owner = newOwner;
    }


    public User getOwner() {
        return owner;
    }

    @Override
    public String toString() {
        return "Garden{id=" + id + ", name=" + name + "', size='" + size + "'}";
    }

    /**
     * Get the String formatted version of the full address
     * @return the full address as a String in the format: address, suburb, city, postcode, country
     */
    public String getLocationString() {
        return new Location(this.address, this.suburb, this.city, this.postcode, this.country, "").displayAddress();
    }
}
