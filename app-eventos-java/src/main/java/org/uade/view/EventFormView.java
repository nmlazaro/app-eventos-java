package org.uade.view;

import org.uade.controller.EventFormController;
import org.uade.controller.HallController;
import org.uade.exception.ValidationException;
import org.uade.model.Event;
import org.uade.model.Hall;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class EventFormView extends JDialog {
    private final EventFormController eventFormController;
    private final HallController hallController;
    private final Event eventToEdit;

    private final JTextField nameField = new JTextField(20);
    private final JTextArea descriptionArea = new JTextArea(4, 20);
    private final JTextField dateField = new JTextField(10);
    private final JTextField locationField = new JTextField(20);
    private final JComboBox<Hall> hallComboBox = new JComboBox<>();
    private final JCheckBox activeCheckBox = new JCheckBox("", true);

    private boolean saved = false;

    public EventFormView(JFrame parent,
                         EventFormController eventFormController,
                         HallController hallController) {
        this(parent, eventFormController, hallController, null);
    }

    public EventFormView(JFrame parent,
                         EventFormController eventFormController,
                         HallController hallController,
                         Event eventToEdit) {
        super(parent, true);
        this.eventFormController = eventFormController;
        this.hallController = hallController;
        this.eventToEdit = eventToEdit;

        setTitle(eventToEdit == null ? "Crear evento" : "Editar evento");
        setSize(480, 460);
        setLocationRelativeTo(parent);

        initUI();
        loadHalls();

        if (eventToEdit != null) {
            prefillFields(eventToEdit);
        }
    }

    private void initUI() {
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;
        addRow(formPanel, gbc, row++, "Nombre:", nameField);
        addRow(formPanel, gbc, row++, "Descripcion:", new JScrollPane(descriptionArea));

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        datePanel.add(dateField);
        JLabel hint = new JLabel("(dd-mm-yyyy)");
        hint.setForeground(Color.GRAY);
        datePanel.add(hint);
        addRow(formPanel, gbc, row++, "Fecha:", datePanel);

        addRow(formPanel, gbc, row++, "Ubicacion:", locationField);

        hallComboBox.setRenderer(new HallRenderer());
        addRow(formPanel, gbc, row++, "Salon:", hallComboBox);

        addRow(formPanel, gbc, row++, "Activo:", activeCheckBox);

        JButton saveButton = new JButton("Guardar");
        JButton cancelButton = new JButton("Cancelar");
        saveButton.addActionListener(e -> onSave());
        cancelButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        setLayout(new BorderLayout());
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row,
                        String label, JComponent comp) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 1.0;
        panel.add(comp, gbc);
    }

    private void loadHalls() {
        ArrayList<Hall> halls = hallController.getAvailable();
        for (Hall hall : halls) {
            hallComboBox.addItem(hall);
        }
        if (eventToEdit != null) {
            Hall current = eventToEdit.getHall();
            boolean present = false;
            for (int i = 0; i < hallComboBox.getItemCount(); i++) {
                if (hallComboBox.getItemAt(i).getId() == current.getId()) {
                    present = true;
                    break;
                }
            }
            if (!present) {
                hallComboBox.addItem(current);
            }
        }
    }

    private void prefillFields(Event event) {
        nameField.setText(event.getName());
        descriptionArea.setText(event.getDescription());
        dateField.setText(event.getFormattedDate());
        locationField.setText(event.getLocation());
        activeCheckBox.setSelected(event.getEventIsActive());

        for (int i = 0; i < hallComboBox.getItemCount(); i++) {
            if (hallComboBox.getItemAt(i).getId() == event.getHall().getId()) {
                hallComboBox.setSelectedIndex(i);
                break;
            }
        }
    }

    private void onSave() {
        String name = nameField.getText().trim();
        String description = descriptionArea.getText().trim();
        String dateText = dateField.getText().trim();
        String location = locationField.getText().trim();
        Hall hall = (Hall) hallComboBox.getSelectedItem();
        Boolean active = activeCheckBox.isSelected();

        LocalDate date;
        try {
            date = LocalDate.parse(dateText, Event.DATE_FORMATTER);
        } catch (DateTimeParseException ex) {
            showError("Fecha invalida. Usar formato dd-mm-yyyy");
            return;
        }

        try {
            if (eventToEdit == null) {
                eventFormController.createEvent(name, description, active, date, location, hall);
            } else {
                eventFormController.updateEvent(eventToEdit, name, description, active, date, location, hall);
            }
            saved = true;
            dispose();
        } catch (ValidationException ex) {
            showError(ex.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error de validacion", JOptionPane.ERROR_MESSAGE);
    }

    public boolean isSaved() {
        return saved;
    }

    private static class HallRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                                                       int index, boolean isSelected,
                                                       boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Hall hall) {
                setText(hall.getName() + " (cap " + hall.getMaxCapacity() + ")");
            }
            return this;
        }
    }
}
