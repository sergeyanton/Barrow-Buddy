package nz.ac.canterbury.team1000.gardenersgrove.form;

import org.springframework.validation.BindingResult;

import static nz.ac.canterbury.team1000.gardenersgrove.form.FormUtils.checkBlank;
import static nz.ac.canterbury.team1000.gardenersgrove.form.FormUtils.checkEmailIsInvalid;

public class VerificationTokenForm {
    protected String verificationToken;
    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    public static void validate(VerificationTokenForm verificationTokenForm, BindingResult bindingResult) {
        ErrorAdder errors = new ErrorAdder(bindingResult, "verificationTokenForm");

        // validate verification code
        if (checkBlank(verificationTokenForm.getVerificationToken())) {
            errors.add("verificationToken", "The verification code is invalid", verificationTokenForm.getVerificationToken());
        }
    }




}
