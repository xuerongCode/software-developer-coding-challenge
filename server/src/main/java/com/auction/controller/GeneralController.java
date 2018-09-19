package com.auction.controller;

import com.auction.db.AuctionRepo;
import com.auction.db.BidRepo;
import com.auction.db.UserRepo;
import com.auction.db.VehicleRepo;
import com.auction.db.model.*;
import com.auction.db.model.Currency;
import com.auction.exception.BidRejectException;
import com.auction.exception.HttpError;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController()
public class GeneralController implements ErrorController {
    private UserRepo userRepo;
    private VehicleRepo vehicleRepo;
    private BidRepo bidRepo;
    private AuctionRepo auctionRepo;

    public GeneralController(UserRepo userRepo, VehicleRepo vehicleRepo, BidRepo bidRepo, AuctionRepo auctionRepo) {
        this.userRepo = userRepo;
        this.vehicleRepo = vehicleRepo;
        this.bidRepo = bidRepo;
        this.auctionRepo = auctionRepo;
    }

    @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public String selectAllUsers() {
        return printJsonList(userRepo.findAll());
    }

    @GetMapping(value= "/vehicle", produces = MediaType.APPLICATION_JSON_VALUE)
    public String selectAllVechiles() {
        return printJsonList(vehicleRepo.findAll());
    }

    @GetMapping(value= "/auction", produces = MediaType.APPLICATION_JSON_VALUE)
    public String selectAllAuctions() {
        return printJsonList(auctionRepo.findAll());
    }

    @GetMapping(value= "/bid", produces = MediaType.APPLICATION_JSON_VALUE)
    public String selectAllBids() { return printJsonList(bidRepo.findAll()); }

    @GetMapping(value= "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String findUserById(@PathVariable("id") Long id) {
        return userRepo.getOne(id).toString();
    }

    @GetMapping(value= "/vehicle/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String findVechileById(@PathVariable("id") Long id){
        return vehicleRepo.getOne(id).toString();
    }

    @GetMapping(value= "/auction/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String findAuctionById(@PathVariable("id") Long id) {
        return auctionRepo.getOne(id).toString();
    }

    @GetMapping(value= "/bid/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String findBidById(@PathVariable("id") Long id) {
        return bidRepo.getOne(id).toString();
    }

    @GetMapping(value= "user/{userId}/bid", produces = MediaType.APPLICATION_JSON_VALUE)
    public String selectBidsByUser(@PathVariable("userId") Long userId) { return printJsonArray(userRepo.getOne(userId).getBids().toArray()); }

    @GetMapping(value= "auction/{auctionId}/bid", produces = MediaType.APPLICATION_JSON_VALUE)
    public String selectBidsByAuction(@PathVariable("auctionId") Long auctionId) {
        return printJsonList(Arrays.asList(auctionRepo.getOne(auctionId).getBids().toArray()));
    }

    @GetMapping(value= "vehicle/{vehicleId}/bid", produces = MediaType.APPLICATION_JSON_VALUE)
    public String selectBidsByVehicle(@PathVariable("vehicleId") Long vehicleId) {
        Vehicle vehicle = vehicleRepo.getOne(vehicleId);
        Auction auction = vehicle.getAuction();
        // TODO: throw excpetion if auction is null.
        Set<Bid> bids = auction.getBids();
        return printJsonList(Arrays.asList(bids.toArray()));

    }

    @GetMapping(value= "auction/{auctionId}/currentWinUser", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getCurrentWinUser(@PathVariable("auctionId") Long auctionId) {
        Auction auction = auctionRepo.getOne(auctionId);
        if (auction.getCurrentWinUser() != null)
            return auction.getCurrentWinUser().toString();
        else
            return "{}";
    }

    @GetMapping(value= "auction/{auctionId}/currentWinBid", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getCurrentWinBid(@PathVariable("auctionId") Long auctionId) {
        Auction auction = auctionRepo.getOne(auctionId);
        if (auction.getCurrentWinBid() != null)
            return auction.getCurrentWinBid().toString();
        else
            return "{}";
    }

    @PostMapping(path= "/auction/{auctionId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> bidForAuction(@RequestHeader("userAuth") Long userId, @PathVariable("auctionId") Long id, @Valid @RequestBody AmountPost amountPost) {
        //TODO: cash auction and lock auction to increase performance
        synchronized (this) {
            // Map amount request.
            Amount bidAmount = new Amount(amountPost.getAmount(), Currency.valueOf(amountPost.getCurrency()));

            // Get foreign reference
            Auction auction = auctionRepo.getOne(id);
            User user = userRepo.getOne(userId);

            Bid currentWinBid = auction.getCurrentWinBid();

            if ( currentWinBid != null ) {
                // Check if user has current highest amount.
                if (user.getId().equals(auction.getCurrentWinUser().getId()))
                    throw new RuntimeException("User has current highest bid for the auction: " + auction.getId());
                // Check If amount is greater than current amount.
                if ( ! (currentWinBid.getAmount().getAmount().compareTo(bidAmount.getAmount()) < 0))
                    throw new BidRejectException("Bid amount is less than current winner: " + currentWinBid.getAmount().getAmount());
                // Check If currency match
                if (! currentWinBid.getAmount().getCurrency().equals(bidAmount.getCurrency()))
                    throw new RuntimeException("The currency for the auction is: "+bidAmount.getCurrency());
            }

            Bid newBid = new Bid();
            newBid.setAuction(auction);
            newBid.setAmount(bidAmount);
            newBid.setUser(user);
            newBid = bidRepo.save(newBid);

            // Update auction.
            auction.setCurrentWinUser(user);
            auction.setCurrentWinBid(newBid);
            auctionRepo.save(auction);

            return new ResponseEntity<Object>(newBid.toString(), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/error", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> handleError() {
        return new ResponseEntity<Object>(new HttpError(HttpStatus.NOT_FOUND, "No Found Service: wrong url"), HttpStatus.NOT_FOUND);
    }


    private String printJsonList(List list) {
        String ret = "[";
        for (int i = 0 ; i<list.size(); i++){
            ret += list.get(i).toString();
            if ( i < list.size()-1)
                ret += ',';
        }
        return ret+= "]";
    }

    private <T> String printJsonArray(T[] Ts) {
        String ret = "[";
        for (int i = 0 ; i<Ts.length; i++){
            ret += Ts[i].toString();
            if ( i < Ts.length-1)
                ret += ',';
        }
        return ret+= "]";
    }


    @Override
    public String getErrorPath() {
        return null;
    }
}
