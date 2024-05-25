package nz.ac.canterbury.team1000.gardenersgrove.controllers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import nz.ac.canterbury.team1000.gardenersgrove.controller.GlobalModelAttributeProvider;
import nz.ac.canterbury.team1000.gardenersgrove.controller.ProfileController;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.form.EditUserForm;
import nz.ac.canterbury.team1000.gardenersgrove.form.PictureForm;
import nz.ac.canterbury.team1000.gardenersgrove.form.SearchForm;
import nz.ac.canterbury.team1000.gardenersgrove.form.UpdatePasswordForm;
import nz.ac.canterbury.team1000.gardenersgrove.service.EmailService;
import nz.ac.canterbury.team1000.gardenersgrove.service.VerificationTokenService;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import nz.ac.canterbury.team1000.gardenersgrove.service.GardenService;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;

import java.time.LocalDate;

@WebMvcTest(controllers = {ProfileController.class, GlobalModelAttributeProvider.class})
@AutoConfigureMockMvc
@WithMockUser
public class ProfileControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

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
	private PictureForm profilePictureForm;

	private EditUserForm editUserForm;
	private SearchForm searchForm;
	private UpdatePasswordForm updatePasswordForm;

	@BeforeEach
	public void BeforeEach() {
		userMock = Mockito.mock(User.class);
		Mockito.when(userMock.getFname()).thenReturn("John");
		Mockito.when(userMock.getLname()).thenReturn("Smith");
		Mockito.when(userMock.getEmail()).thenReturn("johnsmith@gmail.com");
		Mockito.when(userMock.getDateOfBirth()).thenReturn(LocalDate.of(1999, 5, 5));
		Mockito.when(userMock.getDateOfBirthString()).thenReturn("05/05/1999");
		Mockito.when(userMock.getPassword()).thenReturn("encoded_password");
		Mockito.when(userMock.getPicturePath()).thenReturn("/uploads/example.png");

		profilePictureForm = new PictureForm();
		profilePictureForm.setPictureFile(new MockMultipartFile("pictureFile", new byte[0]));

		editUserForm = new EditUserForm();
		editUserForm.setFirstName(userMock.getFname());
		editUserForm.setLastName(userMock.getLname());
		editUserForm.setNoSurnameCheckBox(
			userMock.getLname() == null || userMock.getLname().isEmpty());
		editUserForm.setEmail(userMock.getEmail());
		editUserForm.setDob(userMock.getDateOfBirthString());
		editUserForm.setPicturePath(userMock.getPicturePath());
		editUserForm.setPictureFile(new MockMultipartFile("pictureFile", new byte[0]));

		updatePasswordForm = new UpdatePasswordForm();
		updatePasswordForm.setPassword("Pass123$");
		updatePasswordForm.setNewPassword("Pass123$");
		updatePasswordForm.setRetypeNewPassword("Pass123$");

		searchForm = new SearchForm();
		searchForm.setEmail(userMock.getEmail());

		Mockito.when(userService.getLoggedInUser()).thenReturn(userMock);
		Mockito.when(userService.checkEmail(Mockito.any())).thenReturn(true);
		Mockito.when(passwordEncoder.matches(Mockito.any(), Mockito.any())).thenReturn(true);
	}

	@Test
	public void ProfilePost_WithNoChange_SavesToService() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/profile").with(csrf())
				.flashAttr("profilePictureForm", profilePictureForm))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.redirectedUrl("/profile"));

		Mockito.verify(userService).updateUserByEmail(Mockito.any(), Mockito.any());
		Mockito.verify(userService).authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
	}

	@Test
	public void ProfilePost_WithValidPng_SavesToService() throws Exception {
		profilePictureForm.setPictureFile(new MockMultipartFile(
			"pictureFile", "newPfp.png", "image/png", "file contents".getBytes()));

		mockMvc.perform(MockMvcRequestBuilders.post("/profile").with(csrf())
				.flashAttr("profilePictureForm", profilePictureForm))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.redirectedUrl("/profile"));

		Mockito.verify(userService).updateUserByEmail(Mockito.any(), Mockito.any());
		Mockito.verify(userService).authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
	}

	@Test
	public void ProfilePost_WithValidJpeg_SavesToService() throws Exception {
		profilePictureForm.setPictureFile(new MockMultipartFile(
			"pictureFile", "newPfp.jpeg", "image/jpeg", "file contents".getBytes()));

		mockMvc.perform(MockMvcRequestBuilders.post("/profile").with(csrf())
				.flashAttr("profilePictureForm", profilePictureForm))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.redirectedUrl("/profile"));

		Mockito.verify(userService).updateUserByEmail(Mockito.any(), Mockito.any());
		Mockito.verify(userService).authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
	}

	@Test
	public void ProfilePost_WithValidSvg_SavesToService() throws Exception {
		profilePictureForm.setPictureFile(new MockMultipartFile(
			"pictureFile", "newPfp.svg", "image/svg+xml", "file contents".getBytes()));

		mockMvc.perform(MockMvcRequestBuilders.post("/profile").with(csrf())
				.flashAttr("profilePictureForm", profilePictureForm))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.redirectedUrl("/profile"));

		Mockito.verify(userService).updateUserByEmail(Mockito.any(), Mockito.any());
		Mockito.verify(userService).authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
	}

	@Test
	public void ProfilePost_WithInvalidFileType_HasFieldErrors() throws Exception {
		profilePictureForm.setPictureFile(new MockMultipartFile(
			"pictureFile", "newPfp.webp", "image/webp", "file contents".getBytes()));
		mockMvc.perform(MockMvcRequestBuilders.post("/profile").with(csrf())
				.flashAttr("profilePictureForm", profilePictureForm))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attribute("fName", userMock.getFname()))
			.andExpect(MockMvcResultMatchers.model().attribute("lName", userMock.getLname()))
			.andExpect(MockMvcResultMatchers.model().attribute("email", userMock.getEmail()))
			.andExpect(
				MockMvcResultMatchers.model().attribute("dob", userMock.getDateOfBirthString()))
			.andExpect(
				MockMvcResultMatchers.model().attribute("picturePath", userMock.getPicturePath()))
			.andExpect(MockMvcResultMatchers.view().name("pages/profilePage"))
			.andExpect(MockMvcResultMatchers.model()
				.attributeHasFieldErrors("profilePictureForm", "pictureFile"));

		Mockito.verify(userService, Mockito.never())
			.updateUserByEmail(Mockito.any(), Mockito.any());
		Mockito.verify(userService, Mockito.never())
			.authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
	}

	@Test
	public void ProfilePost_WithTooBigFile_HasFieldErrors() throws Exception {
		byte[] over10mb = new byte[10 * 1024 * 1024 + 1];
		profilePictureForm.setPictureFile(new MockMultipartFile(
			"pictureFile", "newPfp.png", "image/png", over10mb));

		mockMvc.perform(MockMvcRequestBuilders.post("/profile").with(csrf())
				.flashAttr("profilePictureForm", profilePictureForm))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attribute("fName", userMock.getFname()))
			.andExpect(MockMvcResultMatchers.model().attribute("lName", userMock.getLname()))
			.andExpect(MockMvcResultMatchers.model().attribute("email", userMock.getEmail()))
			.andExpect(
				MockMvcResultMatchers.model().attribute("dob", userMock.getDateOfBirthString()))
			.andExpect(
				MockMvcResultMatchers.model().attribute("picturePath", userMock.getPicturePath()))
			.andExpect(MockMvcResultMatchers.view().name("pages/profilePage"))
			.andExpect(MockMvcResultMatchers.model()
				.attributeHasFieldErrors("profilePictureForm", "pictureFile"));

		Mockito.verify(userService, Mockito.never())
			.updateUserByEmail(Mockito.any(), Mockito.any());
		Mockito.verify(userService, Mockito.never())
			.authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
	}

	@Test
	public void ProfileGet_Valid_IsPopulated() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/profile").with(csrf())
				.flashAttr("profilePictureForm", profilePictureForm))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attribute("fName", userMock.getFname()))
			.andExpect(MockMvcResultMatchers.model().attribute("lName", userMock.getLname()))
			.andExpect(MockMvcResultMatchers.model().attribute("email", userMock.getEmail()))
			.andExpect(
				MockMvcResultMatchers.model().attribute("dob", userMock.getDateOfBirthString()))
			.andExpect(
				MockMvcResultMatchers.model().attribute("picturePath", userMock.getPicturePath()));
		// This is called twice because of the global model attribute provider
		Mockito.verify(userService, Mockito.times(2)).getLoggedInUser();
	}

	@Test
	public void EditUserPost_WithNoChange_SavesToService() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/editProfile").with(csrf())
				.flashAttr("editUserForm", editUserForm))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.redirectedUrl("/profile"));

		Mockito.verify(userService).updateUserByEmail(Mockito.any(), Mockito.any());
		Mockito.verify(userService).authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
	}

	@Test
	public void EditUserPost_WithValidChange_SavesToService() throws Exception {
		editUserForm.setFirstName("Jake");
		editUserForm.setLastName("Thomas");
		editUserForm.setNoSurnameCheckBox(false);
		editUserForm.setEmail("jakethomas@hotmail.com");
		Mockito.when(userService.checkEmail(Mockito.any())).thenReturn(false);
		editUserForm.setDob("01/06/1998");
		editUserForm.setPictureFile(new MockMultipartFile(
			"pictureFile", "newPfp.png", "image/png", "file contents".getBytes()));

		mockMvc.perform(MockMvcRequestBuilders.post("/editProfile").with(csrf())
				.flashAttr("editUserForm", editUserForm))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.redirectedUrl("/profile"));

		Mockito.verify(userService).updateUserByEmail(Mockito.any(), Mockito.any());
		Mockito.verify(userService).authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
	}

	@Test
	public void EditUserPost_WithNewJpegProfileImage_SavesToService() throws Exception {
		editUserForm.setPictureFile(new MockMultipartFile(
			"pictureFile", "newPfp.jpeg", "image/jpeg", "file contents".getBytes()));

		mockMvc.perform(MockMvcRequestBuilders.post("/editProfile").with(csrf())
				.flashAttr("editUserForm", editUserForm))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.redirectedUrl("/profile"));

		Mockito.verify(userService).updateUserByEmail(Mockito.any(), Mockito.any());
		Mockito.verify(userService).authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
	}

	@Test
	public void EditUserPost_WithNewSvgProfileImage_SavesToService() throws Exception {
		editUserForm.setPictureFile(new MockMultipartFile(
			"pictureFile", "newPfp.svg", "image/svg+xml", "file contents".getBytes()));

		mockMvc.perform(MockMvcRequestBuilders.post("/editProfile").with(csrf())
				.flashAttr("editUserForm", editUserForm))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.redirectedUrl("/profile"));

		Mockito.verify(userService).updateUserByEmail(Mockito.any(), Mockito.any());
		Mockito.verify(userService).authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
	}

	@Test
	public void EditUserPost_WithValidEmptyDate_SavesToService() throws Exception {
		editUserForm.setDob("");

		mockMvc.perform(MockMvcRequestBuilders.post("/editProfile").with(csrf())
				.flashAttr("editUserForm", editUserForm))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.redirectedUrl("/profile"));

		Mockito.verify(userService).updateUserByEmail(Mockito.any(), Mockito.any());
		Mockito.verify(userService).authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
	}

	@Test
	public void EditUserPost_WithValidNoSurname_SavesToService() throws Exception {
		editUserForm.setLastName("");
		editUserForm.setNoSurnameCheckBox(true);

		mockMvc.perform(MockMvcRequestBuilders.post("/editProfile").with(csrf())
				.flashAttr("editUserForm", editUserForm))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.redirectedUrl("/profile"));

		Mockito.verify(userService).updateUserByEmail(Mockito.any(), Mockito.any());
		Mockito.verify(userService).authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
	}

	@Test
	public void EditUserPost_WithInvalidFirstNameEmpty_HasFieldErrors() throws Exception {
		editUserForm.setFirstName("");

		mockMvc.perform(MockMvcRequestBuilders.post("/editProfile").with(csrf())
				.flashAttr("editUserForm", editUserForm))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/editProfilePage"))
			.andExpect(
				MockMvcResultMatchers.model().attributeHasFieldErrors("editUserForm", "firstName"));

		Mockito.verify(userService, Mockito.never())
			.updateUserByEmail(Mockito.any(), Mockito.any());
		Mockito.verify(userService, Mockito.never())
			.authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
	}

	@Test
	public void EditUserPost_WithInvalidFirstName_HasFieldErrors() throws Exception {
		editUserForm.setFirstName("Jeff3");

		mockMvc.perform(MockMvcRequestBuilders.post("/editProfile").with(csrf())
				.flashAttr("editUserForm", editUserForm))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/editProfilePage"))
			.andExpect(
				MockMvcResultMatchers.model().attributeHasFieldErrors("editUserForm", "firstName"));

		Mockito.verify(userService, Mockito.never())
			.updateUserByEmail(Mockito.any(), Mockito.any());
		Mockito.verify(userService, Mockito.never())
			.authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
	}

	@Test
	public void EditUserPost_WithInvalidFirstNameLong_HasFieldErrors() throws Exception {
		editUserForm.setFirstName("A".repeat(65));

		mockMvc.perform(MockMvcRequestBuilders.post("/editProfile").with(csrf())
				.flashAttr("editUserForm", editUserForm))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/editProfilePage"))
			.andExpect(
				MockMvcResultMatchers.model().attributeHasFieldErrors("editUserForm", "firstName"));

		Mockito.verify(userService, Mockito.never())
			.updateUserByEmail(Mockito.any(), Mockito.any());
		Mockito.verify(userService, Mockito.never())
			.authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
	}

	@Test
	public void EditUserPost_WithInvalidLastName_HasFieldErrors() throws Exception {
		editUserForm.setLastName("James&&&");

		mockMvc.perform(MockMvcRequestBuilders.post("/editProfile").with(csrf())
				.flashAttr("editUserForm", editUserForm))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/editProfilePage"))
			.andExpect(
				MockMvcResultMatchers.model().attributeHasFieldErrors("editUserForm", "lastName"));

		Mockito.verify(userService, Mockito.never())
			.updateUserByEmail(Mockito.any(), Mockito.any());
		Mockito.verify(userService, Mockito.never())
			.authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
	}

	@Test
	public void EditUserPost_WithInvalidLastNameLong_HasFieldErrors() throws Exception {
		editUserForm.setLastName("B".repeat(65));

		mockMvc.perform(MockMvcRequestBuilders.post("/editProfile").with(csrf())
				.flashAttr("editUserForm", editUserForm))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/editProfilePage"))
			.andExpect(
				MockMvcResultMatchers.model().attributeHasFieldErrors("editUserForm", "lastName"));

		Mockito.verify(userService, Mockito.never())
			.updateUserByEmail(Mockito.any(), Mockito.any());
		Mockito.verify(userService, Mockito.never())
			.authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
	}

	@Test
	public void EditUserPost_WithInvalidEmailEmpty_HasFieldErrors() throws Exception {
		editUserForm.setEmail("");
		Mockito.when(userService.checkEmail(Mockito.any())).thenReturn(false);

		mockMvc.perform(MockMvcRequestBuilders.post("/editProfile").with(csrf())
				.flashAttr("editUserForm", editUserForm))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/editProfilePage"))
			.andExpect(
				MockMvcResultMatchers.model().attributeHasFieldErrors("editUserForm", "email"));

		Mockito.verify(userService, Mockito.never())
			.updateUserByEmail(Mockito.any(), Mockito.any());
		Mockito.verify(userService, Mockito.never())
			.authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
	}

	@Test
	public void EditUserPost_WithInvalidEmail_HasFieldErrors() throws Exception {
		editUserForm.setEmail("notanemail");
		Mockito.when(userService.checkEmail(Mockito.any())).thenReturn(false);

		mockMvc.perform(MockMvcRequestBuilders.post("/editProfile").with(csrf())
				.flashAttr("editUserForm", editUserForm))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/editProfilePage"))
			.andExpect(
				MockMvcResultMatchers.model().attributeHasFieldErrors("editUserForm", "email"));

		Mockito.verify(userService, Mockito.never())
			.updateUserByEmail(Mockito.any(), Mockito.any());
		Mockito.verify(userService, Mockito.never())
			.authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
	}

	@Test
	public void EditUserPost_WithInvalidEmailLong_HasFieldErrors() throws Exception {
		editUserForm.setEmail("A".repeat(246) + "@gmail.com");
		Mockito.when(userService.checkEmail(Mockito.any())).thenReturn(false);

		mockMvc.perform(MockMvcRequestBuilders.post("/editProfile").with(csrf())
				.flashAttr("editUserForm", editUserForm))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/editProfilePage"))
			.andExpect(
				MockMvcResultMatchers.model().attributeHasFieldErrors("editUserForm", "email"));

		Mockito.verify(userService, Mockito.never())
			.updateUserByEmail(Mockito.any(), Mockito.any());
		Mockito.verify(userService, Mockito.never())
			.authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
	}

	@Test
	public void EditUserPost_WithInvalidEmailTaken_HasFieldErrors() throws Exception {
		editUserForm.setEmail("takenEmail@hotmail.com");

		mockMvc.perform(MockMvcRequestBuilders.post("/editProfile").with(csrf())
				.flashAttr("editUserForm", editUserForm))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/editProfilePage"))
			.andExpect(
				MockMvcResultMatchers.model().attributeHasFieldErrors("editUserForm", "email"));

		Mockito.verify(userService, Mockito.never())
			.updateUserByEmail(Mockito.any(), Mockito.any());
		Mockito.verify(userService, Mockito.never())
			.authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
	}

	@Test
	public void EditUserPost_WithInvalidDateLeapDay_HasFieldErrors() throws Exception {
		editUserForm.setDob("29/02/2003");

		mockMvc.perform(MockMvcRequestBuilders.post("/editProfile").with(csrf())
				.flashAttr("editUserForm", editUserForm))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/editProfilePage"))
			.andExpect(
				MockMvcResultMatchers.model().attributeHasFieldErrors("editUserForm", "dob"));

		Mockito.verify(userService, Mockito.never())
			.updateUserByEmail(Mockito.any(), Mockito.any());
		Mockito.verify(userService, Mockito.never())
			.authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
	}

	@Test
	public void EditUserPost_WithInvalidDate_HasFieldErrors() throws Exception {
		editUserForm.setDob("BadDate");

		mockMvc.perform(MockMvcRequestBuilders.post("/editProfile").with(csrf())
				.flashAttr("editUserForm", editUserForm))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/editProfilePage"))
			.andExpect(
				MockMvcResultMatchers.model().attributeHasFieldErrors("editUserForm", "dob"));

		Mockito.verify(userService, Mockito.never())
			.updateUserByEmail(Mockito.any(), Mockito.any());
		Mockito.verify(userService, Mockito.never())
			.authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
	}

	@Test
	public void EditUserPost_WithInvalidFileType_HasFieldErrors() throws Exception {
		editUserForm.setPictureFile(new MockMultipartFile(
			"pictureFile", "newPfp.webp", "image/webp", "file contents".getBytes()));

		mockMvc.perform(MockMvcRequestBuilders.post("/editProfile").with(csrf())
				.flashAttr("editUserForm", editUserForm))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/editProfilePage"))
			.andExpect(MockMvcResultMatchers.model()
				.attributeHasFieldErrors("editUserForm", "pictureFile"));

		Mockito.verify(userService, Mockito.never())
			.updateUserByEmail(Mockito.any(), Mockito.any());
		Mockito.verify(userService, Mockito.never())
			.authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
	}

	@Test
	public void EditUserPost_WithTooBigFile_HasFieldErrors() throws Exception {
		byte[] over10mb = new byte[10 * 1024 * 1024 + 1];
		editUserForm.setPictureFile(new MockMultipartFile(
			"pictureFile", "newPfp.png", "image/png", over10mb));

		mockMvc.perform(MockMvcRequestBuilders.post("/editProfile").with(csrf())
				.flashAttr("editUserForm", editUserForm))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/editProfilePage"))
			.andExpect(MockMvcResultMatchers.model()
				.attributeHasFieldErrors("editUserForm", "pictureFile"));

		Mockito.verify(userService, Mockito.never())
			.updateUserByEmail(Mockito.any(), Mockito.any());
		Mockito.verify(userService, Mockito.never())
			.authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
	}

	@Test
	public void EditProfileGet_ValidDetails_FormIsPopulated() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/editProfile").with(csrf())
				.flashAttr("editUserForm", editUserForm))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn();
		EditUserForm modelEditUserForm = (EditUserForm) result.getModelAndView().getModel()
			.get("editUserForm");
		Assertions.assertEquals(userMock.getFname(), modelEditUserForm.getFirstName());
		Assertions.assertEquals(userMock.getLname(), modelEditUserForm.getLastName());
		Assertions.assertEquals(userMock.getEmail(), modelEditUserForm.getEmail());
		Assertions.assertEquals(userMock.getLname() == null || userMock.getLname().isEmpty(),
			modelEditUserForm.getNoSurnameCheckBox());
		Assertions.assertEquals(userMock.getDateOfBirthString(), modelEditUserForm.getDob());
		Assertions.assertEquals(userMock.getPicturePath(), modelEditUserForm.getPicturePath());
		// This is called twice because of the global model attribute provider
		Mockito.verify(userService, Mockito.times(2)).getLoggedInUser();
	}

	@Test
	public void UpdatePasswordPost_WithValidNoChange_SavesToService() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/editProfile/updatePassword").with(csrf())
				.flashAttr("updatePasswordForm", updatePasswordForm))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.redirectedUrl("/profile"));

		Mockito.verify(userService).updateUserByEmail(Mockito.any(), Mockito.any());
	}

	@Test
	public void UpdatePasswordPost_WithValidChange_SavesToService() throws Exception {
		updatePasswordForm.setNewPassword("newPass1234567&");
		updatePasswordForm.setRetypeNewPassword("newPass1234567&");

		mockMvc.perform(MockMvcRequestBuilders.post("/editProfile/updatePassword").with(csrf())
				.flashAttr("updatePasswordForm", updatePasswordForm))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.redirectedUrl("/profile"));

		Mockito.verify(userService).updateUserByEmail(Mockito.any(), Mockito.any());
	}

	@Test
	public void UpdatePasswordPost_WithInvalidPasswordEmpty_HasFieldErrors() throws Exception {
		updatePasswordForm.setPassword("");

		mockMvc.perform(MockMvcRequestBuilders.post("/editProfile/updatePassword").with(csrf())
				.flashAttr("updatePasswordForm", updatePasswordForm))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/updatePasswordPage"))
			.andExpect(MockMvcResultMatchers.model()
				.attributeHasFieldErrors("updatePasswordForm", "password"));

		Mockito.verify(userService, Mockito.never())
			.updateUserByEmail(Mockito.any(), Mockito.any());
	}

	@Test
	public void UpdatePasswordPost_WithInvalidPassword_HasFieldErrors() throws Exception {
		Mockito.when(passwordEncoder.matches(Mockito.any(), Mockito.any())).thenReturn(false);

		mockMvc.perform(MockMvcRequestBuilders.post("/editProfile/updatePassword").with(csrf())
				.flashAttr("updatePasswordForm", updatePasswordForm))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/updatePasswordPage"))
			.andExpect(MockMvcResultMatchers.model()
				.attributeHasFieldErrors("updatePasswordForm", "password"));

		Mockito.verify(userService, Mockito.never())
			.updateUserByEmail(Mockito.any(), Mockito.any());
	}

	@Test
	public void UpdatePasswordPost_WithInvalidNewPasswordEmpty_HasFieldErrors() throws Exception {
		updatePasswordForm.setNewPassword("");
		updatePasswordForm.setRetypeNewPassword("");

		mockMvc.perform(MockMvcRequestBuilders.post("/editProfile/updatePassword").with(csrf())
				.flashAttr("updatePasswordForm", updatePasswordForm))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/updatePasswordPage"))
			.andExpect(MockMvcResultMatchers.model()
				.attributeHasFieldErrors("updatePasswordForm", "newPassword"));

		Mockito.verify(userService, Mockito.never())
			.updateUserByEmail(Mockito.any(), Mockito.any());
	}

	@Test
	public void UpdatePasswordPost_WithInvalidNewPassword_HasFieldErrors() throws Exception {
		updatePasswordForm.setNewPassword("notStrongEnough");
		updatePasswordForm.setRetypeNewPassword("notStrongEnough");

		mockMvc.perform(MockMvcRequestBuilders.post("/editProfile/updatePassword").with(csrf())
				.flashAttr("updatePasswordForm", updatePasswordForm))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/updatePasswordPage"))
			.andExpect(MockMvcResultMatchers.model()
				.attributeHasFieldErrors("updatePasswordForm", "newPassword"));

		Mockito.verify(userService, Mockito.never())
			.updateUserByEmail(Mockito.any(), Mockito.any());
	}

	@Test
	public void UpdatePasswordPost_WithInvalidRetypedPassword_HasFieldErrors() throws Exception {
		updatePasswordForm.setNewPassword("newPass1234567&");
		updatePasswordForm.setRetypeNewPassword("notTheSame123$$$");

		mockMvc.perform(MockMvcRequestBuilders.post("/editProfile/updatePassword").with(csrf())
				.flashAttr("updatePasswordForm", updatePasswordForm))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/updatePasswordPage"))
			.andExpect(MockMvcResultMatchers.model()
				.attributeHasFieldErrors("updatePasswordForm", "retypeNewPassword"));

		Mockito.verify(userService, Mockito.never())
			.updateUserByEmail(Mockito.any(), Mockito.any());
	}

	@Test
	public void SearchFormGet_InvalidEmailEmpty_HasFieldErrors() throws Exception {
		String searchQuery = "";
		Mockito.when(userService.findEmail(searchQuery)).thenReturn(null);

		mockMvc.perform(MockMvcRequestBuilders.get("/searchFriend").param("email", searchQuery).with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/searchFriend"))
			.andExpect(MockMvcResultMatchers.model().attribute("userResult", Matchers.nullValue()));
	}

	@Test
	public void SearchFormGet_OwnEmail_HasErrors() throws Exception {
		String searchQuery = userMock.getEmail();
		Mockito.when(userService.findEmail(searchQuery)).thenReturn(userMock);

		mockMvc.perform(MockMvcRequestBuilders.get("/searchFriend").param("email", searchQuery).with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/searchFriend"))
			.andExpect(MockMvcResultMatchers.model().attribute("userResult", Matchers.nullValue()))
			.andExpect(
				MockMvcResultMatchers.model().attributeHasFieldErrors("searchForm", "email"));;
		Mockito.verify(userService).findEmail(searchQuery);
	}

	@Test
	public void SearchFormGet_EmailDoesNotExist_HasErrors() throws Exception {
		String searchQuery = "asd@ad.com";
		Mockito.when(userService.findEmail(searchQuery)).thenReturn(null);

		mockMvc.perform(MockMvcRequestBuilders.get("/searchFriend").param("email", searchQuery).with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/searchFriendPage"))
			.andExpect(MockMvcResultMatchers.model().attribute("userResult", Matchers.nullValue()))
			.andExpect(
				MockMvcResultMatchers.model().attributeHasFieldErrors("searchForm", "email"));;
		Mockito.verify(userService).findEmail(searchQuery);
	}

	@Test
	public void SearchFormGet_EmailExist_HasNoErrors() throws Exception {
		String searchQuery = "asd@ad.com";
		Mockito.when(userService.findEmail(searchQuery)).thenReturn(userMock);

		mockMvc.perform(MockMvcRequestBuilders.get("/searchFriend").param("email", searchQuery).with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/searchFriendPage"))
			.andExpect(MockMvcResultMatchers.model().attribute("userResult", userMock))
			.andExpect(
				MockMvcResultMatchers.model().attributeHasNoErrors("searchForm"));;
		Mockito.verify(userService).findEmail(searchQuery);
	}

	@Test
	public void SearchFormGet_EmailInvalidForm_HasErrors() throws Exception {
		String searchQuery = "@ad.com";
		Mockito.when(userService.findEmail(searchQuery)).thenReturn(null);

		mockMvc.perform(MockMvcRequestBuilders.get("/searchFriend").param("email", searchQuery).with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/searchFriendPage"))
			.andExpect(MockMvcResultMatchers.model().attribute("userResult", Matchers.nullValue()))
			.andExpect(
				MockMvcResultMatchers.model().attributeHasFieldErrors("searchForm","email"));;
		Mockito.verify(userService).findEmail(searchQuery);
	}
}
