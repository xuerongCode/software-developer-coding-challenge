package com.auction.db.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Embeddable
public class Amount {

    @Column(name= "amount")
    @NotNull
    @Min(0)
    private Double amount;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    public Amount() {
    }

    public Amount(Double amount, Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "{\"_class\":\"Amount\""
                + ',' + "\"amount\":" + amount
                + ',' + "\"currency\":" + currency
                + "}";
    }
}
