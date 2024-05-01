package nz.ac.canterbury.team1000.gardenersgrove.controllers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.time.LocalDate;
import nz.ac.canterbury.team1000.gardenersgrove.entity.Plant;
import nz.ac.canterbury.team1000.gardenersgrove.form.PlantForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import nz.ac.canterbury.team1000.gardenersgrove.controller.GardensController;
import nz.ac.canterbury.team1000.gardenersgrove.entity.Garden;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.service.GardenService;
import nz.ac.canterbury.team1000.gardenersgrove.service.PlantService;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;
import nz.ac.canterbury.team1000.gardenersgrove.service.VerificationTokenService;

@WebMvcTest(controllers = GardensController.class)
@AutoConfigureMockMvc
public class GardenControllerAuthTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private GardenService gardenService;

    @MockBean
    private PlantService plantService;

    @MockBean
    private VerificationTokenService verificationTokenService;

    @Mock
    private Garden gardenMock;

    @Mock
    private Plant plantMock;

    @Mock
    private User loggedInUser;

    @BeforeEach
    public void beforeEach() {
        loggedInUser = Mockito.mock(User.class);
        Mockito.when(loggedInUser.getId()).thenReturn(1L);

        gardenMock = Mockito.mock(Garden.class);
        Mockito.when(gardenMock.getId()).thenReturn(1L);
        Mockito.when(gardenMock.getOwner()).thenReturn(loggedInUser);
        Mockito.when(gardenMock.getName()).thenReturn("Test Garden");
        Mockito.when(gardenMock.getName()).thenReturn("Hamilton Gardens");
        Mockito.when(gardenMock.getAddress()).thenReturn("13 Hungerford Crescent");
        Mockito.when(gardenMock.getSuburb()).thenReturn("Ilam");
        Mockito.when(gardenMock.getCity()).thenReturn("Hamilton");
        Mockito.when(gardenMock.getPostcode()).thenReturn("3216");
        Mockito.when(gardenMock.getCountry()).thenReturn("New Zealand");
        Mockito.when(gardenMock.getSize()).thenReturn(10.0);

        plantMock = Mockito.mock(Plant.class);
        Mockito.when(plantMock.getId()).thenReturn(1L);
        Mockito.when(plantMock.getName()).thenReturn("Test Plant");
        Mockito.when(plantMock.getPlantCount()).thenReturn(1);
        Mockito.when(plantMock.getDescription()).thenReturn("Test plant description...");
        Mockito.when(plantMock.getPlantedOnDate()).thenReturn(LocalDate.of(1, 1, 1));

        Mockito.when(userService.getLoggedInUser()).thenReturn(loggedInUser);
        Mockito.when(gardenService.getGardenById(1L)).thenReturn(gardenMock);
        Mockito.when(plantService.getPlantById(1L)).thenReturn(plantMock);
    }

    @Test
    @WithAnonymousUser
    void testGetGardenCreate_WithUnauthenticatedUser_ReturnsUnauthorizedRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/gardens/create"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void testGetGardenCreate_WithAuthenticatedUser_ReturnsOkRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/gardens/create"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithAnonymousUser
    void testGetGarden_WithUnauthenticatedUser_ReturnsUnauthorizedRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/gardens/1"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void testGetGarden_WithAuthenticatedUserThatDoesNotOwnGarden_ReturnsForbiddenRequest() throws Exception {
        // make another user mock and say that the garden belongs to them
        User otherUser = Mockito.mock(User.class);
        Mockito.when(otherUser.getId()).thenReturn(2L);
        Mockito.when(gardenMock.getOwner()).thenReturn(otherUser);
        
        mockMvc.perform(MockMvcRequestBuilders.get("/gardens/1"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser
    void testGetGarden_WithAuthenticatedUserThatOwnsGarden_ReturnsOkRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/gardens/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithAnonymousUser
    void testEditGarden_WithUnauthenticatedUser_ReturnsUnauthorizedRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/gardens/1/edit"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void testEditGarden_WithAuthenticatedUserThatDoesNotOwnGarden_ReturnsForbiddenRequest() throws Exception {
        User otherUser = Mockito.mock(User.class);
        Mockito.when(otherUser.getId()).thenReturn(2L);
        Mockito.when(gardenMock.getOwner()).thenReturn(otherUser);
        
        mockMvc.perform(MockMvcRequestBuilders.get("/gardens/1/edit"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser
    void testEditGarden_WithAuthenticatedUserThatOwnsGarden_ReturnsOkRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/gardens/1/edit"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithAnonymousUser
    void testGetEditPlantPage_WithUnauthenticatedUser_ReturnsUnauthorizedRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/gardens/1/plants/1/edit"))
            .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void testGetEditPlantPage_WithAuthenticatedUser_ReturnsOkRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/gardens/1/plants/1/edit"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    void testGetEditPlantPage_WithAuthenticatedUserThatDoesNotOwnGarden_ReturnsForbiddenRequest() throws Exception {
        // make another user mock and say that the garden belongs to them
        User otherUser = Mockito.mock(User.class);
        Mockito.when(otherUser.getId()).thenReturn(2L);
        Mockito.when(gardenMock.getOwner()).thenReturn(otherUser);

        mockMvc.perform(MockMvcRequestBuilders.get("/gardens/1/plants/1/edit"))
            .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void testEditPlantPost_WithUnauthenticatedUser_ReturnsUnauthorizedRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/plants/1/edit").with(csrf()))
            .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void testEditPlantPost_WithAuthenticatedUser_ReturnsOkRequest() throws Exception {
        PlantForm plantForm = PlantForm.fromPlant(plantMock);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/plants/1/edit")
            .with(csrf()).flashAttr("editPlantForm", plantForm))
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

    @Test
    @WithMockUser
    void testEditPlantPost_WithAuthenticatedUserThatDoesNotOwnGarden_ReturnsForbiddenRequest() throws Exception {
        // make another user mock and say that the garden belongs to them
        User otherUser = Mockito.mock(User.class);
        Mockito.when(otherUser.getId()).thenReturn(2L);
        Mockito.when(gardenMock.getOwner()).thenReturn(otherUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/plants/1/edit").with(csrf()))
            .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
}
