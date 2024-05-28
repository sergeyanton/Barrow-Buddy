//package nz.ac.canterbury.team1000.gardenersgrove.cucumber.step_definitions;
//
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//
//import io.cucumber.java.en.And;
//import io.cucumber.java.en.Given;
//import io.cucumber.java.en.Then;
//import io.cucumber.java.en.When;
//import java.time.LocalDate;
//import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
//import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//public class U4_MVCStepDefinitions {
//
//	@Autowired
//	private MockMvc mockMvc;
//
//	@MockBean
//	private UserService userService;
//
//	@Mock
//	User userMock;
//
//	@Given("I am currently logged in")
//	public void iAmCurrentlyLoggedIn() {
//		userMock = Mockito.mock(User.class);
//	}
//
//	@And("My current first name in the system is {string}")
//	public void myCurrentFirstNameInTheSystemIs(String firstName) {
//		Mockito.when(userMock.getLname()).thenReturn(firstName);
//	}
//
//	@And("My current last name in the system is {string}")
//	public void myCurrentLastNameInTheSystemIs(String lastName) {
//		Mockito.when(userMock.getLname()).thenReturn(lastName);
//	}
//
//	@And("My current email in the system is {string}")
//	public void myCurrentEmailInTheSystemIs(String email) {
//		Mockito.when(userMock.getLname()).thenReturn(email);
//	}
//
//	@And("My current date of birth in the system is {string}")
//	public void myCurrentDateOfBirthInTheSystemIs(String dob) {
//		Mockito.when(userMock.getLname()).thenReturn(dob);
//
//	}
//
//	@When("I click the Edit button on my user profile page")
//	public void iClickTheEditButtonOnMyUserProfilePage() throws Exception {
//		mockMvc.perform(MockMvcRequestBuilders.get("/editProfile"))
//			.andExpect(MockMvcResultMatchers.status().isOk());
//	}
//
//	@Then("I see my details prefilled on the Edit Profile form, {string}, {string}, {string}, & {string}")
//	public void iSeeMyDetailsPrefilledOnTheEditProfileForm(
//		String firstName, String lastName, String email, String dob) {
//	}
//}