package nz.ac.canterbury.team1000.gardenersgrove.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import java.time.LocalDate;

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
    private PlantService plantService; // this is required because the real GardensController has
                                       // one

    private Plant testPlant;

    @BeforeEach
    public void SetUp() {
        testPlant = new Plant("Plant Name", 3, "Plant Description", LocalDate.of(2023, 10, 15), 1L);
    }

    @Test
    public void CreatePlantPost_WithValidPlant_SavesToService() throws Exception {
        Mockito.when(plantService.addPlant(Mockito.any())).thenReturn(testPlant);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/plants/create").with(csrf())
                .param("plantName", "Plant Name").param("plantCount", "3")
                .param("plantDescription", "Plant Description")
                .param("plantedOnDate", "2023-10-15"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());

        Mockito.verify(plantService, Mockito.times(1)).addPlant(Mockito.any());
    }

    @Test
    public void CreatePlantPost_WithValidPlantEmptyCount_SavesToService() throws Exception {
        Mockito.when(plantService.addPlant(Mockito.any())).thenReturn(testPlant);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/plants/create").with(csrf())
                .param("plantName", "Plant Name").param("plantCount", "")
                .param("plantDescription", "Plant Description")
                .param("plantedOnDate", "2023-10-15"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());

        Mockito.verify(plantService, Mockito.times(1)).addPlant(Mockito.any());
    }

    @Test
    public void CreatePlantPost_WithValidPlantDescription_SavesToService() throws Exception {
        Mockito.when(plantService.addPlant(Mockito.any())).thenReturn(testPlant);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/plants/create").with(csrf())
                .param("plantName", "Plant Name").param("plantCount", "3")
                .param("plantDescription", "").param("plantedOnDate", "2023-10-15"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());

        Mockito.verify(plantService, Mockito.times(1)).addPlant(Mockito.any());
    }

    @Test
    public void CreatePlantPost_WithValidPlantEmptyDate_SavesToService() throws Exception {
        Mockito.when(plantService.addPlant(Mockito.any())).thenReturn(testPlant);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/plants/create").with(csrf())
                .param("plantName", "Plant Name").param("plantCount", "3")
                .param("plantDescription", "Plant Description").param("plantedOnDate", ""))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());

        Mockito.verify(plantService, Mockito.times(1)).addPlant(Mockito.any());
    }

    @Test
    public void CreatePlantPost_WithInvalidPlantBadName_ReturnsError() throws Exception {
        Mockito.when(plantService.addPlant(Mockito.any())).thenReturn(testPlant);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/gardens/1/plants/create").with(csrf()).param("plantName", "%%%%%%")
                        .param("plantCount", "3").param("plantDescription", "Plant Description")
                        .param("plantedOnDate", "2023-10-15"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("plantNameError"));

        Mockito.verify(plantService, Mockito.times(0)).addPlant(Mockito.any());
    }

    @Test
    public void CreatePlantPost_WithInvalidPlantEmptyName_ReturnsError() throws Exception {
        Mockito.when(plantService.addPlant(Mockito.any())).thenReturn(testPlant);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/gardens/1/plants/create").with(csrf()).param("plantName", "")
                        .param("plantCount", "3").param("plantDescription", "Plant Description")
                        .param("plantedOnDate", "2023-10-15"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("plantNameError"));

        Mockito.verify(plantService, Mockito.times(0)).addPlant(Mockito.any());
    }

    @Test
    public void CreatePlantPost_WithInvalidPlantBadCount_ReturnsError() throws Exception {
        Mockito.when(plantService.addPlant(Mockito.any())).thenReturn(testPlant);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/plants/create").with(csrf())
                .param("plantName", "Plant Name").param("plantCount", "not an integer")
                .param("plantDescription", "Plant Description")
                .param("plantedOnDate", "2023-10-15"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("plantCountError"));

        Mockito.verify(plantService, Mockito.times(0)).addPlant(Mockito.any());
    }

    @Test
    public void CreatePlantPost_WithInvalidPlantBadDescription_ReturnsError() throws Exception {
        Mockito.when(plantService.addPlant(Mockito.any())).thenReturn(testPlant);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/plants/create").with(csrf())
                .param("plantName", "Plant Name").param("plantCount", "3")
                .param("plantDescription", "a".repeat(513)).param("plantedOnDate", "2023-10-15"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("plantDescriptionError"));

        Mockito.verify(plantService, Mockito.times(0)).addPlant(Mockito.any());
    }

    @Test
    public void CreatePlantPost_WithInvalidPlantBadDate_ReturnsError() throws Exception {
        Mockito.when(plantService.addPlant(Mockito.any())).thenReturn(testPlant);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/plants/create").with(csrf())
                .param("plantName", "Plant Name").param("plantCount", "3")
                .param("plantDescription", "Plant Description")
                .param("plantedOnDate", "20233-10-15"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("plantedOnDateError"));

        Mockito.verify(plantService, Mockito.times(0)).addPlant(Mockito.any());
    }
}
