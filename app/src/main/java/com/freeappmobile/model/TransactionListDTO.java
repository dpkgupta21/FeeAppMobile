package com.freeappmobile.model;


import java.io.Serializable;
import java.util.ArrayList;

public class TransactionListDTO implements Serializable {
    private ArrayList<TransactionDTO> payment_details;

    public ArrayList<TransactionDTO> getPayment_details() {
        return payment_details;
    }

    public void setPayment_details(ArrayList<TransactionDTO> payment_details) {
        this.payment_details = payment_details;
    }
}
