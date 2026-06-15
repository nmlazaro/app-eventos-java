package org.uade.model;

import org.uade.exception.DuplicateResourceException;
import org.uade.exception.HallCapacityExceededException;
import org.uade.exception.ResourceLimitExceededException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Event implements Comparable<Event>{
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public static final int MAX_CATERING_PER_EVENT = 2;

    private final int id;
    private static int nextId = 0;
    private String name;
    private String description;
    private boolean active;
    private LocalDate date;
    private String location;
    private ArrayList<Catering> cateringList;
    private ArrayList<Equipment> equipmentList;
    private ArrayList<Attendee> confirmedAttendees;
    private Hall hall;

    public Event(String name, String description, Boolean active, LocalDate date, String location, Hall hall) {
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

    public void addAttendee(Attendee attendee) throws DuplicateResourceException, HallCapacityExceededException {
        if (this.confirmedAttendees.contains(attendee)) {
            throw new DuplicateResourceException("El asistente ya esta inscripto en el evento");
        }
        if (this.confirmedAttendees.size() >= this.hall.getMaxCapacity()) {
            throw new HallCapacityExceededException("El salon llego a su capacidad maxima (" + this.hall.getMaxCapacity() + ")");
        }
        this.confirmedAttendees.add(attendee);
    }

    public void removeAttendee(Attendee attendee) {
        this.confirmedAttendees.remove(attendee);
    }

    public void addCatering(Catering catering) throws DuplicateResourceException, ResourceLimitExceededException {
        if (this.cateringList.contains(catering)) {
            throw new DuplicateResourceException("El catering ya esta agregado al evento");
        }
        if (this.cateringList.size() >= MAX_CATERING_PER_EVENT) {
            throw new ResourceLimitExceededException("El limite es de " + MAX_CATERING_PER_EVENT + " caterings por evento");
        }
        this.cateringList.add(catering);
    }

    public void removeCatering(Catering catering) {
        this.cateringList.remove(catering);
    }

    public void addEquipment(Equipment equipment) throws DuplicateResourceException {
        if (this.equipmentList.contains(equipment)) {
            throw new DuplicateResourceException("El equipamiento ya esta agregado al evento");
        }
        this.equipmentList.add(equipment);
    }

    public void removeEquipment(Equipment equipment) {
        this.equipmentList.remove(equipment);
    }

    @Override
    public int compareTo(Event event) {
        return this.date.compareTo(event.date);
    }

    public double getTotalPrice(int hours) {
        double total = this.hall.getEffectivePrice(hours);

        for (Catering catering : cateringList ) {
            total += catering.getEffectivePrice(hours);
        }

        for (Equipment equipment : equipmentList ) {
            total += equipment.getEffectivePrice(hours);
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

    public LocalDate getDate() {
        return this.date;
    }

    public String getFormattedDate() {
        return this.date.format(DATE_FORMATTER);
    }

    public String getLocation() {
        return this.location;
    }

    public String getFullLocation() {
        return this.location + " - " + this.hall.getName();
    }

    public Boolean getEventIsActive() {
        return this.active;
    }

    public Hall getHall() {
        return this.hall;
    }

    public String getHallName() {
        return this.hall.getName();
    }

    public ArrayList<Attendee> getConfirmedAttendees() {
        return this.confirmedAttendees;
    }

    public ArrayList<Catering> getCateringList() {
        return this.cateringList;
    }

    public ArrayList<Equipment> getEquipmentList() {
        return this.equipmentList;
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

    public void setDate(LocalDate date) {
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
