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
    private String location;
    @Column
    private Double size;

    @ManyToOne
    private User owner;

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
     * @param owner    owner of garden
     */
    public Garden(String name, String location, Double size, User owner) {
        this.name = name;
        this.location = location;

        if (size != null && size < 0) {
            throw new IllegalArgumentException("Garden size must be a positive number");
        }

        this.size = size;
        this.owner = owner;
    }

    /**
     * Creates a new garden but takes the size as a string that is then parsed to a double It can
     * either use a , or a . as a decimal separator
     *
     * @param name     name of garden
     * @param location location of garden
     * @param size     size of garden as a string. Separators can be , or .
     * @param owner    owner of garden
     */
    public Garden(String name, String location, String size, User owner) {
        this.name = name;
        this.location = location;
        this.owner = owner;

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
        if (newSize != null && newSize < 0) {
            throw new IllegalArgumentException("Garden size must be a positive number");
        }
        size = newSize;
    }

    public void setSize(String newSize) {
        setSize((newSize.isBlank()) ? null : Double.parseDouble(newSize.replace(",", ".")));
    }

    public void setOwner(User newOwner) {
        owner = newOwner;
    }

    public User getOwner() {
        return owner;
    }

    @Override
    public String toString() {
        return "Garden{id=" + id + ", name=\'" + name + "\', size=\'" + size + "\'}";
    }
}
