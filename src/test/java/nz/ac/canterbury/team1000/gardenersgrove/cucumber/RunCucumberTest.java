package nz.ac.canterbury.team1000.gardenersgrove.cucumber;

import io.cucumber.java.Scenario;
import io.cucumber.junit.platform.engine.Constants;
import io.cucumber.spring.CucumberContextConfiguration;
import nz.ac.canterbury.team1000.gardenersgrove.GardenersGroveApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.platform.suite.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * This a required classto set up the cucumber tests. This code was taken from ATDD workshop
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameters({
    @ConfigurationParameter(key = Constants.GLUE_PROPERTY_NAME, value = "nz.ac.canterbury.team1000.gardenersgrove.cucumber"),
    @ConfigurationParameter(key = Constants.PLUGIN_PROPERTY_NAME, value = "pretty, html:target/cucumber-report/cucumber.html"),
    @ConfigurationParameter(key = Constants.PLUGIN_PUBLISH_QUIET_PROPERTY_NAME, value = "true")
})
@ContextConfiguration(classes = {GardenersGroveApplication.class, CucumberSpringConfiguration.class})
@SpringBootTest
@ActiveProfiles("cucumber")
public class RunCucumberTest {
}

