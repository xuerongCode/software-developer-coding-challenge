package com.auction.db.clr;

import com.auction.db.AuctionRepo;
import com.auction.db.BidRepo;
import com.auction.db.VehicleRepo;
import com.auction.db.model.*;
import com.auction.db.UserRepo;
import org.apache.el.stream.Stream;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ModelCLR implements CommandLineRunner {

    private final UserRepo userRepo;
    private final VehicleRepo vehicleRepo;
    private final AuctionRepo auctionRepo;
    private final BidRepo bidRepo;

    public ModelCLR(UserRepo repo, VehicleRepo vehicleRepo, AuctionRepo auctionRepo, BidRepo bidRepo) {
        this.userRepo = repo;
        this.vehicleRepo = vehicleRepo;
        this.auctionRepo = auctionRepo;
        this.bidRepo = bidRepo;
    }

    @Override
    public void run(String... args) throws Exception {

        // Initial data for web application.
        // Create user Xuerong.
        User xuerongUser = new User("Xuerong");
        xuerongUser = userRepo.save(xuerongUser);

        // Create user Mansi.
        User mansiUser = new User("Mansi");
        mansiUser = userRepo.save(mansiUser);

        // Create vehicle Raptor.
        Vehicle raptorVechicle = new Vehicle("Ford-150 raptor","raptorUrl", new Amount(new Double(90000.55), Currency.CAD));
        raptorVechicle = vehicleRepo.save(raptorVechicle);

        // Create vehicle TypeR.
        Vehicle typerVechicle = new Vehicle("Honda typeR", "typeRUrl", new Amount(new Double(45000.11), Currency.USD));
        typerVechicle = vehicleRepo.save(typerVechicle);

        // Create Auction for both Vehicles.
        Date now = new Date();
        // Auction starts now, and last one hour.
        Auction raptorAuction = new Auction(now, Long.valueOf(60*60*1000));
        // Auction starts now, and last 5 minutes.
        Auction typerAuction = new Auction(new Date(now.getTime()), Long.valueOf(60*5*1000));
        raptorAuction.setVehicle(raptorVechicle);
        typerAuction.setVehicle(typerVechicle);
        auctionRepo.save(raptorAuction);
        auctionRepo.save(typerAuction);
    }
}
