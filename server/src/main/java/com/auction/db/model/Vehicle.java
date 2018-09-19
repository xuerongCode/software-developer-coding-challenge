package com.auction.db.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

/**
 *  Model for the Vechicle
 */
@Entity
@Table(name = "vehicle")
public class Vehicle {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @Column(name= "image_url")
    private String imageUrl;
    @OneToOne(mappedBy = "vehicle", fetch = FetchType.LAZY, orphanRemoval = false)
    private Auction auction;
    @Embedded
    private Amount price;

    public Vehicle() {
    }

    public Vehicle(String name, String imageUrl, Amount price) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Auction getAuction() {
        return auction;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
    }

    public Amount getPrice() {
        return price;
    }

    public void setPrice(Amount price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {

        String ret =  "{\"_class\":\"Vehicle\""
                + ',' + "\"id\":" + id
                + ',' + "\"name\":\"" + name + "\""
                + ',' + "\"imageUrl\":\"" + imageUrl + "\""
                + ',' + "\"price\":" + price;
        if (getAuction() != null)
            ret = ret + ',' + "\"auctionId\":\"" + getAuction().getId() + "\"";
        else
            ret = ret + ',' + "\"auctionId\":" + null;

        return ret + "}";
    }
}
