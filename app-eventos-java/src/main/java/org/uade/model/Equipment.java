package org.uade.model;

public class Equipment extends Resource {
    private double hourPrice;

    public Equipment(int id, String name, boolean isAvailable, double hourPrice) {
        super(id, name, isAvailable);
        this.hourPrice = hourPrice;
    }

    public double getHourPrice() {
        return this.hourPrice;
    }

    public void setHourPrice(double hourPrice) {
        this.hourPrice = hourPrice;
    }

    @Override
    public double getEffectivePrice(int hours) {
        return this.hourPrice * hours;
    }
}
