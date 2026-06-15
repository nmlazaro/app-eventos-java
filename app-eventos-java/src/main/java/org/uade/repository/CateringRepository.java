package org.uade.repository;

import com.google.gson.reflect.TypeToken;
import org.uade.model.Catering;

import java.util.ArrayList;

public class CateringRepository extends JsonRepository<Catering> {
    private static final String FILE_NAME = "catering.json";

    public ArrayList<Catering> loadAll() {
        return readJson("/mockup_db/" + FILE_NAME,
                new TypeToken<ArrayList<Catering>>() {}.getType());
    }

    public void saveAll(ArrayList<Catering> caterings) {
        writeJson(FILE_NAME, caterings);
    }
}
