package com.auction.db.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@NamedQuery(name="Bid.findByAuctionAndUser", query = "SELECT b FROM Bid b WHERE b.auction = ?1 and b.user = ?2")
@Table(name = "bid")
public class Bid {

    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    private Amount amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="auction_id", nullable = false)
    private Auction auction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "currentWinBid")
    private Set<Auction> currentWinAuction;

    public Bid() {
    }

    public Bid(Amount amount) {
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public Auction getAuction() {
        return auction;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Auction> getCurrentWinAuction() {
        return currentWinAuction;
    }

    public void setCurrentWinAuction(Set<Auction> currentWinAuction) {
        this.currentWinAuction = currentWinAuction;
    }

    @Override
    public String toString() {
        return "{\"_class\":\"Bid\""
                + ',' + "\"id\":" + id
                + ',' + "\"amount\":" + amount
                + ',' + "\"userId\":" + user.getId()
                + ',' + "\"auctionId\":" + auction.getId()
                + "}";
    }
}
