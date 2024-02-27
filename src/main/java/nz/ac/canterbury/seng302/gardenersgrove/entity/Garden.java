package nz.ac.canterbury.seng302.gardenersgrove.entity;

import jakarta.persistence.*;

/**
 * Entity class reflecting an entry of name, and favourite programming language
 * Note the @link{Entity} annotation required for declaring this as a persistence entity
 */
@Entity
public class Garden {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String gardenName;

//    @Column(nullable = false)
//    private String language;

    /**
     * JPA required no-args constructor
     */
    protected Garden() {}

    /**
     * Creates a new Garden object
     * @param name name of garden
     */
    public Garden(String name) {
        this.gardenName = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return gardenName;
    }

    @Override
    public String toString() {
        return "Garden{" +
                "id=" + id +
                ", name='" + gardenName + '\'' +
                '}';
    }
}
