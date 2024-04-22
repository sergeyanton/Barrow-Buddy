package nz.ac.canterbury.team1000.gardenersgrove.controllers;

import nz.ac.canterbury.team1000.gardenersgrove.service.GardenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import nz.ac.canterbury.team1000.gardenersgrove.controller.HomeController;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerTest {
    @Mock
    GardenService gardenService;
    @Mock
    UserService userService;

    @InjectMocks
    HomeController homeController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        homeController = new HomeController(gardenService, userService);

        SecurityContextHolder.clearContext();
    }

    @Test
    void getHomePageWhenUserSignedIn() {
        when(userService.isSignedIn()).thenReturn(true);

        String result = homeController.getHome();

        assertEquals("pages/homePage", result);
    }

    @Test
    void getLandingPageWhenUserIsNotSignedIn() {
        when(userService.isSignedIn()).thenReturn(false);

        String result = homeController.getHome();

        assertEquals("pages/landingPage", result);
    }

    @Test
    public void ClickCreateGarden_Anywhere_GoToCorrectForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/gardens/create"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/createGardenPage"));
    }
}
