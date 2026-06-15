package org.uade.controller;

import org.uade.model.Event;

import java.util.ArrayList;
import java.util.Collections;

public class EventMainMenuController {
    private ArrayList<Event> events;
    private Runnable onChange = () -> {};

    public EventMainMenuController() {
        this.events = new ArrayList<>();
    }

    public void setOnChange(Runnable onChange) {
        this.onChange = onChange != null ? onChange : () -> {};
    }

    public void notifyChange() {
        onChange.run();
    }

    public boolean addEvent(Event event) {
        if (event != null) {
            events.add(event);
            onChange.run();
            return true;
        }
        return false;
    }

    public boolean removeEvent(Event event) {
        if (events.contains(event)) {
            events.remove(event);
            onChange.run();
            return true;
        }
        return false;
    }

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
            if (event.getEventIsActive()) {
                activeEvents.add(event);
            }
        }
        return activeEvents;
    }

    public ArrayList<Event> getActiveEventsSortedByDate() {
        ArrayList<Event> sorted = this.getActiveEvents();
        Collections.sort(sorted);
        return sorted;
    }
}
