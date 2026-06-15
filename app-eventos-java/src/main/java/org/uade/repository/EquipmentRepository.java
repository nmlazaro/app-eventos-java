package org.uade.repository;

import com.google.gson.reflect.TypeToken;
import org.uade.model.Equipment;

import java.util.ArrayList;

public class EquipmentRepository extends JsonRepository<Equipment> {
    private static final String FILE_NAME = "equipment.json";

    public ArrayList<Equipment> loadAll() {
        return readJson("/mockup_db/" + FILE_NAME,
                new TypeToken<ArrayList<Equipment>>() {}.getType());
    }

    public void saveAll(ArrayList<Equipment> equipments) {
        writeJson(FILE_NAME, equipments);
    }
}
