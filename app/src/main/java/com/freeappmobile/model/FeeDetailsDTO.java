package com.freeappmobile.model;


import java.io.Serializable;

public class FeeDetailsDTO implements Serializable {
    private  String name ;
    private String amount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
