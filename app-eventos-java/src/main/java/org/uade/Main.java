package org.uade;

import org.uade.controller.EventMainMenuController;
import org.uade.view.MainMenuView;

public class Main {

    public static void main(String[] args) {
        EventMainMenuController controller = new EventMainMenuController();
        MainMenuView view = new MainMenuView(controller);
        view.setVisible(true);
    }
}
