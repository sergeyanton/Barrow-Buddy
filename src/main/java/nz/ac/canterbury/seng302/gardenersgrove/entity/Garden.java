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
    @Column(nullable = false)
    private double size;

    /**
     * JPA required no-args constructor
     */
    protected Garden() {}

    /**
     * Creates a new Garden object
     * 
     * @param name name of garden
     */
    public Garden(String name, String location, double size) {
        this.name = name;
        this.location = location;
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

        Optional<String> validGardenSizeCheck = ValidityCheck.validateGardenSize(size);

        if (validGardenSizeCheck.isPresent()) {
            throw new IllegalArgumentException(validGardenSizeCheck.get());
        }
        this.size = Double.parseDouble(size.replace(",", "."));;
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

    public double getSize() {
        return size;
    }

    public void setName(String newName) {
        name = newName;
    }

    public void setLocation(String newLocation) {
        location = newLocation;
    }

    public void setSize(double newSize) {
        size = newSize;
    }

    @Override
    public String toString() {
        return "Garden{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}
