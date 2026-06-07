package org.uade.model;

public class Catering {
    private final int id;
    private String menuName;
    private String description;
    private double price;
    private Boolean isAvailable;

    public Catering(int id, String menuName, String description, double price,  Boolean isAvailable) {
        this.id = id;
        this.menuName = menuName;
        this.description = description;
        this.price = price;
        this.isAvailable = isAvailable;
    }

    public int getId() {
        return this.id;
    }

    public String getMenuName() {
        return this.menuName;
    }

    public String getDescription() {
        return this.description;
    }

    public double getPrice() {
        return this.price;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setAvailable(Boolean available) {
        this.isAvailable = available;
    }

}
