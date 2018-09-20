package com.auction.service;

import com.auction.db.AuctionRepo;
import com.auction.db.UserRepo;
import com.auction.db.VehicleRepo;
import com.auction.db.model.*;
import com.auction.exception.BidRejectException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.*;
import javax.persistence.EntityNotFoundException;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuctionServiceTest {

    @Autowired
    private AuctionService auctionService;
    @Autowired
    VehicleRepo vehicleRepo;
    @Autowired
    AuctionRepo auctionRepo;
    @Autowired
    UserRepo userRepo;

    private Long userId;
    private Long auctionId;
    private Long secondUserId;
    private AmountPost ranAmount = new AmountPost(1000.11, "CAD");

    @Before
    public void initDB(){
        // Create a Vehicle.
        Vehicle vehicle = new Vehicle();
        vehicle.setName("raptor");
        vehicle.setPrice(new Amount(80000.01, Currency.CAD));
        vehicle = vehicleRepo.save(vehicle);

        // Create an Auction.
        Date now = new Date();
        Auction raptorAuction = new Auction(now, Long.valueOf(60*30*1000));
        raptorAuction.setVehicle(vehicle);
        auctionId = auctionRepo.save(raptorAuction).getId();

        // Create a User.
        User xuerongUser = new User("Xuerong");
        userId = userRepo.save(xuerongUser).getId();
        User Mansi = new User("Xuerong");
        secondUserId = userRepo.save(Mansi).getId();

    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testBidForAuctionWithNonExistingUser() {
        auctionService.bidForAuction(Long.valueOf(11111), auctionId, ranAmount);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testBidForAuctionWithNonExistingAuction() {
        auctionService.bidForAuction(userId, Long.valueOf(11111), ranAmount);
    }

    @Test(expected = BidRejectException.class)
    public void testBidForAuctionWithWrongCurrency() {
        auctionService.bidForAuction(userId, auctionId, new AmountPost(111.11, "USD"));
    }

    @Test(expected = BidRejectException.class)
    public void testSameUserRequestTwoBidsInSequece() {
        auctionService.bidForAuction(userId, auctionId, new AmountPost(111.11, "CAD"));
        auctionService.bidForAuction(userId, auctionId, new AmountPost(123.11, "CAD"));
    }

    @Test(expected = BidRejectException.class)
    public void testBidIsLessThanCurrentHighestBid() {
        auctionService.bidForAuction(userId, auctionId, new AmountPost(111.11, "CAD"));
        auctionService.bidForAuction(secondUserId, auctionId, new AmountPost(33.2, "CAD"));
    }

    @Test
    public void testBid() {
        Bid bid1 = auctionService.bidForAuction(userId, auctionId, new AmountPost(111.11, "CAD"));
        assertThat(bid1).isNotEqualTo(null);
        assertThat(bid1.getAuction().getId()).isEqualTo(auctionId);
        assertThat(bid1.getUser().getId()).isEqualTo(userId);
        assertThat(bid1.getAmount().getAmount()).isEqualTo(Double.valueOf(111.11));
        assertThat(bid1.getAmount().getCurrency()).isEqualTo(Currency.CAD);
        bid1 = auctionService.bidForAuction(secondUserId, auctionId, new AmountPost(133.2, "CAD"));
        assertThat(bid1).isNotEqualTo(null);
        assertThat(bid1.getAuction().getId()).isEqualTo(auctionId);
        assertThat(bid1.getUser().getId()).isEqualTo(secondUserId);
        assertThat(bid1.getAmount().getAmount()).isEqualTo(Double.valueOf(133.2));
        assertThat(bid1.getAmount().getCurrency()).isEqualTo(Currency.CAD);
        bid1 = auctionService.bidForAuction(userId, auctionId, new AmountPost(1111.1, "CAD"));
        assertThat(bid1).isNotEqualTo(null);
        assertThat(bid1.getAuction().getId()).isEqualTo(auctionId);
        assertThat(bid1.getUser().getId()).isEqualTo(userId);
        assertThat(bid1.getAmount().getAmount()).isEqualTo(Double.valueOf(1111.1));
        assertThat(bid1.getAmount().getCurrency()).isEqualTo(Currency.CAD);

    }

}
