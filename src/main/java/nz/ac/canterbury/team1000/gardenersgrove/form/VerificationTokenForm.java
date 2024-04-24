package nz.ac.canterbury.team1000.gardenersgrove.form;

import org.springframework.validation.BindingResult;

import static nz.ac.canterbury.team1000.gardenersgrove.form.FormUtils.checkBlank;

public class VerificationTokenForm {
    protected String verificationToken;
    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    /**
     * Validates the verificationTokenForm data and adds validation errors to the BindingResult.
     *
     * @param verificationTokenForm  the VerificationTokenForm object representing the entered verification token data
     * @param bindingResult          the BindingResult object for validation errors
     */
    public static void validate(VerificationTokenForm verificationTokenForm, BindingResult bindingResult) {
        ErrorAdder errors = new ErrorAdder(bindingResult, "verificationTokenForm");

        // validate verification token
        if (checkBlank(verificationTokenForm.getVerificationToken())) {
            errors.add("verificationToken", "The verification token is invalid", verificationTokenForm.getVerificationToken());
        }
    }




}
