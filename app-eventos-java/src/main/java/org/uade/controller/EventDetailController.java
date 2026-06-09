package org.uade.controller;

import org.uade.model.Attendee;
import org.uade.model.Catering;
import org.uade.model.Equipment;
import org.uade.model.Event;

public class EventDetailController {
    private Event event;
    private AttendeeController attendeeController;
    private CateringController cateringController;
    private EquipmentController equipmentController;

    public EventDetailController(Event event, AttendeeController attendeeController, CateringController cateringController, EquipmentController equipmentController) {
        this.event = event;
        this.attendeeController = attendeeController;
        this.cateringController = cateringController;
        this.equipmentController = equipmentController;
    }

    public Boolean addAttendeeToEvent(Attendee attendee) {
        if (attendee != null && attendeeController.getAttendees().contains(attendee)) {
            return event.addAttendee(attendee);
        }

        return false;
    }

    public Boolean removeAttendeeFromEvent(Attendee attendee) {
        if (attendee != null && attendeeController.getAttendees().contains(attendee)) {
            return event.removeAttendee(attendee);
        }

        return false;
    }

    public Boolean addCateringToEvent(Catering catering) {
        if (catering != null && cateringController.getCaterings().contains(catering)) {
            return event.addCatering(catering);
        }

        return false;
    }

    public Boolean removeCateringFromEvent(Catering catering) {
        if (catering != null && cateringController.getCaterings().contains(catering)) {
            return event.removeCatering(catering);
        }

        return false;
    }

    public Boolean addEquipmentToEvent(Equipment equipment) {
        if (equipment != null && equipmentController.getEquipments().contains(equipment)) {
            return event.addEquipment(equipment);
        }

        return false;
    }

    public Boolean removeEquipmentFromEvent(Equipment equipment) {
        if (equipment != null && equipmentController.getEquipments().contains(equipment)) {
            return event.removeEquipment(equipment);
        }

        return false;
    }
}
