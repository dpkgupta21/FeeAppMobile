package com.freeappmobile.model;


public class TransactionDTO {


    private String txn_date;
    private String txn_id;
    private String invoice_id;
    private String fee_types;
    private String fees;
    private String institute;
    private String institute_area;
    private String student;
    private String institute_id;
    private String fee_type_slug;
    private String dob;
    private String username;
    private String className = "";

    public String getTxn_date() {
        return txn_date;
    }

    public void setTxn_date(String txn_date) {
        this.txn_date = txn_date;
    }

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

    public String getFee_types() {
        return fee_types;
    }

    public void setFee_types(String fee_types) {
        this.fee_types = fee_types;
    }

    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public String getInstitute_area() {
        return institute_area;
    }

    public void setInstitute_area(String institute_area) {
        this.institute_area = institute_area;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getInstitute_id() {
        return institute_id;
    }

    public void setInstitute_id(String institute_id) {
        this.institute_id = institute_id;
    }

    public String getFee_type_slug() {
        return fee_type_slug;
    }

    public void setFee_type_slug(String fee_type_slug) {
        this.fee_type_slug = fee_type_slug;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
