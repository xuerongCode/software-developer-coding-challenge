package com.auction.controller;

import com.auction.db.model.*;
import com.auction.exception.HttpError;
import com.auction.service.AuctionService;
import com.auction.service.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController()
public class AppController implements ErrorController {

    @Autowired
    private GeneralService generalService;
    @Autowired
    private AuctionService auctionService;

    @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public String selectAllUsers() {
        return printJsonList(generalService.selectAllUsers());
    }

    @GetMapping(value= "/vehicle", produces = MediaType.APPLICATION_JSON_VALUE)
    public String selectAllVechiles() {
        return printJsonList(generalService.selectAllVechiles());
    }

    @GetMapping(value= "/auction", produces = MediaType.APPLICATION_JSON_VALUE)
    public String selectAllAuctions() { return printJsonList(generalService.selectAllAuctions()); }

    @GetMapping(value= "/bid", produces = MediaType.APPLICATION_JSON_VALUE)
    public String selectAllBids() { return printJsonList(generalService.selectAllBids()); }

    @GetMapping(value= "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String findUserById(@PathVariable("id") Long id) {
        return generalService.findUserById(id).toString();
    }

    @GetMapping(value= "/vehicle/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String findVechileById(@PathVariable("id") Long id){ return generalService.findVechileById(id).toString(); }

    @GetMapping(value= "/auction/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String findAuctionById(@PathVariable("id") Long id) {
        return generalService.findAuctionById(id).toString();
    }

    @GetMapping(value= "/bid/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String findBidById(@PathVariable("id") Long id) {
        return generalService.findBidById(id).toString();
    }

    @GetMapping(value= "user/{userId}/bid", produces = MediaType.APPLICATION_JSON_VALUE)
    public String selectBidsByUser(@PathVariable("userId") Long userId) { return printJsonList(generalService.selectBidsByUser(userId)); }

    @GetMapping(value= "auction/{auctionId}/bid", produces = MediaType.APPLICATION_JSON_VALUE)
    public String selectBidsByAuction(@PathVariable("auctionId") Long auctionId) { return printJsonList(generalService.selectBidsByAuction(auctionId)); }

    @GetMapping(value= "vehicle/{vehicleId}/bid", produces = MediaType.APPLICATION_JSON_VALUE)
    public String selectBidsByVehicle(@PathVariable("vehicleId") Long vehicleId) { return printJsonList(generalService.selectBidsByVehicle(vehicleId)); }

    @GetMapping(value = "user/{userId}/vehicle/{vehicleId}/bid", produces = MediaType.APPLICATION_JSON_VALUE)
    public String selectBidsOfVehicleByUser(@PathVariable("userId") Long userId, @PathVariable("vehicleId") Long vehicleId) {
        return printJsonList(generalService.selectBidsOfVehicleByUser(userId, vehicleId));
    }

    @GetMapping(value= "auction/{auctionId}/currentWinUser", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getCurrentWinUser(@PathVariable("auctionId") Long auctionId) {
        User user = generalService.getCurrentWinUser(auctionId);
        return user != null ? user.toString() : "{}";
    }

    @GetMapping(value= "auction/{auctionId}/currentWinBid", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getCurrentWinBid(@PathVariable("auctionId") Long auctionId) {
        Bid bid = generalService.getCurrentWinBid(auctionId);
        return bid != null ? bid.toString() : "{}";
    }

    @PostMapping(path= "/auction/{auctionId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> bidForAuction(@RequestHeader("userAuth") Long userId, @PathVariable("auctionId") Long auctionId, @Valid @RequestBody AmountPost amountPost) {
        return new ResponseEntity<Object>(auctionService.bidForAuction(userId, auctionId, amountPost).toString(), HttpStatus.OK);
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

    @Override
    public String getErrorPath() {
        return null;
    }
}
