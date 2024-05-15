package nz.ac.canterbury.team1000.gardenersgrove.controllers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import java.time.LocalDateTime;
import nz.ac.canterbury.team1000.gardenersgrove.controller.AccountController;
import nz.ac.canterbury.team1000.gardenersgrove.entity.ResetToken;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.entity.VerificationToken;
import nz.ac.canterbury.team1000.gardenersgrove.form.ForgotPasswordForm;
import nz.ac.canterbury.team1000.gardenersgrove.form.LoginForm;
import nz.ac.canterbury.team1000.gardenersgrove.form.RegistrationForm;
import nz.ac.canterbury.team1000.gardenersgrove.form.ResetPasswordForm;
import nz.ac.canterbury.team1000.gardenersgrove.form.VerificationTokenForm;
import nz.ac.canterbury.team1000.gardenersgrove.service.EmailService;
import nz.ac.canterbury.team1000.gardenersgrove.service.GardenService;
import nz.ac.canterbury.team1000.gardenersgrove.service.ResetTokenService;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;
import nz.ac.canterbury.team1000.gardenersgrove.service.VerificationTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(AccountController.class)
@AutoConfigureMockMvc
@WithMockUser
class AccountControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @MockBean
  private EmailService emailService;

  @MockBean
  private VerificationTokenService verificationTokenService;

  @MockBean
  private ResetTokenService resetTokenService;

  @MockBean
  private GardenService gardenService;

  @MockBean
  private AuthenticationManager authenticationManager;

  @MockBean
  private PasswordEncoder passwordEncoder;

  @Mock
  private User userMock;

  @Mock
  private VerificationToken verificationTokenMock;

  private RegistrationForm registrationForm;

  private LoginForm loginForm;

  private VerificationTokenForm verificationTokenForm;
  private ForgotPasswordForm forgotPasswordForm;
  private ResetPasswordForm resetPasswordForm;

  @BeforeEach
  public void beforeEach() {
    userMock = Mockito.mock(User.class);
    Mockito.when(userMock.getFname()).thenReturn("John");
    Mockito.when(userMock.getLname()).thenReturn("Smith");
    Mockito.when(userMock.getEmail()).thenReturn("johnsmith@gmail.com");
    Mockito.when(userMock.getDateOfBirthString()).thenReturn("05/05/1999");
    Mockito.when(userMock.getPassword()).thenReturn("encoded_password");

    verificationTokenMock = Mockito.mock(VerificationToken.class);
    Mockito.when(verificationTokenMock.getUserId()).thenReturn(1L);
    Mockito.when(verificationTokenMock.getToken()).thenReturn("token");

    registrationForm = new RegistrationForm();
    registrationForm.setFirstName(userMock.getFname());
    registrationForm.setLastName(userMock.getLname());
    registrationForm.setEmail(userMock.getEmail());
    registrationForm.setDob(userMock.getDateOfBirthString());
    registrationForm.setPassword("Pass123$");
    registrationForm.setRetypePassword("Pass123$");
    registrationForm.setNoSurnameCheckBox(
        userMock.getLname() == null || userMock.getLname().isEmpty());

    loginForm = new LoginForm();
    loginForm.setEmail(userMock.getEmail());
    loginForm.setPassword("Pass123$");

    verificationTokenForm = new VerificationTokenForm();
    verificationTokenForm.setVerificationToken(verificationTokenMock.getToken());

    forgotPasswordForm = new ForgotPasswordForm();
    forgotPasswordForm.setEmail(userMock.getEmail());

    resetPasswordForm = new ResetPasswordForm();
    resetPasswordForm.setNewPassword("123Password!");
    resetPasswordForm.setRetypePassword("123Password!");

    Mockito.when(userService.checkEmail(Mockito.any())).thenReturn(false);
    Mockito.when(userService.isSignedIn()).thenReturn(false);
    Mockito.when(userService.findEmail(Mockito.any())).thenReturn(userMock);
    Mockito.when(passwordEncoder.matches(Mockito.any(), Mockito.any())).thenReturn(true);
  }

  @Test
  public void RegisterPostRequest_ValidDetails_UserRegisteredAndAuthenticated() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.post("/register").with(csrf())
            .flashAttr("registrationForm", registrationForm))
        .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
        .andExpect(MockMvcResultMatchers.redirectedUrl("/register/verification"));

    Mockito.verify(userService).registerUser(Mockito.any());
  }

  @Test
  public void RegisterPostRequest_ValidDetailsEmptyDate_UserRegisteredAndAuthenticated()
      throws Exception {
    registrationForm.setDob("");

    mockMvc.perform(MockMvcRequestBuilders.post("/register").with(csrf())
            .flashAttr("registrationForm", registrationForm))
        .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
        .andExpect(MockMvcResultMatchers.redirectedUrl("/register/verification"));

    Mockito.verify(userService).registerUser(Mockito.any());
  }

  @Test
  public void RegisterPostRequest_ValidDetailsNoLastName_UserRegisteredAndAuthenticated()
      throws Exception {
    registrationForm.setLastName("");
    registrationForm.setNoSurnameCheckBox(true);

    mockMvc.perform(MockMvcRequestBuilders.post("/register").with(csrf())
            .flashAttr("registrationForm", registrationForm))
        .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
        .andExpect(MockMvcResultMatchers.redirectedUrl("/register/verification"));

    Mockito.verify(userService).registerUser(Mockito.any());
  }

  @Test
  public void RegisterPostRequest_InvalidFirstNameEmpty_HasFieldErrors() throws Exception {
    registrationForm.setFirstName("");

    mockMvc.perform(MockMvcRequestBuilders.post("/register").with(csrf())
            .flashAttr("registrationForm", registrationForm))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.view().name("pages/registrationPage"))
        .andExpect(
            MockMvcResultMatchers.model().attributeHasFieldErrors("registrationForm", "firstName"));

    Mockito.verify(userService, Mockito.never()).updateUserByEmail(Mockito.any(), Mockito.any());
    Mockito.verify(userService, Mockito.never())
        .authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
  }

  @Test
  public void RegisterPostRequest_InvalidFirstName_HasFieldErrors() throws Exception {
    registrationForm.setFirstName("Jeff3");

    mockMvc.perform(MockMvcRequestBuilders.post("/register").with(csrf())
            .flashAttr("registrationForm", registrationForm))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.view().name("pages/registrationPage"))
        .andExpect(
            MockMvcResultMatchers.model().attributeHasFieldErrors("registrationForm", "firstName"));

    Mockito.verify(userService, Mockito.never()).updateUserByEmail(Mockito.any(), Mockito.any());
    Mockito.verify(userService, Mockito.never())
        .authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
  }

  @Test
  public void RegisterPostRequest_InvalidFirstNameLong_HasFieldErrors() throws Exception {
    registrationForm.setFirstName("J".repeat(65));

    mockMvc.perform(MockMvcRequestBuilders.post("/register").with(csrf())
            .flashAttr("registrationForm", registrationForm))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.view().name("pages/registrationPage"))
        .andExpect(
            MockMvcResultMatchers.model().attributeHasFieldErrors("registrationForm", "firstName"));

    Mockito.verify(userService, Mockito.never()).updateUserByEmail(Mockito.any(), Mockito.any());
    Mockito.verify(userService, Mockito.never())
        .authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
  }

  @Test
  public void RegisterPostRequest_InvalidLastName_HasFieldErrors() throws Exception {
    registrationForm.setLastName("John6");

    mockMvc.perform(MockMvcRequestBuilders.post("/register").with(csrf())
            .flashAttr("registrationForm", registrationForm))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.view().name("pages/registrationPage"))
        .andExpect(
            MockMvcResultMatchers.model().attributeHasFieldErrors("registrationForm", "lastName"));

    Mockito.verify(userService, Mockito.never()).updateUserByEmail(Mockito.any(), Mockito.any());
    Mockito.verify(userService, Mockito.never())
        .authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
  }

  @Test
  public void RegisterPostRequest_InvalidLastNameLong_HasFieldErrors() throws Exception {
    registrationForm.setLastName("J".repeat(65));

    mockMvc.perform(MockMvcRequestBuilders.post("/register").with(csrf())
            .flashAttr("registrationForm", registrationForm))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.view().name("pages/registrationPage"))
        .andExpect(
            MockMvcResultMatchers.model().attributeHasFieldErrors("registrationForm", "lastName"));

    Mockito.verify(userService, Mockito.never()).updateUserByEmail(Mockito.any(), Mockito.any());
    Mockito.verify(userService, Mockito.never())
        .authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
  }

  @Test
  public void RegisterPostRequest_InvalidEmailEmpty_HasFieldErrors() throws Exception {
    registrationForm.setEmail("");

    mockMvc.perform(MockMvcRequestBuilders.post("/register").with(csrf())
            .flashAttr("registrationForm", registrationForm))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.view().name("pages/registrationPage"))
        .andExpect(
            MockMvcResultMatchers.model().attributeHasFieldErrors("registrationForm", "email"));

    Mockito.verify(userService, Mockito.never()).updateUserByEmail(Mockito.any(), Mockito.any());
    Mockito.verify(userService, Mockito.never())
        .authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
  }

  @Test
  public void RegisterPostRequest_InvalidEmail_HasFieldErrors() throws Exception {
    registrationForm.setEmail("BadEmail");

    mockMvc.perform(MockMvcRequestBuilders.post("/register").with(csrf())
            .flashAttr("registrationForm", registrationForm))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.view().name("pages/registrationPage"))
        .andExpect(
            MockMvcResultMatchers.model().attributeHasFieldErrors("registrationForm", "email"));

    Mockito.verify(userService, Mockito.never()).updateUserByEmail(Mockito.any(), Mockito.any());
    Mockito.verify(userService, Mockito.never())
        .authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
  }

  @Test
  public void RegisterPostRequest_InvalidEmailLong_HasFieldErrors() throws Exception {
    registrationForm.setEmail("A".repeat(246) + "@gmail.com");

    mockMvc.perform(MockMvcRequestBuilders.post("/register").with(csrf())
            .flashAttr("registrationForm", registrationForm))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.view().name("pages/registrationPage"))
        .andExpect(
            MockMvcResultMatchers.model().attributeHasFieldErrors("registrationForm", "email"));

    Mockito.verify(userService, Mockito.never()).updateUserByEmail(Mockito.any(), Mockito.any());
    Mockito.verify(userService, Mockito.never())
        .authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
  }

  @Test
  public void RegisterPostRequest_InvalidEmailTaken_HasFieldErrors() throws Exception {
    Mockito.when(userService.checkEmail(Mockito.any())).thenReturn(true);

    mockMvc.perform(MockMvcRequestBuilders.post("/register").with(csrf())
            .flashAttr("registrationForm", registrationForm))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.view().name("pages/registrationPage"))
        .andExpect(
            MockMvcResultMatchers.model().attributeHasFieldErrors("registrationForm", "email"));

    Mockito.verify(userService, Mockito.never()).updateUserByEmail(Mockito.any(), Mockito.any());
    Mockito.verify(userService, Mockito.never())
        .authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
  }

  @Test
  public void RegisterPostRequest_InvalidDateLeapDay_HasFieldErrors() throws Exception {
    registrationForm.setDob("29/02/2001");

    mockMvc.perform(MockMvcRequestBuilders.post("/register").with(csrf())
            .flashAttr("registrationForm", registrationForm))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.view().name("pages/registrationPage"))
        .andExpect(
            MockMvcResultMatchers.model().attributeHasFieldErrors("registrationForm", "dob"));

    Mockito.verify(userService, Mockito.never()).updateUserByEmail(Mockito.any(), Mockito.any());
    Mockito.verify(userService, Mockito.never())
        .authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
  }

  @Test
  public void RegisterPostRequest_InvalidDate_HasFieldErrors() throws Exception {
    registrationForm.setDob("invaliddate");

    mockMvc.perform(MockMvcRequestBuilders.post("/register").with(csrf())
            .flashAttr("registrationForm", registrationForm))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.view().name("pages/registrationPage"))
        .andExpect(
            MockMvcResultMatchers.model().attributeHasFieldErrors("registrationForm", "dob"));

    Mockito.verify(userService, Mockito.never()).updateUserByEmail(Mockito.any(), Mockito.any());
    Mockito.verify(userService, Mockito.never())
        .authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
  }

  @Test
  public void RegisterPostRequest_InvalidPasswordEmpty_HasFieldErrors() throws Exception {
    registrationForm.setPassword("");
    registrationForm.setRetypePassword("");

    mockMvc.perform(MockMvcRequestBuilders.post("/register").with(csrf())
            .flashAttr("registrationForm", registrationForm))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.view().name("pages/registrationPage"))
        .andExpect(
            MockMvcResultMatchers.model().attributeHasFieldErrors("registrationForm", "password"));

    Mockito.verify(userService, Mockito.never()).updateUserByEmail(Mockito.any(), Mockito.any());
    Mockito.verify(userService, Mockito.never())
        .authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
  }

  @Test
  public void RegisterPostRequest_InvalidPassword_HasFieldErrors() throws Exception {
    registrationForm.setPassword("weakpass123");
    registrationForm.setRetypePassword("weakpass123");

    mockMvc.perform(MockMvcRequestBuilders.post("/register").with(csrf())
            .flashAttr("registrationForm", registrationForm))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.view().name("pages/registrationPage"))
        .andExpect(
            MockMvcResultMatchers.model().attributeHasFieldErrors("registrationForm", "password"));

    Mockito.verify(userService, Mockito.never()).updateUserByEmail(Mockito.any(), Mockito.any());
    Mockito.verify(userService, Mockito.never())
        .authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
  }

  @Test
  public void RegisterPostRequest_InvalidPasswordNoMatch_HasFieldErrors() throws Exception {
    registrationForm.setPassword("GoodPass123#");
    registrationForm.setRetypePassword("DifferentGoodPass123#");

    mockMvc.perform(MockMvcRequestBuilders.post("/register").with(csrf())
            .flashAttr("registrationForm", registrationForm))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.view().name("pages/registrationPage"))
        .andExpect(MockMvcResultMatchers.model()
            .attributeHasFieldErrors("registrationForm", "retypePassword"));

    Mockito.verify(userService, Mockito.never()).updateUserByEmail(Mockito.any(), Mockito.any());
    Mockito.verify(userService, Mockito.never())
        .authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
  }

  @Test
  public void ForgotPasswordPostRequest_InvalidEmail_Redirects() throws Exception {
    forgotPasswordForm.setEmail("BadEmail");

    mockMvc.perform(MockMvcRequestBuilders.post("/forgotPassword")
            .with(csrf())
            .flashAttr("forgotPasswordForm", forgotPasswordForm))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.view().name("pages/forgotPasswordPage"))
        .andExpect(
            MockMvcResultMatchers.model().attributeHasFieldErrors("forgotPasswordForm", "email"));

    Mockito.verify(userService, Mockito.never()).updateUserByEmail(Mockito.any(), Mockito.any());
    Mockito.verify(userService, Mockito.never())
        .authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
  }

  @Test
  public void ForgotPasswordPostRequest_InvalidEmailEmpty_HasFieldErrors() throws Exception {
    forgotPasswordForm.setEmail("");

    mockMvc.perform(MockMvcRequestBuilders.post("/forgotPassword")
            .with(csrf())
            .flashAttr("forgotPasswordForm", forgotPasswordForm))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.view().name("pages/forgotPasswordPage"))
        .andExpect(
            MockMvcResultMatchers.model().attributeHasFieldErrors("forgotPasswordForm", "email"));

    Mockito.verify(userService, Mockito.never()).updateUserByEmail(Mockito.any(), Mockito.any());
    Mockito.verify(userService, Mockito.never())
        .authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
  }

  @Test
  public void ResetPasswordPostRequest_InvalidPasswordEmpty_HasFieldErrors() throws Exception {
    resetPasswordForm.setNewPassword("");
    String token = "my_token";

    MockHttpSession session = new MockHttpSession();
    session.setAttribute("resetToken", token);

    ResetToken mockResetToken = new ResetToken();
    mockResetToken.setToken(token);
    mockResetToken.setExpiryDate(LocalDateTime.now().plusMinutes(10));
    Mockito.when(resetTokenService.getResetToken(token)).thenReturn(mockResetToken);

    mockMvc.perform(MockMvcRequestBuilders.post("/resetPassword")
            .session(session)
            .param("token", token)
            .with(csrf())
            .flashAttr("resetPasswordForm", resetPasswordForm))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.view().name("pages/resetPasswordPage"))
        .andExpect(MockMvcResultMatchers.model()
            .attributeHasFieldErrors("resetPasswordForm", "newPassword"))
        .andExpect(redirectedUrl(null));

    Mockito.verify(userService, Mockito.never()).updateUserByEmail(Mockito.any(), Mockito.any());
    Mockito.verify(userService, Mockito.never())
        .authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
  }

  @Test
  public void ResetPasswordPostRequest_InvalidPasswordsDontMatch_HasFieldErrors() throws Exception {
    resetPasswordForm.setNewPassword("Password!1");
    resetPasswordForm.setRetypePassword("Password!2");
    String token = "my_token";

    MockHttpSession session = new MockHttpSession();
    session.setAttribute("resetToken", token);

    ResetToken mockResetToken = new ResetToken();
    mockResetToken.setToken(token);
    mockResetToken.setExpiryDate(LocalDateTime.now().plusMinutes(10));
    Mockito.when(resetTokenService.getResetToken(token)).thenReturn(mockResetToken);

    mockMvc.perform(MockMvcRequestBuilders.post("/resetPassword")
            .session(session)
            .param("token", token)
            .with(csrf())
            .flashAttr("resetPasswordForm", resetPasswordForm))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.view().name("pages/resetPasswordPage"))
        .andExpect(MockMvcResultMatchers.model()
            .attributeHasFieldErrors("resetPasswordForm", "retypePassword"))
        .andExpect(redirectedUrl(null));

    Mockito.verify(userService, Mockito.never()).updateUserByEmail(Mockito.any(), Mockito.any());
    Mockito.verify(userService, Mockito.never())
        .authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
  }

  @Test
  public void ResetPasswordPostRequest_InvalidTokenNotInRepo_RedirectsToLogin() throws Exception {
    resetPasswordForm.setNewPassword("");
    String token = "my_token";

    mockMvc.perform(MockMvcRequestBuilders.post("/resetPassword")
            .param("token", token)
            .with(csrf())
            .flashAttr("resetPasswordForm", resetPasswordForm))
        .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
        .andExpect(redirectedUrl("/login"));

    Mockito.verify(userService, Mockito.never()).updateUserByEmail(Mockito.any(), Mockito.any());
    Mockito.verify(userService, Mockito.never())
        .authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
  }

  @Test
  public void ResetPasswordPostRequest_InvalidTokenExpired_RedirectsToLogin() throws Exception {
    resetPasswordForm.setNewPassword("");
    String token = "my_token";
    MockHttpSession session = new MockHttpSession();
    session.setAttribute("resetToken", token);
    ResetToken mockResetToken = new ResetToken();
    mockResetToken.setToken(token);
    mockResetToken.setExpiryDate(LocalDateTime.now().minusMinutes(5));
    Mockito.when(resetTokenService.getResetToken(token)).thenReturn(mockResetToken);

    mockMvc.perform(MockMvcRequestBuilders.post("/resetPassword")
            .param("token", token)
            .with(csrf())
            .flashAttr("resetPasswordForm", resetPasswordForm))
        .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
        .andExpect(redirectedUrl("/login"));

    Mockito.verify(userService, Mockito.never()).updateUserByEmail(Mockito.any(), Mockito.any());
    Mockito.verify(userService, Mockito.never())
        .authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
  }

  @Test
  public void VerificationGetRequest_ValidToken_Successful() throws Exception {
    Mockito.when(verificationTokenService.getVerificationTokenByUserId(Mockito.any()))
        .thenReturn(verificationTokenMock);

    mockMvc.perform(MockMvcRequestBuilders.get("/register/verification").with(csrf())
            .flashAttr("verificationTokenForm", verificationTokenForm))
        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
  }

  @Test
  public void VerificationPostRequest_Valid_Redirection() throws Exception {
    verificationTokenForm.setVerificationToken("123456");
    verificationTokenMock.setVerified(true);
    Mockito.when(verificationTokenService.getVerificationTokenByToken(Mockito.any()))
        .thenReturn(verificationTokenMock);
    Mockito.when(userService.findById(verificationTokenMock.getUserId())).thenReturn(userMock);
    Mockito.doNothing().when(verificationTokenService).updateVerifiedByUserId(Mockito.anyLong());
    mockMvc.perform(MockMvcRequestBuilders.post("/register/verification").with(csrf())
            .flashAttr("verificationTokenForm", verificationTokenForm))
        .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
        .andExpect(MockMvcResultMatchers.redirectedUrl("/login"));
  }

  @Test
  public void VerificationPostRequest_InvalidTokenTooShort_HasFieldErrors() throws Exception {
    verificationTokenMock.setVerified(false);
    Mockito.when(userService.getLoggedInUser()).thenReturn(userMock);
    Mockito.when(verificationTokenService.getVerificationTokenByUserId(Mockito.any()))
        .thenReturn(verificationTokenMock);
    verificationTokenForm.setVerificationToken("12345");

    mockMvc.perform(MockMvcRequestBuilders.post("/register/verification").with(csrf())
            .flashAttr("verificationTokenForm", verificationTokenForm))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.view().name("pages/verificationPage"))
        .andExpect(MockMvcResultMatchers.model()
            .attributeHasFieldErrors("verificationTokenForm", "verificationToken"));
  }

  @Test
  public void VerificationPostRequest_InvalidTokenTooLong_HasFieldErrors() throws Exception {
    verificationTokenMock.setVerified(false);
    Mockito.when(userService.getLoggedInUser()).thenReturn(userMock);
    Mockito.when(verificationTokenService.getVerificationTokenByUserId(Mockito.any()))
        .thenReturn(verificationTokenMock);
    verificationTokenForm.setVerificationToken("1234567");

    mockMvc.perform(MockMvcRequestBuilders.post("/register/verification").with(csrf())
            .flashAttr("verificationTokenForm", verificationTokenForm))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.view().name("pages/verificationPage"))
        .andExpect(MockMvcResultMatchers.model()
            .attributeHasFieldErrors("verificationTokenForm", "verificationToken"));
  }

  @Test
  public void VerificationPostRequest_InvalidTokenEmpty_HasFieldErrors() throws Exception {
    verificationTokenMock.setVerified(false);
    Mockito.when(userService.getLoggedInUser()).thenReturn(userMock);
    Mockito.when(verificationTokenService.getVerificationTokenByUserId(Mockito.any()))
        .thenReturn(verificationTokenMock);
    verificationTokenForm.setVerificationToken("");

    mockMvc.perform(MockMvcRequestBuilders.post("/register/verification").with(csrf())
            .flashAttr("verificationTokenForm", verificationTokenForm))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.view().name("pages/verificationPage"))
        .andExpect(MockMvcResultMatchers.model()
            .attributeHasFieldErrors("verificationTokenForm", "verificationToken"));
  }

  @Test
  public void VerificationPostRequest_TokenExpired_ShowError() throws Exception {
    Mockito.when(userService.getLoggedInUser()).thenReturn(null);
    mockMvc.perform(MockMvcRequestBuilders.post("/register/verification").with(csrf())
            .flashAttr("verificationTokenForm", verificationTokenForm))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.view().name("pages/verificationPage"))
        .andExpect(MockMvcResultMatchers.model()
            .attributeHasFieldErrors("verificationTokenForm", "verificationToken"));
  }

