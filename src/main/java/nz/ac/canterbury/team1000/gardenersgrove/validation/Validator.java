package nz.ac.canterbury.team1000.gardenersgrove.validation;

/**
 * This class represents a validation result, including a status and a message.
 */
public class Validator {
    private Boolean status;
    private String message;

    /**
     * Constructs a Validator object with the given status and message.
     * 
     * @param status The status of the validation (true if valid, false if invalid).
     * @param message The message associated with the validation result.
     */
    public Validator(Boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    /**
     * Sets the status and message of the validation result.
     * 
     * @param status The status of the validation (true if valid, false if invalid).
     * @param message The message associated with the validation result.
     */
    public void setValid(Boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

}
