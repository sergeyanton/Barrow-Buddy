package nz.ac.canterbury.team1000.gardenersgrove.controllers;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import nz.ac.canterbury.team1000.gardenersgrove.controller.GardensController;
import nz.ac.canterbury.team1000.gardenersgrove.entity.Plant;
import nz.ac.canterbury.team1000.gardenersgrove.service.GardenService;
import nz.ac.canterbury.team1000.gardenersgrove.service.PlantService;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(controllers = GardensController.class)
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
    private GardenService gardenService;

    @MockBean
    private PlantService plantService;

    @Mock
    private Plant plantMock;

    private PlantForm plantForm;

    @BeforeEach
    public void BeforeEach() {
        plantMock = Mockito.mock(Plant.class);
        Mockito.when(plantMock.getId()).thenReturn(1L);
//        Mockito.when(plantMock.getGardenId()).thenReturn(1L);

        plantForm = new PlantForm();
        plantForm.setName("Red Rose");
        plantForm.setPlantCount("5");
        plantForm.setDescription("It is red and smells like roses");
        plantForm.setPlantedOnDate("25/12/2023");
//        plantForm.setGardenId(1L);

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
}
