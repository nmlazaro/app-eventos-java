package org.uade.view;

import org.uade.controller.EventMainMenuController;

import javax.swing.*;
import java.awt.*;

import org.uade.model.Event;

public class MainMenuView extends JFrame {
    private EventMainMenuController controller;

    public MainMenuView(EventMainMenuController controller) {
        this.controller = controller;

        // Configuracio de la ventana
        setTitle("Gestión de Eventos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Crea el panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Agrega el panel al frame
        add(mainPanel);

        JLabel titleLabel = new JLabel("Incoming events");
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JButton addButton = new JButton("Agregar evento");
        JButton editButton = new JButton("Editar evento");
        JButton detailButton = new JButton("Ver detalle");

        JPanel buttonPanel = new JPanel(); // FlowLayout por defecto
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(detailButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Event event : controller.getAllEvents()) {
            listModel.addElement(event.getName() + " - " + event.getDate() + " - " + event.getLocation());
        }

        JList<String> eventList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(eventList);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
    }
}