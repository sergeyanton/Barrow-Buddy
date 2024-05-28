package nz.ac.canterbury.team1000.gardenersgrove.cucumber;

import io.cucumber.junit.platform.engine.Constants;
import io.cucumber.spring.CucumberContextConfiguration;
import nz.ac.canterbury.team1000.gardenersgrove.GardenersGroveApplication;
import nz.ac.canterbury.team1000.gardenersgrove.auth.SecurityConfiguration;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;
import nz.ac.canterbury.team1000.gardenersgrove.service.VerificationTokenService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.suite.api.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * This a required class to set up the cucumber tests. This code was taken from ATDD workshop
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameters({
	@ConfigurationParameter(key = Constants.GLUE_PROPERTY_NAME, value = "nz.ac.canterbury.team1000.gardenersgrove.cucumber"),
	@ConfigurationParameter(key = Constants.PLUGIN_PROPERTY_NAME, value = "pretty, html:target/cucumber-report/cucumber.html"),
	@ConfigurationParameter(key = Constants.PLUGIN_PUBLISH_QUIET_PROPERTY_NAME, value = "true")
})
//Mocking external beans which are used in the cucumber tests
@MockBean(JavaMailSender.class)
@CucumberContextConfiguration
@WithMockUser
@ContextConfiguration(classes = {GardenersGroveApplication.class, SecurityConfiguration.class})
@SpringBootTest
@ActiveProfiles("cucumber")
@AutoConfigureMockMvc
public class RunCucumberTest {
}

