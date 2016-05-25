package com.freeappmobile.model;


import java.io.Serializable;

public class SaveStudentListDTO implements Serializable {


    private String student;
    private String enrollement_no;
    private String institute;
    private String area;
    private String city;
    private String id;

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getEnrollement_no() {
        return enrollement_no;
    }

    public void setEnrollement_no(String enrollement_no) {
        this.enrollement_no = enrollement_no;
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
