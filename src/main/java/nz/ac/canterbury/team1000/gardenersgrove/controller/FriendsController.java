package nz.ac.canterbury.team1000.gardenersgrove.controller;

import static nz.ac.canterbury.team1000.gardenersgrove.util.Status.APPROVED;
import static nz.ac.canterbury.team1000.gardenersgrove.util.Status.DECLINED;
import static nz.ac.canterbury.team1000.gardenersgrove.util.Status.PENDING;

import java.util.ArrayList;
import java.util.List;
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
			} else {
				userResults = new ArrayList<>();
			}
		}

		if (userResults.isEmpty()) {
			if (searchingByName) {
				bindingResult.addError(new FieldError("searchFriendsForm", "search", searchFriendsForm.getSearch(), false, null, null, "There is nobody with that name in Gardener’s Grove"));
			} else if (searchingByEmail) {
				bindingResult.addError(new FieldError("searchFriendsForm", "search", searchFriendsForm.getSearch(), false, null, null, "There is nobody with that email in Gardener’s Grove"));
			}
			return templateString;
		}

		boolean iAmOnlyResult = userResults.size() == 1 && userResults.contains(currentUser);

		if (iAmOnlyResult) {
			if (searchingByName) {
				bindingResult.addError(new FieldError("searchFriendsForm", "search", searchFriendsForm.getSearch(), false, null, null, "There is nobody else with that name in Gardener’s Grove"));
			} else if (searchingByEmail) {
				bindingResult.addError(new FieldError("searchFriendsForm", "search", searchFriendsForm.getSearch(), false, null, null, "You've searched for your own email. Now, let's find some friends!"));
			}
			return templateString;
		}

		// List of Pair, the string can be "None", "Send" or "Recv"
		List<Pair<String, String>> friendStatus = new ArrayList<>();

		for (User user : userResults) {
			FriendRelationship receivedRelationship = friendRelationshipService.getFriendRelationship(user.getId(), currentUser.getId());
			FriendRelationship sentRelationship = friendRelationshipService.getFriendRelationship(currentUser.getId(), user.getId());

			if (receivedRelationship == null && sentRelationship == null) {
				friendStatus.add(Pair.of("None", ""));
				continue;
			}

			if (receivedRelationship != null) {
				friendStatus.add(Pair.of("Recv", receivedRelationship.getStatus().name()));
			}

			if (sentRelationship != null) {
				friendStatus.add(Pair.of("Sent", sentRelationship.getStatus().name()));
			}
		}
		model.addAttribute("friendStatus", friendStatus);
		model.addAttribute("users", userResults);

		return "pages/searchFriendPage";
	}

	/**
	 * Gets the Thymeleaf page representing the /friends page.
	 *
	 * @param model the object to add attributes to be accessed in the view.
	 * @return the name of the view template to render.
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
		return "pages/viewFriendsPage";
	}

	/**
	 * Handles POST requests to send a friend request.
	 *
	 * @param receiver the email address of the user to be sent a friend request.
	 * @param back the URL to redirect back to after sending the friend request.
	 * @return the redirection URL.
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

	/**
	 * Handles POST requests to cancel an outgoing friend request.
	 *
	 * @param receiver the email address of the user to be sent a friend request.
	 * @param back the URL to redirect back to after sending the friend request.
	 * @return the redirection URL.
	 */
	@PostMapping("/cancelFriend")
	public String postCancelFriendRequest(@RequestParam("receiver") String receiver, @RequestParam("back") String back) {
		logger.info("POST /cancelFriend " + receiver);

		User currentUser = userService.getLoggedInUser();

		// User email taken from the successful search
		User receiverUser = userService.findEmail(receiver);

		logger.info("Cancelling friend request " + receiverUser.getEmail());
		friendRelationshipService.cancelFriendRelationship(currentUser.getId(), receiverUser.getId());

		return "redirect:" + back;
	}

	/**
	 * Handles POST requests to decline a friend request.
	 *
	 * @param senderUserId the ID of the user whose friend request is to be declined.
	 * @param model the Model object to add attributes to be accessed in the view.
	 * @return the redirection URL to the friends page.
	 */
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

	/**
	 * Handles POST requests to accept a friend request.
	 *
	 * @param senderUserId the ID of the user whose friend request is to be accepted.
	 * @param model the Model object to add attributes to be accessed in the view.
	 * @return the redirection URL to the friends page.
	 */
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
