package com.auction.db.model;

import com.auction.db.UserRepo;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GeneralControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepo userRepo;

    @Before
    public void initDB() {
    }

    @Test
    public void selectAllUsers() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/user").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.equalTo("[{\"_class\":\"User\",\"id\":1,\"name\":\"Xuerong\"},{\"_class\":\"User\",\"id\":2,\"name\":\"Mansi\"}]")));
    }

    @Test
    public void findUserById() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/user/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.equalTo("{\"_class\":\"User\",\"id\":1,\"name\":\"Xuerong\"}")));

    }

    @Test
    public void selectAllVehicles() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/vehicle").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.equalTo("[{\"_class\":\"Vehicle\",\"id\":1,\"name\":\"Ford-150 raptor\",\"imageUrl\":\"raptorUrl\",\"price\":{\"_class\":\"Amount\",\"amount\":90000.55,\"currency\":CAD}},{\"_class\":\"Vehicle\",\"id\":2,\"name\":\"Honda typeR\",\"imageUrl\":\"typeRUrl\",\"price\":{\"_class\":\"Amount\",\"amount\":45000.11,\"currency\":USD}}]")));
    }
}
