package org.uade.controller;

import org.uade.model.Event;
import org.uade.model.Hall;

public class EventFormController {
    private EventMainMenuController eventMainMenuController;

    public EventFormController(EventMainMenuController eventMainMenuController) {
        this.eventMainMenuController = eventMainMenuController;
    }

    public Boolean createEvent(String name, String description, Boolean active, String date, String location, Hall hall) {

        if (name != null && description != null && active != null && location != null) {
            Event event = new Event(name, description, active, date, location, hall);
            eventMainMenuController.addEvent(event);

            return true;
        }

        return false;
    }

    public Boolean updateEvent(Event event, String name, String description, String date, String location, Hall hall) {
        if (event != null) {
            event.setName(name);
            event.setDescription(description);
            event.setDate(date);
            event.setLocation(location);
            event.setHall(hall);

            return true;
        }

        return false;
    }

    public Boolean deleteEvent(Event event) {
        if (event != null) {
            event.setActive(false);
            return true;
        }

        return false;
    }
}
