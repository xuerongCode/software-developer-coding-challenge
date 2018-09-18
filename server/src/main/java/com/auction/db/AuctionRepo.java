package com.auction.db;

import com.auction.db.model.Auction;
import com.auction.db.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface AuctionRepo extends JpaRepository<Auction, Long> {

}
