package nz.ac.canterbury.team1000.gardenersgrove.cucumber.step_definitions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import nz.ac.canterbury.team1000.gardenersgrove.form.LoginForm;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class RedirectAfterCancel {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@Given("I am logged in")
	public void i_am_logged_in() throws Exception {
		LoginForm loginForm = new LoginForm();
		loginForm.setEmail("test@example.com");
		loginForm.setPassword("Password1234!");
		Mockito.when(userService.isSignedIn()).thenReturn(true);
		mockMvc.perform(MockMvcRequestBuilders.get("/login").with(csrf())
				.flashAttr("loginForm", loginForm))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.redirectedUrl("/"));
	}

	@Given("I am on the home screen")
	public void i_am_on_the_garden_page() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/landingPage"));
	}

	@When("I press the Create Garden button")
	public void i_click_the_cancel_button() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/garden/create"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/createGarden"));
	}

//	@When("I press the Cancel button")
//	public void i_am_redirected_to_the_garden_list_page() {
//		// Write code here that turns the phrase above into concrete actions
//		throw new io.cucumber.java.PendingException();
//	}
//
//	@Then("I should be on the home screen")
//	public void i_should_be_on_the_garden_list_page() {
//		// Write code here that turns the phrase above into concrete actions
//		throw new io.cucumber.java.PendingException();
//	}
//
//	@Then("I should not see the garden I was creating")
//	public void i_should_not_see_the_garden_I_was_creating() {
//		// Write code here that turns the phrase above into concrete actions
//		throw new io.cucumber.java.PendingException();
//	}


}
