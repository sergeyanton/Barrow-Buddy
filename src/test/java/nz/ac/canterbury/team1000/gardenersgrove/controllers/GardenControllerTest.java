package nz.ac.canterbury.team1000.gardenersgrove.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.util.ArrayList;
import java.util.List;
import nz.ac.canterbury.team1000.gardenersgrove.form.GardenForm;
import nz.ac.canterbury.team1000.gardenersgrove.form.PlantForm;
import nz.ac.canterbury.team1000.gardenersgrove.service.WeatherService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;
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
import nz.ac.canterbury.team1000.gardenersgrove.entity.Plant;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.service.GardenService;
import nz.ac.canterbury.team1000.gardenersgrove.service.PlantService;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;
import nz.ac.canterbury.team1000.gardenersgrove.service.VerificationTokenService;
import org.springframework.ui.Model;

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

    @MockBean
    private WeatherService weatherService;

    @MockBean
    private VerificationTokenService verificationTokenService;

    @Mock
    private Garden gardenMock;

    private GardenForm gardenForm;

    @Mock
    private Model model;

    @Mock
    private User loggedInUser;

    @BeforeEach
    public void BeforeEach() {
        loggedInUser = Mockito.mock(User.class);
        when(userService.getLoggedInUser()).thenReturn(loggedInUser);
        when(loggedInUser.getId()).thenReturn(1L);

        gardenMock = Mockito.mock(Garden.class);
        when(gardenMock.getId()).thenReturn(1L);
        when(gardenMock.getOwner()).thenReturn(loggedInUser);
        when(gardenMock.getName()).thenReturn("Hamilton Gardens");
        when(gardenMock.getAddress()).thenReturn("13 Hungerford Crescent");
        when(gardenMock.getSuburb()).thenReturn("Ilam");
        when(gardenMock.getCity()).thenReturn("Hamilton");
        when(gardenMock.getPostcode()).thenReturn("3216");
        when(gardenMock.getCountry()).thenReturn("New Zealand");
        when(gardenMock.getSize()).thenReturn(46.2);
        when(gardenMock.getIsPublic()).thenReturn(false);

        gardenForm = new GardenForm();
        gardenForm.setName(gardenMock.getName());
        gardenForm.setAddress(gardenMock.getAddress());
        gardenForm.setSuburb(gardenMock.getSuburb());
        gardenForm.setCity(gardenMock.getCity());
        gardenForm.setPostcode(gardenMock.getPostcode());
        gardenForm.setCountry(gardenMock.getCountry());
        gardenForm.setSize(gardenMock.getSize().toString());

        // Mock addGarden(), updateGarden(), and getPlantById to always simply use id = 1
        when(gardenService.addGarden(Mockito.any(Garden.class))).thenAnswer(invocation -> {
            Garden addedGarden = invocation.getArgument(0);
            addedGarden.setId(1L);
            return addedGarden;
        });
        when(gardenService.updateGardenById(Mockito.anyLong(), Mockito.any(Garden.class)))
                .thenAnswer(invocation -> {
                    Garden updatedGarden = invocation.getArgument(1);
                    updatedGarden.setId(1L);
                    return updatedGarden;
                });

        when(gardenService.getGardenById(1L)).thenReturn(gardenMock);
    }

    @Test
    public void CreateGardenPost_WithValidGarden_SavesToService() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/create").with(csrf())
                .flashAttr("createGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1"));

        verify(gardenService).addGarden(Mockito.any());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1,5", "Abc", "A b 123", "A-b c. de, f"})
    void CreateGardenPost_ValidName_SavesToService(String nameField) throws Exception {
        gardenForm.setName(nameField);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/create").with(csrf())
                .flashAttr("createGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1"));

        verify(gardenService).addGarden(Mockito.any());
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"    ", "\t", "\n", "1,5", "Abc", "A b 123", "A-b c. de, f"})
    void CreateGardenPost_ValidAddress_SavesToService(String addressField) throws Exception {
        gardenForm.setAddress(addressField);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/create").with(csrf())
                .flashAttr("createGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1"));

        verify(gardenService).addGarden(Mockito.any());
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"    ", "\t", "\n", "1,5", "Abc", "A b 123", "A-b c. de, f"})
    void CreateGardenPost_ValidSuburb_SavesToService(String suburbField) throws Exception {
        gardenForm.setSuburb(suburbField);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/create").with(csrf())
                .flashAttr("createGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1"));

        verify(gardenService).addGarden(Mockito.any());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1,5", "Abc", "A b 123", "A-b c. de, f"})
    void CreateGardenPost_ValidCity_SavesToService(String cityField) throws Exception {
        gardenForm.setCity(cityField);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/create").with(csrf())
                .flashAttr("createGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1"));

        verify(gardenService).addGarden(Mockito.any());
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"    ", "\t", "\n", "1,5", "Abc", "A b 123", "A-b c. de, f"})
    void CreateGardenPost_ValidPostcode_SavesToService(String postcodeField) throws Exception {
        gardenForm.setPostcode(postcodeField);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/create").with(csrf())
                .flashAttr("createGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1"));

        verify(gardenService).addGarden(Mockito.any());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1,5", "Abc", "A b 123", "A-b c. de, f"})
    void CreateGardenPost_ValidCountry_SavesToService(String countryField) throws Exception {
        gardenForm.setCity(countryField);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/create").with(csrf())
                .flashAttr("createGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1"));

        verify(gardenService).addGarden(Mockito.any());
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"    ", "\t", "\n", "1,5", "1234", "1.1"})
    void CreateGardenPost_ValidSize_SavesToService(String sizeField) throws Exception {
        gardenForm.setSize(sizeField);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/create").with(csrf())
                .flashAttr("createGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1"));

        verify(gardenService).addGarden(Mockito.any());
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"    ", "\t", "\n", "Not #", "Not $", "Not %", "Not @", "Not ^",
            "Not *", "Not &"})
    void CreateGardenPost_InvalidName_ReturnsError(String nameField) throws Exception {
        gardenForm.setName(nameField);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/create").with(csrf())
                .flashAttr("createGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/createGardenPage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("createGardenForm",
                        "name"));

        verify(gardenService, Mockito.never()).addGarden(Mockito.any());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Not #", "Not $", "Not %", "Not @", "Not ^", "Not *", "Not &", "Not (",
            "Not )", "Not +", "Not ="})
    void CreateGardenPost_InvalidAddress_ReturnsError(String addressField) throws Exception {
        gardenForm.setAddress(addressField);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/create").with(csrf())
                .flashAttr("createGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/createGardenPage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("createGardenForm",
                        "address"));

        verify(gardenService, Mockito.never()).addGarden(Mockito.any());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Not #", "Not $", "Not %", "Not @", "Not ^", "Not *", "Not &", "Not (",
            "Not )", "Not +", "Not ="})
    void CreateGardenPost_InvalidSuburb_ReturnsError(String suburbField) throws Exception {
        gardenForm.setSuburb(suburbField);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/create").with(csrf())
                .flashAttr("createGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/createGardenPage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("createGardenForm",
                        "suburb"));

        verify(gardenService, Mockito.never()).addGarden(Mockito.any());
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"    ", "\t", "\n", "Not #", "Not $", "Not %", "Not @", "Not ^",
            "Not *", "Not &", "Not (", "Not )", "Not +", "Not ="})
    void CreateGardenPost_InvalidCity_ReturnsError(String cityField) throws Exception {
        gardenForm.setCity(cityField);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/create").with(csrf())
                .flashAttr("createGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/createGardenPage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("createGardenForm",
                        "city"));

        verify(gardenService, Mockito.never()).addGarden(Mockito.any());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Not #", "Not $", "Not %", "Not @", "Not ^", "Not *", "Not &", "Not (",
            "Not )", "Not +", "Not ="})
    void CreateGardenPost_InvalidPostcode_ReturnsError(String postcodeField) throws Exception {
        gardenForm.setPostcode(postcodeField);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/create").with(csrf())
                .flashAttr("createGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/createGardenPage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("createGardenForm",
                        "postcode"));

        verify(gardenService, Mockito.never()).addGarden(Mockito.any());
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"    ", "\t", "\n", "Not #", "Not $", "Not %", "Not @", "Not ^",
            "Not *", "Not &", "Not (", "Not )", "Not +", "Not ="})
    void CreateGardenPost_InvalidCountry_ReturnsError(String countryField) throws Exception {
        gardenForm.setCountry(countryField);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/create").with(csrf())
                .flashAttr("createGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/createGardenPage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("createGardenForm",
                        "country"));

        verify(gardenService, Mockito.never()).addGarden(Mockito.any());
    }

    @ParameterizedTest
    @ValueSource(
            strings = {"#", "$", "%", "@", "^", "*", "&", "(", ")", "+", "=", "Not a number", "-1"})
    void CreateGardenPost_InvalidSize_ReturnsError(String sizeField) throws Exception {
        gardenForm.setSize(sizeField);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/create").with(csrf())
                .flashAttr("createGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/createGardenPage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("createGardenForm",
                        "size"));

        verify(gardenService, Mockito.never()).addGarden(Mockito.any());
    }

    @Test
    public void EditGardenPost_ValidGarden_SavesToService() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf())
                .flashAttr("editGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1"));

        verify(gardenService).updateGardenById(Mockito.anyLong(), Mockito.any());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1,5", "Abc", "A b 123", "A-b c. de, f"})
    void EditGardenPost_ValidName_SavesToService(String nameField) throws Exception {
        gardenForm.setName(nameField);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf())
                .flashAttr("editGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1"));

        verify(gardenService).updateGardenById(Mockito.anyLong(), Mockito.any());
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"    ", "\t", "\n", "1,5", "Abc", "A b 123", "A-b c. de, f"})
    void EditGardenPost_ValidAddress_SavesToService(String addressField) throws Exception {
        gardenForm.setAddress(addressField);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf())
                .flashAttr("editGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1"));

        verify(gardenService).updateGardenById(Mockito.anyLong(), Mockito.any());
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"    ", "\t", "\n", "1,5", "Abc", "A b 123", "A-b c. de, f"})
    void EditGardenPost_ValidSuburb_SavesToService(String suburbField) throws Exception {
        gardenForm.setSuburb(suburbField);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf())
                .flashAttr("editGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1"));

        verify(gardenService).updateGardenById(Mockito.anyLong(), Mockito.any());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1,5", "Abc", "A b 123", "A-b c. de, f"})
    void EditGardenPost_ValidCity_SavesToService(String cityField) throws Exception {
        gardenForm.setCity(cityField);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf())
                .flashAttr("editGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1"));

        verify(gardenService).updateGardenById(Mockito.anyLong(), Mockito.any());
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"    ", "\t", "\n", "1,5", "Abc", "A b 123", "A-b c. de, f"})
    void EditGardenPost_ValidPostcode_SavesToService(String postcodeField) throws Exception {
        gardenForm.setPostcode(postcodeField);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf())
                .flashAttr("editGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1"));

        verify(gardenService).updateGardenById(Mockito.anyLong(), Mockito.any());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1,5", "Abc", "A b 123", "A-b c. de, f"})
    void EditGardenPost_ValidCountry_SavesToService(String countryField) throws Exception {
        gardenForm.setCity(countryField);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf())
                .flashAttr("editGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1"));

        verify(gardenService).updateGardenById(Mockito.anyLong(), Mockito.any());
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"    ", "\t", "\n", "1,5", "1234", "1.1"})
    void EditGardenPost_ValidSize_SavesToService(String sizeField) throws Exception {
        gardenForm.setSize(sizeField);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf())
                .flashAttr("editGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1"));

        verify(gardenService).updateGardenById(Mockito.anyLong(), Mockito.any());
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"    ", "\t", "\n", "Not #", "Not $", "Not %", "Not @", "Not ^",
            "Not *", "Not &"})
    void EditGardenPost_InvalidName_ReturnsError(String nameField) throws Exception {
        gardenForm.setName(nameField);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf())
                .flashAttr("editGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/editGardenPage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("editGardenForm",
                        "name"));

        verify(gardenService, Mockito.never()).updateGardenById(Mockito.anyLong(),
                Mockito.any());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Not #", "Not $", "Not %", "Not @", "Not ^", "Not *", "Not &", "Not (",
            "Not )", "Not +", "Not ="})
    void EditGardenPost_InvalidAddress_ReturnsError(String addressField) throws Exception {
        gardenForm.setAddress(addressField);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf())
                .flashAttr("editGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/editGardenPage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("editGardenForm",
                        "address"));

        verify(gardenService, Mockito.never()).updateGardenById(Mockito.anyLong(),
                Mockito.any());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Not #", "Not $", "Not %", "Not @", "Not ^", "Not *", "Not &", "Not (",
            "Not )", "Not +", "Not ="})
    void EditGardenPost_InvalidSuburb_ReturnsError(String suburbField) throws Exception {
        gardenForm.setSuburb(suburbField);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf())
                .flashAttr("editGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/editGardenPage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("editGardenForm",
                        "suburb"));

        verify(gardenService, Mockito.never()).updateGardenById(Mockito.anyLong(),
                Mockito.any());
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"    ", "\t", "\n", "Not #", "Not $", "Not %", "Not @", "Not ^",
            "Not *", "Not &", "Not (", "Not )", "Not +", "Not ="})
    void EditGardenPost_InvalidCity_ReturnsError(String cityField) throws Exception {
        gardenForm.setCity(cityField);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf())
                .flashAttr("editGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/editGardenPage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("editGardenForm",
                        "city"));

        verify(gardenService, Mockito.never()).updateGardenById(Mockito.anyLong(),
                Mockito.any());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Not #", "Not $", "Not %", "Not @", "Not ^", "Not *", "Not &", "Not (",
            "Not )", "Not +", "Not ="})
    void EditGardenPost_InvalidPostcode_ReturnsError(String postcodeField) throws Exception {
        gardenForm.setPostcode(postcodeField);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf())
                .flashAttr("editGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/editGardenPage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("editGardenForm",
                        "postcode"));

        verify(gardenService, Mockito.never()).updateGardenById(Mockito.anyLong(),
                Mockito.any());
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"    ", "\t", "\n", "Not #", "Not $", "Not %", "Not @", "Not ^",
            "Not *", "Not &", "Not (", "Not )", "Not +", "Not ="})
    void EditGardenPost_InvalidCountry_ReturnsError(String countryField) throws Exception {
        gardenForm.setCountry(countryField);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf())
                .flashAttr("editGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/editGardenPage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("editGardenForm",
                        "country"));

        verify(gardenService, Mockito.never()).updateGardenById(Mockito.anyLong(),
                Mockito.any());
    }

    @ParameterizedTest
    @ValueSource(
            strings = {"#", "$", "%", "@", "^", "*", "&", "(", ")", "+", "=", "Not a number", "-1"})
    void EditGardenPost_InvalidSize_ReturnsError(String sizeField) throws Exception {
        gardenForm.setSize(sizeField);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf())
                .flashAttr("editGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/editGardenPage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("editGardenForm",
                        "size"));

        verify(gardenService, Mockito.never()).updateGardenById(Mockito.anyLong(),
                Mockito.any());
    }

    @Test
    void EditGardenPost_ValidSizeWorldsLargestGarden_SavesToService() throws Exception {
        gardenForm.setSize("72000");

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf())
                .flashAttr("editGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1"));

        verify(gardenService).updateGardenById(Mockito.anyLong(), Mockito.any());
   }

    @Test
    public void EditGardenGet_ValidGarden_FormIsPopulated() throws Exception {
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get("/gardens/1/edit").with(csrf())
                        .flashAttr("editGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        GardenForm modelEditGardenForm =
                (GardenForm) result.getModelAndView().getModel().get("editGardenForm");
        Assertions.assertEquals(gardenMock.getName(), modelEditGardenForm.getName());
        Assertions.assertEquals(gardenMock.getAddress(), modelEditGardenForm.getAddress());
        Assertions.assertEquals(gardenMock.getSuburb(), modelEditGardenForm.getSuburb());
        Assertions.assertEquals(gardenMock.getCity(), modelEditGardenForm.getCity());
        Assertions.assertEquals(gardenMock.getPostcode(), modelEditGardenForm.getPostcode());
        Assertions.assertEquals(gardenMock.getCountry(), modelEditGardenForm.getCountry());
        Assertions.assertEquals(gardenMock.getSize(), modelEditGardenForm.getSizeDouble());
        verify(gardenService).getGardenById(1L);
    }

    @Test
    public void EditGardenPlantPost_WithInvalidFormData_DoesNotUpdateThePlant() throws Exception {
        Plant originalPlant = new Plant("Poppy", "5", "Red", "26/04/2024", "/images/defaultPlantPic.png", 1L); 
        Plant plant = Mockito.spy(originalPlant);
        // make a copy of the plant so we can check it was unmodified later
        Plant plantCopy = new Plant(plant.getName(), plant.getPlantCount(), plant.getDescription(), plant.getPlantedOnDate(), plant.getPicturePath(), plant.getGardenId());
        when(plant.getId()).thenReturn(1L);
        when(plantService.getPlantById(1L)).thenReturn(plant);

        assertEquals(plant, plantService.getPlantById(1L));

        PlantForm plantForm = PlantForm.fromPlant(plant);

        // edit the plant to have some invalid data
        plantForm.setName("Invalid Name!?@");

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/plants/1/edit").with(csrf())
                .flashAttr("editPlantForm", plantForm))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.view().name("pages/editPlantPage"));

        // check that the plant was not updated
        verify(plantService, Mockito.never()).updatePlant(Mockito.any());
        assertEquals(plantCopy.getName(), plant.getName());
        assertEquals(plantCopy.getPlantCount(), plant.getPlantCount());
        assertEquals(plantCopy.getDescription(), plant.getDescription());
        assertEquals(plantCopy.getPlantedOnDate(), plant.getPlantedOnDate());
        assertEquals(plantCopy.getPicturePath(), plant.getPicturePath());
    }

    @Test
    public void EditGardenPlantPost_WithValidFormData_UpdatesThePlant() throws Exception {
        Plant originalPlant = new Plant("Poppy", "5", "Red", "26/04/2024", "/images/defaultPlantPic.png", 1L); 
        Plant plant = Mockito.spy(originalPlant);
        when(plant.getId()).thenReturn(1L);
        when(plantService.getPlantById(1L)).thenReturn(plant);

        assertEquals(plant, plantService.getPlantById(1L));

        PlantForm plantForm = PlantForm.fromPlant(plant);

        // edit the plant to have some valid data
        plantForm.setName("Violet");

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/plants/1/edit").with(csrf())
                .flashAttr("editPlantForm", plantForm))
        .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
        .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1"));

        // check that the plant was updated
        verify(plantService).updatePlant(plant);
        assertEquals("Violet", plant.getName());
    }

    @Test
    public void BrowseGardensPost_WithQuery_ReturnsPageWithQuerySearch() throws Exception {
        String query = "test";
        List<Garden> searchResults = new ArrayList<>();
        when(gardenService.searchGardens(query)).thenReturn(searchResults);

        mockMvc.perform(MockMvcRequestBuilders.post("/browseGardens").param("query", query).with(csrf()))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name("pages/browseGardensPage"))
            .andExpect(MockMvcResultMatchers.model().attributeExists("gardens"))
            .andExpect(MockMvcResultMatchers.model().attribute("gardens", searchResults))
            .andExpect(MockMvcResultMatchers.model().attribute("query", query));

        verify(gardenService).searchGardens(query);
    }

}
