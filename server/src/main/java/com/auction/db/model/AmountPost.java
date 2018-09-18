package com.auction.db.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class AmountPost {
    @NotNull
    @Min(0)
    private Double amount;

    @NotNull
    @Pattern(regexp = "^(CAD|USD)$")
    private String currency;

    public AmountPost() {
    }

    public AmountPost(Double amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "{\"_class\":\"Amount\""
                + ',' + "\"amount\":" + amount
                + ',' + "\"currency\":\"" + currency + "\""
                + "}";
    }
}
