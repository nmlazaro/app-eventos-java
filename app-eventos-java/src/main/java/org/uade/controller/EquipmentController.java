package org.uade.controller;

import org.uade.model.Equipment;

import java.util.ArrayList;

public class EquipmentController {
    private ArrayList<Equipment> equipments;

    public EquipmentController() {
        this.equipments = new ArrayList<>();
    }

    public boolean addEquipment(Equipment equipment) {
        if (equipment != null && !equipments.contains(equipment)) {
            equipments.add(equipment);
            return true;
        }

        return false;
    }

    public Boolean enableEquipment(Equipment equipment) {
        if (equipment != null && equipments.contains(equipment)) {
            equipment.setAvailable(true);
            return true;
        }

        return false;
    }

    public Boolean disableEquipment(Equipment equipment) {
        if (equipment != null && equipments.contains(equipment)) {
            equipment.setAvailable(false);
            return true;
        }

        return false;
    }

    public ArrayList<Equipment> getEquipments() {
        return equipments;
    }
}
