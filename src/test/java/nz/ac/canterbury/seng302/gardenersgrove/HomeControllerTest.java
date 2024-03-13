package nz.ac.canterbury.seng302.gardenersgrove;
import nz.ac.canterbury.seng302.gardenersgrove.controller.HomeController;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class HomeControllerTest {
    @Mock
    UserService userService;
    @InjectMocks
    HomeController homeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        homeController = new HomeController(userService);

        SecurityContextHolder.clearContext();
    }

    @Test
    void getHomePageWhenUserSignedIn() {
        when(userService.isSignedIn()).thenReturn(true);

        String result = homeController.getHome();

        assertEquals("pages/homePage", result);
    }

    @Test
    void getLandingPageWhenUserIsNotSignedIn() {
        when(userService.isSignedIn()).thenReturn(false);

        String result = homeController.getHome();

        assertEquals("pages/landingPage", result);
    }
}
