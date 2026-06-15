package org.uade.controller;

import org.uade.exception.ValidationException;
import org.uade.model.Resource;

import java.util.ArrayList;

public abstract class ResourceController<T extends Resource> {
    protected ArrayList<T> resources = new ArrayList<>();
    protected Runnable onChange = () -> {};

    public void setOnChange(Runnable onChange) {
        this.onChange = onChange != null ? onChange : () -> {};
    }

    public void add(T resource) throws ValidationException {
        if (resource == null) {
            throw new ValidationException("El recurso no puede ser nulo");
        }
        if (resources.contains(resource)) {
            throw new ValidationException("El recurso ya existe en el sistema");
        }
        resources.add(resource);
        onChange.run();
    }

    public void edit(T resource) throws ValidationException {
        if (resource == null || !resources.contains(resource)) {
            throw new ValidationException("El recurso no existe en el sistema");
        }
        onChange.run();
    }

    public void enable(T resource) throws ValidationException {
        if (resource == null || !resources.contains(resource)) {
            throw new ValidationException("El recurso no existe en el sistema");
        }
        resource.setAvailable(true);
        onChange.run();
    }

    public void disable(T resource) throws ValidationException {
        if (resource == null || !resources.contains(resource)) {
            throw new ValidationException("El recurso no existe en el sistema");
        }
        resource.setAvailable(false);
        onChange.run();
    }

    public ArrayList<T> getAll() {
        return this.resources;
    }

    public ArrayList<T> getAvailable() {
        ArrayList<T> available = new ArrayList<>();
        for (T resource : resources) {
            if (resource.isAvailable()) {
                available.add(resource);
            }
        }
        return available;
    }

    public T getById(int id) {
        for (T resource : resources) {
            if (resource.getId() == id) {
                return resource;
            }
        }
        return null;
    }

    public int getNextId() {
        int max = 0;
        for (T resource : resources) {
            if (resource.getId() > max) {
                max = resource.getId();
            }
        }
        return max + 1;
    }
}
