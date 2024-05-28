package nz.ac.canterbury.team1000.gardenersgrove.form;

import static nz.ac.canterbury.team1000.gardenersgrove.form.FormUtils.MAX_DB_STR_LEN;
import static nz.ac.canterbury.team1000.gardenersgrove.form.FormUtils.checkBlank;
import static nz.ac.canterbury.team1000.gardenersgrove.form.FormUtils.checkEmailIsInvalid;
import static nz.ac.canterbury.team1000.gardenersgrove.form.FormUtils.checkOverMaxLength;

import org.springframework.validation.BindingResult;

/**
 * Entity used to parse and store the data sent through a search friend GET request
 */
public class SearchForm {
	protected String emailSearch;
	public String getEmailSearch() {
		return emailSearch;
	}
	public void setEmailSearch(String emailSearch) {
		this.emailSearch = emailSearch;
	}

	/**
	 * Validates the 'Email' data and adds validation error to the BindingResult.
	 *
	 * @param searchForm    the SearchForm object representing the email that user is searching for
	 * @param bindingResult the BindingResult object for validation errors
	 */
	public static void validate(SearchForm searchForm, BindingResult bindingResult) {
		ErrorAdder errors = new ErrorAdder(bindingResult, "searchForm");

		// validate email
		if (checkBlank(searchForm.getEmailSearch()) || checkEmailIsInvalid(searchForm.getEmailSearch())) {
			errors.add("emailSearch", "Email address must be in the form ‘jane@doe.nz’",
				searchForm.getEmailSearch());
		} else if (checkOverMaxLength(searchForm.getEmailSearch(), MAX_DB_STR_LEN)) {
			errors.add("emailSearch",
				"Email address must be " + MAX_DB_STR_LEN + " characters long or less",
				searchForm.getEmailSearch());
		}
	}
}
