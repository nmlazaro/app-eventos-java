package org.uade.controller;

import org.uade.exception.ValidationException;
import org.uade.model.Event;
import org.uade.model.Hall;

import java.time.LocalDate;

public class EventFormController {
    private EventMainMenuController eventMainMenuController;

    public EventFormController(EventMainMenuController eventMainMenuController) {
        this.eventMainMenuController = eventMainMenuController;
    }

    public void createEvent(String name, String description, Boolean active, LocalDate date, String location, Hall hall) throws ValidationException {
        validate(name, description, active, date, location, hall);
        Event event = new Event(name, description, active, date, location, hall);
        eventMainMenuController.addEvent(event);
    }

    public void updateEvent(Event event, String name, String description, Boolean active, LocalDate date, String location, Hall hall) throws ValidationException {
        if (event == null) {
            throw new ValidationException("El evento a editar no puede ser nulo");
        }
        validate(name, description, active, date, location, hall);
        event.setName(name);
        event.setDescription(description);
        event.setActive(active);
        event.setDate(date);
        event.setLocation(location);
        event.setHall(hall);
        eventMainMenuController.notifyChange();
    }

    public void deleteEvent(Event event) {
        if (event != null) {
            event.setActive(false);
            eventMainMenuController.notifyChange();
        }
    }

    private void validate(String name, String description, Boolean active, LocalDate date, String location, Hall hall) throws ValidationException {
        if (name == null || name.isBlank()) {
            throw new ValidationException("El nombre del evento no puede estar vacio");
        }
        if (description == null) {
            throw new ValidationException("La descripcion no puede ser nula");
        }
        if (active == null) {
            throw new ValidationException("El estado activo no puede ser nulo");
        }
        if (date == null) {
            throw new ValidationException("La fecha no puede ser nula");
        }
        if (location == null || location.isBlank()) {
            throw new ValidationException("La ubicacion no puede estar vacia");
        }
        if (hall == null) {
            throw new ValidationException("Debe seleccionar un salon");
        }
    }
}
