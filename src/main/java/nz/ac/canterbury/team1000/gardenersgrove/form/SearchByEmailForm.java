package nz.ac.canterbury.team1000.gardenersgrove.form;

import static nz.ac.canterbury.team1000.gardenersgrove.form.FormUtils.MAX_DB_STR_LEN;
import static nz.ac.canterbury.team1000.gardenersgrove.form.FormUtils.checkBlank;
import static nz.ac.canterbury.team1000.gardenersgrove.form.FormUtils.checkEmailIsInvalid;
import static nz.ac.canterbury.team1000.gardenersgrove.form.FormUtils.checkOverMaxLength;

import org.springframework.validation.BindingResult;

/**
 * Entity used to parse and store the data sent through a search friend GET request
 */
public class SearchByEmailForm {
	protected String email;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Validates the 'Email' data and adds validation error to the BindingResult.
	 *
	 * @param searchByEmailForm    the SearchByEmailForm object representing the email that user is searching for
	 * @param bindingResult the BindingResult object for validation errors
	 */
	public static void validate(SearchByEmailForm searchByEmailForm, BindingResult bindingResult) {
		ErrorAdder errors = new ErrorAdder(bindingResult, "searchByEmailForm");

		// validate email
		if (checkBlank(searchByEmailForm.getEmail()) || checkEmailIsInvalid(searchByEmailForm.getEmail())) {
			errors.add("email", "Email address must be in the form ‘jane@doe.nz’",
				searchByEmailForm.getEmail());
		} else if (checkOverMaxLength(searchByEmailForm.getEmail(), MAX_DB_STR_LEN)) {
			errors.add("email",
				"Email address must be " + MAX_DB_STR_LEN + " characters long or less",
				searchByEmailForm.getEmail());
		}
	}
}
