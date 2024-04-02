package nz.ac.canterbury.team1000.gardenersgrove;

import nz.ac.canterbury.team1000.gardenersgrove.service.GardenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;
import nz.ac.canterbury.team1000.gardenersgrove.controller.HomeController;
import nz.ac.canterbury.team1000.gardenersgrove.service.GardenService;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class HomeControllerTest {
    @Mock
    GardenService gardenService;
    @Mock
    UserService userService;

    @InjectMocks
    HomeController homeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        homeController = new HomeController(gardenService, userService);

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
