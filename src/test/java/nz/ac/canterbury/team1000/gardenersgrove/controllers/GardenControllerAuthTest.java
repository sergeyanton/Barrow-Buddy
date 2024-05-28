package nz.ac.canterbury.team1000.gardenersgrove.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.time.LocalDate;
import nz.ac.canterbury.team1000.gardenersgrove.entity.Plant;
import nz.ac.canterbury.team1000.gardenersgrove.form.PlantForm;
import nz.ac.canterbury.team1000.gardenersgrove.service.FriendRelationshipService;
import nz.ac.canterbury.team1000.gardenersgrove.service.ModerationService;
import nz.ac.canterbury.team1000.gardenersgrove.service.WeatherService;
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
    private ModerationService moderationService;

    @MockBean
    private WeatherService weatherService;

    @MockBean
    private VerificationTokenService verificationTokenService;

    @MockBean
    private FriendRelationshipService friendRelationshipService;

    @Mock
    private Garden privateGardenMock;

    @Mock
    private Garden publicGardenMock;

    @Mock
    private Plant privatePlantMock;

    @Mock
    private Plant publicPlantMock;

    @Mock
    private User loggedInUser;

    @BeforeEach
    public void beforeEach() {
        loggedInUser = Mockito.mock(User.class);
        Mockito.when(loggedInUser.getId()).thenReturn(1L);

        privateGardenMock = Mockito.mock(Garden.class);
        Mockito.when(privateGardenMock.getId()).thenReturn(1L);
        Mockito.when(privateGardenMock.getOwner()).thenReturn(loggedInUser);
        Mockito.when(privateGardenMock.getName()).thenReturn("Test Private Garden");
        Mockito.when(privateGardenMock.getName()).thenReturn("Hamilton Gardens");
        Mockito.when(privateGardenMock.getAddress()).thenReturn("13 Hungerford Crescent");
        Mockito.when(privateGardenMock.getSuburb()).thenReturn("Ilam");
        Mockito.when(privateGardenMock.getCity()).thenReturn("Hamilton");
        Mockito.when(privateGardenMock.getPostcode()).thenReturn("3216");
        Mockito.when(privateGardenMock.getCountry()).thenReturn("New Zealand");
        Mockito.when(privateGardenMock.getSize()).thenReturn(10.0);
        Mockito.when(privateGardenMock.getIsPublic()).thenReturn(false);

        publicGardenMock = Mockito.mock(Garden.class);
        when(publicGardenMock.getId()).thenReturn(2L);
        when(publicGardenMock.getOwner()).thenReturn(loggedInUser);
        when(publicGardenMock.getName()).thenReturn("Test Public Garden");
        when(publicGardenMock.getAddress()).thenReturn("123 Sesame Street");
        when(publicGardenMock.getSuburb()).thenReturn("Sesame");
        when(publicGardenMock.getCity()).thenReturn("Street");
        when(publicGardenMock.getPostcode()).thenReturn("3216");
        when(publicGardenMock.getCountry()).thenReturn("New Zealand");
        when(publicGardenMock.getSize()).thenReturn(100.0);
        when(publicGardenMock.getIsPublic()).thenReturn(true);

        privatePlantMock = Mockito.mock(Plant.class);
        Mockito.when(privatePlantMock.getId()).thenReturn(1L);
        Mockito.when(privatePlantMock.getName()).thenReturn("Test Private Plant");
        Mockito.when(privatePlantMock.getPlantCount()).thenReturn(1);
        Mockito.when(privatePlantMock.getDescription()).thenReturn("Test plant description...");
        Mockito.when(privatePlantMock.getPlantedOnDate()).thenReturn(LocalDate.of(1, 1, 1));
        Mockito.when(privatePlantMock.getPicturePath()).thenReturn("path/of/picture");

        publicPlantMock = Mockito.mock(Plant.class);
        Mockito.when(publicPlantMock.getId()).thenReturn(2L);
        Mockito.when(publicPlantMock.getName()).thenReturn("Test Public Plant");
        Mockito.when(publicPlantMock.getPlantCount()).thenReturn(1);
        Mockito.when(publicPlantMock.getDescription()).thenReturn("Test plant description...");
        Mockito.when(publicPlantMock.getPlantedOnDate()).thenReturn(LocalDate.of(1, 1, 1));
        Mockito.when(publicPlantMock.getPicturePath()).thenReturn("path/of/picture");

        Mockito.when(userService.getLoggedInUser()).thenReturn(loggedInUser);
        Mockito.when(gardenService.getGardenById(1L)).thenReturn(privateGardenMock);
        Mockito.when(gardenService.getGardenById(2L)).thenReturn(publicGardenMock);
        Mockito.when(plantService.getPlantById(1L)).thenReturn(privatePlantMock);
        Mockito.when(plantService.getPlantById(2L)).thenReturn(publicPlantMock);
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
    void testGetPrivateGarden_WithUnauthenticatedUser_ReturnsUnauthorizedRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/gardens/1"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithAnonymousUser
    void testGetPublicGarden_WithUnauthenticatedUser_ReturnsUnauthorizedRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/gardens/2"))
            .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void testGetPrivateGarden_WithAuthenticatedUserThatDoesNotOwnGarden_ReturnsForbiddenRequest() throws Exception {
        // make another user mock and say that the garden belongs to them
        User otherUser = Mockito.mock(User.class);
        Mockito.when(otherUser.getId()).thenReturn(2L);
        Mockito.when(privateGardenMock.getOwner()).thenReturn(otherUser);
        
        mockMvc.perform(MockMvcRequestBuilders.get("/gardens/1"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser
    void testGetPublicGarden_WithAuthenticatedUserThatDoesNotOwnGarden_ReturnsOkRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/gardens/2"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    void testGetPrivateGarden_WithAuthenticatedUserThatOwnsGarden_ReturnsOkRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/gardens/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    void testGetPublicGarden_WithAuthenticatedUserThatOwnsGarden_ReturnsOkRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/gardens/2"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithAnonymousUser
    void testEditPrivateGarden_WithUnauthenticatedUser_ReturnsUnauthorizedRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/gardens/1/edit"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithAnonymousUser
    void testEditPublicGarden_WithUnauthenticatedUser_ReturnsUnauthorizedRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/gardens/2/edit"))
            .andExpect(MockMvcResultMatchers.status().isUnauthorized());
        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/2/edit").with(csrf()))
            .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void testEditPrivateGarden_WithAuthenticatedUserThatDoesNotOwnGarden_ReturnsForbiddenRequest() throws Exception {
        User otherUser = Mockito.mock(User.class);
        Mockito.when(otherUser.getId()).thenReturn(2L);
        Mockito.when(privateGardenMock.getOwner()).thenReturn(otherUser);
        
        mockMvc.perform(MockMvcRequestBuilders.get("/gardens/1/edit"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    // TODO fix
    @Test
    @WithMockUser
    void testEditPublicGarden_WithAuthenticatedUserThatDoesNotOwnGarden_ReturnsForbiddenRequest() throws Exception {
        User otherUser = Mockito.mock(User.class);
        Mockito.when(otherUser.getId()).thenReturn(2L);
        Mockito.when(publicGardenMock.getOwner()).thenReturn(otherUser);

        mockMvc.perform(MockMvcRequestBuilders.get("/gardens/2/edit"))
            .andExpect(MockMvcResultMatchers.status().isForbidden());
        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/2/edit").with(csrf()))
            .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser
    void testEditPrivateGarden_WithAuthenticatedUserThatOwnsGarden_ReturnsOkRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/gardens/1/edit"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    void testEditPublicGarden_WithAuthenticatedUserThatOwnsGarden_ReturnsOkRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/gardens/2/edit"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithAnonymousUser
    void testGetEditPrivatePlantPage_WithUnauthenticatedUser_ReturnsUnauthorizedRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/gardens/1/plants/1/edit"))
            .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithAnonymousUser
    void testGetEditPublicPlantPage_WithUnauthenticatedUser_ReturnsUnauthorizedRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/gardens/2/plants/2/edit"))
            .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void testGetEditPrivatePlantPage_WithAuthenticatedUser_ReturnsOkRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/gardens/1/plants/1/edit"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    void testGetEditPublicPlantPage_WithAuthenticatedUser_ReturnsOkRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/gardens/2/plants/2/edit"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    void testGetEditPrivatePlantPage_WithAuthenticatedUserThatDoesNotOwnGarden_ReturnsForbiddenRequest() throws Exception {
        // make another user mock and say that the garden belongs to them
        User otherUser = Mockito.mock(User.class);
        Mockito.when(otherUser.getId()).thenReturn(2L);
        Mockito.when(privateGardenMock.getOwner()).thenReturn(otherUser);

        mockMvc.perform(MockMvcRequestBuilders.get("/gardens/1/plants/1/edit"))
            .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    // TODO fix
    @Test
    @WithMockUser
    void testGetEditPublicPlantPage_WithAuthenticatedUserThatDoesNotOwnGarden_ReturnsForbiddenRequest() throws Exception {
        // make another user mock and say that the garden belongs to them
        User otherUser = Mockito.mock(User.class);
        Mockito.when(otherUser.getId()).thenReturn(2L);
        Mockito.when(publicGardenMock.getOwner()).thenReturn(otherUser);

        mockMvc.perform(MockMvcRequestBuilders.get("/gardens/2/plants/2/edit"))
            .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void testEditPrivatePlantPost_WithUnauthenticatedUser_ReturnsUnauthorizedRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/plants/1/edit").with(csrf()))
            .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithAnonymousUser
    void testEditPublicPlantPost_WithUnauthenticatedUser_ReturnsUnauthorizedRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/2/plants/2/edit").with(csrf()))
            .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void testEditPrivatePlantPost_WithAuthenticatedUser_ReturnsOkRequest() throws Exception {
        PlantForm plantForm = PlantForm.fromPlant(privatePlantMock);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/plants/1/edit")
            .with(csrf()).flashAttr("editPlantForm", plantForm))
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

    @Test
    @WithMockUser
    void testEditPublicPlantPost_WithAuthenticatedUser_ReturnsOkRequest() throws Exception {
        PlantForm plantForm = PlantForm.fromPlant(publicPlantMock);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/2/plants/2/edit")
                .with(csrf()).flashAttr("editPlantForm", plantForm))
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

    @Test
    @WithMockUser
    void testEditPrivatePlantPost_WithAuthenticatedUserThatDoesNotOwnGarden_ReturnsForbiddenRequest() throws Exception {
        // make another user mock and say that the garden belongs to them
        User otherUser = Mockito.mock(User.class);
        Mockito.when(otherUser.getId()).thenReturn(2L);
        Mockito.when(privateGardenMock.getOwner()).thenReturn(otherUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/plants/1/edit").with(csrf()))
            .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser
    void testEditPublicPlantPost_WithAuthenticatedUserThatDoesNotOwnGarden_ReturnsForbiddenRequest() throws Exception {
        // make another user mock and say that the garden belongs to them
        User otherUser = Mockito.mock(User.class);
        Mockito.when(otherUser.getId()).thenReturn(2L);
        Mockito.when(publicGardenMock.getOwner()).thenReturn(otherUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/2/plants/2/edit").with(csrf()))
            .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

}
