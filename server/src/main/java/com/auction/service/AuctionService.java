package com.auction.service;

import com.auction.db.AuctionRepo;
import com.auction.db.BidRepo;
import com.auction.db.UserRepo;
import com.auction.db.VehicleRepo;
import com.auction.db.model.*;
import com.auction.exception.BidRejectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.validation.Valid;

@Service
public class AuctionService {
    @Autowired
    UserRepo userRepo;
    @Autowired
    private VehicleRepo vehicleRepo;
    @Autowired
    private BidRepo bidRepo;
    @Autowired
    private AuctionRepo auctionRepo;

    public Bid bidForAuction(Long userId, Long auctionId, AmountPost amountPost) {
        //TODO: cash auction and lock auction to increase performance
        synchronized (this) {
            // Map amount request.
            Amount bidAmount = new Amount(amountPost.getAmount(), Currency.valueOf(amountPost.getCurrency()));

            // Get foreign reference
            Auction auction = auctionRepo.getOne(auctionId);
            Vehicle vehicle = auction.getVehicle();
            User user = userRepo.getOne(userId);

            Bid currentWinBid = auction.getCurrentWinBid();

            if ( currentWinBid != null ) {
                // Check if user has current highest amount.
                if (user.getId().equals(auction.getCurrentWinUser().getId()))
                    throw new RuntimeException("User has current highest bid for the auction: " + auction.getId());
                // Check If amount is greater than current amount.
                if ( ! (currentWinBid.getAmount().getAmount().compareTo(bidAmount.getAmount()) < 0))
                    throw new BidRejectException("Bid amount is less than current winner: " + currentWinBid.getAmount().getAmount());
            }
            // Check If currency match
            if (! vehicle.getPrice().getCurrency().equals(bidAmount.getCurrency()))
                throw new RuntimeException("The currency for the auction is: "+vehicle.getPrice().getCurrency());

            Bid newBid = new Bid();
            newBid.setAuction(auction);
            newBid.setAmount(bidAmount);
            newBid.setUser(user);
            newBid = bidRepo.save(newBid);

            // Update auction.
            auction.setCurrentWinUser(user);
            auction.setCurrentWinBid(newBid);
            auctionRepo.save(auction);

            return newBid;
        }
    }
}
