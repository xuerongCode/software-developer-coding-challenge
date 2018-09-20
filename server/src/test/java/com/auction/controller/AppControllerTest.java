package com.auction.controller;

import com.auction.db.AuctionRepo;
import com.auction.db.UserRepo;
import com.auction.db.VehicleRepo;
import com.auction.db.model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;

import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AppControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private AuctionRepo auctionRepo;
    @Autowired
    private VehicleRepo vehicleRepo;

    private User storeUser;
    private Auction storeAuction;
    private Vehicle storeVehicle;

    @Before
    public void initDB() {
        // Create a Vehicle.
        Vehicle vehicle = new Vehicle();
        vehicle.setName("C63");
        vehicle.setImageUrl("C63Url");
        vehicle.setPrice(new Amount(80000.01, Currency.CAD));
        storeVehicle = vehicleRepo.save(vehicle);

        // Create an Auction.
        Date now = new Date(0);
        Auction c63Auction = new Auction(now, Long.valueOf(60*30*1000));
        c63Auction.setDuration(Long.valueOf(100000));
        c63Auction.setVehicle(vehicle);
        storeAuction = auctionRepo.save(c63Auction);

        // Create a User.
        User admUser = new User("Adm");
        storeUser = userRepo.save(admUser);
    }

    @Test
    public void selectAllUsers() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/user").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", not(empty())));

    }
    @Test
    public void selectAllVehicles() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/vehicle").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", not(empty())));
    }
    @Test
    public void selectAllAuctions() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/auction").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", not(empty())));

    }

    @Test
    public void findUserById() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/user/"+storeUser.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$._class", is("User")))
                .andExpect(jsonPath("$.id", is(storeUser.getId().intValue())))
                .andExpect(jsonPath("$.name", is(storeUser.getName())));

    }
    @Test
    public void findUserByWrongId() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/user/444").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(404))
                .andExpect(jsonPath("$.status", is("NOT_FOUND")))
                .andExpect(jsonPath("$.message", is("Unable to find com.auction.db.model.User with id " + "444")));

    }
    @Test
    public void findVehicleById() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/vehicle/" + storeVehicle.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$._class", is("Vehicle")))
                .andExpect(jsonPath("$.id", is(storeVehicle.getId().intValue())))
                .andExpect(jsonPath("$.name", is(storeVehicle.getName())))
                .andExpect(jsonPath("$.imageUrl", is(storeVehicle.getImageUrl())))
                .andExpect(jsonPath("$.price.amount", is(storeVehicle.getPrice().getAmount())))
                .andExpect(jsonPath("$.price.currency", is(storeVehicle.getPrice().getCurrency().toString())));

    }
    @Test
    public void findVehicleByWrongId() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/vehicle/444").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(404))
                .andExpect(jsonPath("$.status", is("NOT_FOUND")))
                .andExpect(jsonPath("$.message", is("Unable to find com.auction.db.model.Vehicle with id " + "444")));
    }
    @Test
    public void findAuctionById() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/auction/" + storeAuction.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$._class", is("Auction")))
                .andExpect(jsonPath("$.id", is(storeAuction.getId().intValue())))
                .andExpect(jsonPath("$.duration", is(storeAuction.getDuration().intValue())))
                .andExpect(jsonPath("$.currentWinUserId", nullValue()))
                .andExpect(jsonPath("$.currentWinBidId", nullValue()));

    }
    @Test
    public void findAuctionByWrongId() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/auction/444").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(404))
                .andExpect(jsonPath("$.status", is("NOT_FOUND")))
                .andExpect(jsonPath("$.message", is("Unable to find com.auction.db.model.Auction with id " + "444")));

    }
    @Test
    public void selectBidsByUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/user/"+storeUser.getId()+"/bid").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", empty()));

    }
    @Test
    public void selectBidsByAuction() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/auction/"+storeAuction.getId()+"/bid").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", empty()));

    }
    @Test
    public void selectBidsByVehicle() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/vehicle/"+storeVehicle.getId()+"/bid").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", empty()));

    }
    @Test
    public void getCurrentWinBidForVehicle() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/vehicle/"+storeVehicle.getId()+"/currentWinBid").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("{}"));
    }
    @Test
    public void bidFlowTest() throws Exception {
        // Initial data
        // Create a Vehicle.
        Vehicle vehicle = new Vehicle();
        vehicle.setName("raptor");
        vehicle.setPrice(new Amount(80000.01, Currency.CAD));
        vehicle = vehicleRepo.save(vehicle);

        // Create an Auction.
        Date now = new Date();
        Auction auction = new Auction(now, Long.valueOf(60*30*1000));
        auction.setVehicle(vehicle);
        auction = auctionRepo.save(auction);

        // Create a User.
        User firstUser = new User("Xuerong");
        firstUser = userRepo.save(firstUser);
        User secondUser = new User("mansi");
        secondUser = userRepo.save(secondUser);

        // Test car bidding history.
        mvc.perform(MockMvcRequestBuilders.get("/vehicle/"+vehicle.getId()+"/bid").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", empty()));

        // Test user bidding on a vehicle.
        mvc.perform(MockMvcRequestBuilders.get("/user/"+firstUser.getId()+"/vehicle/"+vehicle.getId()+"/bid").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", empty()));
        mvc.perform(MockMvcRequestBuilders.get("/user/"+secondUser.getId()+"/vehicle/"+vehicle.getId()+"/bid").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", empty()));

        // Test current win Bid for a Vehicle.
        mvc.perform(MockMvcRequestBuilders.get("/vehicle/"+vehicle.getId()+"/currentWinBid").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("{}"));

        // Create first bid.
        Amount firstAmount = new Amount(10.0, Currency.CAD);

        // First bid call;
        mvc.perform(MockMvcRequestBuilders.post("/auction/"+auction.getId()).header("userAuth", firstUser.getId().toString()).content(firstAmount.toString()).contentType("application/json").accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(jsonPath("$.userId", is(firstUser.getId().intValue())))
            .andExpect(jsonPath("$.amount.amount", is(firstAmount.getAmount())))
            .andExpect(jsonPath("$.amount.currency", is(firstAmount.getCurrency().toString())))
            .andExpect(jsonPath("$.auctionId", is(auction.getId().intValue())));

        // Test car bidding history.
        mvc.perform(MockMvcRequestBuilders.get("/vehicle/"+vehicle.getId()+"/bid").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[*].amount.amount", contains(firstAmount.getAmount())));

        // Test firstUser bidding on the vehicle.
        mvc.perform(MockMvcRequestBuilders.get("/user/"+firstUser.getId()+"/vehicle/"+vehicle.getId()+"/bid").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[*].amount.amount", contains(firstAmount.getAmount())));
        // Test secondUser bidding on the vehicle
        mvc.perform(MockMvcRequestBuilders.get("/user/"+secondUser.getId()+"/vehicle/"+vehicle.getId()+"/bid").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", empty()));

        // Test current win Bid for the Vehicle.
        mvc.perform(MockMvcRequestBuilders.get("/vehicle/"+vehicle.getId()+"/currentWinBid").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$._class", is("Bid")))
                .andExpect(jsonPath("$.amount.amount", is(firstAmount.getAmount())))
                .andExpect(jsonPath("$.userId", is(firstUser.getId().intValue())))
                .andExpect(jsonPath("$.auctionId", is(auction.getId().intValue())));

        // Create second bid.
        Amount secondAmount = new Amount(20.0, Currency.CAD);

        // Second bid call.
        mvc.perform(MockMvcRequestBuilders.post("/auction/"+auction.getId()).header("userAuth", secondUser.getId().toString()).content(secondAmount.toString()).contentType("application/json").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.userId", is(secondUser.getId().intValue())))
                .andExpect(jsonPath("$.amount.amount", is(secondAmount.getAmount())))
                .andExpect(jsonPath("$.amount.currency", is(secondAmount.getCurrency().toString())))
                .andExpect(jsonPath("$.auctionId", is(auction.getId().intValue())));

        // Test car bidding history.
        mvc.perform(MockMvcRequestBuilders.get("/vehicle/"+vehicle.getId()+"/bid").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].amount.amount", containsInAnyOrder(firstAmount.getAmount(), secondAmount.getAmount())));

        // Test secondUser bidding on the vehicle.
        mvc.perform(MockMvcRequestBuilders.get("/user/"+secondUser.getId()+"/vehicle/"+vehicle.getId()+"/bid").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[*].amount.amount", contains(secondAmount.getAmount())));

        // Test current win Bid for the Vehicle.
        mvc.perform(MockMvcRequestBuilders.get("/vehicle/"+vehicle.getId()+"/currentWinBid").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$._class", is("Bid")))
                .andExpect(jsonPath("$.amount.amount", is(secondAmount.getAmount())))
                .andExpect(jsonPath("$.userId", is(secondUser.getId().intValue())))
                .andExpect(jsonPath("$.auctionId", is(auction.getId().intValue())));

        // Create third bid.
        Amount thirdAmount = new Amount(30.0, Currency.CAD);

        // Third bid call.
        mvc.perform(MockMvcRequestBuilders.post("/auction/"+auction.getId()).header("userAuth", firstUser.getId().toString()).content(thirdAmount.toString()).contentType("application/json").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.userId", is(firstUser.getId().intValue())))
                .andExpect(jsonPath("$.amount.amount", is(thirdAmount.getAmount())))
                .andExpect(jsonPath("$.amount.currency", is(thirdAmount.getCurrency().toString())))
                .andExpect(jsonPath("$.auctionId", is(auction.getId().intValue())));

        // Test car bidding history.
        mvc.perform(MockMvcRequestBuilders.get("/vehicle/"+vehicle.getId()+"/bid").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[*].amount.amount", containsInAnyOrder(firstAmount.getAmount(), secondAmount.getAmount(), thirdAmount.getAmount())));

        // Test firstUser bidding on the vehicle.
        mvc.perform(MockMvcRequestBuilders.get("/user/"+firstUser.getId()+"/vehicle/"+vehicle.getId()+"/bid").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].amount.amount", containsInAnyOrder(firstAmount.getAmount(), thirdAmount.getAmount())));

        // Test secondUser bidding on the vehicle.
        mvc.perform(MockMvcRequestBuilders.get("/user/"+secondUser.getId()+"/vehicle/"+vehicle.getId()+"/bid").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[*].amount.amount", contains(secondAmount.getAmount())));

        // Test current win Bid for the Vehicle.
        mvc.perform(MockMvcRequestBuilders.get("/vehicle/"+vehicle.getId()+"/currentWinBid").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$._class", is("Bid")))
                .andExpect(jsonPath("$.amount.amount", is(thirdAmount.getAmount())))
                .andExpect(jsonPath("$.userId", is(firstUser.getId().intValue())))
                .andExpect(jsonPath("$.auctionId", is(auction.getId().intValue())));
    }

}
