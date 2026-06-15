package org.uade.controller;

import org.uade.exception.ValidationException;
import org.uade.model.Attendee;

import java.util.ArrayList;

public class AttendeeController {
    private ArrayList<Attendee> attendees;
    private Runnable onChange = () -> {};

    public AttendeeController() {
        this.attendees = new ArrayList<>();
    }

    public void setOnChange(Runnable onChange) {
        this.onChange = onChange != null ? onChange : () -> {};
    }

    public void addAttendee(Attendee attendee) throws ValidationException {
        if (attendee == null) {
            throw new ValidationException("El asistente no puede ser nulo");
        }
        if (attendees.contains(attendee)) {
            throw new ValidationException("El asistente ya existe en el sistema");
        }
        attendees.add(attendee);
        onChange.run();
    }

    public void editAttendee(Attendee attendee) throws ValidationException {
        if (attendee == null || !attendees.contains(attendee)) {
            throw new ValidationException("El asistente no existe en el sistema");
        }
        onChange.run();
    }

    public void enableAttendee(Attendee attendee) throws ValidationException {
        if (attendee == null || !attendees.contains(attendee)) {
            throw new ValidationException("El asistente no existe en el sistema");
        }
        attendee.setActive(true);
        onChange.run();
    }

    public void disableAttendee(Attendee attendee) throws ValidationException {
        if (attendee == null || !attendees.contains(attendee)) {
            throw new ValidationException("El asistente no existe en el sistema");
        }
        attendee.setActive(false);
        onChange.run();
    }

    public Attendee getAttendeeByID(int id) {
        for (Attendee attendee : attendees) {
            if (attendee.getId() == id) {
                return attendee;
            }
        }
        return null;
    }

    public ArrayList<Attendee> getAttendees() {
        return attendees;
    }

    public ArrayList<Attendee> getActiveAttendees() {
        ArrayList<Attendee> active = new ArrayList<>();
        for (Attendee a : attendees) {
            if (a.isActive()) {
                active.add(a);
            }
        }
        return active;
    }

    public int getNextId() {
        int max = 0;
        for (Attendee a : attendees) {
            if (a.getId() > max) {
                max = a.getId();
            }
        }
        return max + 1;
    }
}
