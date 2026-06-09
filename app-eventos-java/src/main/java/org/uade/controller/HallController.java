package org.uade.controller;

import org.uade.model.Hall;

import java.util.ArrayList;

public class HallController {
    private ArrayList<Hall> halls;

    public HallController() {
        this.halls = new ArrayList<>();
    }

    public Boolean addHall(Hall hall) {
        if (hall != null && !halls.contains(hall)) {
            halls.add(hall);
            return true;
        }

        return false;
    }

    public Boolean enableHall(Hall hall) {
        if (hall != null && halls.contains(hall)) {
            hall.setAvailable(true);
            return true;
        }

        return false;
    }

    public Boolean disableHall(Hall hall) {
        if (hall != null && halls.contains(hall)) {
            hall.setAvailable(false);
            return true;
        }

        return false;
    }

    public ArrayList<Hall> getHalls() {
        return halls;
    }
}
