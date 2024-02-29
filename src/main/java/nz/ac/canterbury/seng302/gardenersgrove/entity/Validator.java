package nz.ac.canterbury.seng302.gardenersgrove.entity;

public class Validator {
    private Boolean status;
    private String message;

    public Validator(Boolean status, String message){
        this.status = status;
        this.message = message;
    }

    public void setValid(Boolean status, String message){
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
