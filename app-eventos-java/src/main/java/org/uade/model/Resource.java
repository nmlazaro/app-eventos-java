package org.uade.model;

public abstract class Resource {
    protected final int id;
    protected String name;
    protected boolean isAvailable;

    public Resource(int id, String name, boolean isAvailable) {
        this.id = id;
        this.name = name;
        this.isAvailable = isAvailable;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public boolean isAvailable() {
        return this.isAvailable;
    }

    public void setName(String name) {
        if (name != null) {
            this.name = name;
        }
    }

    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }

    public abstract double getEffectivePrice(int hours);
}
