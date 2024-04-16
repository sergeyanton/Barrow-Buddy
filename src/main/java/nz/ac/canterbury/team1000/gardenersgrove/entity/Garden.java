package nz.ac.canterbury.team1000.gardenersgrove.entity;

import jakarta.persistence.*;

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
    private String street;
    @Column(nullable = false)
    private String streetNumber;
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

    /**
     * JPA required no-args constructor
     */
    protected Garden() {
    }

    /**
     * Creates a new Garden object
     *
     * @param name     name of garden
     * @param location location of garden
     * @param size     size of garden
     */
    public Garden(String name, String street, String streetNumber, String suburb, String city, String postcode, String country, Double size) {
        this.name = name;
        this.street = street;
        this.streetNumber = streetNumber;
        this.suburb = suburb;
        this.city = city;
        this.postcode = postcode;
        this.country = country;


        if (size != null && size < 0) {
            throw new IllegalArgumentException("Garden size must be a positive number");
        }

        this.size = size;
    }

    /**
     * Creates a new garden but takes the size as a string that is then parsed to a double It can
     * either use a ',' or a '.' as a decimal separator
     *
     * @param name     name of garden
     * @param location location of garden
     * @param size     size of garden
     */
    public Garden(String name, String street, String streetNumber, String suburb, String city, String postcode, String country, String size) {
        this.name = name;
        this.street = street;
        this.streetNumber = streetNumber;
        this.suburb = suburb;
        this.city = city;
        this.postcode = postcode;
        this.country = country;

        this.setSize(size);
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

    public String getStreet() {
        return street;
    }
    public String getStreetNumber() {
        return streetNumber;
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

    public void setName(String newName) {
        name = newName;
    }

//    public void setLocation(String newLocation) {
//        location = newLocation;
//    }

    public void setSize(Double newSize) {
        size = newSize;
    }

    public void setSize(String newSize) {
        this.size = (newSize.isBlank()) ? null : Double.parseDouble(newSize.replace(",", "."));
    }

    @Override
    public String toString() {
        return "Garden{id=" + id + ", name=" + name + "', size='" + size + "'}";
    }
}
