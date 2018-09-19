package com.auction.db;

import com.auction.db.model.Amount;
import com.auction.db.model.Auction;
import com.auction.db.model.Currency;
import com.auction.db.model.Vehicle;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AuctionRepoTest {

    @Autowired
    VehicleRepo vehicleRepo;
    @Autowired
    AuctionRepo auctionRepo;

    private Vehicle storeVehicle;

    @Before
    public void initDB() {
        Vehicle vehicle = new Vehicle();
        vehicle.setName("raptor");
        vehicle.setPrice(new Amount(80000.01, Currency.CAD));
        storeVehicle = vehicleRepo.save(vehicle);
    }

    @Test
    public void saveAuction(){
        Date now = new Date(0);
        Auction raptorAuction = new Auction(now, Long.valueOf(60*30*1000));
        raptorAuction.setVehicle(storeVehicle);
        auctionRepo.save(raptorAuction);
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void saveAuctionWithNonExistVehicle(){
        Date now = new Date(0);
        Auction raptorAuction = new Auction(now, Long.valueOf(60*30*1000));
        raptorAuction.setVehicle(new Vehicle());
        auctionRepo.save(raptorAuction);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void saveAuctionWithNoVehicle(){
        Date now = new Date(0);
        Auction raptorAuction = new Auction(now, Long.valueOf(60*30*1000));
        auctionRepo.save(raptorAuction);
    }

    @Test
    public void findAuction(){
        Date now = new Date(0);
        Auction raptorAuction = new Auction(now, Long.valueOf(60*30*1000));
        raptorAuction.setVehicle(storeVehicle);
        raptorAuction = auctionRepo.save(raptorAuction);

        Auction save = auctionRepo.getOne(raptorAuction.getId());
        assertThat(save).isNotEqualTo(null);
        assertThat(save.getId()).isEqualTo(raptorAuction.getId());
        assertThat(save.getCurrentWinBid()).isEqualTo(raptorAuction.getCurrentWinBid());
        assertThat(save.getCurrentWinUser()).isEqualTo(raptorAuction.getCurrentWinUser());
    }
}
