package nz.ac.canterbury.team1000.gardenersgrove.controllers;

import nz.ac.canterbury.team1000.gardenersgrove.service.GardenService;
import nz.ac.canterbury.team1000.gardenersgrove.service.PlantService;
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

@WebMvcTest(controllers = {HomeController.class, GardensController.class})
@AutoConfigureMockMvc
@WithMockUser
class HomeControllerTest {
    @MockBean
    private GardenService gardenService;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private PlantService plantService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        homeController = new HomeController(gardenService, userService);

        SecurityContextHolder.clearContext();
    }

    @Test
    void getHomePageWhenUserSignedIn() throws Exception {
        when(userService.isSignedIn()).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/homePage"));
    }

    @Test
    void getLandingPageWhenUserIsNotSignedIn() throws Exception {
        when(userService.isSignedIn()).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/landingPage"));
    }

    @Test
    public void ClickCreateGarden_Anywhere_GoToCorrectForm() throws Exception {
        when(userService.isSignedIn()).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.get("/gardens/create"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/createGardenPage"));
    }

    @Test
    @WithAnonymousUser
    public void ClickCreateGarden_AnywhereWhenNotLoggedIn_ReturnsUserHome() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/gardens/create"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}
