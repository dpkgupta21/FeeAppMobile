package com.freeappmobile.model;


import java.io.Serializable;

public class PaymentDTO implements Serializable {

    private String txn_id;
    private String invoice_id;

    public String getTxn_id() {
        return txn_id;
    }

    public void setTxn_id(String txn_id) {
        this.txn_id = txn_id;
    }

    public String getInvoice_id() {
        return invoice_id;
    }

    public void setInvoice_id(String invoice_id) {
        this.invoice_id = invoice_id;
    }
}
