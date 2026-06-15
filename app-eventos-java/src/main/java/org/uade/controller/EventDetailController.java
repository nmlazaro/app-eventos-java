package org.uade.controller;

import org.uade.exception.DuplicateResourceException;
import org.uade.exception.HallCapacityExceededException;
import org.uade.exception.ResourceLimitExceededException;
import org.uade.exception.ValidationException;
import org.uade.model.Attendee;
import org.uade.model.Catering;
import org.uade.model.Equipment;
import org.uade.model.Event;

public class EventDetailController {
    private Event event;
    private AttendeeController attendeeController;
    private CateringController cateringController;
    private EquipmentController equipmentController;
    private Runnable onChange = () -> {};

    public EventDetailController(Event event,
                                 AttendeeController attendeeController,
                                 CateringController cateringController,
                                 EquipmentController equipmentController) {
        this.event = event;
        this.attendeeController = attendeeController;
        this.cateringController = cateringController;
        this.equipmentController = equipmentController;
    }

    public void setOnChange(Runnable onChange) {
        this.onChange = onChange != null ? onChange : () -> {};
    }

    public Event getEvent() {
        return this.event;
    }

    public void addAttendeeToEvent(Attendee attendee) throws ValidationException, DuplicateResourceException, HallCapacityExceededException {
        if (attendee == null) {
            throw new ValidationException("El asistente no puede ser nulo");
        }
        if (!attendeeController.getAttendees().contains(attendee)) {
            throw new ValidationException("El asistente no existe en el sistema");
        }
        event.addAttendee(attendee);
        onChange.run();
    }

    public void removeAttendeeFromEvent(Attendee attendee) {
        if (attendee != null) {
            event.removeAttendee(attendee);
            onChange.run();
        }
    }

    public void addCateringToEvent(Catering catering) throws ValidationException, DuplicateResourceException, ResourceLimitExceededException {
        if (catering == null) {
            throw new ValidationException("El catering no puede ser nulo");
        }
        if (!cateringController.getAll().contains(catering)) {
            throw new ValidationException("El catering no existe en el sistema");
        }
        event.addCatering(catering);
        onChange.run();
    }

    public void removeCateringFromEvent(Catering catering) {
        if (catering != null) {
            event.removeCatering(catering);
            onChange.run();
        }
    }

    public void addEquipmentToEvent(Equipment equipment) throws ValidationException, DuplicateResourceException {
        if (equipment == null) {
            throw new ValidationException("El equipamiento no puede ser nulo");
        }
        if (!equipmentController.getAll().contains(equipment)) {
            throw new ValidationException("El equipamiento no existe en el sistema");
        }
        event.addEquipment(equipment);
        onChange.run();
    }

    public void removeEquipmentFromEvent(Equipment equipment) {
        if (equipment != null) {
            event.removeEquipment(equipment);
            onChange.run();
        }
    }
}
