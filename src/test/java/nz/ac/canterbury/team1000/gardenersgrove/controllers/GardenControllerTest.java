package nz.ac.canterbury.team1000.gardenersgrove.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.util.ArrayList;
import java.util.List;
import nz.ac.canterbury.team1000.gardenersgrove.form.GardenForm;
import nz.ac.canterbury.team1000.gardenersgrove.form.PlantForm;
import nz.ac.canterbury.team1000.gardenersgrove.service.FriendRelationshipService;
import nz.ac.canterbury.team1000.gardenersgrove.service.ModerationService;
import nz.ac.canterbury.team1000.gardenersgrove.service.WeatherService;
import org.hamcrest.Matchers;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    private ModerationService moderationService;

    @MockBean
    private WeatherService weatherService;

    @MockBean
    private VerificationTokenService verificationTokenService;

    @MockBean
    private FriendRelationshipService friendRelationshipService;

    @Mock
    private Garden gardenMock;

    @Mock
    private Garden publicGardenMock;

    private GardenForm gardenForm;

    private GardenForm publicGardenForm;

    @Mock
    private Model model;

    @Mock
    private User loggedInUser;

    // ValueSource does not like it when I use "a".repeat(512) as it does not see it as a constant
    private static final String stringWithLength512 = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
        + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
        + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
        + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
        + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
        + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
        + "aaaaaaaaaaaaaaaaaaaaaaaaaaaa";

    // ValueSource does not like it when I use "a".repeat(513) as it does not see it as a constant
    private static final String stringWithLength513 = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
        + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
        + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
        + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
        + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
        + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
        + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

	private Page<Garden> publicGardens;
	private List<Garden> allGardens;

	@BeforeEach
    public void BeforeEach() {
        loggedInUser = Mockito.mock(User.class);
        Mockito.when(userService.getLoggedInUser()).thenReturn(loggedInUser);
        Mockito.when(loggedInUser
            .getId()).thenReturn(1L);

        gardenMock = Mockito.mock(Garden.class);
		Mockito.when(gardenMock.getId()).thenReturn(1L);
		Mockito.when(gardenMock.getOwner()).thenReturn(loggedInUser);
		Mockito.when(gardenMock.getName()).thenReturn("Hamilton Gardens");
		Mockito.when(gardenMock.getAddress()).thenReturn("13 Hungerford Crescent");
		Mockito.when(gardenMock.getSuburb()).thenReturn("Ilam");
		Mockito.when(gardenMock.getCity()).thenReturn("Hamilton");
		Mockito.when(gardenMock.getPostcode()).thenReturn("3216");
		Mockito.when(gardenMock.getCountry()).thenReturn("New Zealand");
		Mockito.when(gardenMock.getSize()).thenReturn(46.2);
		Mockito.when(gardenMock.getDescription()).thenReturn("A very cool garden");
		Mockito.when(gardenMock.getIsPublic()).thenReturn(false);

        // mock a public garden
        publicGardenMock = Mockito.mock(Garden.class);
        when(publicGardenMock.getId()).thenReturn(2L);
        when(publicGardenMock.getOwner()).thenReturn(loggedInUser);
        when(publicGardenMock.getName()).thenReturn("Public Garden");
        when(publicGardenMock.getAddress()).thenReturn("123 Sesame Street");
        when(publicGardenMock.getSuburb()).thenReturn("Sesame");
        when(publicGardenMock.getCity()).thenReturn("Street");
        when(publicGardenMock.getPostcode()).thenReturn("3216");
        when(publicGardenMock.getCountry()).thenReturn("New Zealand");
        when(publicGardenMock.getSize()).thenReturn(100.0);
		Mockito.when(gardenMock.getDescription()).thenReturn("A great garden");
		when(publicGardenMock.getIsPublic()).thenReturn(true);

        gardenForm = new GardenForm();
        gardenForm.setName(gardenMock.getName());
        gardenForm.setAddress(gardenMock.getAddress());
        gardenForm.setSuburb(gardenMock.getSuburb());
        gardenForm.setCity(gardenMock.getCity());
        gardenForm.setPostcode(gardenMock.getPostcode());
        gardenForm.setCountry(gardenMock.getCountry());
        gardenForm.setSize(gardenMock.getSize().toString());
        gardenForm.setDescription(gardenMock.getDescription());
		gardenForm.setPublicity(gardenMock.getIsPublic());

		// form for public garden
		publicGardenForm = new GardenForm();
		publicGardenForm.setName(publicGardenMock.getName());
		publicGardenForm.setAddress(publicGardenMock.getAddress());
		publicGardenForm.setSuburb(publicGardenMock.getSuburb());
		publicGardenForm.setCity(publicGardenMock.getCity());
		publicGardenForm.setPostcode(publicGardenMock.getPostcode());
		publicGardenForm.setCountry(publicGardenMock.getCountry());
		publicGardenForm.setSize(publicGardenMock.getSize().toString());
		publicGardenForm.setPublicity(publicGardenMock.getIsPublic());

		// array list of public gardens and all gardens
		publicGardens = new PageImpl<>(List.of(publicGardenMock));

		allGardens = new ArrayList<>();
		allGardens.add(publicGardenMock);
		allGardens.add(gardenMock);

		// Mock addGarden(), updateGarden(), and getPlantById to always simply use id = 1
        Mockito.when(gardenService.addGarden(Mockito.any(Garden.class))).thenAnswer(invocation -> {
            Garden addedGarden = invocation.getArgument(0);
            addedGarden.setId(1L);
            return addedGarden;
        });
        Mockito.when(gardenService.updateGardenById(Mockito.anyLong(), Mockito.any(Garden.class)))
                .thenAnswer(invocation -> {
                    Garden updatedGarden = invocation.getArgument(1);
                    updatedGarden.setId(1L);
                    return updatedGarden;
                });

        Mockito.when(gardenService.getGardenById(1L)).thenReturn(gardenMock);
        Mockito.when(moderationService.isAllowed(Mockito.any())).thenReturn(true);
        Mockito.when(gardenService.getPublicGardens(Mockito.any())).thenReturn(publicGardens);
    }

    @Test
    public void CreateGardenPost_WithValidGarden_SavesToService() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/create").with(csrf())
                .flashAttr("createGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1"));

        Mockito.verify(gardenService).addGarden(Mockito.any());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1,5", "Abc", "A b 123", "A-b c. de, f"})
    void CreateGardenPost_ValidName_SavesToService(String nameField) throws Exception {
        gardenForm.setName(nameField);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/create").with(csrf())
                .flashAttr("createGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1"));

        Mockito.verify(gardenService).addGarden(Mockito.any());
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

        Mockito.verify(gardenService).addGarden(Mockito.any());
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

        Mockito.verify(gardenService).addGarden(Mockito.any());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1,5", "Abc", "A b 123", "A-b c. de, f"})
    void CreateGardenPost_ValidCity_SavesToService(String cityField) throws Exception {
        gardenForm.setCity(cityField);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/create").with(csrf())
                .flashAttr("createGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1"));

        Mockito.verify(gardenService).addGarden(Mockito.any());
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

        Mockito.verify(gardenService).addGarden(Mockito.any());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1,5", "Abc", "A b 123", "A-b c. de, f"})
    void CreateGardenPost_ValidCountry_SavesToService(String countryField) throws Exception {
        gardenForm.setCity(countryField);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/create").with(csrf())
                .flashAttr("createGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1"));

        Mockito.verify(gardenService).addGarden(Mockito.any());
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

        Mockito.verify(gardenService).addGarden(Mockito.any());
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"", " ", "\t", "\n", "abc", "abc123", "abc123!@#", stringWithLength512})
    void CreateGardenPost_ValidDescription_SavesToService(String descriptionField) throws Exception {
        gardenForm.setDescription(descriptionField);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/create").with(csrf())
                .flashAttr("createGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1"));

        Mockito.verify(gardenService).addGarden(Mockito.any());
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

        Mockito.verify(gardenService, Mockito.never()).addGarden(Mockito.any());
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

        Mockito.verify(gardenService, Mockito.never()).addGarden(Mockito.any());
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

        Mockito.verify(gardenService, Mockito.never()).addGarden(Mockito.any());
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

        Mockito.verify(gardenService, Mockito.never()).addGarden(Mockito.any());
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

        Mockito.verify(gardenService, Mockito.never()).addGarden(Mockito.any());
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

        Mockito.verify(gardenService, Mockito.never()).addGarden(Mockito.any());
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

        Mockito.verify(gardenService, Mockito.never()).addGarden(Mockito.any());
    }
    @ParameterizedTest
    @ValueSource(
        strings = {"123", "!@#", stringWithLength513})
    void CreateGardenPost_InvalidDescription_ReturnsError(String descriptionField) throws Exception {
        gardenForm.setDescription(descriptionField);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/create").with(csrf())
                .flashAttr("createGardenForm", gardenForm))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name("pages/createGardenPage"))
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("createGardenForm",
                "description"));

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

    @ParameterizedTest
    @ValueSource(strings = {"1,5", "Abc", "A b 123", "A-b c. de, f"})
    void EditGardenPost_ValidName_SavesToService(String nameField) throws Exception {
        gardenForm.setName(nameField);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf())
                .flashAttr("editGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1"));

        Mockito.verify(gardenService).updateGardenById(Mockito.anyLong(), Mockito.any());
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

        Mockito.verify(gardenService).updateGardenById(Mockito.anyLong(), Mockito.any());
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

        Mockito.verify(gardenService).updateGardenById(Mockito.anyLong(), Mockito.any());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1,5", "Abc", "A b 123", "A-b c. de, f"})
    void EditGardenPost_ValidCity_SavesToService(String cityField) throws Exception {
        gardenForm.setCity(cityField);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf())
                .flashAttr("editGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1"));

        Mockito.verify(gardenService).updateGardenById(Mockito.anyLong(), Mockito.any());
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

        Mockito.verify(gardenService).updateGardenById(Mockito.anyLong(), Mockito.any());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1,5", "Abc", "A b 123", "A-b c. de, f"})
    void EditGardenPost_ValidCountry_SavesToService(String countryField) throws Exception {
        gardenForm.setCity(countryField);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf())
                .flashAttr("editGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1"));

        Mockito.verify(gardenService).updateGardenById(Mockito.anyLong(), Mockito.any());
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

        Mockito.verify(gardenService).updateGardenById(Mockito.anyLong(), Mockito.any());
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"", " ", "\t", "\n", "abc", "abc123", "abc123!@#", stringWithLength512})
    void EditGardenPost_ValidDescription_SavesToService(String descriptionField) throws Exception {
        gardenForm.setDescription(descriptionField);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf())
                .flashAttr("editGardenForm", gardenForm))
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
            .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1"));

        Mockito.verify(gardenService).updateGardenById(Mockito.anyLong(), Mockito.any());
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

        Mockito.verify(gardenService, Mockito.never()).updateGardenById(Mockito.anyLong(),
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

        Mockito.verify(gardenService, Mockito.never()).updateGardenById(Mockito.anyLong(),
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

        Mockito.verify(gardenService, Mockito.never()).updateGardenById(Mockito.anyLong(),
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

        Mockito.verify(gardenService, Mockito.never()).updateGardenById(Mockito.anyLong(),
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

        Mockito.verify(gardenService, Mockito.never()).updateGardenById(Mockito.anyLong(),
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

        Mockito.verify(gardenService, Mockito.never()).updateGardenById(Mockito.anyLong(),
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

        Mockito.verify(gardenService, Mockito.never()).updateGardenById(Mockito.anyLong(),
                Mockito.any());
    }

    @ParameterizedTest
    @ValueSource(
        strings = {"123", "!@#", stringWithLength513})
    void EditGardenPost_InvalidDescription_ReturnsError(String descriptionField) throws Exception {
        gardenForm.setDescription(descriptionField);

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf())
                .flashAttr("editGardenForm", gardenForm))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name("pages/editGardenPage"))
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("editGardenForm",
                "description"));

        Mockito.verify(gardenService, Mockito.never()).updateGardenById(Mockito.anyLong(),
            Mockito.any());
    }

    @Test
    void EditGardenPost_ValidSizeWorldsLargestGarden_SavesToService() throws Exception {
        gardenForm.setSize("72000");

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/edit").with(csrf())
                .flashAttr("editGardenForm", gardenForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1"));

        Mockito.verify(gardenService).updateGardenById(Mockito.anyLong(), Mockito.any());
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
        Assertions.assertEquals(gardenMock.getDescription(), modelEditGardenForm.getDescription());
        Assertions.assertEquals(gardenMock.getAddress(), modelEditGardenForm.getAddress());
        Assertions.assertEquals(gardenMock.getSuburb(), modelEditGardenForm.getSuburb());
        Assertions.assertEquals(gardenMock.getCity(), modelEditGardenForm.getCity());
        Assertions.assertEquals(gardenMock.getPostcode(), modelEditGardenForm.getPostcode());
        Assertions.assertEquals(gardenMock.getCountry(), modelEditGardenForm.getCountry());
        Assertions.assertEquals(gardenMock.getSize(), modelEditGardenForm.getSizeDouble());
        Mockito.verify(gardenService).getGardenById(1L);
    }

    @Test
    public void EditGardenPlantPost_WithInvalidFormData_DoesNotUpdateThePlant() throws Exception {
        Plant originalPlant = new Plant("Poppy", "5", "Red", "26/04/2024", "/images/defaultPlantPic.png", 1L); 
        Plant plant = Mockito.spy(originalPlant);
        // make a copy of the plant so we can check it was unmodified later
        Plant plantCopy = new Plant(plant.getName(), plant.getPlantCount(), plant.getDescription(), plant.getPlantedOnDate(), plant.getPicturePath(), plant.getGardenId());
        Mockito.when(plant.getId()).thenReturn(1L);
        Mockito.when(plantService.getPlantById(1L)).thenReturn(plant);

        assertEquals(plant, plantService.getPlantById(1L));

        PlantForm plantForm = PlantForm.fromPlant(plant);

        // edit the plant to have some invalid data
        plantForm.setName("Invalid Name!?@");

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/plants/1/edit").with(csrf())
                .flashAttr("editPlantForm", plantForm))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.view().name("pages/editPlantPage"));

        // check that the plant was not updated
        Mockito.verify(plantService, Mockito.never()).updatePlant(Mockito.any());
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
        Mockito.when(plant.getId()).thenReturn(1L);
        Mockito.when(plantService.getPlantById(1L)).thenReturn(plant);

        assertEquals(plant, plantService.getPlantById(1L));

        PlantForm plantForm = PlantForm.fromPlant(plant);

        // edit the plant to have some valid data
        plantForm.setName("Violet");

        mockMvc.perform(MockMvcRequestBuilders.post("/gardens/1/plants/1/edit").with(csrf())
                .flashAttr("editPlantForm", plantForm))
        .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
        .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1"));

        // check that the plant was updated
        Mockito.verify(plantService).updatePlant(plant);
        assertEquals("Violet", plant.getName());
    }

    @Test
    public void BrowseGardensGet_WithoutQuery_ReturnsPageWithDefaultPublicGardens() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/browseGardens").with(csrf()))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name("pages/browseGardensPage"))
            .andExpect(MockMvcResultMatchers.model().attributeExists("gardens"))
            .andExpect(MockMvcResultMatchers.model().attribute("gardens", publicGardens));
        verify(gardenService).getPublicGardens(Mockito.any());
    }

    @Test
    public void BrowseGardensGet_WithQueryWithMatch_ReturnsPageWithQuerySearch() throws Exception {
        String publicGardenQuery = "Public Garden";
        when(gardenService.searchGardens(Mockito.eq(publicGardenQuery), Mockito.any())).thenReturn(publicGardens);

        mockMvc.perform(MockMvcRequestBuilders.get("/browseGardens").param("query", publicGardenQuery).with(csrf()))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name("pages/browseGardensPage"))
            .andExpect(MockMvcResultMatchers.model().attributeExists("gardens"))
            .andExpect(MockMvcResultMatchers.model().attribute("gardens", publicGardens));
        verify(gardenService).searchGardens(Mockito.eq(publicGardenQuery), Mockito.any());
    }

    @Test
    public void BrowseGardensGet_WithQueryWithNoMatch_ReturnsPageWithEmptyResults() throws Exception {
        String query = "No match";
        when(gardenService.searchGardens(Mockito.eq(query), Mockito.any())).thenReturn(new PageImpl<Garden>(List.of()));

        mockMvc.perform(MockMvcRequestBuilders.get("/browseGardens").param("query", query).with(csrf()))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name("pages/browseGardensPage"))
            .andExpect(MockMvcResultMatchers.model().attributeExists("gardens"))
            .andExpect(MockMvcResultMatchers.model().attribute("gardens", Matchers.equalTo(new PageImpl<Garden>(List.of()))));
        verify(gardenService).searchGardens(Mockito.eq(query), Mockito.any());
    }

    @Test
    public void BrowseGardensGet_WithPrivateGardenQuery_ReturnsPageWithEmptyResults() throws Exception {
        String privateGardenQuery = "Hamilton";
        when(gardenService.searchGardens(Mockito.eq(privateGardenQuery), Mockito.any())).thenReturn(new PageImpl<Garden>(List.of()));

        mockMvc.perform(MockMvcRequestBuilders.get("/browseGardens").param("query", privateGardenQuery).with(csrf()))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name("pages/browseGardensPage"))
            .andExpect(MockMvcResultMatchers.model().attributeExists("gardens"))
            .andExpect(MockMvcResultMatchers.model().attribute("gardens", Matchers.equalTo(new PageImpl<Garden>(List.of()))));
        verify(gardenService).searchGardens(Mockito.eq(privateGardenQuery), Mockito.any());
    }

}
