package nz.ac.canterbury.seng302.gardenersgrove.entity;

import jakarta.persistence.*;

/**
 * Entity class reflecting an entry of name, TODO and other attributes that a garden has
 * Note the @link{Entity} annotation required for declaring this as a persistence entity
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
     * @param name name of garden
     */
    public Garden(String name, String location, double size) {
        this.name = name;
        this.location = location;
        this.size = size;
    }

    public Long getId() {
        return id;
    }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public double getSize() { return size; }

    @Override
    public String toString() {
        return "Garden{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
