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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

	@Autowired
	private AuthenticationManager authenticationManager;

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
									@RequestParam("relationshipStatus") String relationshipStatus,
									Model model) {
		logger.info("POST /addFriend " + receiver);

		User receiverUser = userService.findEmail(receiver);

		FriendRelationship request = new FriendRelationship(userService.getLoggedInUser(), receiverUser, Status.PENDING);
		friendRelationshipService.addFriendRelationship(request);

		// Get the current state of their relationship and pass it back
		FriendRelationship existingRelationship = friendRelationshipService.getFriendRelationship(userService.getLoggedInUser().getId(), receiverUser.getId());

		model.addAttribute("emailSearch", searchForm.getEmailSearch());
		model.addAttribute("userResult", receiverUser);
		model.addAttribute("searchForm", searchForm);
		model.addAttribute("relationshipStatus", existingRelationship.getStatus().name());

		return "pages/searchByEmailPage";
	}

}
