package org.uade.model;

import java.util.ArrayList;

public class Hall {
    private final int id;
    private String hallName;
    private String hallType;
    private int maxCapacity;
    private Boolean isAvailable;
    private double price;


    public Hall(int id, String hallName, String hallType, int maxCapacity, Boolean isAvailable, double price) {
        this.id = id;
        this.hallName = hallName;
        this.hallType = hallType;
        this.maxCapacity = maxCapacity;
        this.isAvailable = isAvailable;
        this.price = price;
    }

    public int getId() {
        return this.id;
    }

    public String getHallName() {
        return this.hallName;
    }

    public Boolean getAvailable() {
        return this.isAvailable;
    }

    public String getHallType() {
        return this.hallType;
    }

    public int getMaxCapacity() {
        return this.maxCapacity;
    }

    public double getPrice() {
        return this.price;
    }

    public void setAvailable(Boolean available) {
        this.isAvailable = available;
    }
}
