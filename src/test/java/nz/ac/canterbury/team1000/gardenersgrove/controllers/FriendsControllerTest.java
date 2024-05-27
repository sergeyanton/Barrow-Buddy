package nz.ac.canterbury.team1000.gardenersgrove.controllers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.time.LocalDate;
import nz.ac.canterbury.team1000.gardenersgrove.controller.FriendsController;
import nz.ac.canterbury.team1000.gardenersgrove.controller.GlobalModelAttributeProvider;
import nz.ac.canterbury.team1000.gardenersgrove.entity.FriendRelationship;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.form.SearchForm;
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

	private SearchForm searchForm;

	@BeforeEach
	public void BeforeEach() {
		userMock = Mockito.mock(User.class);
		Mockito.when(userMock.getEmail()).thenReturn("johnsmith@gmail.com");

		searchForm = new SearchForm();
		searchForm.setEmailSearch(userMock.getEmail());

		Mockito.when(userService.getLoggedInUser()).thenReturn(userMock);
		Mockito.when(userService.checkEmail(Mockito.any())).thenReturn(true);
	}

	@Test
	public void SearchFormGet_InvalidEmailEmpty_HasFieldErrors() throws Exception {
		String searchQuery = "";
		Mockito.when(userService.findEmail(searchQuery)).thenReturn(null);

		mockMvc.perform(
				MockMvcRequestBuilders.get("/searchByEmail").param("emailSearch", searchQuery).with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/searchByEmailPage"))
			.andExpect(MockMvcResultMatchers.model().attribute("userResult", Matchers.nullValue()));
	}

	@Test
	public void SearchFormGet_OwnEmail_HasErrors() throws Exception {
		String searchQuery = userMock.getEmail();
		Mockito.when(userService.findEmail(searchQuery)).thenReturn(userMock);

		mockMvc.perform(MockMvcRequestBuilders.get("/searchByEmail").param("emailSearch", searchQuery).with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/searchByEmailPage"))
			.andExpect(MockMvcResultMatchers.model().attribute("userResult", Matchers.nullValue()))
			.andExpect(
				MockMvcResultMatchers.model().attributeHasFieldErrors("searchForm", "emailSearch"));;
		Mockito.verify(userService).findEmail(searchQuery);
	}

	@Test
	public void SearchFormGet_EmailDoesNotExist_HasErrors() throws Exception {
		String searchQuery = "asd@ad.com";
		Mockito.when(userService.findEmail(searchQuery)).thenReturn(null);

		mockMvc.perform(MockMvcRequestBuilders.get("/searchByEmail").param("emailSearch", searchQuery).with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/searchByEmailPage"))
			.andExpect(MockMvcResultMatchers.model().attribute("userResult", Matchers.nullValue()))
			.andExpect(
				MockMvcResultMatchers.model().attributeHasFieldErrors("searchForm", "emailSearch"));;
		Mockito.verify(userService).findEmail(searchQuery);
	}

	@Test
	public void SearchFormGet_EmailExist_HasNoErrors() throws Exception {
		String searchQuery = "asd@ad.com";
		Mockito.when(userService.findEmail(searchQuery)).thenReturn(userMock);

		mockMvc.perform(MockMvcRequestBuilders.get("/searchByEmail").param("emailSearch", searchQuery).with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/searchByEmailPage"))
			.andExpect(MockMvcResultMatchers.model().attribute("userResult", userMock))
			.andExpect(
				MockMvcResultMatchers.model().attributeHasNoErrors("searchForm"));;
		Mockito.verify(userService).findEmail(searchQuery);
	}

	@Test
	public void SearchFormGet_EmailInvalidForm_HasErrors() throws Exception {
		String searchQuery = "@ad.com";
		Mockito.when(userService.findEmail(searchQuery)).thenReturn(null);

		mockMvc.perform(MockMvcRequestBuilders.get("/searchByEmail").param("emailSearch", searchQuery).with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/searchByEmailPage"))
			.andExpect(MockMvcResultMatchers.model().attribute("userResult", Matchers.nullValue()))
			.andExpect(
				MockMvcResultMatchers.model().attributeHasFieldErrors("searchForm","emailSearch"));;
		Mockito.verify(userService).findEmail(searchQuery);
	}

	@Test
	public void SearchFormGet_NoFriendRequestSent_DisplaysNoRelationship() throws Exception {
		String searchQuery = "friend@example.com";
		User otherUser = new User("Friend", "User", searchQuery, "Password!456", LocalDate.now(), null);
		Mockito.when(userService.findEmail(searchQuery)).thenReturn(otherUser);
		Mockito.when(friendRelationshipService.getFriendRelationship(userMock.getId(), otherUser.getId())).thenReturn(null);
		Mockito.when(friendRelationshipService.getFriendRelationship(otherUser.getId(), userMock.getId())).thenReturn(null);

		mockMvc.perform(MockMvcRequestBuilders.get("/searchByEmail").param("emailSearch", searchQuery).with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/searchByEmailPage"))
			.andExpect(MockMvcResultMatchers.model().attribute("userResult", otherUser))
			.andExpect(MockMvcResultMatchers.model().attribute("relationshipStatus", Matchers.nullValue()));
	}

	@Test
	public void SearchFormGet_FriendRequestAlreadySent_DisplaysRelationshipPending() throws Exception {
		String searchQuery = "friend@example.com";
		User otherUser = new User("Friend", "User", searchQuery, "Password!456", LocalDate.now(), null);
		FriendRelationship requestPending = new FriendRelationship(userMock, otherUser, Status.PENDING);
		Mockito.when(userService.findEmail(searchQuery)).thenReturn(otherUser);
		Mockito.when(friendRelationshipService.getFriendRelationship(userMock.getId(), otherUser.getId())).thenReturn(requestPending);

		mockMvc.perform(MockMvcRequestBuilders.get("/searchByEmail").param("emailSearch", searchQuery).with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/searchByEmailPage"))
			.andExpect(MockMvcResultMatchers.model().attribute("userResult", otherUser))
			.andExpect(MockMvcResultMatchers.model().attribute("relationshipStatus", "PENDING"))
			.andExpect(MockMvcResultMatchers.model().attribute("receiverSentPendingRequest", "false"));
	}

	@Test
	public void SearchFormGet_AlreadyFriendsUserSent_DisplaysRelationshipFriends() throws Exception {
		String searchQuery = "friend@example.com";
		User friendUser = new User("Friend", "User", searchQuery, "Password!456", LocalDate.now(), null);
		FriendRelationship friendship = new FriendRelationship(userMock, friendUser, Status.APPROVED);
		Mockito.when(userService.findEmail(searchQuery)).thenReturn(friendUser);
		Mockito.when(friendRelationshipService.getFriendRelationship(userMock.getId(), friendUser.getId())).thenReturn(friendship);

		mockMvc.perform(MockMvcRequestBuilders.get("/searchByEmail").param("emailSearch", searchQuery).with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/searchByEmailPage"))
			.andExpect(MockMvcResultMatchers.model().attribute("userResult", friendUser))
			.andExpect(MockMvcResultMatchers.model().attribute("relationshipStatus", "APPROVED"))
			.andExpect(MockMvcResultMatchers.model().attribute("receiverSentPendingRequest", "false"));
	}

	@Test
	public void SearchFormGet_AlreadyFriendsUserReceived_DisplaysRelationshipFriends() throws Exception {
		String searchQuery = "friend@example.com";
		User friendUser = new User("Friend", "User", searchQuery, "Password!456", LocalDate.now(), null);
		FriendRelationship friendship = new FriendRelationship(friendUser, userMock, Status.APPROVED);
		Mockito.when(userService.findEmail(searchQuery)).thenReturn(friendUser);
		Mockito.when(friendRelationshipService.getFriendRelationship(friendUser.getId(), userMock.getId())).thenReturn(friendship);

		mockMvc.perform(MockMvcRequestBuilders.get("/searchByEmail").param("emailSearch", searchQuery).with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/searchByEmailPage"))
			.andExpect(MockMvcResultMatchers.model().attribute("userResult", friendUser))
			.andExpect(MockMvcResultMatchers.model().attribute("relationshipStatus", "APPROVED"))
			.andExpect(MockMvcResultMatchers.model().attribute("receiverSentPendingRequest", "false"));
	}

	@Test
	public void FriendRequestPost_SendsFriendRequestSuccessfully() throws Exception {
		String receiverEmail = "friend@example.com";
		User otherUser = new User("Friend", "User", receiverEmail, "Password!456", LocalDate.now(), null);
		FriendRelationship newFriendRequest = new FriendRelationship(userMock, otherUser, Status.PENDING);
		newFriendRequest.setId(1L);
		Mockito.when(userService.findEmail(receiverEmail)).thenReturn(otherUser);
		Mockito.when(friendRelationshipService.addFriendRelationship(Mockito.any(FriendRelationship.class)))
			.thenReturn(newFriendRequest);
		Mockito.when(friendRelationshipService.getFriendRelationship(userMock.getId(), otherUser.getId()))
			.thenReturn(newFriendRequest);

		mockMvc.perform(MockMvcRequestBuilders.post("/addFriend")
				.param("receiver", receiverEmail)
				.param("emailSearch", receiverEmail)
				.param("relationshipStatus", "")
				.param("receiverSentPendingRequest", "false").with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/searchByEmailPage"))
			.andExpect(MockMvcResultMatchers.model().attribute("userResult", otherUser))
			.andExpect(MockMvcResultMatchers.model().attribute("relationshipStatus", "PENDING"))
			.andExpect(MockMvcResultMatchers.model().attribute("receiverSentPendingRequest", "false"));
		Mockito.verify(friendRelationshipService).addFriendRelationship(Mockito.any(FriendRelationship.class));
	}

}
