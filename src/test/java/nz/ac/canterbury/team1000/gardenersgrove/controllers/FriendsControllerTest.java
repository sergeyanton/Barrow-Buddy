package nz.ac.canterbury.team1000.gardenersgrove.controllers;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import nz.ac.canterbury.team1000.gardenersgrove.controller.FriendsController;
import nz.ac.canterbury.team1000.gardenersgrove.controller.GlobalModelAttributeProvider;
import nz.ac.canterbury.team1000.gardenersgrove.entity.FriendRelationship;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.form.SearchFriendsForm;
import nz.ac.canterbury.team1000.gardenersgrove.service.EmailService;
import nz.ac.canterbury.team1000.gardenersgrove.service.FriendRelationshipService;
import nz.ac.canterbury.team1000.gardenersgrove.service.GardenService;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;
import nz.ac.canterbury.team1000.gardenersgrove.util.Status;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = {FriendsController.class, GlobalModelAttributeProvider.class})
@AutoConfigureMockMvc
@WithMockUser
public class FriendsControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@MockBean
	private FriendRelationshipService friendRelationshipService;

	@MockBean
	private EmailService emailService;

	@MockBean
	private AuthenticationManager authenticationManager;

	@MockBean
	private GardenService gardenService;


	@MockBean
	private PasswordEncoder passwordEncoder;

	@Mock
	private User userMock;

	private User loggedInUser;

	private User friendUser;

	private User otherUser;

	private SearchFriendsForm searchFriendsForm;

	@BeforeEach
	public void BeforeEach() {
		userMock = Mockito.mock(User.class);
		Mockito.when(userMock.getEmail()).thenReturn("johnsmith@gmail.com");
		Mockito.when(userMock.getId()).thenReturn(1L);

		searchFriendsForm = new SearchFriendsForm();
		searchFriendsForm.setSearch(userMock.getEmail());

		Mockito.when(userService.getLoggedInUser()).thenReturn(userMock);
		Mockito.when(userService.checkEmail(Mockito.any())).thenReturn(true);

	}

	@Test
	public void SearchFriendsFormGet_InvalidEmailEmpty_HasFieldErrors() throws Exception {
		String searchQuery = "";
		Mockito.when(userService.findEmail(searchQuery)).thenReturn(null);

		mockMvc.perform(
				MockMvcRequestBuilders.get("/searchFriend").param("search", searchQuery).with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/searchFriendPage"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("search"))
			.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("users"));
	}

	@Test
	public void SearchFriendsFormGet_OwnEmail_HasErrors() throws Exception {
		String searchQuery = userMock.getEmail();
		Mockito.when(userService.findEmail(searchQuery)).thenReturn(null);

		mockMvc.perform(
				MockMvcRequestBuilders.get("/searchFriend").param("search", searchQuery).with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/searchFriendPage"))
			.andExpect(MockMvcResultMatchers.model().attribute("users", Matchers.empty()))
			.andExpect(
				MockMvcResultMatchers.model().attributeHasFieldErrors("searchFriendsForm", "search"));;
		Mockito.verify(userService).findEmail(searchQuery);
	}

	@Test
	public void SearchFriendsFormGet_EmailDoesNotExist_HasErrors() throws Exception {
		String searchQuery = "asd@ad.com";
		Mockito.when(userService.findEmail(searchQuery)).thenReturn(null);

		mockMvc.perform(MockMvcRequestBuilders.get("/searchFriend").param("search", searchQuery).with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/searchFriendPage"))
			.andExpect(MockMvcResultMatchers.model().attribute("users", Matchers.empty()))
			.andExpect(
				MockMvcResultMatchers.model().attributeHasFieldErrors("searchFriendsForm", "search"));;
		Mockito.verify(userService).findEmail(searchQuery);
	}

	@Test
	public void SearchFriendsFormGet_EmailInvalidForm_HasErrors() throws Exception {
		String searchQuery = "@ad.com";
		mockMvc.perform(MockMvcRequestBuilders.get("/searchFriend").param("search", searchQuery).with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/searchFriendPage"))
			.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("users"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("searchFriendsForm","search"));
		Mockito.verify(userService, Mockito.never()).findEmail(searchQuery);
	}

//	@Test
//	public void SearchFormGet_NoFriendRequestSent_DisplaysNoRelationship() throws Exception {
//		String searchQuery = "friend@example.com";
//		User otherUser = new User("Friend", "User", searchQuery, "Password!456", LocalDate.now(), null);
//		Mockito.when(userService.findEmail(searchQuery)).thenReturn(otherUser);
//		Mockito.when(friendRelationshipService.getFriendRelationship(userMock.getId(), otherUser.getId())).thenReturn(null);
//		Mockito.when(friendRelationshipService.getFriendRelationship(otherUser.getId(), userMock.getId())).thenReturn(null);
//
//		mockMvc.perform(MockMvcRequestBuilders.get("/searchByEmail").param("emailSearch", searchQuery).with(csrf()))
//			.andExpect(MockMvcResultMatchers.status().isOk())
//			.andExpect(MockMvcResultMatchers.view().name("pages/searchByEmailPage"))
//			.andExpect(MockMvcResultMatchers.model().attribute("userResult", otherUser))
//			.andExpect(MockMvcResultMatchers.model().attribute("relationshipStatus", Matchers.nullValue()));
//	}

	@Test
	public void SearchFriendsFormGet_NameExist_HasNoErrors() throws Exception {
		String searchQuery = "Name That Exists";
		User otherUserMock = Mockito.mock(User.class);
		Mockito.when(otherUserMock.getId()).thenReturn(2L);
		Mockito.when(userService.getUsersByFullName(searchQuery)).thenReturn(List.of(otherUserMock));

		mockMvc.perform(MockMvcRequestBuilders.get("/searchFriend").param("search", searchQuery).with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/searchFriendPage"))
			.andExpect(MockMvcResultMatchers.model().attribute("users", List.of(otherUserMock)))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("searchFriendsForm", "email"))
			.andExpect(MockMvcResultMatchers.model().attributeErrorCount("searchFriendsForm", 1));

		Mockito.verify(userService).getUsersByFullName(searchQuery);
	}

	@Test
	public void SearchFriendsFormGet_NameInvalidForm_HasErrors() throws Exception {
		String searchQuery = "This name has a % in it";

		mockMvc.perform(MockMvcRequestBuilders.get("/searchFriend").param("search", searchQuery).with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/searchFriendPage"))
			.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("users"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("searchFriendsForm","search"));
		Mockito.verify(userService, Mockito.never()).findEmail(searchQuery);
	}

	@Test
	public void SearchFriendsFormGet_NameDoesNotExist_HasErrors() throws Exception {
		String searchQuery = "Name That Doesnt Exist";
		Mockito.when(userService.getUsersByFullName(searchQuery)).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.get("/searchFriend").param("search", searchQuery).with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/searchFriendPage"))
			.andExpect(MockMvcResultMatchers.model().attribute("users", Matchers.empty()))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("searchFriendsForm","search"));

		Mockito.verify(userService).getUsersByFullName(searchQuery);
	}


//	@Test
//	public void SearchFormGet_FriendRequestAlreadySent_DisplaysRelationshipPending() throws Exception {
//		String searchQuery = "friend@example.com";
//		User otherUser = new User("Friend", "User", searchQuery, "Password!456", LocalDate.now(), null);
//		FriendRelationship requestPending = new FriendRelationship(userMock, otherUser, Status.PENDING);
//		Mockito.when(userService.findEmail(searchQuery)).thenReturn(otherUser);
//		Mockito.when(friendRelationshipService.getFriendRelationship(userMock.getId(), otherUser.getId())).thenReturn(requestPending);
//
//		mockMvc.perform(MockMvcRequestBuilders.get("/searchByEmail").param("emailSearch", searchQuery).with(csrf()))
//			.andExpect(MockMvcResultMatchers.status().isOk())
//			.andExpect(MockMvcResultMatchers.view().name("pages/searchByEmailPage"))
//			.andExpect(MockMvcResultMatchers.model().attribute("userResult", otherUser))
//			.andExpect(MockMvcResultMatchers.model().attribute("relationshipStatus", "PENDING"))
//			.andExpect(MockMvcResultMatchers.model().attribute("receiverSentPendingRequest", "false"));
//	}
//
//	@Test
//	public void SearchFormGet_AlreadyFriendsUserSent_DisplaysRelationshipFriends() throws Exception {
//		String searchQuery = "friend@example.com";
//		User friendUser = new User("Friend", "User", searchQuery, "Password!456", LocalDate.now(), null);
//		FriendRelationship friendship = new FriendRelationship(userMock, friendUser, Status.APPROVED);
//		Mockito.when(userService.findEmail(searchQuery)).thenReturn(friendUser);
//		Mockito.when(friendRelationshipService.getFriendRelationship(userMock.getId(), friendUser.getId())).thenReturn(friendship);
//
//		mockMvc.perform(MockMvcRequestBuilders.get("/searchFriend").param("search", searchQuery).with(csrf()))
//			.andExpect(MockMvcResultMatchers.status().isOk())
//			.andExpect(MockMvcResultMatchers.view().name("pages/searchFriendPage"));
//	}
//
//	@Test
//	public void SearchFormGet_AlreadyFriendsUserReceived_DisplaysRelationshipFriends() throws Exception {
//		String searchQuery = "friend@example.com";
//		User friendUser = new User("Friend", "User", searchQuery, "Password!456", LocalDate.now(), null);
//		FriendRelationship friendship = new FriendRelationship(friendUser, userMock, Status.APPROVED);
//		Mockito.when(userService.findEmail(searchQuery)).thenReturn(friendUser);
//		Mockito.when(friendRelationshipService.getFriendRelationship(friendUser.getId(), userMock.getId())).thenReturn(friendship);
//
//		mockMvc.perform(MockMvcRequestBuilders.get("/searchByEmail").param("emailSearch", searchQuery).with(csrf()))
//			.andExpect(MockMvcResultMatchers.status().isOk())
//			.andExpect(MockMvcResultMatchers.view().name("pages/searchByEmailPage"))
//			.andExpect(MockMvcResultMatchers.model().attribute("userResult", friendUser))
//			.andExpect(MockMvcResultMatchers.model().attribute("relationshipStatus", "APPROVED"))
//			.andExpect(MockMvcResultMatchers.model().attribute("receiverSentPendingRequest", "false"));
//	}
//
//	@Test
//	public void FriendRequestPost_SendsFriendRequestSuccessfully() throws Exception {
//		String receiverEmail = "friend@example.com";
//		User otherUser = new User("Friend", "User", receiverEmail, "Password!456", LocalDate.now(), null);
//		FriendRelationship newFriendRequest = new FriendRelationship(userMock, otherUser, Status.PENDING);
//		newFriendRequest.setId(1L);
//		Mockito.when(userService.findEmail(receiverEmail)).thenReturn(otherUser);
//		Mockito.when(friendRelationshipService.addFriendRelationship(Mockito.any(FriendRelationship.class)))
//			.thenReturn(newFriendRequest);
//		Mockito.when(friendRelationshipService.getFriendRelationship(userMock.getId(), otherUser.getId()))
//			.thenReturn(newFriendRequest);
//
//		mockMvc.perform(MockMvcRequestBuilders.post("/addFriend")
//				.param("receiver", receiverEmail)
//				.param("emailSearch", receiverEmail)
//				.param("relationshipStatus", "")
//				.param("receiverSentPendingRequest", "false").with(csrf()))
//			.andExpect(MockMvcResultMatchers.status().isOk())
//			.andExpect(MockMvcResultMatchers.view().name("pages/searchByEmailPage"))
//			.andExpect(MockMvcResultMatchers.model().attribute("userResult", otherUser))
//			.andExpect(MockMvcResultMatchers.model().attribute("relationshipStatus", "PENDING"))
//			.andExpect(MockMvcResultMatchers.model().attribute("receiverSentPendingRequest", "false"));
//		Mockito.verify(friendRelationshipService).addFriendRelationship(Mockito.any(FriendRelationship.class));
//	}

}
