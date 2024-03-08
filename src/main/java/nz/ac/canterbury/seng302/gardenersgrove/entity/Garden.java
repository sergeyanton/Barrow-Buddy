package nz.ac.canterbury.seng302.gardenersgrove.entity;

import java.util.Optional;
import jakarta.persistence.*;
import nz.ac.canterbury.seng302.gardenersgrove.classes.ValidityCheck;

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
    private String location;
    @Column
    private Double size;

    /**
     * JPA required no-args constructor
     */
    protected Garden() {}

    /**
     * Creates a new Garden object
     *
     * @param name name of garden
     * @param location location of garden
     * @param size size of garden
     */
    public Garden(String name, String location, Double size) {
        this.name = name;
        this.location = location;

        if (size != null && size < 0) {
            throw new IllegalArgumentException("Garden size must be a positive number");
        }

        this.size = size;
    }

    /**
     * Creates a new garden but takes the size as a string that is then parsed to a double It can
     * either use a , or a . as a decimal separator
     * 
     * @param name name of garden
     * @param location location of garden
     * @param size size of garden
     */
    public Garden(String name, String location, String size) {
        this.name = name;
        this.location = location;

        this.setSize(size);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public Double getSize() {
        return size;
    }

    public void setName(String newName) {
        name = newName;
    }

    public void setLocation(String newLocation) {
        location = newLocation;
    }

    public void setSize(Double newSize) {
        size = newSize;
    }

    public void setSize(String newSize) {
        Optional<String> validGardenSizeCheck = ValidityCheck.validateGardenSize(newSize);

        if (validGardenSizeCheck.isPresent()) {
            throw new IllegalArgumentException(validGardenSizeCheck.get());
        }

        this.size = (newSize.isBlank()) ? null : Double.parseDouble(newSize.replace(",", "."));
    }

    @Override
    public String toString() {
        return "Garden{id=" + id + ", name=\'" + name + "\', size=\'" + size + "\'}";
    }
}
