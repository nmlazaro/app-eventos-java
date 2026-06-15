package org.uade.model;

public class Attendee {
    private final int id;
    private String name;
    private String email;
    private String phone;
    private Boolean active;

    public Attendee(int id, String name, String email, String phone, Boolean active) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.active = active != null ? active : Boolean.TRUE;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPhone() {
        return this.phone;
    }

    public boolean isActive() {
        return this.active != null ? this.active : true;
    }

    public void setName(String name) {
        if (name != null) {
            this.name = name;
        }
    }

    public void setEmail(String email) {
        if (email != null) {
            this.email = email;
        }
    }

    public void setPhone(String phone) {
        if (phone != null) {
            this.phone = phone;
        }
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
