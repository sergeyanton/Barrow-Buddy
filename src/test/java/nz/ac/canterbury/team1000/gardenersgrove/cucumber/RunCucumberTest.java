package nz.ac.canterbury.team1000.gardenersgrove.cucumber;

import io.cucumber.junit.platform.engine.Constants;
import io.cucumber.spring.CucumberContextConfiguration;
import nz.ac.canterbury.team1000.gardenersgrove.GardenersGroveApplication;
import org.junit.platform.suite.api.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;


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
@ContextConfiguration(classes = GardenersGroveApplication.class)

@AutoConfigureMockMvc
@CucumberContextConfiguration
@SpringBootTest
@ActiveProfiles("cucumber")
public class RunCucumberTest {

}

