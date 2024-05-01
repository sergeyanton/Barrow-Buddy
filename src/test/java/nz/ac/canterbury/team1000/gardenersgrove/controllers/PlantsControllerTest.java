package nz.ac.canterbury.team1000.gardenersgrove.controllers;

import nz.ac.canterbury.team1000.gardenersgrove.entity.Garden;
import nz.ac.canterbury.team1000.gardenersgrove.form.PictureForm;
import nz.ac.canterbury.team1000.gardenersgrove.form.PlantForm;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import nz.ac.canterbury.team1000.gardenersgrove.controller.GardensController;
import nz.ac.canterbury.team1000.gardenersgrove.entity.Plant;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.service.GardenService;
import nz.ac.canterbury.team1000.gardenersgrove.service.PlantService;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;
import nz.ac.canterbury.team1000.gardenersgrove.service.VerificationTokenService;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(controllers = {GardensController.class})
@AutoConfigureMockMvc
@WithMockUser
public class PlantsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthenticationManager authenticationManager;
    
    @MockBean
    private VerificationTokenService verificationTokenService;

    @MockBean
    private GardenService gardenService;

    @MockBean
    private PlantService plantService;

    @Mock
    private Plant plantMock;
    @Mock
    private Garden gardenMock;
    private PictureForm plantPictureForm;
    private PlantForm plantForm;

    @Mock
    private User loggedInUser;

    @BeforeEach
    public void BeforeEach() {
        loggedInUser = Mockito.mock(User.class);
        Mockito.when(userService.getLoggedInUser()).thenReturn(loggedInUser);
        Mockito.when(loggedInUser.getId()).thenReturn(1L);

        gardenMock = Mockito.mock(Garden.class);
        Mockito.when(gardenService.getGardenById(1L)).thenReturn(gardenMock);
        Mockito.when(gardenMock.getId()).thenReturn(1L);
        Mockito.when(gardenMock.getOwner()).thenReturn(loggedInUser);

        plantMock = Mockito.mock(Plant.class);
        Mockito.when(plantMock.getId()).thenReturn(1L);
        Mockito.when(plantMock.getName()).thenReturn("Red Rose");
        Mockito.when(plantMock.getPlantCount()).thenReturn(5);
        Mockito.when(plantMock.getDescription()).thenReturn("It is red and smells like roses");
        Mockito.when(plantMock.getPlantedOnDate()).thenReturn(LocalDate.of(2023, 12, 25));
        Mockito.when(plantMock.getPlantedOnDateString()).thenReturn("25/12/2023");
        Mockito.when(plantMock.getGardenId()).thenReturn(1L);

        plantForm = new PlantForm();
        plantForm.setName(plantMock.getName());
        plantForm.setPlantCount(plantMock.getPlantCount().toString());
        plantForm.setDescription(plantMock.getDescription());
        plantForm.setPlantedOnDate(plantMock.getPlantedOnDateString());
        plantForm.setGardenId(plantMock.getGardenId());
        plantForm.setPictureFile(new MockMultipartFile("pictureFile", new byte[0]));

        plantPictureForm = new PictureForm();
        plantPictureForm.setPictureFile(new MockMultipartFile("pictureFile", new byte[0]));

        // Mock addPlant(), updatePlant(), and getPlantById to always simply use id = 1
        Mockito.when(plantService.addPlant(Mockito.any(Plant.class))).thenAnswer(invocation -> {
            Plant addedPlant = invocation.getArgument(0);
            addedPlant.setId(1L);
            return addedPlant;
        });
        Mockito.when(plantService.updatePlant(Mockito.any(Plant.class))).thenAnswer(invocation -> {
            Plant updatedPlant = invocation.getArgument(0);
            updatedPlant.setId(1L);
            return updatedPlant;
        });

        Mockito.when(plantService.getPlantById(1L)).thenReturn(plantMock);
        Mockito.when(gardenService.getGardenById(1L)).thenReturn(gardenMock);
        Mockito.when(plantService.getPlantsByGardenId(1L)).thenReturn(List.of(plantMock));
    }

    @Test
    public void CreatePlantPost_WithValidPlant_SavesToService() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/plants/create").with(csrf())
                        .flashAttr("createPlantForm", plantForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1"));

        Mockito.verify(plantService).addPlant(Mockito.any());
    }

    @Test
    public void CreatePlantPost_WithValidPlantEmptyCount_SavesToService() throws Exception {
        plantForm.setPlantCount("");

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/plants/create").with(csrf())
                        .flashAttr("createPlantForm", plantForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1"));

        Mockito.verify(plantService).addPlant(Mockito.any());
    }

    @Test
    public void CreatePlantPost_WithValidEmptyPlantDescription_SavesToService() throws Exception {
        plantForm.setDescription("");

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/plants/create").with(csrf())
                        .flashAttr("createPlantForm", plantForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1"));

        Mockito.verify(plantService).addPlant(Mockito.any());
    }

    @Test
    public void CreatePlantPost_WithValidPlantEmptyDate_SavesToService() throws Exception {
        plantForm.setPlantedOnDate("");

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/plants/create").with(csrf())
                        .flashAttr("createPlantForm", plantForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1"));

        Mockito.verify(plantService).addPlant(Mockito.any());
    }

    @Test
    public void CreatePlantPost_WithInvalidPlantBadName_ReturnsError() throws Exception {
        plantForm.setName("%%%");

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/plants/create").with(csrf())
                        .flashAttr("createPlantForm", plantForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/createPlantPage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("createPlantForm", "name"));

        Mockito.verify(plantService, Mockito.never()).addPlant(Mockito.any());
    }

    @Test
    public void CreatePlantPost_WithInvalidPlantEmptyName_ReturnsError() throws Exception {
        plantForm.setName("");

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/plants/create").with(csrf())
                        .flashAttr("createPlantForm", plantForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/createPlantPage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("createPlantForm", "name"));

        Mockito.verify(plantService, Mockito.never()).addPlant(Mockito.any());
    }

    @Test
    public void CreatePlantPost_WithInvalidPlantBadCount_ReturnsError() throws Exception {
        plantForm.setPlantCount("Not a number");

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/plants/create").with(csrf())
                        .flashAttr("createPlantForm", plantForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/createPlantPage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("createPlantForm", "plantCount"));

        Mockito.verify(plantService, Mockito.never()).addPlant(Mockito.any());
    }

    @Test
    public void CreatePlantPost_WithInvalidPlantBadDescription_ReturnsError() throws Exception {
        plantForm.setDescription("a".repeat(513));

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/plants/create").with(csrf())
                        .flashAttr("createPlantForm", plantForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/createPlantPage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("createPlantForm", "description"));

        Mockito.verify(plantService, Mockito.never()).addPlant(Mockito.any());
    }

    @Test
    public void CreatePlantPost_WithInvalidPlantBadDate_ReturnsError() throws Exception {
        plantForm.setPlantedOnDate("Not a date");

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/plants/create").with(csrf())
                        .flashAttr("createPlantForm", plantForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/createPlantPage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("createPlantForm", "plantedOnDate"));

        Mockito.verify(plantService, Mockito.never()).addPlant(Mockito.any());
    }

    @Test
    public void CreatePlantPost_WithInvalidPlantBadDateDays_ReturnsError() throws Exception {
        plantForm.setPlantedOnDate("29/02/2023");

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/plants/create").with(csrf())
                        .flashAttr("createPlantForm", plantForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/createPlantPage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("createPlantForm", "plantedOnDate"));

        Mockito.verify(plantService, Mockito.never()).addPlant(Mockito.any());
    }

    @Test
    public void PlantPicturePost_WithNoChange_SavesToService() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/plants/1").with(csrf())
                        .flashAttr("plantPictureForm", plantPictureForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1"));

        Mockito.verify(plantService).updatePlant(Mockito.any());
    }

    @Test
    public void PlantPicturePost_WithValidPng_SavesToService() throws Exception {
        plantPictureForm.setPictureFile(new MockMultipartFile(
                "pictureFile", "newPfp.png", "image/png", "file contents".getBytes()));

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/plants/1").with(csrf())
                        .flashAttr("plantPictureForm", plantPictureForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1"));

        Mockito.verify(plantService).updatePlant(Mockito.any());
    }

    @Test
    public void PlantPicturePost_WithValidJpeg_SavesToService() throws Exception {
        plantPictureForm.setPictureFile(new MockMultipartFile(
                "pictureFile", "newPfp.jpg", "image/jpeg", "file contents".getBytes()));

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/plants/1").with(csrf())
                        .flashAttr("plantPictureForm", plantPictureForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1"));

        Mockito.verify(plantService).updatePlant(Mockito.any());
    }

    @Test
    public void PlantPicturePost_WithValidSvg_SavesToService() throws Exception {
        plantPictureForm.setPictureFile(new MockMultipartFile(
                "pictureFile", "newPfp.svg", "image/svg+xml", "file contents".getBytes()));

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/plants/1").with(csrf())
                        .flashAttr("plantPictureForm", plantPictureForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1"));

        Mockito.verify(plantService).updatePlant(Mockito.any());
    }

    @Test
    public void PlantPicturePost_WithInvalidFileType_HasFieldErrors() throws Exception {
        plantPictureForm.setPictureFile(new MockMultipartFile(
                "pictureFile", "newPfp.webp", "image/webp", "file contents".getBytes()));

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/plants/1").with(csrf())
                        .flashAttr("plantPictureForm", plantPictureForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("garden"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("plants"))
                .andExpect(MockMvcResultMatchers.view().name("pages/gardenProfilePage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("plantPictureForm", "pictureFile"));

        Mockito.verify(plantService, Mockito.never()).updatePlant(Mockito.any());
    }

    @Test
    public void PlantPicturePost_WithTooBigFile_HasFieldErrors() throws Exception {
        byte[] over10mb = new byte[10 * 1024 * 1024 + 1];
        plantPictureForm.setPictureFile(new MockMultipartFile(
                "pictureFile", "newPfp.png", "image/png", over10mb));

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/plants/1").with(csrf())
                        .flashAttr("plantPictureForm", plantPictureForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("garden"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("plants"))
                .andExpect(MockMvcResultMatchers.view().name("pages/gardenProfilePage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("plantPictureForm", "pictureFile"));

        Mockito.verify(plantService, Mockito.never()).updatePlant(Mockito.any());
    }

    @Test
    public void ProfileGet_Valid_IsPopulated() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/gardens/1").with(csrf())
                        .flashAttr("plantPictureForm", plantPictureForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("garden"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("plants"));
    }

    @Test
    public void EditPlantGet_Valid_IsPopulated() throws Exception {
        // create the plant and add to garden
        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/plants/create").with(csrf())
                .flashAttr("createPlantForm", plantForm))
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
            .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1"));

        Mockito.verify(plantService).addPlant(Mockito.any());
        // then click edit and verify the prefilled values
        mockMvc.perform(MockMvcRequestBuilders.get("/gardens/1/plants/1/edit").with(csrf()))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.model().attributeExists("editPlantForm"))
            .andExpect(MockMvcResultMatchers.content().string(containsString("value=\"Red Rose\"")))
            .andExpect(MockMvcResultMatchers.content().string(containsString("value=\"5\"")))
            .andExpect(MockMvcResultMatchers.content().string(containsString("value=\"It is red and smells like roses\"")))
            .andExpect(MockMvcResultMatchers.content().string(containsString("value=\"25/12/2023\"")));
    }

}
