package nz.ac.canterbury.team1000.gardenersgrove.controller;

import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.service.FriendRelationshipService;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

// TODO
@Controller
public class ManageFriendsController {

	private final UserService userService;
	private final FriendRelationshipService friendRelationshipService;

	@Autowired
	private AuthenticationManager authenticationManager;

	final Logger logger = LoggerFactory.getLogger(ProfileController.class);


	@Autowired
	public ManageFriendsController(UserService userService, FriendRelationshipService friendRelationshipService) {
		this.userService = userService;
		this.friendRelationshipService = friendRelationshipService;
	}

//	@PostMapping("/addFriend")
//	public String postFriendRequest(@RequestParam("receiver") String receiver) {
//		logger.info("POST /addFriend");
//
//
//		return "pages/searchByEmailPage";
//	}

}
