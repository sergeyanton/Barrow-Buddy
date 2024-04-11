package nz.ac.canterbury.team1000.gardenersgrove.controllers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.form.GardenForm;
import nz.ac.canterbury.team1000.gardenersgrove.form.RegistrationForm;
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
import nz.ac.canterbury.team1000.gardenersgrove.entity.Garden;
import nz.ac.canterbury.team1000.gardenersgrove.service.GardenService;
import nz.ac.canterbury.team1000.gardenersgrove.service.PlantService;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;

import java.time.LocalDate;

@WebMvcTest(controllers = GardensController.class)
@AutoConfigureMockMvc
@WithMockUser
public class GardenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private GardenService gardenService;

    @MockBean
    private PlantService plantService; // this is required because the real GardensController has one

    private static Garden garden;
    @BeforeEach
    public void setup() {
        garden = new Garden("Hamilton Gardens", "Hamilton", "42");
    }

    @Test
    public void CreateGardenPost_WithValidGarden_SavesToService() throws Exception {
        GardenForm gardenForm = new GardenForm();
        gardenForm.setName(garden.getName());
        gardenForm.setLocation(garden.getLocation());
        gardenForm.setSize(garden.getSize().toString());

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/create").with(csrf())
                        .flashAttr("createGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/" + garden.getId()));

        Mockito.verify(gardenService).addGarden(Mockito.any());
    }
