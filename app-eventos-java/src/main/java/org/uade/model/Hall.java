package org.uade.model;

public class Hall {
    private int id;
    private String hallName;
    private String hallFloor;
    private String hallType;
    private int maxCapacity;
    private Boolean isAvailable;


    public Hall(int id, String hallName, String hallFloor, String hallType, int maxCapacity, Boolean isAvailable) {
        this.id = id;
        this.hallName = hallName;
        this.hallFloor = hallFloor;
        this.hallType = hallType;
        this.maxCapacity = maxCapacity;
        this.isAvailable = isAvailable;
    }

    public int getId() {
        return this.id;
    }

    public Boolean getAvailable() {
        return this.isAvailable;
    }

    public void setAvailable(Boolean available) {
        this.isAvailable = available;
    }
}
