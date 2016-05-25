package com.freeappmobile.model;


import java.io.Serializable;

public class StudentDTO implements Serializable {

    private String name;
    private String dob;
    private String class_division;
    private String institute;
    private String academic_year;
    private String place;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getClass_division() {
        return class_division;
    }

    public void setClass_division(String class_division) {
        this.class_division = class_division;
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public String getAcademic_year() {
        return academic_year;
    }

    public void setAcademic_year(String academic_year) {
        this.academic_year = academic_year;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
