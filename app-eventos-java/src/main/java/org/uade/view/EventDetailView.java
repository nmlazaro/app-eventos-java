package org.uade.view;

import org.uade.controller.AttendeeController;
import org.uade.controller.CateringController;
import org.uade.controller.EquipmentController;
import org.uade.controller.EventDetailController;
import org.uade.model.Attendee;
import org.uade.model.Catering;
import org.uade.model.Equipment;
import org.uade.model.Event;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

public class EventDetailView extends JDialog {
    private final Event event;
    private final EventDetailController detailController;
    private final AttendeeController attendeeController;
    private final CateringController cateringController;
    private final EquipmentController equipmentController;

    public EventDetailView(JFrame parent,
                           Event event,
                           EventDetailController detailController,
                           AttendeeController attendeeController,
                           CateringController cateringController,
                           EquipmentController equipmentController) {
        super(parent, true);
        this.event = event;
        this.detailController = detailController;
        this.attendeeController = attendeeController;
        this.cateringController = cateringController;
        this.equipmentController = equipmentController;

        setTitle("Detalle - " + event.getName());
        setSize(720, 540);
        setLocationRelativeTo(parent);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Info", buildInfoTab());
        tabs.addTab("Asistentes", new AttendeePanel());
        tabs.addTab("Catering", new CateringPanel());
        tabs.addTab("Equipamiento", new EquipmentPanel());

        setLayout(new BorderLayout());
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel buildInfoTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        addInfoRow(panel, gbc, row++, "Nombre:", new JLabel(event.getName()));
        addInfoRow(panel, gbc, row++, "Fecha:", new JLabel(event.getFormattedDate()));
        addInfoRow(panel, gbc, row++, "Ubicacion:", new JLabel(event.getLocation()));
        addInfoRow(panel, gbc, row++, "Salon:", new JLabel(event.getHallName() + " (cap " + event.getHall().getMaxCapacity() + ")"));
        addInfoRow(panel, gbc, row++, "Estado:", new JLabel(event.getEventIsActive() ? "Activo" : "Inactivo"));

        JTextArea descriptionArea = new JTextArea(event.getDescription(), 4, 30);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        descriptionArea.setOpaque(false);
        addInfoRow(panel, gbc, row++, "Descripcion:", new JScrollPane(descriptionArea));

        JSpinner hoursSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 24, 1));
        addInfoRow(panel, gbc, row++, "Horas:", hoursSpinner);

        JLabel priceLabel = new JLabel();
        addInfoRow(panel, gbc, row++, "Precio total:", priceLabel);

        Runnable updatePrice = () -> priceLabel.setText("$ " + event.getTotalPrice((Integer) hoursSpinner.getValue()));
        hoursSpinner.addChangeListener(e -> updatePrice.run());
        updatePrice.run();

        return panel;
    }

    private void addInfoRow(JPanel panel, GridBagConstraints gbc, int row,
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

    private class AttendeePanel extends TwoListPanel<Attendee> {
        public AttendeePanel() {
            super("Disponibles", "Inscriptos");
            setRenderer(new AttendeeRenderer());
            refresh();
        }

        @Override
        protected void doAdd(Attendee item) throws Exception {
            detailController.addAttendeeToEvent(item);
        }

        @Override
        protected void doRemove(Attendee item) throws Exception {
            detailController.removeAttendeeFromEvent(item);
        }

        @Override
        public void refresh() {
            ArrayList<Attendee> confirmed = event.getConfirmedAttendees();
            ArrayList<Attendee> available = new ArrayList<>();
            for (Attendee a : attendeeController.getActiveAttendees()) {
                if (!confirmed.contains(a)) {
                    available.add(a);
                }
            }
            setAvailable(available);
            setSelected(new ArrayList<>(confirmed));
        }
    }

    private class CateringPanel extends TwoListPanel<Catering> {
        public CateringPanel() {
            super("Disponibles", "En el evento");
            setRenderer(new CateringRenderer());
            refresh();
        }

        @Override
        protected void doAdd(Catering item) throws Exception {
            detailController.addCateringToEvent(item);
        }

        @Override
        protected void doRemove(Catering item) throws Exception {
            detailController.removeCateringFromEvent(item);
        }

        @Override
        public void refresh() {
            ArrayList<Catering> selected = event.getCateringList();
            ArrayList<Catering> available = new ArrayList<>();
            for (Catering c : cateringController.getAvailable()) {
                if (!selected.contains(c)) {
                    available.add(c);
                }
            }
            setAvailable(available);
            setSelected(new ArrayList<>(selected));
        }
    }

    private class EquipmentPanel extends TwoListPanel<Equipment> {
        public EquipmentPanel() {
            super("Disponibles", "En el evento");
            setRenderer(new EquipmentRenderer());
            refresh();
        }

        @Override
        protected void doAdd(Equipment item) throws Exception {
            detailController.addEquipmentToEvent(item);
        }

        @Override
        protected void doRemove(Equipment item) throws Exception {
            detailController.removeEquipmentFromEvent(item);
        }

        @Override
        public void refresh() {
            ArrayList<Equipment> selected = event.getEquipmentList();
            ArrayList<Equipment> available = new ArrayList<>();
            for (Equipment eq : equipmentController.getAvailable()) {
                if (!selected.contains(eq)) {
                    available.add(eq);
                }
            }
            setAvailable(available);
            setSelected(new ArrayList<>(selected));
        }
    }

    private static class AttendeeRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                                                       int index, boolean isSelected,
                                                       boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Attendee attendee) {
                setText(attendee.getName() + " - " + attendee.getEmail());
            }
            return this;
        }
    }

    private static class CateringRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                                                       int index, boolean isSelected,
                                                       boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Catering catering) {
                setText(catering.getName() + " - $ " + catering.getPrice());
            }
            return this;
        }
    }

    private static class EquipmentRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                                                       int index, boolean isSelected,
                                                       boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Equipment equipment) {
                setText(equipment.getName() + " - $ " + equipment.getHourPrice() + " / hora");
            }
            return this;
        }
    }
}
