package com.auction.controller;

import com.auction.db.UserRepo;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.hamcrest.Matchers.*;

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
        // Init test data.
    }

    @Test
    public void selectAllUsers() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/user").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*]._class", contains("User","User")))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("Xuerong", "Mansi")));

    }
    @Test
    public void selectAllVehicles() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/vehicle").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*]._class", contains("Vehicle", "Vehicle")))
                .andExpect(jsonPath("$[*].id", contains(notNullValue(), notNullValue())))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("Ford-150 raptor", "Honda typeR")))
                .andExpect(jsonPath("$[*].imageUrl", containsInAnyOrder("raptorUrl", "typeRUrl")))
                .andExpect(jsonPath("$[*].price.amount", containsInAnyOrder(90000.55, 45000.11)))
                .andExpect(jsonPath("$[*].price.currency", containsInAnyOrder("CAD", "USD")))
                .andExpect(jsonPath("$[0].auctionId", notNullValue()))
                .andExpect(jsonPath("$[1].auctionId", notNullValue()));
    }
    @Test
    public void selectAllAuctions() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/auction").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*]._class", contains("Auction", "Auction")))
                .andExpect(jsonPath("$[*].id", contains(notNullValue(), notNullValue())))
                .andExpect(jsonPath("$[*].startAt", containsInAnyOrder("1969-12-31 19:00:00.0", "1969-12-31 19:02:00.0")))
                .andExpect(jsonPath("$[*].duration", containsInAnyOrder(1800000, 600000)))
                .andExpect(jsonPath("$[*].vehicleId", contains(notNullValue(), notNullValue())))
                .andExpect(jsonPath("$[*].currentWinUserId", contains(nullValue(), nullValue())))
                .andExpect(jsonPath("$[*].currentWinBidId", contains(nullValue(), nullValue())));

    }
    @Test
    public void selectAllBids() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/bid").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", empty()));
    }
    @Test
    public void findUserById() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/user/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$._class", is("User")))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Xuerong")));

    }
    @Test
    public void findUserByWrongId() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/user/4").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(404))
                .andExpect(jsonPath("$.status", is("NOT_FOUND")))
                .andExpect(jsonPath("$.message", is("Unable to find com.auction.db.model.User with id " + "4")));

    }
    @Test
    public void findVehicleById() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/vehicle/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$._class", is("Vehicle")))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Ford-150 raptor")))
                .andExpect(jsonPath("$.imageUrl", is("raptorUrl")))
                .andExpect(jsonPath("$.price.amount", is(90000.55)))
                .andExpect(jsonPath("$.price.currency", is("CAD")));

    }
    @Test
    public void findVehicleByWrongId() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/vehicle/4").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(404))
                .andExpect(jsonPath("$.status", is("NOT_FOUND")))
                .andExpect(jsonPath("$.message", is("Unable to find com.auction.db.model.Vehicle with id " + "4")));
    }
    @Test
    public void findAuctionById() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/auction/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$._class", is("Auction")))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.startAt", is("1969-12-31 19:00:00.0")))
                .andExpect(jsonPath("$.duration", is(1800000)))
                .andExpect(jsonPath("$.currentWinUserId", nullValue()))
                .andExpect(jsonPath("$.currentWinBidId", nullValue()));

    }
    @Test
    public void findAuctionByWrongId() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/auction/4").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(404))
                .andExpect(jsonPath("$.status", is("NOT_FOUND")))
                .andExpect(jsonPath("$.message", is("Unable to find com.auction.db.model.Auction with id " + "4")));

    }
    @Test
    public void selectBidsByUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/user/1/bid").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

    }
    @Test
    public void selectBidsByAuction() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/auction/1/bid").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", empty()));

    }
    @Test
    public void selectBidsByVehicle() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/vehicle/1/bid").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", empty()));

    }

}