//
//    @Test
//    public void CreateGardenPost_WithValidGardenEuropeanFormat_SavesToService() throws Exception {
//        Mockito.when(gardenService.addGarden(Mockito.any())).thenReturn(testGarden);
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.post("/gardens/create").with(csrf()).param("gardenName", "gardenName")
//                        .param("gardenLocation", "gardenLocation").param("gardenSize", "1,5"))
//                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
//                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/" + testGarden.getId()));
//
//        Mockito.verify(gardenService, Mockito.times(1)).addGarden(Mockito.any());
//    }
//
//    @Test
//    public void CreateGardenPost_WithValidGardenEmptySize_SavesToService() throws Exception {
//        Mockito.when(gardenService.addGarden(Mockito.any())).thenReturn(testGarden);
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.post("/gardens/create").with(csrf()).param("gardenName", "gardenName")
//                        .param("gardenLocation", "gardenLocation").param("gardenSize", ""))
//                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
//                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/" + testGarden.getId()));
//
//        Mockito.verify(gardenService, Mockito.times(1)).addGarden(Mockito.any());
//    }
//
//    @Test
//    public void CreateGardenPost_WithValidGardenWhitespaceSize_SavesToService() throws Exception {
//        Mockito.when(gardenService.addGarden(Mockito.any())).thenReturn(testGarden);
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.post("/gardens/create").with(csrf()).param("gardenName", "gardenName")
//                        .param("gardenLocation", "gardenLocation").param("gardenSize", "    "))
//                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
//                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/" + testGarden.getId()));
//
//        Mockito.verify(gardenService, Mockito.times(1)).addGarden(Mockito.any());
//    }
//
//    @Test
//    public void CreateGardenPost_WithInvalidGardenNameEmptyString_ReturnsError() throws Exception {
//        Mockito.when(gardenService.addGarden(Mockito.any())).thenReturn(testGarden);
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/create").with(csrf()).param("gardenName", "")
//                .param("gardenLocation", "gardenLocation").param("gardenSize", "1"))
//                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers
//                        .model().attribute("gardenNameError", "Garden name cannot by empty"));
//
//        Mockito.verify(gardenService, Mockito.times(0)).addGarden(Mockito.any());
//    }
//
//    @Test
//    public void CreateGardenPost_WithInvalidGardenLocationEmptyString_ReturnsError()
//            throws Exception {
//        Mockito.when(gardenService.addGarden(Mockito.any())).thenReturn(testGarden);
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.post("/gardens/create").with(csrf()).param("gardenName", "gardenName")
//                        .param("gardenLocation", "").param("gardenSize", "1"))
//                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers
//                        .model().attribute("gardenLocationError", "Location cannot be empty"));
//
//        Mockito.verify(gardenService, Mockito.times(0)).addGarden(Mockito.any());
//    }
//
//    @Test
//    public void CreateGardenPost_InvalidSizeNegativeNumber_ReturnsError() throws Exception {
//        Mockito.when(gardenService.addGarden(Mockito.any())).thenReturn(testGarden);
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.post("/gardens/create").with(csrf()).param("gardenName", "gardenName")
//                        .param("gardenLocation", "gardenLocation").param("gardenSize", "-2"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.model().attribute("gardenSizeError",
//                        "Garden size must be a positive number"));
//
//        Mockito.verify(gardenService, Mockito.times(0)).addGarden(Mockito.any());
//    }
//
//    @Test
//    public void CreateGardenPost_InvalidSizeNotANumber_ReturnsError() throws Exception {
//        Mockito.when(gardenService.addGarden(Mockito.any())).thenReturn(testGarden);
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.post("/gardens/create").with(csrf()).param("gardenName", "gardenName")
//                        .param("gardenLocation", "").param("gardenSize", "5 metres"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.model().attributeExists("gardenLocationError"));
//
//        Mockito.verify(gardenService, Mockito.times(0)).addGarden(Mockito.any());
//    }
//
//
//    @Test
//    public void EditGardenPost_ValidGarden_SavesToService() throws Exception {
//        Mockito.when(gardenService.getGardenById(1L)).thenReturn(testGarden);
//        Mockito.when(gardenService.updateGarden(Mockito.any())).thenReturn(testGarden);
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf()).param("gardenName", "gardenName")
//                        .param("gardenLocation", "gardenLocation").param("gardenSize", "2"))
//                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
//
//        Mockito.verify(gardenService, Mockito.times(1)).updateGarden(Mockito.any());
//    }
//
//    @Test
//    public void EditGardenPost_ValidGardenEuropeanFormat_SavesToService() throws Exception {
//        Mockito.when(gardenService.getGardenById(1L)).thenReturn(testGarden);
//        Mockito.when(gardenService.updateGarden(Mockito.any())).thenReturn(testGarden);
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf()).param("gardenName", "gardenName")
//                        .param("gardenLocation", "gardenLocation").param("gardenSize", "3,9"))
//                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
//
//        Mockito.verify(gardenService, Mockito.times(1)).updateGarden(Mockito.any());
//    }
//
//    @Test
//    public void EditGardenPost_ValidEmptySize_SavesToService() throws Exception {
//        Mockito.when(gardenService.getGardenById(1L)).thenReturn(testGarden);
//        Mockito.when(gardenService.updateGarden(Mockito.any())).thenReturn(testGarden);
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf()).param("gardenName", "gardenName")
//                        .param("gardenLocation", "gardenLocation").param("gardenSize", ""))
//                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
//
//        Mockito.verify(gardenService, Mockito.times(1)).updateGarden(Mockito.any());
//    }
//
//    @Test
//    public void EditGardenPost_ValidWhitespaceSize_SavesToService() throws Exception {
//        Mockito.when(gardenService.getGardenById(1L)).thenReturn(testGarden);
//        Mockito.when(gardenService.updateGarden(Mockito.any())).thenReturn(testGarden);
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf()).param("gardenName", "gardenName")
//                        .param("gardenLocation", "gardenLocation").param("gardenSize", "    "))
//                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
//
//        Mockito.verify(gardenService, Mockito.times(1)).updateGarden(Mockito.any());
//    }
//
//
//    @Test
//    public void EditGardenPost_InvalidNameEmptyString_ReturnsError() throws Exception {
//        Mockito.when(gardenService.getGardenById(1L)).thenReturn(testGarden);
//        Mockito.when(gardenService.updateGarden(Mockito.any())).thenReturn(testGarden);
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf()).param("gardenName", "")
//                .param("gardenLocation", "gardenLocation").param("gardenSize", "2"))
//                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers
//                        .model().attribute("gardenNameError", "Garden name cannot by empty"));
//
//        Mockito.verify(gardenService, Mockito.times(0)).updateGarden(Mockito.any());
//    }
//
//    @Test
//    public void EditGardenPost_InvalidLocationEmptyString_ReturnsError() throws Exception {
//        Mockito.when(gardenService.getGardenById(1L)).thenReturn(testGarden);
//        Mockito.when(gardenService.updateGarden(Mockito.any())).thenReturn(testGarden);
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf()).param("gardenName", "gardenName")
//                        .param("gardenLocation", "").param("gardenSize", "2"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.model().attributeExists("gardenLocationError"));
//
//        Mockito.verify(gardenService, Mockito.times(0)).updateGarden(Mockito.any());
//    }
//
//    @Test
//    public void EditGardenPost_InvalidSizeNegativeNumber_ReturnsError() throws Exception {
//        Mockito.when(gardenService.getGardenById(1L)).thenReturn(testGarden);
//        Mockito.when(gardenService.updateGarden(Mockito.any())).thenReturn(testGarden);
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf()).param("gardenName", "gardenName")
//                        .param("gardenLocation", "gardenLocation").param("gardenSize", "-2"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.model().attributeExists("gardenSizeError"));
//
//        Mockito.verify(gardenService, Mockito.times(0)).updateGarden(Mockito.any());
//    }
//
//    @Test
//    public void EditGardenPost_InvalidSizeNotANumber_ReturnsError() throws Exception {
//        Mockito.when(gardenService.getGardenById(1L)).thenReturn(testGarden);
//        Mockito.when(gardenService.updateGarden(Mockito.any())).thenReturn(testGarden);
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf()).param("gardenName", "gardenName")
//                        .param("gardenLocation", "gardenLocation").param("gardenSize", "3m"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.model().attributeExists("gardenSizeError"));
//
//        Mockito.verify(gardenService, Mockito.times(0)).updateGarden(Mockito.any());
//    }
//
//    @Test
//    public void EditGardenGet_ValidGarden_ReturnsGarden() throws Exception {
//        Mockito.when(gardenService.getGardenById(1L)).thenReturn(testGarden);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/gardens/1/edit"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(
//                        MockMvcResultMatchers.model().attribute("gardenName", testGarden.getName()))
//                .andExpect(
//                        MockMvcResultMatchers.model().attribute("gardenSize", testGarden.getSize()))
//                .andExpect(MockMvcResultMatchers.model().attribute("gardenLocation",
//                        testGarden.getLocation()));
//
//        Mockito.verify(gardenService, Mockito.times(1)).getGardenById(1L);
//    }
}
