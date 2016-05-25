package com.freeappmobile.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by YusataInfotech on 5/20/2016.
 */
public class FeeStructureDTO implements Serializable {

    private  String school_fee;
    private String convenience_fee;
    private String total_fee;
    private ArrayList<FeeDetailsDTO> fees_details;

    public String getSchool_fee() {
        return school_fee;
    }

    public void setSchool_fee(String school_fee) {
        this.school_fee = school_fee;
    }

    public String getConvenience_fee() {
        return convenience_fee;
    }

    public void setConvenience_fee(String convenience_fee) {
        this.convenience_fee = convenience_fee;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public ArrayList<FeeDetailsDTO> getFees_details() {
        return fees_details;
    }

    public void setFees_details(ArrayList<FeeDetailsDTO> fees_details) {
        this.fees_details = fees_details;
    }
}
