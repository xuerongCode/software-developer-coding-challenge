package com.auction.db.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name= "auction")
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name= "start_at")
    private Date startAt;
    private Long duration;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="vehicle_id", nullable = false)
    private Vehicle vehicle;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "auction", orphanRemoval = true)
    private Set<Bid> bids;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="win_user_id",nullable = true)
    private User currentWinUser;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="win_bid_id",nullable = true)
    private Bid currentWinBid;


    public Auction() {
    }

    public Auction(Date startAt, Long duration) {
        this.startAt = startAt;
        this.duration = duration;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStartAt() {
        return startAt;
    }

    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }


    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Set<Bid> getBids() {
        return bids;
    }

    public void setBids(Set<Bid> bids) {
        this.bids = bids;
    }

    public User getCurrentWinUser() {
        return currentWinUser;
    }

    public void setCurrentWinUser(User currentWinUser) {
        this.currentWinUser = currentWinUser;
    }

    public Bid getCurrentWinBid() {
        return currentWinBid;
    }

    public void setCurrentWinBid(Bid currentWinBid) {
        this.currentWinBid = currentWinBid;
    }

    @Override
    public String toString() {
        String windBidId = getCurrentWinBid() == null ? "null" : "\"" + getCurrentWinBid().getId() + "\"";
        String winUserId = getCurrentWinUser() == null ? "null" : "\"" + getCurrentWinUser().getId() + "\"";
        return "{\"_class\":\"Auction\""
                + ',' + "\"id\":" + id
                + ',' + "\"startAt\":\"" + startAt + "\""
                + ',' + "\"duration\":" + duration
                + ',' + "\"vehicleId\":" + vehicle.getId()
                + ',' + "\"currentWinUserId\":" + winUserId
                + ',' + "\"currentWinBidId\":" + windBidId
                + "}";
    }
}
