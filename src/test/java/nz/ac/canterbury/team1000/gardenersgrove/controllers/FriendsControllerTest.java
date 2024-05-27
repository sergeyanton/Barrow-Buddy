package nz.ac.canterbury.team1000.gardenersgrove.controllers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import nz.ac.canterbury.team1000.gardenersgrove.controller.FriendsController;
import nz.ac.canterbury.team1000.gardenersgrove.controller.GlobalModelAttributeProvider;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.form.SearchForm;
import nz.ac.canterbury.team1000.gardenersgrove.service.EmailService;
import nz.ac.canterbury.team1000.gardenersgrove.service.FriendRelationshipService;
import nz.ac.canterbury.team1000.gardenersgrove.service.GardenService;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;
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
	public void SearchFormGet_NoRequestSent_DisplaysNoRelationship() throws Exception {

	}
	@Test
	public void SearchFormGet_FriendRequestAlreadySent_DisplaysRelationshipPending() throws Exception {

	}

	@Test
	public void SearchFormGet_AlreadyFriendsUserSent_DisplaysRelationshipFriends() throws Exception {

	}

	@Test
	public void SearchFormGet_AlreadyFriendsUserReceived_DisplaysRelationshipFriends() throws Exception {

	}




}
