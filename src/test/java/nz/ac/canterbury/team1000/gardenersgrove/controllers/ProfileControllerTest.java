package nz.ac.canterbury.team1000.gardenersgrove.controllers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import jakarta.servlet.http.HttpServletRequest;
import nz.ac.canterbury.team1000.gardenersgrove.controller.ProfileController;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.form.EditUserForm;
import nz.ac.canterbury.team1000.gardenersgrove.form.UpdatePasswordForm;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;

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

        Mockito.when(userService.getLoggedInUser()).thenReturn(userMock);
//        Mockito.when(userService.checkEmail(Mockito.any())).thenReturn(false);
    }

    @Test
    public void EditUserPost_WithValidDetails_SavesToService() throws Exception {
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
}
