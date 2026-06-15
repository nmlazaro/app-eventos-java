package org.uade.view;

import org.uade.controller.EventMainMenuController;
import org.uade.model.Event;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

public class ParticipationReportView extends JDialog {
    private static final String[] COLUMNS = {
            "Evento", "Fecha", "Asistentes", "Capacidad", "% Ocupacion", "Estado"
    };

    private final EventMainMenuController eventController;

    public ParticipationReportView(JFrame parent, EventMainMenuController eventController) {
        super(parent, true);
        this.eventController = eventController;

        setTitle("Analisis de participacion");
        setSize(780, 520);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Analisis de participacion", SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16f));
        add(titleLabel, BorderLayout.NORTH);

        add(buildSummaryPanel(), BorderLayout.SOUTH);
        add(new JScrollPane(buildTable()), BorderLayout.CENTER);
    }

    private JTable buildTable() {
        DefaultTableModel model = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Event event : eventController.getAllEvents()) {
            int attendees = event.getConfirmedAttendees().size();
            int capacity = event.getHall().getMaxCapacity();
            double percentage = capacity > 0 ? (attendees * 100.0) / capacity : 0;
            String estado = event.getEventIsActive() ? "Activo" : "Inactivo";
            model.addRow(new Object[]{
                    event.getName(),
                    event.getFormattedDate(),
                    attendees,
                    capacity,
                    String.format("%.1f %%", percentage),
                    estado
            });
        }

        JTable table = new JTable(model);
        table.setRowHeight(22);
        table.getTableHeader().setReorderingAllowed(false);
        return table;
    }

    private JPanel buildSummaryPanel() {
        ArrayList<Event> all = eventController.getAllEvents();
        int totalEvents = all.size();
        int activeEvents = 0;
        int totalAttendees = 0;
        for (Event event : all) {
            if (event.getEventIsActive()) {
                activeEvents++;
            }
            totalAttendees += event.getConfirmedAttendees().size();
        }
        double average = totalEvents > 0 ? (double) totalAttendees / totalEvents : 0;

        JPanel panel = new JPanel(new GridLayout(1, 4, 10, 10));
        panel.add(buildStat("Eventos totales", String.valueOf(totalEvents)));
        panel.add(buildStat("Eventos activos", String.valueOf(activeEvents)));
        panel.add(buildStat("Asistentes inscriptos", String.valueOf(totalAttendees)));
        panel.add(buildStat("Promedio por evento", String.format("%.1f", average)));
        return panel;
    }

    private JPanel buildStat(String title, String value) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(valueLabel.getFont().deriveFont(Font.BOLD, 18f));
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);
        return panel;
    }
}
