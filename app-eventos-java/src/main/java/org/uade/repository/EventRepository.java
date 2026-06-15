package org.uade.repository;

import com.google.gson.reflect.TypeToken;
import org.uade.model.Attendee;
import org.uade.model.Catering;
import org.uade.model.Equipment;
import org.uade.model.Event;
import org.uade.model.Hall;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class EventRepository extends JsonRepository<Event> {
    private static final String FILE_NAME = "events.json";

    public ArrayList<Event> loadAll(ArrayList<Hall> halls,
                                    ArrayList<Attendee> attendees,
                                    ArrayList<Catering> caterings,
                                    ArrayList<Equipment> equipments) {
        ArrayList<Event> events = new ArrayList<>();

        InputStream is = getClass().getResourceAsStream("/mockup_db/" + FILE_NAME);
        if (is == null) {
            return events;
        }

        try (InputStreamReader reader = new InputStreamReader(is)) {
            Type listType = new TypeToken<ArrayList<EventDTO>>() {}.getType();
            ArrayList<EventDTO> dtos = gson.fromJson(reader, listType);
            if (dtos == null) {
                return events;
            }

            for (EventDTO dto : dtos) {
                Hall hall = findHallById(halls, dto.hallId);
                if (hall == null) {
                    continue;
                }

                LocalDate date;
                try {
                    date = LocalDate.parse(dto.date, Event.DATE_FORMATTER);
                } catch (DateTimeParseException e) {
                    continue;
                }

                Event event = new Event(dto.name, dto.description, dto.active,
                        date, dto.location, hall);

                if (dto.attendeeIds != null) {
                    for (Integer id : dto.attendeeIds) {
                        Attendee a = findAttendeeById(attendees, id);
                        if (a != null) {
                            try {
                                event.addAttendee(a);
                            } catch (Exception ignored) {
                            }
                        }
                    }
                }
                if (dto.cateringIds != null) {
                    for (Integer id : dto.cateringIds) {
                        Catering c = findCateringById(caterings, id);
                        if (c != null) {
                            try {
                                event.addCatering(c);
                            } catch (Exception ignored) {
                            }
                        }
                    }
                }
                if (dto.equipmentIds != null) {
                    for (Integer id : dto.equipmentIds) {
                        Equipment eq = findEquipmentById(equipments, id);
                        if (eq != null) {
                            try {
                                event.addEquipment(eq);
                            } catch (Exception ignored) {
                            }
                        }
                    }
                }

                events.add(event);
            }
        } catch (Exception e) {
            return events;
        }

        return events;
    }

    public void saveAll(ArrayList<Event> events) {
        ArrayList<EventDTO> dtos = new ArrayList<>();
        for (Event event : events) {
            EventDTO dto = new EventDTO();
            dto.name = event.getName();
            dto.description = event.getDescription();
            dto.active = event.getEventIsActive();
            dto.date = event.getFormattedDate();
            dto.location = event.getLocation();
            dto.hallId = event.getHall().getId();
            dto.attendeeIds = new ArrayList<>();
            for (Attendee a : event.getConfirmedAttendees()) {
                dto.attendeeIds.add(a.getId());
            }
            dto.cateringIds = new ArrayList<>();
            for (Catering c : event.getCateringList()) {
                dto.cateringIds.add(c.getId());
            }
            dto.equipmentIds = new ArrayList<>();
            for (Equipment eq : event.getEquipmentList()) {
                dto.equipmentIds.add(eq.getId());
            }
            dtos.add(dto);
        }
        writeJson(FILE_NAME, dtos);
    }

    private Hall findHallById(ArrayList<Hall> halls, int id) {
        for (Hall hall : halls) {
            if (hall.getId() == id) {
                return hall;
            }
        }
        return null;
    }

    private Attendee findAttendeeById(ArrayList<Attendee> attendees, int id) {
        for (Attendee a : attendees) {
            if (a.getId() == id) {
                return a;
            }
        }
        return null;
    }

    private Catering findCateringById(ArrayList<Catering> caterings, int id) {
        for (Catering c : caterings) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    private Equipment findEquipmentById(ArrayList<Equipment> equipments, int id) {
        for (Equipment eq : equipments) {
            if (eq.getId() == id) {
                return eq;
            }
        }
        return null;
    }

    private static class EventDTO {
        String name;
        String description;
        Boolean active;
        String date;
        String location;
        int hallId;
        ArrayList<Integer> attendeeIds;
        ArrayList<Integer> cateringIds;
        ArrayList<Integer> equipmentIds;
    }
}
