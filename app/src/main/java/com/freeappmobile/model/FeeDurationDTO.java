package com.freeappmobile.model;


import java.io.Serializable;

public class FeeDurationDTO implements Serializable {
    private String slug;
    private String name;

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
