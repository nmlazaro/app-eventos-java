package org.uade.controller;

import org.uade.model.Event;

import java.util.ArrayList;
import java.util.Collections;

public class EventMainMenuController {
    private ArrayList<Event> events;

    public EventMainMenuController() {
        this.events = new ArrayList<>();
    }

    public boolean addEvent(Event event) {
        if(event != null) {
            events.add(event);
            return true;
        }

        return false;
    }

    public boolean removeEvent(Event event) {
        if (events.contains(event)) {
            events.remove(event);

            return true;
        }

        return false;
    }

    // ordenamiento natural por fecha mas cercana YYYY-MM-DD
    public ArrayList<Event> getEventsSortedByDate() {
        ArrayList<Event> sorted = new ArrayList<>(this.events);
        Collections.sort(sorted);

        return sorted;
    }

    public ArrayList<Event> getAllEvents() {
        return this.events;
    }

    public ArrayList<Event> getActiveEvents() {

        ArrayList<Event> activeEvents = new ArrayList<>();

        for (Event event : this.events) {
            if(event.getEventIsActive()) {
                activeEvents.add(event);
            }
        }

        return activeEvents;
    }
}
