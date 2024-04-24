package nz.ac.canterbury.team1000.gardenersgrove.form;

import org.springframework.validation.BindingResult;

import static nz.ac.canterbury.team1000.gardenersgrove.form.FormUtils.checkBlank;
import static nz.ac.canterbury.team1000.gardenersgrove.form.FormUtils.checkEmailIsInvalid;

public class VerificationTokenForm {
    protected String verificationCode;
    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public static void validate(VerificationTokenForm verificationTokenForm, BindingResult bindingResult) {
        ErrorAdder errors = new ErrorAdder(bindingResult, "loginForm");

        // validate verification code
        if (checkBlank(verificationTokenForm.getVerificationCode())) {
            errors.add("verificationCode", "The verification code is invalid", verificationTokenForm.getVerificationCode());
        }
    }




}
