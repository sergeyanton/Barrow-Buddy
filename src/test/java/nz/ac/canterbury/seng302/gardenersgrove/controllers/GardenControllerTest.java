package nz.ac.canterbury.seng302.gardenersgrove.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import nz.ac.canterbury.seng302.gardenersgrove.controller.GardensController;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = GardensController.class)
@AutoConfigureMockMvc
public class GardenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GardenService gardenService;

    private Garden testGarden;

    @BeforeEach
    public void SetUp() {
        testGarden = new Garden("gardenName", "gardenLocation", 1.0);
    }

    @Test
    public void CreateGardenPost_WithValidGarden_SavesToService() throws Exception {
        Mockito.when(gardenService.addGarden(Mockito.any())).thenReturn(testGarden);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/gardens/create").param("gardenName", "gardenName")
                        .param("gardenLocation", "gardenLocation").param("gardenSize", "1"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());

        Mockito.verify(gardenService, Mockito.times(1)).addGarden(Mockito.any());
    }

    @Test
    public void CreateGardenPost_WithValidGardenEuropeanFormat_SavesToService() throws Exception {
        Mockito.when(gardenService.addGarden(Mockito.any())).thenReturn(testGarden);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/gardens/create").param("gardenName", "gardenName")
                                .param("gardenLocation", "gardenLocation").param("gardenSize", "1,5"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());

        Mockito.verify(gardenService, Mockito.times(1)).addGarden(Mockito.any());
    }

    @Test
    public void CreateGardenPost_WithValidGardenEmptySize_SavesToService() throws Exception {
        Mockito.when(gardenService.addGarden(Mockito.any())).thenReturn(testGarden);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/gardens/create").param("gardenName", "gardenName")
                                .param("gardenLocation", "gardenLocation").param("gardenSize", ""))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());

        Mockito.verify(gardenService, Mockito.times(1)).addGarden(Mockito.any());
    }

    @Test
    public void CreateGardenPost_WithValidGardenWhitespaceSize_SavesToService() throws Exception {
        Mockito.when(gardenService.addGarden(Mockito.any())).thenReturn(testGarden);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/gardens/create").param("gardenName", "gardenName")
                                .param("gardenLocation", "gardenLocation").param("gardenSize", "    "))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());

        Mockito.verify(gardenService, Mockito.times(1)).addGarden(Mockito.any());
    }

    @Test
    public void CreateGardenPost_WithInvalidGardenNameEmptyString_ReturnsError() throws Exception {
        Mockito.when(gardenService.addGarden(Mockito.any())).thenReturn(testGarden);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/create").param("gardenName", "")
                .param("gardenLocation", "gardenLocation").param("gardenSize", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("gardenNameError"));

        Mockito.verify(gardenService, Mockito.times(0)).addGarden(Mockito.any());
    }

    @Test
    public void CreateGardenPost_WithInvalidGardenLocationEmptyString_ReturnsError() throws Exception {
        Mockito.when(gardenService.addGarden(Mockito.any())).thenReturn(testGarden);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/gardens/create").param("gardenName", "gardenName")
                        .param("gardenLocation", "").param("gardenSize", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("gardenLocationError"));

        Mockito.verify(gardenService, Mockito.times(0)).addGarden(Mockito.any());
    }

    @Test
    public void CreateGardenPost_InvalidSizeNegativeNumber_ReturnsError() throws Exception {
        Mockito.when(gardenService.addGarden(Mockito.any())).thenReturn(testGarden);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/gardens/create").param("gardenName", "gardenName")
                                .param("gardenLocation", "").param("gardenSize", "-2"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("gardenLocationError"));

        Mockito.verify(gardenService, Mockito.times(0)).addGarden(Mockito.any());
    }

    @Test
    public void CreateGardenPost_InvalidSizeNotANumber_ReturnsError() throws Exception {
        Mockito.when(gardenService.addGarden(Mockito.any())).thenReturn(testGarden);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/gardens/create").param("gardenName", "gardenName")
                                .param("gardenLocation", "").param("gardenSize", "5 metres"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("gardenLocationError"));

        Mockito.verify(gardenService, Mockito.times(0)).addGarden(Mockito.any());
    }


    @Test
    public void EditGardenPost_ValidGarden_SavesToService() throws Exception {
        Mockito.when(gardenService.getGardenById(1L)).thenReturn(testGarden);
        Mockito.when(gardenService.updateGarden(Mockito.any())).thenReturn(testGarden);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/gardens/1/edit").param("gardenName", "gardenName")
                                .param("gardenLocation", "gardenLocation").param("gardenSize", "2"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());

        Mockito.verify(gardenService, Mockito.times(1)).updateGarden(Mockito.any());
    }

    @Test
    public void EditGardenPost_ValidGardenEuropeanFormat_SavesToService() throws Exception {
        Mockito.when(gardenService.getGardenById(1L)).thenReturn(testGarden);
        Mockito.when(gardenService.updateGarden(Mockito.any())).thenReturn(testGarden);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/gardens/1/edit").param("gardenName", "gardenName")
                                .param("gardenLocation", "gardenLocation").param("gardenSize", "3,9"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());

        Mockito.verify(gardenService, Mockito.times(1)).updateGarden(Mockito.any());
    }

    @Test
    public void EditGardenPost_ValidEmptySize_SavesToService() throws Exception {
        Mockito.when(gardenService.getGardenById(1L)).thenReturn(testGarden);
        Mockito.when(gardenService.updateGarden(Mockito.any())).thenReturn(testGarden);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/gardens/1/edit").param("gardenName", "gardenName")
                                .param("gardenLocation", "gardenLocation").param("gardenSize", ""))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());

        Mockito.verify(gardenService, Mockito.times(1)).updateGarden(Mockito.any());
    }

    @Test
    public void EditGardenPost_ValidWhitespaceSize_SavesToService() throws Exception {
        Mockito.when(gardenService.getGardenById(1L)).thenReturn(testGarden);
        Mockito.when(gardenService.updateGarden(Mockito.any())).thenReturn(testGarden);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/gardens/1/edit").param("gardenName", "gardenName")
                                .param("gardenLocation", "gardenLocation").param("gardenSize", "    "))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());

        Mockito.verify(gardenService, Mockito.times(1)).updateGarden(Mockito.any());
    }


    @Test
    public void EditGardenPost_InvalidNameEmptyString_ReturnsError() throws Exception {
        Mockito.when(gardenService.getGardenById(1L)).thenReturn(testGarden);
        Mockito.when(gardenService.updateGarden(Mockito.any())).thenReturn(testGarden);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/edit").param("gardenName", "")
                .param("gardenLocation", "gardenLocation").param("gardenSize", "2"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("gardenNameError"));

        Mockito.verify(gardenService, Mockito.times(0)).updateGarden(Mockito.any());
    }

    @Test
    public void EditGardenPost_InvalidLocationEmptyString_ReturnsError() throws Exception {
        Mockito.when(gardenService.getGardenById(1L)).thenReturn(testGarden);
        Mockito.when(gardenService.updateGarden(Mockito.any())).thenReturn(testGarden);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/gardens/1/edit").param("gardenName", "gardenName")
                        .param("gardenLocation", "").param("gardenSize", "2"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("gardenLocationError"));

        Mockito.verify(gardenService, Mockito.times(0)).updateGarden(Mockito.any());
    }

    @Test
    public void EditGardenPost_InvalidSizeNegativeNumber_ReturnsError() throws Exception {
        Mockito.when(gardenService.getGardenById(1L)).thenReturn(testGarden);
        Mockito.when(gardenService.updateGarden(Mockito.any())).thenReturn(testGarden);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/gardens/1/edit").param("gardenName", "gardenName")
                                .param("gardenLocation", "gardenLocation").param("gardenSize", "-2"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("gardenSizeError"));

        Mockito.verify(gardenService, Mockito.times(0)).updateGarden(Mockito.any());
    }

    @Test
    public void EditGardenPost_InvalidSizeNotANumber_ReturnsError() throws Exception {
        Mockito.when(gardenService.getGardenById(1L)).thenReturn(testGarden);
        Mockito.when(gardenService.updateGarden(Mockito.any())).thenReturn(testGarden);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/gardens/1/edit").param("gardenName", "gardenName")
                                .param("gardenLocation", "gardenLocation").param("gardenSize", "3m"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("gardenSizeError"));

        Mockito.verify(gardenService, Mockito.times(0)).updateGarden(Mockito.any());
    }
}
