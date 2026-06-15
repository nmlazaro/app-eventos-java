package org.uade.model;

public class Catering extends Resource {
    private String description;
    private double price;

    public Catering(int id, String name, String description, double price, boolean isAvailable) {
        super(id, name, isAvailable);
        this.description = description;
        this.price = price;
    }

    public String getDescription() {
        return this.description;
    }

    public double getPrice() {
        return this.price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public double getEffectivePrice(int hours) {
        return this.price;
    }
}
