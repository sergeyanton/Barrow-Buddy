package nz.ac.canterbury.team1000.gardenersgrove.cucumber.step_definitions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.form.EditUserForm;
import nz.ac.canterbury.team1000.gardenersgrove.form.LoginForm;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
public class U4_StepDefinitions {
	@Autowired
	UserService userService;
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	PasswordEncoder passwordEncoder;
	EditUserForm editUserForm = new EditUserForm();
	private User testUser;
	private MvcResult mvcResult;
	private final String PASSWORD = "Password1234!";

	@Given("I am logged in with the name {string} {string}, email {string}, and date of birth {string}")
	public void iAmLoggedInWithTheNameEmailAndDateOfBirth(String fName, String lName, String email,
		String dob) throws Exception {

		// if user does not exist yet, create and register the user
		if (userService.checkEmail(email)) {
			testUser = userService.findEmail(email);
		} else {
			testUser = new User(fName, lName == "" ? null : lName, email, passwordEncoder.encode(PASSWORD), LocalDate.parse(dob, DateTimeFormatter.ofPattern("dd/MM/uuuu")), "/images/default_pic.jpg");
			testUser.grantAuthority("ROLE_USER");
			userService.registerUser(testUser);
		}

		// log the user in, so they are authorized to make changes
		LoginForm loginForm = new LoginForm();
		loginForm.setEmail(testUser.getEmail());
		loginForm.setPassword(PASSWORD);
		mockMvc.perform(MockMvcRequestBuilders.post("/login").with(csrf())
				.flashAttr("loginForm", loginForm))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.redirectedUrl("/home"));
	}

	@Given("I am on the user profile page")
	public void iAmOnTheUserProfilePage() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/profile").with(csrf()))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
			.andExpect(MockMvcResultMatchers.view().name("pages/profilePage"));
	}

	@Given("A user exists with email {string}")
	public void emailIsTaken(String email) {
		if (!userService.checkEmail(email)) {
			User userWithGivenEmail = new User("FirstName", "LastName", email, passwordEncoder.encode(PASSWORD),
				LocalDate.of(2000, 1, 1), "/images/default_pic.jpg");
			userWithGivenEmail.grantAuthority("ROLE_USER");
			userService.registerUser(userWithGivenEmail);
		}
	}

	@Given("I am on the edit user profile form")
	public void iAmOnTheEditUserProfileForm() {
		editUserForm.setPictureFile(new MockMultipartFile("pictureFile", new byte[0]));
	}

	@And("I enter details {string}, {string}, {string}, and {string} on the edit user profile form")
	public void iEnterDetailsFirstNameLastNameEmailAndDobOnTheEditUserProfileForm(String fName, String lName, String email, String dob) {
		editUserForm.setFirstName(fName);
		editUserForm.setLastName(lName);
		editUserForm.setEmail(email);
		editUserForm.setDob(dob);
	}

	@And("I tick the checkbox for no last name on the edit user profile form")
	public void iTickTheCheckboxForNoLastNameOnTheEditUserProfileForm() {
		editUserForm.setNoSurnameCheckBox(true);
	}

	@And("I do not tick the checkbox for no last name on the edit user profile form")
	public void iDoNotTickTheCheckboxForNoLastNameOnTheEditUserProfileForm() {
		editUserForm.setNoSurnameCheckBox(false);
	}

	@And("I enter a first name {int} characters long and a last name {int} characters long")
	public void iEnterAFirstNameCharactersLongAndALastNameCharactersLong(int firstNameLength, int lastNameLength) {
		editUserForm.setFirstName("F".repeat(firstNameLength));
		editUserForm.setLastName("L".repeat(lastNameLength));
	}

	@And("I change my date of birth to mean that I turn {int} years old in {int} days on the edit user profile form")
	public void iEnterADateOfBirthThatMeansITurnYearsOldInDaysOnTheEditUserProfileForm(int age, int numDays) {
		editUserForm.setDob(LocalDate.now().minusYears(age).plusDays(numDays).format(DateTimeFormatter.ofPattern("dd/MM/uuuu")));
	}

	@When("I click the edit button on my user profile page")
	public void iClickTheEditButtonOnMyUserProfilePage() throws Exception {
		mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/editProfile").with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn();
	}

	@When("I click the edit profile button")
	public void iClickTheEditProfileButton() throws Exception {
		mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/editProfile").with(csrf())
			.flashAttr("editUserForm", editUserForm)).andReturn();
	}

	@When("I click the cancel button on my edit user profile form")
	public void iClickTheCancelButtonOnMyEditUserProfileForm() throws Exception {
		mvcResult = mockMvc.perform(
				MockMvcRequestBuilders.get("/profile").with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn();
	}

	@Then("I see my details prefilled on the Edit Profile form, {string}, {string}, {string}, & {string}")
	public void iSeeMyDetailsPrefilledOnTheEditProfileForm(String fName, String lName, String email, String dob) {
		editUserForm = (EditUserForm) mvcResult.getModelAndView().getModel().get("editUserForm");
		assertEquals(fName, editUserForm.getFirstName());
		assertEquals(lName, editUserForm.getLastName());
		assertEquals(email, editUserForm.getEmail());
		assertEquals(dob, editUserForm.getDob());
	}

	@Then("I see the checkbox for having no last name is checked")
	public void iSeeTheCheckboxForHavingNoLastNameIsChecked() {
		assertEquals(true, editUserForm.getNoSurnameCheckBox());
	}

	@Then("My new details are saved")
	public void myNewDetailsAreSaved() throws Exception {
		MockMvcResultMatchers.status().is3xxRedirection().match(mvcResult);
		MockMvcResultMatchers.redirectedUrl("/profile").match(mvcResult);
	}

	@Then("I am shown the error message {string} on the edit user profile form")
	public void iAmShownTheErrorMessageErrorMessageOnTheEditUserProfileForm(String errorMessage) throws Exception {
		MockMvcResultMatchers.status().isOk().match(mvcResult);
		MockMvcResultMatchers.view().name("pages/editProfilePage").match(mvcResult);

		BindingResult bindingResult = (BindingResult) mvcResult.getModelAndView().getModel().get(BindingResult.MODEL_KEY_PREFIX + "editUserForm");

		boolean errorMessageShown = false;
		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			if (errorMessage.equals(fieldError.getDefaultMessage())) {
				errorMessageShown = true;
			}
		}

		Assertions.assertTrue(errorMessageShown);
	}

	@Then("I am taken back to my profile page and my details are still {string} {string}, {string}, and {string}")
	public void iAmTakenBackToMyProfilePageAndMyDetailsAreStillAnd(String fName, String lName,
		String email, String dob){
		Map<String, Object> modelTemplate = mvcResult.getModelAndView().getModel();
		assertEquals(fName, modelTemplate.get("fName"));
		assertEquals(lName, modelTemplate.get("lName"));
		assertEquals(email, modelTemplate.get("email"));
		assertEquals(dob, modelTemplate.get("dob"));
	}
}
