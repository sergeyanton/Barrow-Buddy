package nz.ac.canterbury.seng302.gardenersgrove.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.repository.GardenRepository;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;

@SpringBootTest
@AutoConfigureMockMvc
public class GardenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private GardenRepository gardenRepository;

    @Autowired
    private GardenService gardenService;

    @BeforeEach
    public void setUp() {
        gardenRepository.deleteAll();
    }

    @Test
    public void CreateGardenPost_WithValidGardenWithEmptySize_CreatesGardenWithNullSize()
            throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/gardens/create").param("gardenName", "gardenName")
                        .param("gardenLocation", "gardenLocation").param("gardenSize", ""))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1")).andDo(result -> {
                    Garden createdGarden = gardenService.getGardenById(1);
                    assertNull(createdGarden.getSize());
                });
    }

    @Test
    public void CreateGardenPost_WithValidGarden_CreatesGardenInService() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/gardens/create").param("gardenName", "gardenName")
                        .param("gardenLocation", "gardenLocation").param("gardenSize", "1"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1")).andDo(result -> {
                    Garden createdGarden = gardenService.getGardenById(1);
                    assertEquals("gardenName", createdGarden.getName());
                    assertEquals("gardenLocation", createdGarden.getLocation());
                    assertEquals(1, createdGarden.getSize());
                });
    }

    @Test
    public void EditGardenPost_WithValidGarden_CreatesGardenInService() throws Exception {
        Garden garden = new Garden("gardenName", "gardenLocation", 1.0);
        gardenRepository.save(garden);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/gardens/1/edit").param("gardenName", "gardenName")
                        .param("gardenLocation", "gardenLocation").param("gardenSize", "2"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1")).andDo(result -> {
                    Garden createdGarden = gardenService.getGardenById(1);
                    assertEquals("gardenName", createdGarden.getName());
                    assertEquals("gardenLocation", createdGarden.getLocation());
                    assertEquals(2.0, createdGarden.getSize());
                });
    }

    @Test
    public void EditGardenPost_ClearSetSize_UpdatesGardenInServiceSizeToNull() throws Exception {
        Garden garden = new Garden("gardenName", "gardenLocation", 1.0);
        gardenRepository.save(garden);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/gardens/1/edit").param("gardenName", "gardenName")
                        .param("gardenLocation", "gardenLocation").param("gardenSize", ""))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/gardens/1")).andDo(result -> {
                    Garden createdGarden = gardenService.getGardenById(1);
                    assertNull(createdGarden.getSize());
                });
    }
}
