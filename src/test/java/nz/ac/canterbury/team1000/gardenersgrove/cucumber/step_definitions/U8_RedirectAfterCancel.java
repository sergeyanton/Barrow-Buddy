package nz.ac.canterbury.team1000.gardenersgrove.cucumber.step_definitions;

import static nz.ac.canterbury.team1000.gardenersgrove.form.FormUtils.VALID_DATE_FORMAT;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.form.GardenForm;
import nz.ac.canterbury.team1000.gardenersgrove.form.LoginForm;
import nz.ac.canterbury.team1000.gardenersgrove.form.PictureForm;
import nz.ac.canterbury.team1000.gardenersgrove.form.RegistrationForm;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.BindingResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class U8_RedirectAfterCancel {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;


	private User testUser;



	@Given("A user exists with email {string} and password {string}")
	public void a_user_exists_with_the_email_and_password(String email, String password) {
		if (userService.getUserByEmailAndPassword(email, password) != null) return;
		this.testUser = new User("Jung", "Kook", email, passwordEncoder.encode(password), LocalDate.parse("01/01/2000", VALID_DATE_FORMAT), "/images/default_pic.jpg");

		testUser.grantAuthority("ROLE_USER");
		userService.registerUser(testUser);
	}

	@Given("I am logged in as {string} with password {string}")
	public void i_am_logged_in_as_with_password(String email, String password) throws Exception {
		LoginForm loginForm = new LoginForm();
		loginForm.setEmail(email);
		loginForm.setPassword(password);

		mockMvc.perform(MockMvcRequestBuilders.post("/login").with(csrf())
				.flashAttr("loginForm", loginForm))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.redirectedUrl("/home"));
	}


	@And("I am on the home screen")
	public void i_am_on_the_garden_page() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/").with(csrf()))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
			.andExpect(MockMvcResultMatchers.view().name("pages/homePage"));
	}

	@When("I press the Create Garden button")
	public void i_click_the_cancel_button() throws Exception {
		GardenForm gardenForm = new GardenForm();
		gardenForm.setName("Test Garden");
		gardenForm.setCity("Test City");
		gardenForm.setCountry("Test Country");
		mockMvc.perform(MockMvcRequestBuilders.get("/gardens/create").with(csrf())
				.flashAttr("createGardenForm", gardenForm))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
			.andExpect(MockMvcResultMatchers.view().name("pages/createGardenPage"));
		;
	}


}
