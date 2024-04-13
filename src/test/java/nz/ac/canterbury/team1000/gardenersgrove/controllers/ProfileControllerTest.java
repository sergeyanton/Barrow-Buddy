package nz.ac.canterbury.team1000.gardenersgrove.controllers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import nz.ac.canterbury.team1000.gardenersgrove.controller.ProfileController;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.form.EditUserForm;
import nz.ac.canterbury.team1000.gardenersgrove.form.UpdatePasswordForm;
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
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;

@WebMvcTest(controllers = ProfileController.class)
@AutoConfigureMockMvc
@WithMockUser
public class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Mock
    private User userMock;
    private EditUserForm editUserForm;
    private UpdatePasswordForm updatePasswordForm;

    @BeforeEach
    public void BeforeEach() {
        userMock = Mockito.mock(User.class);
        Mockito.when(userMock.getEmail()).thenReturn("johnsmith@gmail.com");

        editUserForm = new EditUserForm();
        editUserForm.setFirstName("John");
        editUserForm.setLastName("Smith");
        editUserForm.setNoSurnameCheckBox(false);
        editUserForm.setEmail("johnsmith@gmail.com");
        editUserForm.setDob("05/05/1999");

        updatePasswordForm.setPassword("Pass123$");
        updatePasswordForm.setNewPassword("NewPass456&");
        updatePasswordForm.setRetypeNewPassword("NewPass456&");

        Mockito.when(userService.getLoggedInUser()).thenReturn(userMock);
        Mockito.when(userService.checkEmail(Mockito.any())).thenReturn(true);
    }

    @Test
    public void EditUserPost_WithNoChange_SavesToService() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/editProfile").with(csrf())
                        .flashAttr("editUserForm", editUserForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/profile"));

        Mockito.verify(userService).updateUserByEmail(Mockito.any(), Mockito.any());
        Mockito.verify(userService).authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    public void EditUserPost_WithValidChange_SavesToService() throws Exception {
        editUserForm.setFirstName("Jake");
        editUserForm.setLastName("Thomas");
        editUserForm.setNoSurnameCheckBox(false);
        editUserForm.setEmail("jakethomas@hotmail.com");
        Mockito.when(userService.checkEmail(Mockito.any())).thenReturn(false);
        editUserForm.setDob("01/06/1998");

        mockMvc.perform(MockMvcRequestBuilders.post("/editProfile").with(csrf())
                        .flashAttr("editUserForm", editUserForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/profile"));

        Mockito.verify(userService).updateUserByEmail(Mockito.any(), Mockito.any());
        Mockito.verify(userService).authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    public void EditUserPost_WithValidEmptyDate_SavesToService() throws Exception {
        editUserForm.setDob("");

        mockMvc.perform(MockMvcRequestBuilders.post("/editProfile").with(csrf())
                        .flashAttr("editUserForm", editUserForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/profile"));

        Mockito.verify(userService).updateUserByEmail(Mockito.any(), Mockito.any());
        Mockito.verify(userService).authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    public void EditUserPost_WithValidNoSurname_SavesToService() throws Exception {
        editUserForm.setLastName("");
        editUserForm.setNoSurnameCheckBox(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/editProfile").with(csrf())
                        .flashAttr("editUserForm", editUserForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/profile"));

        Mockito.verify(userService).updateUserByEmail(Mockito.any(), Mockito.any());
        Mockito.verify(userService).authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    public void EditUserPost_WithInvalidFirstNameEmpty_HasFieldErrors() throws Exception {
        editUserForm.setFirstName("");

        mockMvc.perform(MockMvcRequestBuilders.post("/editProfile").with(csrf())
                        .flashAttr("editUserForm", editUserForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/editProfilePage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("editUserForm", "firstName"));

        Mockito.verify(userService, Mockito.never()).updateUserByEmail(Mockito.any(), Mockito.any());
        Mockito.verify(userService, Mockito.never()).authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    public void EditUserPost_WithInvalidFirstName_HasFieldErrors() throws Exception {
        editUserForm.setFirstName("Jeff3");

        mockMvc.perform(MockMvcRequestBuilders.post("/editProfile").with(csrf())
                        .flashAttr("editUserForm", editUserForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/editProfilePage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("editUserForm", "firstName"));

        Mockito.verify(userService, Mockito.never()).updateUserByEmail(Mockito.any(), Mockito.any());
        Mockito.verify(userService, Mockito.never()).authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
    }
    @Test
    public void EditUserPost_WithInvalidFirstNameLong_HasFieldErrors() throws Exception {
        editUserForm.setFirstName("A".repeat(65));

        mockMvc.perform(MockMvcRequestBuilders.post("/editProfile").with(csrf())
                        .flashAttr("editUserForm", editUserForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/editProfilePage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("editUserForm", "firstName"));

        Mockito.verify(userService, Mockito.never()).updateUserByEmail(Mockito.any(), Mockito.any());
        Mockito.verify(userService, Mockito.never()).authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    public void EditUserPost_WithInvalidEmailEmpty_HasFieldErrors() throws Exception {
        editUserForm.setEmail("");
        Mockito.when(userService.checkEmail(Mockito.any())).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.post("/editProfile").with(csrf())
                        .flashAttr("editUserForm", editUserForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/editProfilePage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("editUserForm", "email"));

        Mockito.verify(userService, Mockito.never()).updateUserByEmail(Mockito.any(), Mockito.any());
        Mockito.verify(userService, Mockito.never()).authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    public void EditUserPost_WithInvalidEmail_HasFieldErrors() throws Exception {
        editUserForm.setEmail("notanemail");
        Mockito.when(userService.checkEmail(Mockito.any())).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.post("/editProfile").with(csrf())
                        .flashAttr("editUserForm", editUserForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/editProfilePage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("editUserForm", "email"));

        Mockito.verify(userService, Mockito.never()).updateUserByEmail(Mockito.any(), Mockito.any());
        Mockito.verify(userService, Mockito.never()).authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    public void EditUserPost_WithInvalidEmailTaken_HasFieldErrors() throws Exception {
        editUserForm.setEmail("takenEmail@hotmail.com");
        Mockito.when(userService.checkEmail(Mockito.any())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/editProfile").with(csrf())
                        .flashAttr("editUserForm", editUserForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/editProfilePage"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("editUserForm", "email"));

        Mockito.verify(userService, Mockito.never()).updateUserByEmail(Mockito.any(), Mockito.any());
        Mockito.verify(userService, Mockito.never()).authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
    }
}
