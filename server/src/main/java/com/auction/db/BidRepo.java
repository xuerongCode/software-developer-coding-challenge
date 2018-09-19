package com.auction.db;

import com.auction.db.model.Auction;
import com.auction.db.model.Bid;
import com.auction.db.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface BidRepo extends JpaRepository<Bid, Long> {
    public List<Bid> findByAuctionAndUser(Auction auction, User user);
}
