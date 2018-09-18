package com.auction.db.model;

import javax.persistence.*;
import java.util.Set;

/**
 *  Model for the User
 */
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Bid> bids;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "currentWinUser")
    private Set<Auction> currentWinAuction;

    public User () {

    }

    public User(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Bid> getBids() {
        return bids;
    }

    public void setBids(Set<Bid> bids) {
        this.bids = bids;
    }

    public Set<Auction> getCurrentWinAuction() {
        return currentWinAuction;
    }

    public void setCurrentWinAuction(Set<Auction> currentWinAuction) {
        this.currentWinAuction = currentWinAuction;
    }

    @Override
    public String toString() {
        return "{\"_class\":\"User\""
                + ',' + "\"id\":" + id
                + ',' + "\"name\":\"" + name + "\""
                + "}";
    }
}