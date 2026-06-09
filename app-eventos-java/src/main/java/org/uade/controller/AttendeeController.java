package org.uade.controller;

import org.uade.model.Attendee;

import java.util.ArrayList;

public class AttendeeController {
    private ArrayList<Attendee> attendees;

    public AttendeeController() {
        this.attendees = new ArrayList<>();
    }

    public Boolean addAttendee(Attendee attendee) {
        if (attendee != null && !attendees.contains(attendee)) {
            attendees.add(attendee);
            return true;
        }

        return false;
    }

    public Boolean removeAttendee(Attendee attendee) {
        if (attendee != null && attendees.contains(attendee)) {
            attendees.remove(attendee);
            return true;
        }

        return false;
    }

    public Attendee getAttendyByID(int id) {
        for  (Attendee attendee : attendees) {
            if(attendee.getId() == id) {
                return attendee;
            }
        }

        return null;
    }

    public ArrayList<Attendee> getAttendees() {
        return attendees;
    }
}
