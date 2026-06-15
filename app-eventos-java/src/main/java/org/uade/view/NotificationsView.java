package org.uade.view;

import org.uade.controller.EventMainMenuController;
import org.uade.model.Event;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;

public class NotificationsView extends JDialog {
    private final EventMainMenuController eventController;
    private final DefaultListModel<Event> listModel = new DefaultListModel<>();
    private final JLabel emptyLabel = new JLabel("No hay eventos en el rango seleccionado.", SwingConstants.CENTER);

    public NotificationsView(JFrame parent, EventMainMenuController eventController) {
        super(parent, true);
        this.eventController = eventController;

        setTitle("Notificaciones - Proximos eventos");
        setSize(520, 420);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Eventos proximos", SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16f));
        add(titleLabel, BorderLayout.NORTH);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Ventana de notificacion (dias):"));
        JSpinner daysSpinner = new JSpinner(new SpinnerNumberModel(30, 1, 365, 1));
        topPanel.add(daysSpinner);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(topPanel, BorderLayout.NORTH);

        JList<Event> eventList = new JList<>(listModel);
        eventList.setCellRenderer(new UpcomingEventRenderer());
        centerPanel.add(new JScrollPane(eventList), BorderLayout.CENTER);

        emptyLabel.setForeground(Color.GRAY);
        centerPanel.add(emptyLabel, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        daysSpinner.addChangeListener(e -> refreshList((Integer) daysSpinner.getValue()));
        refreshList((Integer) daysSpinner.getValue());
    }

    private void refreshList(int daysAhead) {
        listModel.clear();
        ArrayList<Event> upcoming = getUpcomingEvents(daysAhead);
        Collections.sort(upcoming);
        for (Event event : upcoming) {
            listModel.addElement(event);
        }
        emptyLabel.setVisible(upcoming.isEmpty());
    }

    private ArrayList<Event> getUpcomingEvents(int daysAhead) {
        LocalDate today = LocalDate.now();
        LocalDate limit = today.plusDays(daysAhead);
        ArrayList<Event> result = new ArrayList<>();
        for (Event event : eventController.getActiveEvents()) {
            LocalDate date = event.getDate();
            if (!date.isBefore(today) && !date.isAfter(limit)) {
                result.add(event);
            }
        }
        return result;
    }

    private static class UpcomingEventRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                                                       int index, boolean isSelected,
                                                       boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Event event) {
                long daysUntil = ChronoUnit.DAYS.between(LocalDate.now(), event.getDate());
                String suffix;
                if (daysUntil == 0) {
                    suffix = "hoy";
                } else if (daysUntil == 1) {
                    suffix = "manana";
                } else {
                    suffix = "en " + daysUntil + " dias";
                }
                setText(event.getFormattedDate() + "  -  " + event.getName()
                        + "  (" + suffix + ")");
            }
            return this;
        }
    }
}
