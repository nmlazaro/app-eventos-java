package org.uade.model;

public class Equipment {
    private final int id;
    private String name;
    private Boolean isAvailable;
    private double hourPrice;

    public Equipment(int id, String name, Boolean isAvailable, double price) {
        this.id = id;
        this.name = name;
        this.isAvailable = isAvailable;
        this.hourPrice = price;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Boolean getIsAvailable() {
        return this.isAvailable;
    }

    public double getHourPrice() {
        return this.hourPrice;
    }

    public double getTotalPrice(int hours) {
        return this.hourPrice * hours;
    }

    public void setAvailable(Boolean available) {
        this.isAvailable = available;
    }

}
