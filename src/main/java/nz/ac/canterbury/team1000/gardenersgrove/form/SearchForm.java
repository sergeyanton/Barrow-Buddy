package nz.ac.canterbury.team1000.gardenersgrove.form;

import static nz.ac.canterbury.team1000.gardenersgrove.form.FormUtils.MAX_DB_STR_LEN;
import static nz.ac.canterbury.team1000.gardenersgrove.form.FormUtils.checkBlank;
import static nz.ac.canterbury.team1000.gardenersgrove.form.FormUtils.checkEmailIsInvalid;
import static nz.ac.canterbury.team1000.gardenersgrove.form.FormUtils.checkOverMaxLength;

import org.springframework.validation.BindingResult;

public class SearchForm {
	protected String emailSearch;
	public String getEmailSearch() {
		return emailSearch;
	}
	public void setEmailSearch(String emailSearch) {
		this.emailSearch = emailSearch;
	}

	public static void validate(SearchForm searchForm, BindingResult bindingResult) {
		ErrorAdder errors = new ErrorAdder(bindingResult, "searchForm");

		// validate email
		if (checkBlank(searchForm.getEmailSearch()) || checkEmailIsInvalid(searchForm.getEmailSearch())) {
			errors.add("email", "Email address must be in the form ‘jane@doe.nz’",
				searchForm.getEmailSearch());
		} else if (checkOverMaxLength(searchForm.getEmailSearch(), MAX_DB_STR_LEN)) {
			errors.add("email",
				"Email address must be " + MAX_DB_STR_LEN + " characters long or less",
				searchForm.getEmailSearch());
		}
	}
}
