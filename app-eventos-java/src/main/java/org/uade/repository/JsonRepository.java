package org.uade.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public abstract class JsonRepository<T> {
    protected static final String SOURCE_DIR = "src/main/resources/mockup_db";
    protected static final String CLASSES_DIR = "target/classes/mockup_db";

    protected final Gson gson = new Gson();
    protected final Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();

    protected ArrayList<T> readJson(String resourcePath, Type listType) {
        InputStream is = getClass().getResourceAsStream(resourcePath);
        if (is == null) {
            return new ArrayList<>();
        }
        try (InputStreamReader reader = new InputStreamReader(is)) {
            ArrayList<T> result = gson.fromJson(reader, listType);
            return result != null ? result : new ArrayList<>();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    protected void writeJson(String fileName, Object data) {
        writeTo(Path.of(SOURCE_DIR, fileName), data);
        Path classes = Path.of(CLASSES_DIR, fileName);
        if (Files.exists(classes.getParent())) {
            writeTo(classes, data);
        }
    }

    private void writeTo(Path target, Object data) {
        try {
            Files.createDirectories(target.getParent());
            try (Writer writer = Files.newBufferedWriter(target)) {
                prettyGson.toJson(data, writer);
            }
        } catch (IOException e) {
            System.err.println("No se pudo escribir " + target + ": " + e.getMessage());
        }
    }
}