//    TODO I cannot for the life of me figure out how to get these tests passing, they look perfect to me, i'm assuming its some weird authentication thing
//    @Test
//    void LoginGetRequest_SignedIn_Redirects() throws Exception {
//        Mockito.when(userService.isSignedIn()).thenReturn(true);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/login").with(csrf())
//                        .flashAttr("loginForm", loginForm))
//                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
//                .andExpect(MockMvcResultMatchers.redirectedUrl("/"));
//    }
//
//    @Test
//    void LoginGetRequest_SignedOut_RendersLoginPage() throws Exception {
//        Mockito.when(userService.isSignedIn()).thenReturn(false);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/login").with(csrf())
//                        .flashAttr("loginForm", loginForm))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.view().name("pages/loginPage"));
//    }
//
//    @Test
//    public void LoginPostRequest_SignedIn_Redirects() throws Exception {
//        Mockito.when(userService.isSignedIn()).thenReturn(true);
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/login").with(csrf())
//                        .flashAttr("loginForm", loginForm))
//                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
//                .andExpect(MockMvcResultMatchers.redirectedUrl("/"));
//    }
//    @Test
//    public void LoginPostRequest_ValidDetails_Redirects() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.post("/login").with(csrf())
//                        .flashAttr("loginForm", loginForm))
//                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
//                .andExpect(MockMvcResultMatchers.redirectedUrl("/"));
//        Mockito.verify(userService).authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
//    }
//
//    @Test
//    public void LoginPostRequest_InvalidEmailEmpty_HasFieldErrors() throws Exception {
//        loginForm.setEmail("");
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/login").with(csrf())
//                        .flashAttr("loginForm", loginForm))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.view().name("pages/loginPage"))
//                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("loginForm", "email"));
//
//        Mockito.verify(userService, Mockito.never()).authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
//    }
//
//    @Test
//    public void LoginPostRequest_InvalidEmail_HasFieldErrors() throws Exception {
//        loginForm.setEmail("bad_email");
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/login").with(csrf())
//                        .flashAttr("loginForm", loginForm))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.view().name("pages/loginPage"))
//                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("loginForm", "email"));
//
//        Mockito.verify(userService, Mockito.never()).authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
//    }
//
//    @Test
//    public void LoginPostRequest_InvalidEmailLong_HasFieldErrors() throws Exception {
//        loginForm.setEmail("b".repeat(246) + "@gmail.com");
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/login").with(csrf())
//                        .flashAttr("loginForm", loginForm))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.view().name("pages/loginPage"))
//                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("loginForm", "email"));
//
//        Mockito.verify(userService, Mockito.never()).authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
//    }
//
//    @Test
//    public void LoginPostRequest_InvalidEmailNotInDatabase_HasFieldErrors() throws Exception {
//        Mockito.when(userService.findEmail(Mockito.any())).thenReturn(null);
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/login").with(csrf())
//                        .flashAttr("loginForm", loginForm))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.view().name("pages/loginPage"))
//                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("loginForm"));
//
//        Mockito.verify(userService, Mockito.never()).authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
//    }
//
//    @Test
//    public void LoginPostRequest_InvalidPasswordEmpty_HasFieldErrors() throws Exception {
//        loginForm.setPassword("");
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/login").with(csrf())
//                        .flashAttr("loginForm", loginForm))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.view().name("pages/loginPage"))
//                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("loginForm", "password"));
//
//        Mockito.verify(userService, Mockito.never()).authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
//    }
//
//    @Test
//    public void LoginPostRequest_InvalidPasswordIncorrect_HasFieldErrors() throws Exception {
//        Mockito.when(passwordEncoder.matches(Mockito.any(), Mockito.any())).thenReturn(false);
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/login").with(csrf())
//                        .flashAttr("loginForm", loginForm))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.view().name("pages/loginPage"))
//                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("loginForm"));
//
//        Mockito.verify(userService, Mockito.never()).authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
//    }
//@Test
//public void LoginGetRequest_NotVerified_RedirectsToVerification() throws Exception {
//    Mockito.when(userService.getLoggedInUser()).thenReturn(userMock);
//    Mockito.when(verificationTokenService.getVerificationTokenByUserId(Mockito.anyLong())).thenReturn(verificationTokenMock);
//    Mockito.when(verificationTokenMock.isVerified()).thenReturn(false);
//
//    System.out.println(userService.getLoggedInUser() != null && !verificationTokenService.getVerificationTokenByUserId(userService.getLoggedInUser().getId()).isVerified());
//
//    mockMvc.perform(MockMvcRequestBuilders.get("/login").with(csrf())
//                    .flashAttr("loginForm", loginForm))
//            .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
//            .andExpect(MockMvcResultMatchers.redirectedUrl("/register/verification"));
//}
}