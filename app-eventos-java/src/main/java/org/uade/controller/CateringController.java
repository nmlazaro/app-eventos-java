package org.uade.controller;

import org.uade.model.Catering;

import java.util.ArrayList;

public class CateringController {
    private ArrayList<Catering> caterings;

    public CateringController() {
        this.caterings = new ArrayList<>();
    }

    public Boolean addCatering(Catering catering) {
        if(catering != null && !caterings.contains(catering)) {
            caterings.add(catering);
            return true;
        }

        return false;
    }

    public Boolean enableCatering(Catering catering) {
        if (catering != null && caterings.contains(catering)) {
            catering.setAvailable(true);
            return true;
        }
        return false;
    }

    public Boolean disableCatering(Catering catering) {
        if(catering != null && caterings.contains(catering)) {
            catering.setAvailable(false);
            return true;
        }

        return false;
    }

    public ArrayList<Catering> getCaterings() {
        return caterings;
    }
}
