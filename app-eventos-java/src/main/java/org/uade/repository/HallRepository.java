package org.uade.repository;

import com.google.gson.reflect.TypeToken;
import org.uade.model.Hall;

import java.util.ArrayList;

public class HallRepository extends JsonRepository<Hall> {
    private static final String FILE_NAME = "halls.json";

    public ArrayList<Hall> loadAll() {
        return readJson("/mockup_db/" + FILE_NAME,
                new TypeToken<ArrayList<Hall>>() {}.getType());
    }

    public void saveAll(ArrayList<Hall> halls) {
        writeJson(FILE_NAME, halls);
    }
}
