package com.auction.service;

import com.auction.db.AuctionRepo;
import com.auction.db.BidRepo;
import com.auction.db.UserRepo;
import com.auction.db.VehicleRepo;
import com.auction.db.model.Auction;
import com.auction.db.model.Bid;
import com.auction.db.model.User;
import com.auction.db.model.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service
public class GeneralService {
    @Autowired
    UserRepo userRepo;
    @Autowired
    private VehicleRepo vehicleRepo;
    @Autowired
    private BidRepo bidRepo;
    @Autowired
    private AuctionRepo auctionRepo;

    public List<User> selectAllUsers() { return userRepo.findAll(); }
    public List<Vehicle> selectAllVechiles() { return vehicleRepo.findAll(); }
    public List<Auction> selectAllAuctions() { return auctionRepo.findAll(); }
    public List<Bid> selectAllBids() { return bidRepo.findAll(); }


    public User findUserById( Long id) { return userRepo.getOne(id); }
    public Vehicle findVechileById( Long id){
        return vehicleRepo.getOne(id);
    }
    public Auction findAuctionById( Long id) {
        return auctionRepo.getOne(id);
    }
    public Bid findBidById( Long id) {
        return bidRepo.getOne(id);
    }

    public List<Object> selectBidsByUser(Long userId) { return Arrays.asList(userRepo.getOne(userId).getBids().toArray()); }
    public List<Object> selectBidsByAuction( Long auctionId) {
        return Arrays.asList(auctionRepo.getOne(auctionId).getBids().toArray());
    }
    public List<Object> selectBidsByVehicle( Long vehicleId) {
        Vehicle vehicle = vehicleRepo.getOne(vehicleId);
        Auction auction = vehicle.getAuction();
        // TODO: throw excpetion if auction is null.
        Set<Bid> bids = auction.getBids();
        return Arrays.asList(bids.toArray());
    }

    public List<Bid> selectBidsOfVehicleByUser(Long userId, Long vehicleId) {
        Vehicle vehicle = vehicleRepo.getOne(vehicleId);
        Auction auction = new Auction();
        auction.setId(vehicle.getAuction().getId());
        User user = new User();
        user.setId(userId);
        return bidRepo.findByAuctionAndUser(auction, user);
    }

    public User getCurrentWinUser(Long auctionId) {
        Auction auction = auctionRepo.getOne(auctionId);
        return auction.getCurrentWinUser();
    }

    public Bid getCurrentWinBid(Long auctionId) {
        Auction auction = auctionRepo.getOne(auctionId);
        return auction.getCurrentWinBid();
    }

    public Bid getCurrentWinBidForVehicle(Long vehicleId) {
        Vehicle vehicle = vehicleRepo.getOne(vehicleId);
        return getCurrentWinBid(vehicle.getAuction().getId());
    }
}
