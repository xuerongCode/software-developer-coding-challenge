package com.auction.controller;

import com.auction.db.AuctionRepo;
import com.auction.db.BidRepo;
import com.auction.db.UserRepo;
import com.auction.db.VehicleRepo;
import com.auction.db.model.*;
import com.auction.db.model.Currency;
import com.auction.exception.HttpError;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.xml.ws.Response;
import java.util.*;
import java.util.stream.Collectors;

@RestController
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

    @GetMapping("/user")
    public String selectAllUsers() {
        return printJsonList(userRepo.findAll());
    }

    @GetMapping("/vehicle")
    public String selectAllVechiles() {
        return printJsonList(vehicleRepo.findAll());
    }

    @GetMapping("/auction")
    public String selectAllAuctions() {
        return printJsonList(auctionRepo.findAll());
    }

    @GetMapping("/user/{id}")
    public String findUserById(@PathVariable("id") Long id) {
        return userRepo.getOne(id).toString();
    }

    @GetMapping("/vehicle/{id}")
    public String findVechileById(@PathVariable("id") Long id){
        return vehicleRepo.getOne(id).toString();
    }

    @GetMapping("/auction/{id}")
    public String findAuctionById(@PathVariable("id") Long id) {
        return auctionRepo.getOne(id).toString();
    }

    @GetMapping("/bid")
    public String selectAllBids() {
        return printJsonList(bidRepo.findAll());
    }

    @GetMapping("/bid/{id}")
    public String findBidById(@PathVariable("id") Long id) {
        return bidRepo.getOne(id).toString();
    }

    @GetMapping("user/{userId}/bid")
    public String selectBidsByUser(@PathVariable("userId") Long userId) {
        return printJsonList(Arrays.asList(userRepo.getOne(userId).getBids().toArray()));
    }

    @GetMapping("auction/{auctionId}/bid")
    public String selectBidsByAuction(@PathVariable("auctionId") Long auctionId) {
        return printJsonList(Arrays.asList(auctionRepo.getOne(auctionId).getBids().toArray()));
    }

    @GetMapping("auction/{auctionId}/currentWinUser")
    public String getCurrentWinUser(@PathVariable("auctionId") Long auctionId) {
        Auction auction = auctionRepo.getOne(auctionId);
        if (auction.getCurrentWinUser() != null)
            return auction.getCurrentWinUser().toString();
        else
            return "{}";
    }

    @GetMapping("auction/{auctionId}/currentWinBid")
    public String getCurrentWinBid(@PathVariable("auctionId") Long auctionId) {
        Auction auction = auctionRepo.getOne(auctionId);
        if (auction.getCurrentWinBid() != null)
            return auction.getCurrentWinBid().toString();
        else
            return "{}";
    }

    @PostMapping(path= "/auction/{auctionId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> bidForAuction(@RequestHeader("userAuth") Long userId, @PathVariable("auctionId") Long id, @Valid @RequestBody AmountPost amountPost) {
        //TODO: thread safe
        // Map amount request.
        Amount bidAmount = new Amount(amountPost.getAmount(), Currency.valueOf(amountPost.getCurrency()));

        // Get foreign key
        Auction auction = auctionRepo.getOne(id);
        User user = userRepo.getOne(userId);

        // Process If amount is greater than curren

        Bid bid = new Bid();
        bid.setUser(user);
        bid.setAuction(auction);
        bid.setAmount(bidAmount);

        Bid saveBid = bidRepo.save(bid);
        auction.setCurrentWinUser(user);
        auction.setCurrentWinBid(saveBid);
        auctionRepo.save(auction);
        return new ResponseEntity<Object>(saveBid.toString(), HttpStatus.OK);
    }

    @RequestMapping("/error")
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


    @Override
    public String getErrorPath() {
        return null;
    }
}
