package nz.ac.canterbury.seng302.gardenersgrove.entity;

import java.util.Optional;
import jakarta.persistence.*;
import nz.ac.canterbury.seng302.gardenersgrove.classes.ValidityCheck;
import java.time.LocalDate;

/**
 * Entity class reflecting an entry of name, count, description, planted on date, and garden id of a plant
 */
@Entity
public class Plant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private int plantCount;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private LocalDate plantedOnDate;
    @Column(nullable = false)
    private int gardenId;

    /**
     * JPA required no-args constructor
     */
    protected Plant() {}

    /**
     * Creates a new Plant object
     *
     * @param name name of plant
     * @param count number of plant occurrence in garden
     * @param description description of plant
     * @param plantedOnDate date when plant was planted (in DD/MM/YYYY format)
     * @param gardenId ID of garden where the plant is currently in
     */
    public Plant(String name, int count, String description, LocalDate plantedOnDate, int gardenId) {
        this.name = name;
        this.plantCount = count;
        this.description = description;
        this.plantedOnDate = plantedOnDate;
        this.gardenId = gardenId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return plantCount;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlantCount(int plantCount) { this.plantCount = plantCount; }

    public void setDescription(String description) { this.description = description; }

    public void setPlantedOnDate(LocalDate plantedOnDate) {
        this.plantedOnDate = plantedOnDate;
    }

    @Override
    public String toString() {
        return "Plant{" + "id=" + id + ", name='" + name + ", count=" + plantCount + ", description=" + description +
                ", planted on date=" + plantedOnDate + ", garden id=" + gardenId + '\'' + '}';
    }
}
