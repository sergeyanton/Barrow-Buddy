package nz.ac.canterbury.team1000.gardenersgrove.controller;

import java.util.Objects;
import nz.ac.canterbury.team1000.gardenersgrove.entity.FriendRelationship;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.form.SearchForm;
import nz.ac.canterbury.team1000.gardenersgrove.service.FriendRelationshipService;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;
import nz.ac.canterbury.team1000.gardenersgrove.util.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller class for managing friend relationships between users.
 */
@Controller
public class FriendsController {

	private final UserService userService;
	private final FriendRelationshipService friendRelationshipService;

	final Logger logger = LoggerFactory.getLogger(ProfileController.class);


	@Autowired
	public FriendsController(UserService userService, FriendRelationshipService friendRelationshipService) {
		this.userService = userService;
		this.friendRelationshipService = friendRelationshipService;
	}

	/**
	 * Handles POST requests to the /addFriend endpoint.
	 * This adds a new 'pending' relationship to the database between the logged-in user and search result user.
	 *
	 * @param receiver      Email address of the searched user to be sent a friend request
	 * @param searchForm    The search form input to be persisted
	 * @param model         Used to pass through attributes to the view
	 * @return              The same search by email page with the attributes persisted
	 */
	@PostMapping("/addFriend")
	public String postFriendRequest(@RequestParam("receiver") String receiver,
									@RequestParam("emailSearch") String emailSearch,
									@ModelAttribute("searchForm") SearchForm searchForm,
									@ModelAttribute("friendRelationship") FriendRelationship friendRelationship,
									@RequestParam("relationshipStatus") String relationshipStatus,
									Model model) {
		logger.info("POST /addFriend " + receiver);

		logger.info("relationship: " + friendRelationship);
		logger.info("relationship status: " + friendRelationship.getStatus());

		// User email taken from the successful search
		User receiverUser = userService.findEmail(receiver);

		FriendRelationship request = new FriendRelationship(userService.getLoggedInUser(), receiverUser, Status.PENDING);
		friendRelationshipService.addFriendRelationship(request);

		// Get the current state of their relationship and pass it back
		FriendRelationship existingRelationship = friendRelationshipService.getFriendRelationship(userService.getLoggedInUser().getId(), receiverUser.getId());

		model.addAttribute("emailSearch", searchForm.getEmailSearch());
		model.addAttribute("userResult", receiverUser);
		model.addAttribute("searchForm", searchForm);
		model.addAttribute("relationshipStatus", existingRelationship.getStatus().name());
		model.addAttribute("friendRelationship", existingRelationship);

		return "pages/searchByEmailPage";
	}

	/**
	 * Handles GET requests for searching users by email.
	 *
	 * @param searchForm    the SearchForm object containing the search parameters
	 * @param emailSearch   the email address to search for, which is optional and defaults to an empty string if not provided
	 * @param bindingResult the BindingResult object for validation errors
	 * @param model         the Model object to add attributes to be accessed in the view
	 * @return the name of the view template to render
	 */
	@GetMapping("/searchByEmail")
	public String getSearchByEmail( @ModelAttribute("searchForm") SearchForm searchForm,
		@RequestParam(required = false, defaultValue = "") String emailSearch,
		BindingResult bindingResult,Model model) {
		logger.info("GET /searchByEmail");
		User userResult;
		User currentUser = userService.getLoggedInUser();
		String relationshipStatus = null;
		FriendRelationship relationship = null;

		if (!emailSearch.isBlank()) {
			SearchForm.validate(searchForm, bindingResult);
			userResult =  userService.findEmail(emailSearch);

			if (!bindingResult.hasErrors()) {
				if (userResult == null) {
					// No user found
					bindingResult.addError(new FieldError("searchForm", "emailSearch", searchForm.getEmailSearch(), false, null, null, "There is nobody with that email in Gardener’s Grove"));
				} else if (Objects.equals(currentUser.getEmail(), emailSearch)) {
					// User searched for themselves
					bindingResult.addError(new FieldError("searchForm", "emailSearch", searchForm.getEmailSearch(), false, null, null, "You've searched for your own email. Now, let's find some friends!"));
				} else {
					// User search is valid
					// First check if they have received a relationship
					FriendRelationship receivedRelationship = friendRelationshipService.getFriendRelationship(userResult.getId(), currentUser.getId());
					if (receivedRelationship != null) {
						// If they already have a relationship
						relationshipStatus = receivedRelationship.getStatus().name();
						// TODO would be good to pass in the Relationship object
						relationship = receivedRelationship;
					} else {
						// If not, check if they have initiated a relationship
						FriendRelationship sentRelationship = friendRelationshipService.getFriendRelationship(currentUser.getId(), userResult.getId());
						if (sentRelationship != null) {
							// If they already have a relationship
							relationshipStatus = sentRelationship.getStatus().name();
							relationship = sentRelationship;
						}
					}

				}
			}

			if (bindingResult.hasErrors()) {
				return "pages/searchByEmailPage";
			}
		} else {
			userResult = null;
		}
		model.addAttribute("emailSearch", emailSearch);
		model.addAttribute("searchForm", searchForm);
		model.addAttribute("userResult", userResult);
		model.addAttribute("relationshipStatus", relationshipStatus);
		model.addAttribute("friendRelationship", relationship);


		return "pages/searchByEmailPage";
	}

}
