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

    @BeforeEach
    public void BeforeEach() {
        gardenMock = Mockito.mock(Garden.class);
        Mockito.when(gardenMock.getId()).thenReturn(1L);
        Mockito.when(gardenMock.getName()).thenReturn("Hamilton Gardens");
        Mockito.when(gardenMock.getCountry()).thenReturn("Hamilton");
        Mockito.when(gardenMock.getSize()).thenReturn(46.2);

        gardenForm = new GardenForm();
        gardenForm.setName("Hamilton Gardens");
        gardenForm.setStreet("a");
        gardenForm.setStreetNumber("b");
        gardenForm.setSuburb("c");
        gardenForm.setCity("d");
        gardenForm.setPostcode("e");
        gardenForm.setCountry("f");
        gardenForm.setSize("46.2");
        gardenForm.setName(gardenMock.getName());
        // gardenForm.setLocation(gardenMock.getLocation());
        gardenForm.setSize(gardenMock.getSize().toString());

        // Mock addGarden(), updateGarden(), and getPlantById to always simply use id = 1
        Mockito.when(gardenService.addGarden(Mockito.any(Garden.class))).thenAnswer(invocation -> {
            Garden addedGarden = invocation.getArgument(0);
            addedGarden.setId(1L);
            return addedGarden;
        });
        Mockito.when(gardenService.updateGardenById(Mockito.anyLong(), Mockito.any(Garden.class))).thenAnswer(invocation -> {
            Garden updatedGarden = invocation.getArgument(1);
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
    public void CreateGardenPost_WithInvalidGardenLocationStreetEmptyString_ReturnsError() throws Exception {
        gardenForm.setStreet("");

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/create").with(csrf())
                        .flashAttr("createGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/createGardenPage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("createGardenForm", "street"));

        Mockito.verify(gardenService, Mockito.never()).addGarden(Mockito.any());
    }

    @Test
    public void CreateGardenPost_WithInvalidGardenLocationStreetNumberEmptyString_ReturnsError() throws Exception {
        gardenForm.setStreetNumber("");

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/create").with(csrf())
                        .flashAttr("createGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/createGardenPage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("createGardenForm", "streetNumber"));

        Mockito.verify(gardenService, Mockito.never()).addGarden(Mockito.any());
    }

    @Test
    public void CreateGardenPost_WithInvalidGardenLocationSuburbEmptyString_ReturnsError() throws Exception {
        gardenForm.setSuburb("");

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/create").with(csrf())
                        .flashAttr("createGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/createGardenPage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("createGardenForm", "suburb"));

        Mockito.verify(gardenService, Mockito.never()).addGarden(Mockito.any());
    }

    @Test
    public void CreateGardenPost_WithInvalidGardenLocationCityEmptyString_ReturnsError() throws Exception {
        gardenForm.setCity("");

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/create").with(csrf())
                        .flashAttr("createGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/createGardenPage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("createGardenForm", "city"));

        Mockito.verify(gardenService, Mockito.never()).addGarden(Mockito.any());
    }

    @Test
    public void CreateGardenPost_WithInvalidGardenLocationPostcodeEmptyString_ReturnsError() throws Exception {
        gardenForm.setPostcode("");

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/create").with(csrf())
                        .flashAttr("createGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/createGardenPage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("createGardenForm", "postcode"));

        Mockito.verify(gardenService, Mockito.never()).addGarden(Mockito.any());
    }

    @Test
    public void CreateGardenPost_WithInvalidGardenLocationCountryEmptyString_ReturnsError() throws Exception {
        gardenForm.setCountry("");

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/create").with(csrf())
                        .flashAttr("createGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/createGardenPage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("createGardenForm", "country"));

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

        Mockito.verify(gardenService).updateGardenById(Mockito.anyLong(), Mockito.any());
    }

    @Test
    public void EditGardenPost_ValidGardenEuropeanFormat_SavesToService() throws Exception {
        gardenForm.setSize("1,5");
        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf())
                        .flashAttr("editGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1"));

        Mockito.verify(gardenService).updateGardenById(Mockito.anyLong(), Mockito.any());

    }

    @Test
    public void EditGardenPost_ValidEmptySize_SavesToService() throws Exception {
        gardenForm.setSize("");
        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf())
                        .flashAttr("editGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1"));

        Mockito.verify(gardenService).updateGardenById(Mockito.anyLong(), Mockito.any());

    }

    @Test
    public void EditGardenPost_ValidWhitespaceSize_SavesToService() throws Exception {
        gardenForm.setSize("      ");
        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf())
                        .flashAttr("editGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1"));

        Mockito.verify(gardenService).updateGardenById(Mockito.anyLong(), Mockito.any());

    }

    @Test
    public void EditGardenPost_InvalidNameEmptyString_ReturnsError() throws Exception {
        gardenForm.setName("");

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf())
                        .flashAttr("editGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/editGardenPage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("editGardenForm", "name"));

        Mockito.verify(gardenService, Mockito.never()).updateGardenById(Mockito.anyLong(), Mockito.any());
    }

    @Test
    public void EditGardenPost_InvalidLocationStreetEmptyString_ReturnsError() throws Exception {
        gardenForm.setStreet("");

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf())
                        .flashAttr("editGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/editGardenPage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("editGardenForm", "street"));

        Mockito.verify(gardenService, Mockito.never()).updateGardenById(Mockito.anyLong(), Mockito.any());
    }

    @Test
    public void EditGardenPost_InvalidLocationStreetNumberEmptyString_ReturnsError() throws Exception {
        gardenForm.setStreetNumber("");

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf())
                        .flashAttr("editGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/editGardenPage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("editGardenForm", "streetNumber"));

        Mockito.verify(gardenService, Mockito.never()).updateGardenById(Mockito.anyLong(), Mockito.any());
    }

    @Test
    public void EditGardenPost_InvalidLocationSuburbEmptyString_ReturnsError() throws Exception {
        gardenForm.setSuburb("");

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf())
                        .flashAttr("editGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/editGardenPage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("editGardenForm", "suburb"));

        Mockito.verify(gardenService, Mockito.never()).updateGardenById(Mockito.anyLong(), Mockito.any());
    }

    @Test
    public void EditGardenPost_InvalidLocationCityEmptyString_ReturnsError() throws Exception {
        gardenForm.setCity("");

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf())
                        .flashAttr("editGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/editGardenPage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("editGardenForm", "city"));

        Mockito.verify(gardenService, Mockito.never()).updateGardenById(Mockito.anyLong(), Mockito.any());
    }

    @Test
    public void EditGardenPost_InvalidLocationPostcodeEmptyString_ReturnsError() throws Exception {
        gardenForm.setPostcode("");

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf())
                        .flashAttr("editGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/editGardenPage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("editGardenForm", "postcode"));

        Mockito.verify(gardenService, Mockito.never()).updateGardenById(Mockito.anyLong(), Mockito.any());
    }

    @Test
    public void EditGardenPost_InvalidLocationCountryEmptyString_ReturnsError() throws Exception {
        gardenForm.setCountry("");

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf())
                        .flashAttr("editGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/editGardenPage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("editGardenForm", "country"));

        Mockito.verify(gardenService, Mockito.never()).updateGardenById(Mockito.anyLong(), Mockito.any());
    }

    @Test
    public void EditGardenPost_InvalidSizeNegativeNumber_ReturnsError() throws Exception {
        gardenForm.setSize("-1");

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf())
                        .flashAttr("editGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/editGardenPage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("editGardenForm", "size"));

        Mockito.verify(gardenService, Mockito.never()).updateGardenById(Mockito.anyLong(), Mockito.any());
    }

    @Test
    public void EditGardenPost_InvalidSizeNotANumber_ReturnsError() throws Exception {
        gardenForm.setSize("Bad Number");

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf())
                        .flashAttr("editGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/editGardenPage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("editGardenForm", "size"));

        Mockito.verify(gardenService, Mockito.never()).updateGardenById(Mockito.anyLong(), Mockito.any());
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
        Assertions.assertEquals(gardenMock.getSize(), modelEditGardenForm.getSizeDouble());
        System.out.println(gardenMock.getSize());
        System.out.println(modelEditGardenForm.getSizeDouble());
        Mockito.verify(gardenService).getGardenById(1L);
    }
}
