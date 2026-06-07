package org.uade.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Event implements Comparable<Event>{
    private final int id;
    private static int nextId = 0;
    private String name;
    private String description;
    private boolean active;
    private String date;
    private String location;
    private ArrayList<Catering> cateringList;
    private ArrayList<Equipment> equipmentList;
    private ArrayList<Attendee> confirmedAttendees;
    private Hall hall;

    public Event(String name, String description, Boolean active, String date, String location, Hall hall) {
        this.id = nextId++;
        this.name = name;
        this.description = description;
        this.active = active;
        this.date = date;
        this.location = location;
        this.hall = hall;
        this.confirmedAttendees = new ArrayList<>();
        this.cateringList = new ArrayList<>();
        this.equipmentList = new ArrayList<>();

    }

    public Boolean addAttendee(Attendee attendee) {
        if(this.confirmedAttendees.size() < this.hall.getMaxCapacity()) {
            this.confirmedAttendees.add(attendee);
            return true;
        }

        return false;
    }

    public Boolean removeAttendee(Attendee attendee) {
        if(this.confirmedAttendees.contains(attendee)) {
            this.confirmedAttendees.remove(attendee);

            return true;
        }

        return false;
    }

    public Boolean addCatering(Catering catering) {
        // Puse 2 como logica de negocio para el limite de caterings por evento
        if(this.cateringList.size() <= 2 && !this.cateringList.contains(catering)) {
            this.cateringList.add(catering);

            return true;
        }

        return false;
    }

    public Boolean removeCatering(Catering catering) {
        if(this.cateringList.contains(catering)) {
            this.cateringList.remove(catering);

            return true;
        }

        return false;
    }

    public Boolean addEquipment(Equipment equipment) {
        if(!this.equipmentList.contains(equipment)) {
            this.equipmentList.add(equipment);

            return true;
        }

        return false;
    }

    public Boolean removeEquipment(Equipment equipment) {
        if(this.equipmentList.contains(equipment)) {
            this.equipmentList.remove(equipment);

            return true;
        }

        return false;
    }

    @Override
    public int compareTo(Event event) {
        return this.date.compareTo(event.date);
    }

    public double getTotalPrice(int hours) {
        double total = this.hall.getPrice();

        for (Catering catering : cateringList ) {
            total += catering.getPrice();
        }

        for (Equipment equipment : equipmentList ) {
            total += equipment.getTotalPrice(hours);
        }

        return total;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getDate() {
        return this.date;
    }

    public String getLocation() {
        return this.location + " " + this.hall.getHallName();
    }

    public Boolean getEventIsActive() {
        return this.active;
    }

    public String getHallName() {
        return this.hall.getHallName();
    }

    public void setName(String name) {
        if (name != null) {
            this.name = name;
        }
    }

    public void setDescription(String description) {
        if (description != null) {
            this.description = description;
        }
    }

    public void setLocation(String location) {
        if (location != null) {
            this.location = location;
        }
    }

    public void setHall(Hall hall) {
        if (hall != null) {
            this.hall = hall;
        }

    }

    public void setDate(String date) {
        if (date != null) {
            this.date = date;
        }
    }

    public void setActive(Boolean active) {
        if (active != null) {
            this.active = active;
        }
    }


}
