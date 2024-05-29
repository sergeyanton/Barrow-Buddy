package nz.ac.canterbury.team1000.gardenersgrove.controllers;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import nz.ac.canterbury.team1000.gardenersgrove.controller.FriendsController;
import nz.ac.canterbury.team1000.gardenersgrove.controller.GlobalModelAttributeProvider;
import nz.ac.canterbury.team1000.gardenersgrove.entity.FriendRelationship;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.form.GardenForm;
import nz.ac.canterbury.team1000.gardenersgrove.form.SearchFriendsForm;
import nz.ac.canterbury.team1000.gardenersgrove.service.EmailService;
import nz.ac.canterbury.team1000.gardenersgrove.service.FriendRelationshipService;
import nz.ac.canterbury.team1000.gardenersgrove.service.GardenService;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;
import nz.ac.canterbury.team1000.gardenersgrove.util.Status;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
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

	private SearchFriendsForm searchFriendsForm;

	@BeforeEach
	public void BeforeEach() {
		userMock = Mockito.mock(User.class);
		Mockito.when(userMock.getEmail()).thenReturn("johnsmith@gmail.com");
		Mockito.when(userMock.getFullName()).thenReturn("John Smith");
		Mockito.when(userMock.getId()).thenReturn(1L);

		searchFriendsForm = new SearchFriendsForm();

		Mockito.when(userService.getLoggedInUser()).thenReturn(userMock);
		Mockito.when(userService.checkEmail(Mockito.any())).thenReturn(true);
	}

	@Test
	public void SearchFriendsFormGet_EmptySearch_HasNoErrors() throws Exception {
		String searchQuery = "";

		mockMvc.perform(
				MockMvcRequestBuilders.get("/searchFriend").param("search", searchQuery).with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/searchFriendPage"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("search"))
			.andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("searchFriendsForm"))
			.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("users"));
	}

	@Test
	public void SearchFriendsFormGet_NameExists_HasCorrectErrors() throws Exception {
		String searchQuery = "Name That Exists";
		User otherUserMock = Mockito.mock(User.class);
		Mockito.when(otherUserMock.getId()).thenReturn(2L);
		Mockito.when(userService.getUsersByFullName(searchQuery))
			.thenReturn(List.of(otherUserMock));

		mockMvc.perform(
				MockMvcRequestBuilders.get("/searchFriend").param("search", searchQuery).with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/searchFriendPage"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("search"))
			.andExpect(MockMvcResultMatchers.model().attribute("users", List.of(otherUserMock)))
			.andExpect(
				MockMvcResultMatchers.model().attributeHasFieldErrors("searchFriendsForm", "email"))
			.andExpect(MockMvcResultMatchers.model().attributeErrorCount("searchFriendsForm", 1));

		Mockito.verify(userService).getUsersByFullName(searchQuery);
	}

	@Test
	public void SearchFriendsFormGet_EmailExists_HasCorrectErrors() throws Exception {
		String searchQuery = "email@that.exists";
		User otherUserMock = Mockito.mock(User.class);
		Mockito.when(otherUserMock.getId()).thenReturn(2L);
		Mockito.when(userService.findEmail(searchQuery)).thenReturn(otherUserMock);

		mockMvc.perform(
				MockMvcRequestBuilders.get("/searchFriend").param("search", searchQuery).with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/searchFriendPage"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("search"))
			.andExpect(MockMvcResultMatchers.model().attribute("users", List.of(otherUserMock)))
			.andExpect(
				MockMvcResultMatchers.model().attributeHasFieldErrors("searchFriendsForm", "name"))
			.andExpect(MockMvcResultMatchers.model().attributeErrorCount("searchFriendsForm", 1));

		Mockito.verify(userService).findEmail(searchQuery);
	}

	@Test
	public void SearchFriendsFormGet_NameDoesNotExist_HasCorrectErrors() throws Exception {
		String searchQuery = "Name That Doesnt Exist";
		Mockito.when(userService.getUsersByFullName(searchQuery)).thenReturn(new ArrayList<>());

		mockMvc.perform(
				MockMvcRequestBuilders.get("/searchFriend").param("search", searchQuery).with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/searchFriendPage"))
			.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("users"))
			.andExpect(MockMvcResultMatchers.model()
				.attributeHasFieldErrors("searchFriendsForm", "search", "email"))
			.andExpect(MockMvcResultMatchers.model().attributeErrorCount("searchFriendsForm", 2));

		Mockito.verify(userService).getUsersByFullName(searchQuery);
	}

	@Test
	public void SearchFriendsFormGet_EmailDoesNotExist_HasCorrectErrors() throws Exception {
		String searchQuery = "asd@ad.com";
		Mockito.when(userService.findEmail(searchQuery)).thenReturn(null);

		mockMvc.perform(
				MockMvcRequestBuilders.get("/searchFriend").param("search", searchQuery).with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/searchFriendPage"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("search"))
			.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("users"))
			.andExpect(MockMvcResultMatchers.model()
				.attributeHasFieldErrors("searchFriendsForm", "search", "name"))
			.andExpect(MockMvcResultMatchers.model().attributeErrorCount("searchFriendsForm", 2));

		Mockito.verify(userService).findEmail(searchQuery);
	}

	@Test
	public void SearchFriendsFormGet_NameInvalidForm_HasAllErrors() throws Exception {
		String searchQuery = "This name has a % in it";

		mockMvc.perform(
				MockMvcRequestBuilders.get("/searchFriend").param("search", searchQuery).with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/searchFriendPage"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("search"))
			.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("users"))
			.andExpect(MockMvcResultMatchers.model()
				.attributeHasFieldErrors("searchFriendsForm", "search", "name", "email"));

		Mockito.verify(userService, Mockito.never()).findEmail(searchQuery);
		Mockito.verify(userService, Mockito.never()).getUsersByFullName(searchQuery);
	}

	@Test
	public void SearchFriendsFormGet_EmailInvalidForm_HasAllErrors() throws Exception {
		String searchQuery = "@ad.com";
		mockMvc.perform(
				MockMvcRequestBuilders.get("/searchFriend").param("search", searchQuery).with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/searchFriendPage"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("search"))
			.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("users"))
			.andExpect(MockMvcResultMatchers.model()
				.attributeHasFieldErrors("searchFriendsForm", "search", "name", "email"));

		Mockito.verify(userService, Mockito.never()).findEmail(searchQuery);
		Mockito.verify(userService, Mockito.never()).getUsersByFullName(searchQuery);
	}

	@Test
	public void SearchFriendsFormGet_OwnEmail_HasCorrectErrors() throws Exception {
		String searchQuery = userMock.getEmail();
		Mockito.when(userService.findEmail(searchQuery)).thenReturn(userMock);

		mockMvc.perform(
				MockMvcRequestBuilders.get("/searchFriend").param("search", searchQuery).with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/searchFriendPage"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("search"))
			.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("users"))
			.andExpect(MockMvcResultMatchers.model()
				.attributeHasFieldErrors("searchFriendsForm", "search", "name"))
			.andExpect(MockMvcResultMatchers.model().attributeErrorCount("searchFriendsForm", 2));

		Mockito.verify(userService).findEmail(searchQuery);
	}

	@Test
	public void SearchFriendsFormGet_OwnName_HasCorrectErrors() throws Exception {
		String searchQuery = userMock.getFullName();
		Mockito.when(userService.getUsersByFullName(searchQuery)).thenReturn(List.of(userMock));

		mockMvc.perform(
				MockMvcRequestBuilders.get("/searchFriend").param("search", searchQuery).with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/searchFriendPage"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("search"))
			.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("users"))
			.andExpect(MockMvcResultMatchers.model()
				.attributeHasFieldErrors("searchFriendsForm", "search", "email"))
			.andExpect(MockMvcResultMatchers.model().attributeErrorCount("searchFriendsForm", 2));

		Mockito.verify(userService).getUsersByFullName(searchQuery);
	}

	@Test
	public void SearchFriendFormGet_NoFriendRequestSent_DisplaysNoRelationship() throws Exception {
		String searchQuery = "friend@example.com";
		User otherUser = new User("Friend", "User", searchQuery, "Password!456", LocalDate.now(),
			null);
		Mockito.when(userService.findEmail(searchQuery)).thenReturn(otherUser);
		Mockito.when(
				friendRelationshipService.getFriendRelationship(userMock.getId(), otherUser.getId()))
			.thenReturn(null);
		Mockito.when(
				friendRelationshipService.getFriendRelationship(otherUser.getId(), userMock.getId()))
			.thenReturn(null);

		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.get("/searchFriend").param("search", searchQuery).with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/searchFriendPage"))
			.andReturn();
		List<User> usersList = (List<User>) result.getModelAndView().getModel().get("users");
		List<Pair<String, String>> friendStatus = (List<Pair<String, String>>) result.getModelAndView()
			.getModel().get("friendStatus");
		Assertions.assertTrue(usersList.contains(otherUser));
		Assertions.assertEquals((friendStatus.get(usersList.indexOf(otherUser))).getFirst(),
			"None");
	}

	@Test
	public void SearchFriendFormGet_FriendRequestAlreadySent_DisplaysRelationshipPending()
		throws Exception {
		String searchQuery = "friend@example.com";
		User otherUser = new User("Friend", "User", searchQuery, "Password!456", LocalDate.now(),
			null);
		FriendRelationship requestPending = new FriendRelationship(userMock, otherUser,
			Status.PENDING);
		Mockito.when(userService.findEmail(searchQuery)).thenReturn(otherUser);
		Mockito.when(
				friendRelationshipService.getFriendRelationship(userMock.getId(), otherUser.getId()))
			.thenReturn(requestPending);

		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.get("/searchFriend").param("search", searchQuery).with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/searchFriendPage"))
			.andReturn();

		List<User> usersList = (List<User>) result.getModelAndView().getModel().get("users");
		List<Pair<String, String>> friendStatus = (List<Pair<String, String>>) result.getModelAndView()
			.getModel().get("friendStatus");
		Assertions.assertTrue(usersList.contains(otherUser));
		Assertions.assertEquals((friendStatus.get(usersList.indexOf(otherUser))).getFirst(),
			"Sent");
		Assertions.assertEquals((friendStatus.get(usersList.indexOf(otherUser))).getSecond(),
			"PENDING");
	}

	@Test
	public void SearchFriendFormGet_AlreadyFriendsUserSent_DisplaysRelationshipFriends()
		throws Exception {
		String searchQuery = "friend@example.com";
		User friendUser = new User("Friend", "User", searchQuery, "Password!456", LocalDate.now(),
			null);
		FriendRelationship friendship = new FriendRelationship(userMock, friendUser,
			Status.APPROVED);
		Mockito.when(userService.findEmail(searchQuery)).thenReturn(friendUser);
		Mockito.when(
				friendRelationshipService.getFriendRelationship(userMock.getId(), friendUser.getId()))
			.thenReturn(friendship);

		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.get("/searchFriend").param("search", searchQuery).with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/searchFriendPage"))
			.andReturn();

		List<User> usersList = (List<User>) result.getModelAndView().getModel().get("users");
		List<Pair<String, String>> friendStatus = (List<Pair<String, String>>) result.getModelAndView()
			.getModel().get("friendStatus");
		Assertions.assertTrue(usersList.contains(friendUser));
		Assertions.assertEquals((friendStatus.get(usersList.indexOf(friendUser))).getFirst(),
			"Sent");
		Assertions.assertEquals((friendStatus.get(usersList.indexOf(friendUser))).getSecond(),
			"APPROVED");

	}

	@Test
	public void SearchFormGet_AlreadyFriendsUserReceived_DisplaysRelationshipFriends()
		throws Exception {
		String searchQuery = "friend@example.com";
		User friendUser = new User("Friend", "User", searchQuery, "Password!456", LocalDate.now(),
			null);
		FriendRelationship friendship = new FriendRelationship(friendUser, userMock,
			Status.APPROVED);
		Mockito.when(userService.findEmail(searchQuery)).thenReturn(friendUser);
		Mockito.when(
				friendRelationshipService.getFriendRelationship(friendUser.getId(), userMock.getId()))
			.thenReturn(friendship);

		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.get("/searchFriend").param("search", searchQuery).with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/searchFriendPage"))
			.andReturn();

		List<User> usersList = (List<User>) result.getModelAndView().getModel().get("users");
		List<Pair<String, String>> friendStatus = (List<Pair<String, String>>) result.getModelAndView()
			.getModel().get("friendStatus");
		Assertions.assertTrue(usersList.contains(friendUser));
		Assertions.assertEquals((friendStatus.get(usersList.indexOf(friendUser))).getFirst(),
			"Recv");
		Assertions.assertEquals((friendStatus.get(usersList.indexOf(friendUser))).getSecond(),
			"APPROVED");
	}

	@Test
	public void FriendRequestPost_SendsFriendRequestSuccessfully() throws Exception {
		String receiverEmail = "friend@example.com";
		User otherUser = new User("Friend", "User", receiverEmail, "Password!456", LocalDate.now(),
			null);
		FriendRelationship newFriendRequest = new FriendRelationship(userMock, otherUser,
			Status.PENDING);
		newFriendRequest.setId(1L);
		Mockito.when(userService.findEmail(receiverEmail)).thenReturn(otherUser);
		Mockito.when(
				friendRelationshipService.addFriendRelationship(Mockito.any(FriendRelationship.class)))
			.thenReturn(newFriendRequest);
		Mockito.when(
				friendRelationshipService.getFriendRelationship(userMock.getId(), otherUser.getId()))
			.thenReturn(newFriendRequest);

		mockMvc.perform(MockMvcRequestBuilders.post("/addFriend")
				.param("receiver", receiverEmail)
				.param("search", receiverEmail)
				.param("back", "/searchFriendPage")
				.with(csrf()))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/searchFriendPage"));

		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.get("/searchFriend").param("search", receiverEmail).with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn();

		List<User> usersList = (List<User>) result.getModelAndView().getModel().get("users");
		List<Pair<String, String>> friendStatus = (List<Pair<String, String>>) result.getModelAndView()
			.getModel().get("friendStatus");

		Assertions.assertTrue(usersList.contains(otherUser));
		Assertions.assertEquals((friendStatus.get(usersList.indexOf(otherUser))).getFirst(),
			"Sent");
		Assertions.assertEquals((friendStatus.get(usersList.indexOf(otherUser))).getSecond(),
			"PENDING");
	}

}
