package nz.ac.canterbury.team1000.gardenersgrove.controller;

import static nz.ac.canterbury.team1000.gardenersgrove.util.Status.APPROVED;
import static nz.ac.canterbury.team1000.gardenersgrove.util.Status.DECLINED;
import static nz.ac.canterbury.team1000.gardenersgrove.util.Status.PENDING;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import nz.ac.canterbury.team1000.gardenersgrove.entity.FriendRelationship;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.form.SearchFriendsForm;
import nz.ac.canterbury.team1000.gardenersgrove.service.FriendRelationshipService;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;
import nz.ac.canterbury.team1000.gardenersgrove.util.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
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
	 * Handles GET requests for searching users by email.
	 *
	 * @param searchFriendsForm    the SearchFriendsForm object containing the search parameters
	 * @param search        the user input that they are searching for, which is optional and defaults to an empty string if not provided
	 * @param bindingResult the BindingResult object for validation errors
	 * @param model         the Model object to add attributes to be accessed in the view
	 * @return the name of the view template to render
	 */
	@GetMapping("/searchFriend")
	public String searchFriend( @ModelAttribute("searchFriendsForm") SearchFriendsForm searchFriendsForm,
		@RequestParam(required = false, defaultValue = "") String search,
		BindingResult bindingResult, Model model) {
		logger.info("GET /searchFriend");
		String templateString = "pages/searchFriendPage";

		List<User> userResults = new ArrayList<>();
		User currentUser = userService.getLoggedInUser();

		model.addAttribute("search", search);

		if (search.isBlank()) {
			return templateString;
		}

		SearchFriendsForm.validate(searchFriendsForm, bindingResult);

		Boolean searchingByEmail = !bindingResult.hasFieldErrors("email");
		Boolean searchingByName = !bindingResult.hasFieldErrors("name");
		if (!searchingByEmail && !searchingByName) {
			bindingResult.addError(new FieldError("searchFriendsForm", "search", searchFriendsForm.getSearch(), false, null, null, "Please enter a valid name or email"));
			return templateString;
		}

		if (searchingByName) {
			userResults = userService.getUsersByFullName(search);
		} else if (searchingByEmail) {
			User userResult = userService.findEmail(search);

			if (userResult != null) {
				userResults = List.of(userResult);
			}
		}

		Boolean noResults = userResults.isEmpty();
		if (noResults && searchingByName) {
			// there are no results and the search was for a name
			bindingResult.addError(new FieldError("searchFriendsForm", "search", searchFriendsForm.getSearch(), false, null, null, "There is nobody with that name in Gardener’s Grove"));
		} else if (noResults && searchingByEmail) {
			bindingResult.addError(new FieldError("searchFriendsForm", "search", searchFriendsForm.getSearch(), false, null, null, "There is nobody with that email in Gardener’s Grove"));
		}

		Boolean iAmOnlyResult = userResults.size() == 1 && userResults.contains(currentUser);
		if (iAmOnlyResult && searchingByName) {
			bindingResult.addError(new FieldError("searchFriendsForm", "search", searchFriendsForm.getSearch(), false, null, null, "There is nobody else with that name in Gardener’s Grove"));
		} else if (iAmOnlyResult && searchingByEmail) {
			bindingResult.addError(new FieldError("searchFriendsForm", "search", searchFriendsForm.getSearch(), false, null, null, "You've searched for your own email. Now, let's find some friends!"));
		}

		if (iAmOnlyResult) {
			return templateString;
		}

		// List of Pair, the string can be "None", "Send" or "Recv"
		List<Pair<String, String>> friendStatus = new ArrayList<>();

		for (User user : userResults) {
			FriendRelationship receivedRelationship = friendRelationshipService.getFriendRelationship(user.getId(), currentUser.getId());
			FriendRelationship sentRelationship = friendRelationshipService.getFriendRelationship(currentUser.getId(), user.getId());
			logger.info("Adding user " + user);

			if (receivedRelationship == null && sentRelationship == null) {
				logger.info("None");
				friendStatus.add(Pair.of("None", ""));
				continue;
			}

			if (receivedRelationship != null) {
				logger.info("Recv");
				friendStatus.add(Pair.of("Recv", receivedRelationship.getStatus().name()));
			}

			if (sentRelationship != null) {
				logger.info("Sent");
				friendStatus.add(Pair.of("Sent", sentRelationship.getStatus().name()));
			}
		}

		model.addAttribute("friendStatus", friendStatus);
		model.addAttribute("users", userResults);

		return "pages/searchFriendPage";
	}

	/**
	 * Gets the thymeleaf page representing the /friends page.
	 * @return thymeleaf viewFriendsPage
	 */
	@GetMapping("/friends")
	public String getFriendsPage(Model model) {
		User loggedInUser = userService.getLoggedInUser();
		// Getting incoming requests
		List<FriendRelationship> incomingRequests = friendRelationshipService.getRelationshipsByReceiverIdAndStatus(loggedInUser.getId(), PENDING);
		List<User> requestingUsers = new ArrayList<>();

		for (FriendRelationship request : incomingRequests) {
			requestingUsers.add(userService.getUserById(request.getSender().getId()));
		}

		// Getting current friends
		List<FriendRelationship> receivedFriends = friendRelationshipService.getRelationshipsByReceiverIdAndStatus(loggedInUser.getId(), Status.APPROVED);
		List<FriendRelationship> sentFriends = friendRelationshipService.getRelationshipsBySenderIdAndStatus(loggedInUser.getId(), Status.APPROVED);

		List<User> newFriends = new ArrayList<>();
		for (FriendRelationship receivedRequest : receivedFriends) {
			newFriends.add(userService.getUserById(receivedRequest.getSender().getId()));
		}
		for (FriendRelationship sentRequest : sentFriends) {
			newFriends.add(userService.getUserById(sentRequest.getReceiver().getId()));
		}

		// Getting outgoing requests
		List<FriendRelationship> outgoingPendingRequests = friendRelationshipService.getRelationshipsBySenderIdAndStatus(loggedInUser.getId(), PENDING);
		List<FriendRelationship> outgoingDeclinedRequests = friendRelationshipService.getRelationshipsBySenderIdAndStatus(loggedInUser.getId(), DECLINED);

		List<User> pendingRequests = new ArrayList<>();
		for (FriendRelationship request : outgoingPendingRequests) {
			pendingRequests.add(userService.getUserById(request.getSender().getId()));
		}

		List<User> declinedRequests = new ArrayList<>();
		for (FriendRelationship request : outgoingDeclinedRequests) {
			declinedRequests.add(userService.getUserById(request.getSender().getId()));
		}

		model.addAttribute("declinedRequests", declinedRequests);
		model.addAttribute("pendingRequests", pendingRequests);
		model.addAttribute("friends", newFriends);
		model.addAttribute("requestingUsers", requestingUsers);
		logger.info("GET /friends");
		return "pages/viewFriendsPage";
	}

