package nz.ac.canterbury.team1000.gardenersgrove.form;

import static nz.ac.canterbury.team1000.gardenersgrove.form.FormUtils.*;

import org.springframework.validation.BindingResult;

/**
 * Entity used to parse and store the data sent through a search friend GET request
 */
public class SearchFriendsForm {
	protected String search;
	public String getSearch() {
		return search;
	}
	public void setSearch(String search) {
		this.search = search;
	}



	/**
	 * Validates the 'Search' field and adds validation error to the BindingResult.
	 *
	 * @param searchFriendsForm    the SearchFriendsForm object representing the email that user is searching for
	 * @param bindingResult the BindingResult object for validation errors
	 */
	public static void validate(SearchFriendsForm searchFriendsForm, BindingResult bindingResult) {
		ErrorAdder errors = new ErrorAdder(bindingResult, "searchFriendsForm");

		// check if it is a valid name
		if (!checkOnlyHasLettersMacronsSpacesHyphensApostrophes(searchFriendsForm.getSearch()) || (checkOverMaxLength(
			searchFriendsForm.getSearch(), MAX_USER_NAME_LEN))) {
			// this error will not display, but it will let the controller know
			errors.add("name", null,
				searchFriendsForm.getSearch());
		}

		// check if it is a valid email
		if (checkEmailIsInvalid(searchFriendsForm.getSearch()) || checkOverMaxLength(
			searchFriendsForm.getSearch(), MAX_DB_STR_LEN)) {
			errors.add("email", null,
				searchFriendsForm.getSearch());
		}
	}
}
