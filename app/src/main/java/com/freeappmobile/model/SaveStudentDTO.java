package com.freeappmobile.model;


import java.io.Serializable;
import java.util.ArrayList;

public class SaveStudentDTO implements Serializable {

    private ArrayList<SaveStudentListDTO> student_details;

    public ArrayList<SaveStudentListDTO> getStudent_details() {
        return student_details;
    }

    public void setStudent_details(ArrayList<SaveStudentListDTO> student_details) {
        this.student_details = student_details;
    }
}
