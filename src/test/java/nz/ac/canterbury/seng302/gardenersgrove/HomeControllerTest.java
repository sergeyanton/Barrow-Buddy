package nz.ac.canterbury.seng302.gardenersgrove;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void ClickCreateGarden_Anywhere_GoToCorrectForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/gardens/create"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("createGarden"));
    }
}