package org.uade.model;

public class Hall extends Resource {
    private String hallType;
    private int maxCapacity;
    private double price;

    public Hall(int id, String name, String hallType, int maxCapacity, boolean isAvailable, double price) {
        super(id, name, isAvailable);
        this.hallType = hallType;
        this.maxCapacity = maxCapacity;
        this.price = price;
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

    public void setHallType(String hallType) {
        if (hallType != null) {
            this.hallType = hallType;
        }
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public double getEffectivePrice(int hours) {
        return this.price;
    }
}
