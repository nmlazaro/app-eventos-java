package org.uade;

import org.uade.controller.AttendeeController;
import org.uade.controller.CateringController;
import org.uade.controller.EquipmentController;
import org.uade.controller.EventFormController;
import org.uade.controller.EventMainMenuController;
import org.uade.controller.HallController;
import org.uade.model.Attendee;
import org.uade.model.Catering;
import org.uade.model.Equipment;
import org.uade.model.Event;
import org.uade.model.Hall;
import org.uade.repository.AttendeeRepository;
import org.uade.repository.CateringRepository;
import org.uade.repository.EquipmentRepository;
import org.uade.repository.EventRepository;
import org.uade.repository.HallRepository;
import org.uade.view.MainMenuView;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        HallRepository hallRepo = new HallRepository();
        CateringRepository cateringRepo = new CateringRepository();
        EquipmentRepository equipmentRepo = new EquipmentRepository();
        AttendeeRepository attendeeRepo = new AttendeeRepository();
        EventRepository eventRepo = new EventRepository();

        ArrayList<Hall> halls = hallRepo.loadAll();
        ArrayList<Catering> caterings = cateringRepo.loadAll();
        ArrayList<Equipment> equipments = equipmentRepo.loadAll();
        ArrayList<Attendee> attendees = attendeeRepo.loadAll();
        ArrayList<Event> events = eventRepo.loadAll(halls, attendees, caterings, equipments);

        HallController hallController = new HallController();
        CateringController cateringController = new CateringController();
        EquipmentController equipmentController = new EquipmentController();
        AttendeeController attendeeController = new AttendeeController();
        EventMainMenuController eventMainMenuController = new EventMainMenuController();

        for (Hall hall : halls) {
            hallController.getAll().add(hall);
        }
        for (Catering catering : caterings) {
            cateringController.getAll().add(catering);
        }
        for (Equipment equipment : equipments) {
            equipmentController.getAll().add(equipment);
        }
        for (Attendee attendee : attendees) {
            attendeeController.getAttendees().add(attendee);
        }
        for (Event event : events) {
            eventMainMenuController.getAllEvents().add(event);
        }

        hallController.setOnChange(() -> hallRepo.saveAll(hallController.getAll()));
        cateringController.setOnChange(() -> cateringRepo.saveAll(cateringController.getAll()));
        equipmentController.setOnChange(() -> equipmentRepo.saveAll(equipmentController.getAll()));
        attendeeController.setOnChange(() -> attendeeRepo.saveAll(attendeeController.getAttendees()));
        eventMainMenuController.setOnChange(() -> eventRepo.saveAll(eventMainMenuController.getAllEvents()));

        EventFormController eventFormController = new EventFormController(eventMainMenuController);

        Runnable eventsOnChange = eventMainMenuController::notifyChange;

        MainMenuView view = new MainMenuView(
                eventMainMenuController,
                eventFormController,
                attendeeController,
                cateringController,
                equipmentController,
                hallController,
                eventsOnChange
        );
        view.setVisible(true);
    }
}
