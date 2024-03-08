package nz.ac.canterbury.seng302.gardenersgrove.entity;

import java.time.format.DateTimeFormatter;
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
    private String plantedOnDate;
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
    public Plant(String name, String count, String description, String plantedOnDate, String gardenId) {
        setName(name);
        setCount(count);
        setDescription(description);
        setPlantedOnDate(plantedOnDate);
        setGardenId(gardenId);
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

    public String getPlantedOnDate() { return plantedOnDate; }

    public int getGardenId() { return gardenId; }

    public void setName(String name) {
        this.name = name;
    }

    public void setCount(String plantCount) { this.plantCount = Integer.parseInt(plantCount); }

    public void setDescription(String description) { this.description = description; }

    public void setPlantedOnDate(String plantedOnDate) {
        String[] dateList = plantedOnDate.split("/");
        int[] dateAsInteger = {Integer.parseInt(dateList[0]), Integer.parseInt(dateList[1]), Integer.parseInt(dateList[2])}; // Day, Month, Year
        LocalDate newDate = LocalDate.of(dateAsInteger[2], dateAsInteger[1], dateAsInteger[0]);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.plantedOnDate = newDate.format(dateTimeFormatter);
    }

    public void setGardenId(String gardenid) {
        this.gardenId = Integer.parseInt(gardenid);
    }

    @Override
    public String toString() {
        return "Plant{" + "id=" + id + ", name='" + name + ", count=" + plantCount + ", description=" + description +
                ", planted on date=" + plantedOnDate + ", garden id=" + gardenId + '\'' + '}';
    }
}
