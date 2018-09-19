package com.auction.db;

import com.auction.db.model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BidRepoTest {

    @Autowired
    VehicleRepo vehicleRepo;
    @Autowired
    AuctionRepo auctionRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    BidRepo bidRepo;

    private Vehicle storeVehicle;
    private User storeUser;
    private Auction storeAuction;


    @Before
    public void initDB(){
        // Create a Vehicle.
        Vehicle vehicle = new Vehicle();
        vehicle.setName("raptor");
        vehicle.setPrice(new Amount(80000.01, Currency.CAD));
        storeVehicle = vehicleRepo.save(vehicle);

        // Create an Auction.
        Date now = new Date(0);
        Auction raptorAuction = new Auction(now, Long.valueOf(60*30*1000));
        raptorAuction.setVehicle(storeVehicle);
        storeAuction = auctionRepo.save(raptorAuction);

        // Create a User.
        User xuerongUser = new User("Xuerong");
        storeUser = userRepo.save(xuerongUser);

    }

    @Test
    public void saveBid() {
        Bid bid = new Bid();
        bid.setAmount(new Amount(100.0, Currency.CAD));
        bid.setUser(storeUser);
        bid.setAuction(storeAuction);
        bidRepo.save(bid);
    }

    @Test()
    public void findUser() {
        Bid bid = new Bid();
        bid.setAmount(new Amount(100.0, Currency.CAD));
        bid.setUser(storeUser);
        bid.setAuction(storeAuction);
        bid = bidRepo.save(bid);

        Bid store = bidRepo.getOne(bid.getId());

        assertThat(store).isNotEqualTo(null);
        assertThat(store.getUser()).isNotEqualTo(null);
        assertThat(store.getAuction()).isNotEqualTo(null);
        assertThat(store.getAuction().getId()).isEqualTo(storeAuction.getId());
        assertThat(store.getUser().getId()).isEqualTo(storeUser.getId());

    }

    @Test
    public void deleteBid() {
        Bid bid = new Bid();
        bid.setAmount(new Amount(100.0, Currency.CAD));
        bid.setUser(storeUser);
        bid.setAuction(storeAuction);
        bid = bidRepo.save(bid);
        bidRepo.delete(bid);
    }

    @Test
    public void findByAuctionAndUser() {
        Bid bid = new Bid();
        bid.setAmount(new Amount(100.0, Currency.CAD));
        bid.setUser(storeUser);
        bid.setAuction(storeAuction);
        bidRepo.save(bid);

        bid = new Bid();
        bid.setAmount(new Amount(50.0, Currency.CAD));
        bid.setUser(storeUser);
        bid.setAuction(storeAuction);
        bidRepo.save(bid);

        List<Bid> bids = bidRepo.findByAuctionAndUser(storeAuction, storeUser);

        assertThat(bids).isNotEqualTo(null);
        assertThat(bids.size()).isEqualTo(2);
        for (Bid b : bids) {
            assertThat(b.getAmount().getAmount()).isIn(100.0, 50.0);
            assertThat(b.getUser().getId()).isIn(storeUser.getId());
            assertThat(b.getAuction().getId()).isIn(storeAuction.getId());
        }

    }

}
