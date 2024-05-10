package nz.ac.canterbury.team1000.gardenersgrove.cucumber.step_definitions;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
public class VerificationAfterRegister {
  @BeforeAll
  public static void before_or_after_all() {

    
  }
  @Given("I have registered with the first name {string} and last name {string}, email {string} and password {string}")
  public void iHaveRegisteredWithValidCredentials(String firstName, String lastName, String email, String password) {

  }

  @When("I don't verify my account and try log in")
  public void iDonTVerifyMyAccountAndTryLogIn() {


  }

  @Then("I am redirected to the page with URL {string}")
  public void iAmRedirectedToThePageWithURL(String arg0) {
  }
}
