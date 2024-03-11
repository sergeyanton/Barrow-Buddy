package nz.ac.canterbury.seng302.gardenersgrove.entity;

import jakarta.persistence.*;
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
    @Column
    private Integer plantCount;
    @Column
    private String description;
    @Column
    private LocalDate plantedOnDate;
    @Column(nullable = false)
    private Long gardenId;

    /**
     * JPA required no-args constructor
     */
    protected Plant() {}

    /**
     * Creates a new Plant object
     *
     * @param name name of plant
     * @param plantCount number of plant occurrence in garden
     * @param description description of plant
     * @param plantedOnDate date when plant was planted (in DD/MM/YYYY format)
     * @param gardenId ID of garden where the plant is currently in
     */
    public Plant(String name, Integer plantCount, String description, LocalDate plantedOnDate, Long gardenId) {
        this.name = name;
        this.plantCount = plantCount;
        this.description = description;
        this.plantedOnDate = plantedOnDate;
        this.gardenId = gardenId;
    }


    /**
     * Creates a new Plant object given strings for each attribute
     *
     * @param name name of plant
     * @param plantCount number of plant occurrence in garden
     * @param description description of plant
     * @param plantedOnDate date when plant was planted (in DD/MM/YYYY format)
     * @param gardenId ID of garden where the plant is currently in
     */
    public Plant(String name, String plantCount, String description, String plantedOnDate, Long gardenId) {
        this.name = name;
        this.setPlantCount(plantCount);
        this.setDescription(description);
        this.setPlantedOnDate(plantedOnDate);
        this.gardenId = gardenId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPlantCount() {
        return plantCount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getPlantedOnDate() { return plantedOnDate; }

    public Long getGardenId() { return gardenId; }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlantCount(Integer plantCount) {
        this.plantCount = plantCount;
    }

    /**
     * Sets plantCount from a string representation
     * @param plantCount the amount of this plant in this garden (optional)
     */
    public void setPlantCount(String plantCount) {
        this.plantCount = (plantCount.isBlank()) ? null : Integer.parseInt(plantCount);
    }

    public void setDescription(String description) { this.description = (description.isBlank()) ? null : description; }

    public void setPlantedOnDate(LocalDate plantedOnDate) {
        this.plantedOnDate = plantedOnDate;
    }

    /**
     * Sets plantedOnDate given a string representation in DD/MM/YYYY format
     * @param plantedOnDate string representing the date of planting
     */
    public void setPlantedOnDate(String plantedOnDate) {
        if (plantedOnDate.isBlank()) {
            this.plantedOnDate = null;
        } else {
            String[] dateList = plantedOnDate.split("/");
            this.plantedOnDate = LocalDate.of(Integer.parseInt(dateList[2]), Integer.parseInt(dateList[1]), Integer.parseInt(dateList[0]));
        }
    }

    public void setGardenId(Long gardenId) {
        this.gardenId = gardenId;
    }

    /**
     * Sets gardenId from a String representation
     * @param gardenId the id of the garden this plant belongs to
     */
    public void setGardenId(String gardenId) {
        this.gardenId = Long.parseLong(gardenId);
    }

    @Override
    public String toString() {
        return "Plant{" + "id=" + id + ", name='" + name + ", count=" + plantCount + ", description=" + description +
                ", planted on date=" + plantedOnDate + ", garden id=" + gardenId + '\'' + '}';
    }
}
