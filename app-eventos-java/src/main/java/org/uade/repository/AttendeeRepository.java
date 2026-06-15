package org.uade.repository;

import com.google.gson.reflect.TypeToken;
import org.uade.model.Attendee;

import java.util.ArrayList;

public class AttendeeRepository extends JsonRepository<Attendee> {
    private static final String FILE_NAME = "attendees.json";

    public ArrayList<Attendee> loadAll() {
        return readJson("/mockup_db/" + FILE_NAME,
                new TypeToken<ArrayList<Attendee>>() {}.getType());
    }

    public void saveAll(ArrayList<Attendee> attendees) {
        writeJson(FILE_NAME, attendees);
    }
}
