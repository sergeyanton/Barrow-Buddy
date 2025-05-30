package nz.ac.canterbury.team1000.gardenersgrove.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;

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
    private Double latitude;
    @Column
    private Double longitude;
    @Column
    private Double size;
    @Column(length = 512)
    private String description;

    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @CreationTimestamp(source = SourceType.DB)
    private LocalDateTime  createdAt;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(name = "is_public", nullable = false)
    private boolean isPublic;


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

    public Garden(String name, String address, String suburb, String city, String postcode, String country, Double latitude, Double longitude, Double size, String description, User owner, boolean isPublic) {

        this.name = name;
        this.address = address;
        this.suburb = suburb;
        this.city = city;
        this.postcode = postcode;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;


        if (size != null && size < 0) {
            throw new IllegalArgumentException("Garden size must be a positive number");
        }

        this.size = size;
        this.setDescription(description);
        this.isPublic = isPublic;
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

    public Garden(String name, String address, String suburb, String city, String postcode, String country, Double latitude, Double longitude, String size, String description, User owner, boolean isPublic) {

        this.name = name;
        this.address = address;
        this.suburb = suburb;
        this.city = city;
        this.postcode = postcode;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
        setSize(size);
        this.setDescription(description);
        this.isPublic = isPublic;

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
    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getSize() {
        return size;
    }

    public String getDescription() {
        return description;
    }

    public boolean getIsPublic() {
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
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }


    public void setSize(Double newSize) {
        if (newSize != null && newSize < 0) {
            throw new IllegalArgumentException("Garden size must be a positive number");
        }
        size = newSize;
    }

    public void setSize(String newSize) {
        setSize((newSize.isBlank()) ? null : Double.parseDouble(newSize.replace(",", ".")));
    }

    public void setDescription(String description) {
        if (description == null) {
            this.description = null;
        } else if (description.trim().isBlank()) {
            this.description = null;
        } else {
            this.description = description.trim();
        }
    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
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
        return new Location(this.address, this.suburb, this.city, this.postcode, this.country, this.latitude, this.latitude, "").displayAddress();
    }
}
