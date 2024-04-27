package nz.ac.canterbury.team1000.gardenersgrove.controllers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import nz.ac.canterbury.team1000.gardenersgrove.form.GardenForm;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import nz.ac.canterbury.team1000.gardenersgrove.controller.GardensController;
import nz.ac.canterbury.team1000.gardenersgrove.entity.Garden;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.service.GardenService;
import nz.ac.canterbury.team1000.gardenersgrove.service.PlantService;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;

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
    private PlantService plantService;

    @Mock
    private Garden gardenMock;

    private GardenForm gardenForm;

    @Mock
    private User loggedInUser;

    @BeforeEach
    public void BeforeEach() {
        loggedInUser = Mockito.mock(User.class);
        Mockito.when(userService.getLoggedInUser()).thenReturn(loggedInUser);
        Mockito.when(loggedInUser.getId()).thenReturn(1L);

        gardenMock = Mockito.mock(Garden.class);
        Mockito.when(gardenMock.getId()).thenReturn(1L);
        Mockito.when(gardenMock.getOwner()).thenReturn(loggedInUser);
        Mockito.when(gardenMock.getName()).thenReturn("Hamilton Gardens");
        Mockito.when(gardenMock.getLocation()).thenReturn("Hamilton");
        Mockito.when(gardenMock.getSize()).thenReturn(46.2);

        gardenForm = new GardenForm();
        gardenForm.setName(gardenMock.getName());
        gardenForm.setLocation(gardenMock.getLocation());
        gardenForm.setSize(gardenMock.getSize().toString());

        // Mock addGarden(), updateGarden(), and getPlantById to always simply use id = 1
        Mockito.when(gardenService.addGarden(Mockito.any(Garden.class))).thenAnswer(invocation -> {
            Garden addedGarden = invocation.getArgument(0);
            addedGarden.setId(1L);
            return addedGarden;
        });
        Mockito.when(gardenService.updateGarden(Mockito.any(Garden.class))).thenAnswer(invocation -> {
            Garden updatedGarden = invocation.getArgument(0);
            updatedGarden.setId(1L);
            return updatedGarden;
        });

        Mockito.when(gardenService.getGardenById(1L)).thenReturn(gardenMock);
    }

    @Test
    public void CreateGardenPost_WithValidGarden_SavesToService() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/create").with(csrf())
                        .flashAttr("createGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1"));

        Mockito.verify(gardenService).addGarden(Mockito.any());
    }

    @Test
    public void CreateGardenPost_WithValidGardenEuropeanFormat_SavesToService() throws Exception {
        gardenForm.setSize("1,5");

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/create").with(csrf())
                        .flashAttr("createGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1"));

        Mockito.verify(gardenService).addGarden(Mockito.any());
    }

    @Test
    public void CreateGardenPost_WithValidGardenEmptySize_SavesToService() throws Exception {
        gardenForm.setSize("");

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/create").with(csrf())
                        .flashAttr("createGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1"));

        Mockito.verify(gardenService).addGarden(Mockito.any());
    }

    @Test
    public void CreateGardenPost_WithValidGardenWhitespaceSize_SavesToService() throws Exception {
        gardenForm.setSize("      ");

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/create").with(csrf())
                        .flashAttr("createGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1"));

        Mockito.verify(gardenService).addGarden(Mockito.any());
    }

    @Test
    public void CreateGardenPost_WithInvalidGardenNameEmptyString_ReturnsError() throws Exception {
        gardenForm.setName("");

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/create").with(csrf())
                        .flashAttr("createGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/createGardenPage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("createGardenForm", "name"));

        Mockito.verify(gardenService, Mockito.never()).addGarden(Mockito.any());
    }

    @Test
    public void CreateGardenPost_WithInvalidGardenLocationEmptyString_ReturnsError() throws Exception {
        gardenForm.setLocation("");

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/create").with(csrf())
                        .flashAttr("createGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/createGardenPage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("createGardenForm", "location"));

        Mockito.verify(gardenService, Mockito.never()).addGarden(Mockito.any());
    }

    @Test
    public void CreateGardenPost_InvalidSizeNegativeNumber_ReturnsError() throws Exception {
        gardenForm.setSize("-1");

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/create").with(csrf())
                        .flashAttr("createGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/createGardenPage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("createGardenForm", "size"));

        Mockito.verify(gardenService, Mockito.never()).addGarden(Mockito.any());
    }

    @Test
    public void CreateGardenPost_InvalidSizeNotANumber_ReturnsError() throws Exception {
        gardenForm.setSize("Not a number");

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/create").with(csrf())
                        .flashAttr("createGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/createGardenPage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("createGardenForm", "size"));

        Mockito.verify(gardenService, Mockito.never()).addGarden(Mockito.any());
    }


    @Test
    public void EditGardenPost_ValidGarden_SavesToService() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf())
                        .flashAttr("editGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1"));

        Mockito.verify(gardenService).updateGarden(Mockito.any());
    }

    @Test
    public void EditGardenPost_ValidGardenEuropeanFormat_SavesToService() throws Exception {
        gardenForm.setSize("1,5");
        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf())
                        .flashAttr("editGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1"));

        Mockito.verify(gardenService).updateGarden(Mockito.any());
    }

    @Test
    public void EditGardenPost_ValidEmptySize_SavesToService() throws Exception {
        gardenForm.setSize("");
        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf())
                        .flashAttr("editGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1"));

        Mockito.verify(gardenService).updateGarden(Mockito.any());
    }

    @Test
    public void EditGardenPost_ValidWhitespaceSize_SavesToService() throws Exception {
        gardenForm.setSize("      ");
        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf())
                        .flashAttr("editGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1"));

        Mockito.verify(gardenService).updateGarden(Mockito.any());
    }

    @Test
    public void EditGardenPost_InvalidNameEmptyString_ReturnsError() throws Exception {
        gardenForm.setName("");

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf())
                        .flashAttr("editGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/editGardenPage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("editGardenForm", "name"));

        Mockito.verify(gardenService, Mockito.never()).updateGarden(Mockito.any());
    }

    @Test
    public void EditGardenPost_InvalidLocationEmptyString_ReturnsError() throws Exception {
        gardenForm.setLocation("");

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf())
                        .flashAttr("editGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/editGardenPage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("editGardenForm", "location"));

        Mockito.verify(gardenService, Mockito.never()).updateGarden(Mockito.any());
    }

    @Test
    public void EditGardenPost_InvalidSizeNegativeNumber_ReturnsError() throws Exception {
        gardenForm.setSize("-1");

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf())
                        .flashAttr("editGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/editGardenPage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("editGardenForm", "size"));

        Mockito.verify(gardenService, Mockito.never()).updateGarden(Mockito.any());
    }

    @Test
    public void EditGardenPost_InvalidSizeNotANumber_ReturnsError() throws Exception {
        gardenForm.setSize("Bad Number");

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf())
                        .flashAttr("editGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/editGardenPage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("editGardenForm", "size"));

        Mockito.verify(gardenService, Mockito.never()).updateGarden(Mockito.any());
    }

    @Test
    public void EditGardenGet_ValidGarden_FormIsPopulated() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/gardens/1/edit").with(csrf())
                        .flashAttr("editGardenForm", gardenForm))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();
        GardenForm modelEditGardenForm = (GardenForm) result.getModelAndView().getModel().get("editGardenForm");
        Assertions.assertEquals(gardenMock.getName(), modelEditGardenForm.getName());
        Assertions.assertEquals(gardenMock.getLocation(), modelEditGardenForm.getLocation());
        Assertions.assertEquals(gardenMock.getSize().toString(), modelEditGardenForm.getSize());
        Mockito.verify(gardenService).getGardenById(1L);
    }
}
