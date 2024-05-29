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
import nz.ac.canterbury.team1000.gardenersgrove.form.GardenForm;
import nz.ac.canterbury.team1000.gardenersgrove.form.SearchFriendsForm;
import nz.ac.canterbury.team1000.gardenersgrove.service.EmailService;
import nz.ac.canterbury.team1000.gardenersgrove.service.FriendRelationshipService;
import nz.ac.canterbury.team1000.gardenersgrove.service.GardenService;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;
import nz.ac.canterbury.team1000.gardenersgrove.util.Status;
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
		Mockito.when(userService.getUsersByFullName(searchQuery)).thenReturn(List.of(otherUserMock));

		mockMvc.perform(MockMvcRequestBuilders.get("/searchFriend").param("search", searchQuery).with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/searchFriendPage"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("search"))
			.andExpect(MockMvcResultMatchers.model().attribute("users", List.of(otherUserMock)))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("searchFriendsForm", "email"))
			.andExpect(MockMvcResultMatchers.model().attributeErrorCount("searchFriendsForm", 1));

		Mockito.verify(userService).getUsersByFullName(searchQuery);
	}

	@Test
	public void SearchFriendsFormGet_EmailExists_HasCorrectErrors() throws Exception {
		String searchQuery = "email@that.exists";
		User otherUserMock = Mockito.mock(User.class);
		Mockito.when(otherUserMock.getId()).thenReturn(2L);
		Mockito.when(userService.findEmail(searchQuery)).thenReturn(otherUserMock);

		mockMvc.perform(MockMvcRequestBuilders.get("/searchFriend").param("search", searchQuery).with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/searchFriendPage"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("search"))
			.andExpect(MockMvcResultMatchers.model().attribute("users", List.of(otherUserMock)))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("searchFriendsForm", "name"))
			.andExpect(MockMvcResultMatchers.model().attributeErrorCount("searchFriendsForm", 1));

		Mockito.verify(userService).findEmail(searchQuery);
	}

	@Test
	public void SearchFriendsFormGet_NameDoesNotExist_HasCorrectErrors() throws Exception {
		String searchQuery = "Name That Doesnt Exist";
		Mockito.when(userService.getUsersByFullName(searchQuery)).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.get("/searchFriend").param("search", searchQuery).with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/searchFriendPage"))
			.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("users"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("searchFriendsForm","search", "email"))
			.andExpect(MockMvcResultMatchers.model().attributeErrorCount("searchFriendsForm", 2));

		Mockito.verify(userService).getUsersByFullName(searchQuery);
	}

	@Test
	public void SearchFriendsFormGet_EmailDoesNotExist_HasCorrectErrors() throws Exception {
		String searchQuery = "asd@ad.com";
		Mockito.when(userService.findEmail(searchQuery)).thenReturn(null);

		mockMvc.perform(MockMvcRequestBuilders.get("/searchFriend").param("search", searchQuery).with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/searchFriendPage"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("search"))
			.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("users"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("searchFriendsForm", "search", "name"))
			.andExpect(MockMvcResultMatchers.model().attributeErrorCount("searchFriendsForm", 2));

		Mockito.verify(userService).findEmail(searchQuery);
	}

	@Test
	public void SearchFriendsFormGet_NameInvalidForm_HasAllErrors() throws Exception {
		String searchQuery = "This name has a % in it";

		mockMvc.perform(MockMvcRequestBuilders.get("/searchFriend").param("search", searchQuery).with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/searchFriendPage"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("search"))
			.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("users"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("searchFriendsForm","search", "name", "email"));

		Mockito.verify(userService, Mockito.never()).findEmail(searchQuery);
		Mockito.verify(userService, Mockito.never()).getUsersByFullName(searchQuery);
	}

	@Test
	public void SearchFriendsFormGet_EmailInvalidForm_HasAllErrors() throws Exception {
		String searchQuery = "@ad.com";
		mockMvc.perform(MockMvcRequestBuilders.get("/searchFriend").param("search", searchQuery).with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/searchFriendPage"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("search"))
			.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("users"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("searchFriendsForm","search", "name", "email"));

		Mockito.verify(userService, Mockito.never()).findEmail(searchQuery);
		Mockito.verify(userService, Mockito.never()).getUsersByFullName(searchQuery);
	}

	@Test
	public void SearchFriendsFormGet_OwnEmail_HasCorrectErrors() throws Exception {
		String searchQuery = userMock.getEmail();
		Mockito.when(userService.findEmail(searchQuery)).thenReturn(userMock);

		mockMvc.perform(MockMvcRequestBuilders.get("/searchFriend").param("search", searchQuery).with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/searchFriendPage"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("search"))
			.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("users"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("searchFriendsForm", "search", "name"))
			.andExpect(MockMvcResultMatchers.model().attributeErrorCount("searchFriendsForm", 2));

		Mockito.verify(userService).findEmail(searchQuery);
	}

	@Test
	public void SearchFriendsFormGet_OwnName_HasCorrectErrors() throws Exception {
		String searchQuery = userMock.getFullName();
		Mockito.when(userService.getUsersByFullName(searchQuery)).thenReturn(List.of(userMock));

		mockMvc.perform(MockMvcRequestBuilders.get("/searchFriend").param("search", searchQuery).with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/searchFriendPage"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("search"))
			.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("users"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("searchFriendsForm", "search", "email"))
			.andExpect(MockMvcResultMatchers.model().attributeErrorCount("searchFriendsForm", 2));

		Mockito.verify(userService).getUsersByFullName(searchQuery);
	}

	@Test
	public void CancelFriendRequestPost_CancelsSuccessfully() throws Exception {
		String receiverEmail = "myfriend@example.com";
		otherUser = new User("Friend", "User", receiverEmail, "Password!456", LocalDate.now(), null);
		Mockito.when(userService.findEmail(receiverEmail)).thenReturn(otherUser);

		mockMvc.perform(MockMvcRequestBuilders.post("/cancelFriend")
				.param("receiver", receiverEmail).with(csrf()))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.redirectedUrl("/friends"));

		Mockito.verify(friendRelationshipService).cancelFriendRelationship(userMock.getId(), otherUser.getId());
	}
}
