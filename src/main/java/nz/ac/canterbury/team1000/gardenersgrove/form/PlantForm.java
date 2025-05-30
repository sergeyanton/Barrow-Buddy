package nz.ac.canterbury.team1000.gardenersgrove.form;

import nz.ac.canterbury.team1000.gardenersgrove.entity.Plant;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import org.springframework.web.multipart.MultipartFile;

import static nz.ac.canterbury.team1000.gardenersgrove.form.FormUtils.*;

public class PlantForm {
    protected String name;
    protected String plantCount;
    protected String description;
    protected String plantedOnDate;
    protected String picturePath;
    protected MultipartFile pictureFile;

    // Somehow this is implicitly set by Spring MVC. Despite the setter appearing uncalled, it is in the background.
    protected Long gardenId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlantCount() {
        return plantCount;
    }

    public void setPlantCount(String plantCount) {
        this.plantCount = plantCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlantedOnDate() {
        return plantedOnDate;
    }

    public void setPlantedOnDate(String plantedOnDate) {
        this.plantedOnDate = plantedOnDate;
    }

    public Long getGardenId() {
        return gardenId;
    }

    public void setGardenId(Long gardenId) {
        this.gardenId = gardenId;
    }
    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public MultipartFile getPictureFile() {
        return pictureFile;
    }

    public void setPictureFile(MultipartFile pictureFile) {
        this.pictureFile = pictureFile;
    }

    /**
     * Generates a Plant object with the values from the form.
     * @return new Plant with attributes directly from the input values in the form.
     */
    public Plant getPlant() {
        return new Plant(
                this.name,
                this.plantCount,
                this.description,
                this.plantedOnDate,
                this.picturePath,
                this.gardenId);
    }

    /**
     * Updates an existing Plant object with the values from the form.
     * 
     * @param plant the Plant object to update
     */
    public void updatePlant(Plant plant) {
        plant.setName(this.name);
        plant.setPlantCount(this.plantCount);
        plant.setDescription(this.description);
        plant.setPlantedOnDate(this.plantedOnDate);
        plant.setPicturePath(this.picturePath);
    }

    

    /**
     * Validates the 'New Plant' form data and adds validation errors to the BindingResult.
     *
     * @param createPlantForm the PlantForm object representing the details of the garden being created
     * @param bindingResult   the BindingResult object for validation errors
     */
    public static void validate(PlantForm createPlantForm, BindingResult bindingResult) {
        // Create an ErrorAdder instance with the BindingResult and object name
        ErrorAdder errors = new ErrorAdder(bindingResult, "createPlantForm");

        // Validate plant name
        if (checkBlank(createPlantForm.getName())) {
            errors.add("name", "Plant name must not be empty", createPlantForm.getName());
        } else if (!checkValidPlantName(createPlantForm.getName())) {
            errors.add("name", "Plant name must only include letters, numbers, spaces, dots, hyphens or apostrophes", createPlantForm.getName());
        } else if (checkOverMaxLength(createPlantForm.getName(), MAX_DB_STR_LEN)) {
            errors.add("name", "Name must be 255 characters or less", createPlantForm.getName());
        }

        // Validate plant count (if there is one)
        if (!checkBlank(createPlantForm.getPlantCount())) {
            if (checkNotPositiveInteger(createPlantForm.getPlantCount())) {
                errors.add("plantCount", "Plant count must be a positive integer",
                    createPlantForm.getPlantCount());
            } else if (checkNumberTooBig(createPlantForm.getPlantCount(), MAX_PLANT_COUNT)) {
                errors.add("plantCount", "The maximum allowed count is set at 268000 based on data from Quaora on the number of         Angiosperms plants in the world.",
                    createPlantForm.getPlantCount());
            }
        }

        // Validate description (if there is one)
        if (!checkBlank(createPlantForm.getDescription())) {
            if (checkOverMaxLength(createPlantForm.getDescription(), 512)) {
                errors.add("description", "Plant description must be less than 512 characters", createPlantForm.getDescription());
            }
        }

        // Validate date of birth (if there is one)
        if (!checkBlank(createPlantForm.getPlantedOnDate())) {
            if (checkDateNotInCorrectFormat(createPlantForm.getPlantedOnDate())) {
                errors.add("plantedOnDate", "Date is not in valid format, DD/MM/YYYY", createPlantForm.getPlantedOnDate());
            } else if (!checkDateBefore(createPlantForm.getPlantedOnDate(), LocalDate.now().plusDays(1))) {
                errors.add("plantedOnDate", "Date cannot be in the future", createPlantForm.getPlantedOnDate());
            }
        }

        // Validate image
        if (createPlantForm.getPictureFile() != null && !createPlantForm.getPictureFile().isEmpty()) {
            if (checkImageWrongType(createPlantForm.getPictureFile())) {
                errors.add("pictureFile", "Image must be of type png, jpg or svg", null);
            } else if (checkImageTooBig(createPlantForm.getPictureFile())) {
                errors.add("pictureFile", "Image must be less than 10MB", null);
            }
        }
    }

    /**
     * Create a PlantForm from the Plant object.
     * 
     * @param plant the Plant object to create the form from.
     * @return the populated PlantForm object.
     */
    public static PlantForm fromPlant(Plant plant) {
        PlantForm plantForm = new PlantForm();

        // set all the fields blank
        plantForm.setName("");
        plantForm.setPlantCount("");
        plantForm.setDescription("");
        plantForm.setPlantedOnDate("");
        if (plant == null) return plantForm;

        if (plant.getName() != null) plantForm.setName(plant.getName());
        if (plant.getPlantCount() != null) plantForm.setPlantCount(plant.getPlantCount().toString());
        if (plant.getDescription() != null) plantForm.setDescription(plant.getDescription());
        plantForm.setPlantedOnDate(FormUtils.dateToString(plant.getPlantedOnDate()));
        plantForm.setGardenId(plant.getGardenId());
        plantForm.setPicturePath(plant.getPicturePath());

        return plantForm;
    }

    /**
     * Returns a string representation of the PlantForm object.
     * 
     * @return a string representation of the PlantForm object.
     */
    @Override
    public String toString() {
        return "PlantForm{" +
                "name='" + name + '\'' +
                ", plantCount='" + plantCount + '\'' +
                ", description='" + description + '\'' +
                ", plantedOnDate='" + plantedOnDate + '\'' +
                ", gardenId=" + gardenId +
                '}';
    }
}