//	public String getSearchByEmail( @ModelAttribute("searchForm") SearchForm searchForm,
//		@RequestParam(required = false, defaultValue = "") String emailSearch,
//		BindingResult bindingResult,Model model) {
//		String relationshipStatus = null;
//		String receiverSentPendingRequest = "false";
//
//		if (!emailSearch.isBlank()) {
//			SearchForm.validate(searchForm, bindingResult);
//			userResult =  userService.findEmail(emailSearch);
//
//			if (!bindingResult.hasErrors()) {
//				if (userResult == null) {
//					// No user found
//					bindingResult.addError(new FieldError("searchForm", "emailSearch", searchForm.getEmailSearch(), false, null, null, "There is nobody with that email in Gardener’s Grove"));
//				} else if (Objects.equals(currentUser.getEmail(), emailSearch)) {
//					// User searched for themselves
//					bindingResult.addError(new FieldError("searchForm", "emailSearch", searchForm.getEmailSearch(), false, null, null, "You've searched for your own email. Now, let's find some friends!"));
//				} else {
//					// User search is valid
//					// First check if they have received a relationship
//					FriendRelationship receivedRelationship = friendRelationshipService.getFriendRelationship(userResult.getId(), currentUser.getId());
//					if (receivedRelationship != null) {
//						// If they already have a relationship
//						relationshipStatus = receivedRelationship.getStatus().name();
//						if (relationshipStatus.equals("PENDING")) {
//							receiverSentPendingRequest = "true";
//						}
//					} else {
//						// If not, check if they have initiated a relationship
//						FriendRelationship sentRelationship = friendRelationshipService.getFriendRelationship(currentUser.getId(), userResult.getId());
//						if (sentRelationship != null) {
//							// If they already have a relationship
//							relationshipStatus = sentRelationship.getStatus().name();
//						}
//					}
//
//				}
//			}
//
//			if (bindingResult.hasErrors()) {
//				return "pages/searchByEmailPage";
//			}
//		} else {
//			userResult = null;
//		}
//		model.addAttribute("emailSearch", emailSearch);
//		model.addAttribute("searchForm", searchForm);
//		model.addAttribute("userResult", userResult);
//		model.addAttribute("relationshipStatus", relationshipStatus);
//		model.addAttribute("receiverSentPendingRequest", receiverSentPendingRequest);
//
//		return "pages/searchByEmailPage";
//	}

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
	public String postFriendRequest(@RequestParam("receiver") String receiver, @RequestParam("back") String back) {
		logger.info("POST /addFriend " + receiver);

		User currentUser = userService.getLoggedInUser();

		// User email taken from the successful search
		User receiverUser = userService.findEmail(receiver);

		logger.info("adding friend " + receiverUser.getEmail());
		FriendRelationship existing = friendRelationshipService.getFriendRelationship(currentUser.getId(), receiverUser.getId());
		Boolean notExisting = existing == null;

		if (notExisting) {
			logger.info("Adding new");
			FriendRelationship newRequest = new FriendRelationship(currentUser, receiverUser, PENDING);

			friendRelationshipService.addFriendRelationship(newRequest);
		} else {
			logger.info("Found existing");
		}

		return "redirect:" + back;
	}

	@PostMapping("/declineFriend")
	public String postDeclineFriend(@RequestParam("senderUserId") Long senderUserId,
		Model model) {
		logger.info("POST /declineFriend " + senderUserId);

		User loggedInUser = userService.getLoggedInUser();

		friendRelationshipService.updateFriendRelationship(senderUserId, loggedInUser.getId(), Status.DECLINED);

		List<FriendRelationship> requests = friendRelationshipService.getRelationshipsByReceiverIdAndStatus(loggedInUser.getId(), PENDING);
		List<User> requestingUsers = new ArrayList<>();

		for (FriendRelationship request : requests) {
			requestingUsers.add(userService.getUserById(request.getSender().getId()));
		}

		List<FriendRelationship> receivedFriends = friendRelationshipService.getRelationshipsByReceiverIdAndStatus(loggedInUser.getId(), Status.APPROVED);
		List<FriendRelationship> sentFriends = friendRelationshipService.getRelationshipsBySenderIdAndStatus(loggedInUser.getId(), Status.APPROVED);

		List<User> newFriends = new ArrayList<>();
		for (FriendRelationship receivedRequest : receivedFriends) {
			newFriends.add(userService.getUserById(receivedRequest.getSender().getId()));
		}
		for (FriendRelationship sentRequest : sentFriends) {
			newFriends.add(userService.getUserById(sentRequest.getReceiver().getId()));
		}

		// Getting outgoing requests
		List<FriendRelationship> outgoingPendingRequests = friendRelationshipService.getRelationshipsBySenderIdAndStatus(loggedInUser.getId(), PENDING);
		List<FriendRelationship> outgoingDeclinedRequests = friendRelationshipService.getRelationshipsBySenderIdAndStatus(loggedInUser.getId(), DECLINED);

		List<User> pendingRequests = new ArrayList<>();
		for (FriendRelationship request : outgoingPendingRequests) {
			pendingRequests.add(userService.getUserById(request.getSender().getId()));
		}

		List<User> declinedRequests = new ArrayList<>();
		for (FriendRelationship request : outgoingDeclinedRequests) {
			declinedRequests.add(userService.getUserById(request.getSender().getId()));
		}

		model.addAttribute("declinedRequests", declinedRequests);
		model.addAttribute("pendingRequests", pendingRequests);
		model.addAttribute("friends", newFriends);
		model.addAttribute("requestingUsers", requestingUsers);
		logger.info("GET /friends");
		return "redirect:/friends";
	}

	@PostMapping("/acceptFriend")
	public String postAcceptFriend(@RequestParam("senderUserId") Long senderUserId,
		Model model) {
		logger.info("POST /acceptFriend " + senderUserId);

		User loggedInUser = userService.getLoggedInUser();

		friendRelationshipService.updateFriendRelationship(senderUserId, loggedInUser.getId(), Status.APPROVED);

		// Get pending requests
		List<FriendRelationship> requests = friendRelationshipService.getRelationshipsByReceiverIdAndStatus(loggedInUser.getId(), PENDING);
		List<User> requestingUsers = new ArrayList<>();

		for (FriendRelationship request : requests) {
			requestingUsers.add(userService.getUserById(request.getSender().getId()));
		}

		// Get current list of friends
		List<FriendRelationship> receivedFriends = friendRelationshipService.getRelationshipsByReceiverIdAndStatus(loggedInUser.getId(), Status.APPROVED);
		List<FriendRelationship> sentFriends = friendRelationshipService.getRelationshipsBySenderIdAndStatus(loggedInUser.getId(), Status.APPROVED);

		List<User> newFriends = new ArrayList<>();
		for (FriendRelationship receivedRequest : receivedFriends) {
			newFriends.add(userService.getUserById(receivedRequest.getSender().getId()));
		}
		for (FriendRelationship sentRequest : sentFriends) {
			newFriends.add(userService.getUserById(sentRequest.getReceiver().getId()));


		}

		// Getting outgoing requests
		List<FriendRelationship> outgoingPendingRequests = friendRelationshipService.getRelationshipsBySenderIdAndStatus(loggedInUser.getId(), PENDING);
		List<FriendRelationship> outgoingDeclinedRequests = friendRelationshipService.getRelationshipsBySenderIdAndStatus(loggedInUser.getId(), DECLINED);

		List<User> pendingRequests = new ArrayList<>();
		for (FriendRelationship request : outgoingPendingRequests) {
			pendingRequests.add(userService.getUserById(request.getReceiver().getId()));
		}

		List<User> declinedRequests = new ArrayList<>();
		for (FriendRelationship request : outgoingDeclinedRequests) {
			declinedRequests.add(userService.getUserById(request.getReceiver().getId()));
		}

		model.addAttribute("declinedRequests", declinedRequests);
		model.addAttribute("pendingRequests", pendingRequests);
		model.addAttribute("friends", newFriends);
		model.addAttribute("requestingUsers", requestingUsers);
		logger.info("GET /friends");
		return "redirect:/friends";
	}

}
