package nz.ac.canterbury.team1000.gardenersgrove.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import jakarta.servlet.http.HttpServletRequest;
import nz.ac.canterbury.team1000.gardenersgrove.controller.GlobalModelAttributeProvider;
import nz.ac.canterbury.team1000.gardenersgrove.service.GardenService;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;

public class GlobalModelAttributeProviderTest {
    @Mock
    private GardenService gardenService;

    @Mock
    private UserService userService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private GlobalModelAttributeProvider globalModelAttributeProvider;



    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    public void testGetPaginationPath_noQuery_returnsEmptyString() {
        when(request.getRequestURI()).thenReturn("/test");
        when(request.getQueryString()).thenReturn(null);
        when(request.getContextPath()).thenReturn("/test");

        String paginationPath = globalModelAttributeProvider.getPaginationPath(request);

        assertEquals("", paginationPath);
    }

    @Test
    public void testGetPaginationPath_withPageQuery_returnsJustTheRoute() {
        when(request.getRequestURI()).thenReturn("/test/gardens");
        when(request.getQueryString()).thenReturn("page=2");
        when(request.getContextPath()).thenReturn("/test");

        String paginationPath = globalModelAttributeProvider.getPaginationPath(request);

        assertEquals("/gardens", paginationPath);
    }

    @Test
    public void testGetPaginationPath_withOtherQueries_returnsTheRouteAndParamsExceptPage() {
        when(request.getRequestURI()).thenReturn("/test/gardens");
        when(request.getQueryString()).thenReturn("sort=name&page=2");
        when(request.getContextPath()).thenReturn("/test");

        String paginationPath = globalModelAttributeProvider.getPaginationPath(request);

        assertEquals("/gardens?sort=name", paginationPath);
    }

    @Test
    public void testGetPaginationPath_withMultiplePageQueries_returnsRouteAndAllParamsExceptPage() {
        when(request.getRequestURI()).thenReturn("/test/gardens");
        when(request.getQueryString()).thenReturn("sort=name&page=2&filter=active&page=3");
        when(request.getContextPath()).thenReturn("/test");

        String paginationPath = globalModelAttributeProvider.getPaginationPath(request);

        assertEquals("/gardens?sort=name&filter=active", paginationPath);
    }

    @Test
    public void testGardenPaginationPath_withParamsOnRoot_returnsRouteAndAllParamsExceptPage() {
        when(request.getRequestURI()).thenReturn("/test");
        when(request.getQueryString()).thenReturn("sort=name&page=2&filter=active&page=3");
        when(request.getContextPath()).thenReturn("/test");

        String paginationPath = globalModelAttributeProvider.getPaginationPath(request);

        assertEquals("?sort=name&filter=active", paginationPath);
    }

    @Test
    public void testGardenPaginationPath_withParamsNoContextPath_returnsRouteAndAllParamsExceptPage() {
        when(request.getRequestURI()).thenReturn("/gardens");
        when(request.getQueryString()).thenReturn("sort=name&page=2&filter=active&page=3");
        when(request.getContextPath()).thenReturn("");

        String paginationPath = globalModelAttributeProvider.getPaginationPath(request);

        assertEquals("/gardens?sort=name&filter=active", paginationPath);
    }
}