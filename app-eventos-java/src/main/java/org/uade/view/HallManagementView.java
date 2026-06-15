package org.uade.view;

import org.uade.controller.HallController;
import org.uade.exception.ValidationException;
import org.uade.model.Hall;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.Component;
import java.awt.GridLayout;
import java.util.List;

public class HallManagementView extends AbstractManagementView<Hall> {
    private final HallController hallController;

    public HallManagementView(JFrame parent, HallController hallController) {
        super(parent, "Gestion de salones");
        this.hallController = hallController;
        setRenderer(new HallRenderer());
        refresh();
    }

    @Override
    protected List<Hall> getItems() {
        return hallController.getAll();
    }

    @Override
    protected void doAdd() throws Exception {
        HallFormData data = promptForm(null);
        if (data == null) {
            return;
        }
        Hall hall = new Hall(hallController.getNextId(), data.name, data.hallType,
                data.maxCapacity, true, data.price);
        hallController.add(hall);
    }

    @Override
    protected void doEdit(Hall hall) throws Exception {
        HallFormData data = promptForm(hall);
        if (data == null) {
            return;
        }
        hall.setName(data.name);
        hall.setHallType(data.hallType);
        hall.setMaxCapacity(data.maxCapacity);
        hall.setPrice(data.price);
        hallController.edit(hall);
    }

    @Override
    protected void toggleActive(Hall hall) throws Exception {
        if (hall.isAvailable()) {
            hallController.disable(hall);
        } else {
            hallController.enable(hall);
        }
    }

    private HallFormData promptForm(Hall current) throws ValidationException {
        JTextField nameField = new JTextField(current != null ? current.getName() : "");
        JTextField typeField = new JTextField(current != null ? current.getHallType() : "");
        JTextField capacityField = new JTextField(current != null ? String.valueOf(current.getMaxCapacity()) : "");
        JTextField priceField = new JTextField(current != null ? String.valueOf(current.getPrice()) : "");

        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));
        form.add(new JLabel("Nombre:"));
        form.add(nameField);
        form.add(new JLabel("Tipo:"));
        form.add(typeField);
        form.add(new JLabel("Capacidad:"));
        form.add(capacityField);
        form.add(new JLabel("Precio:"));
        form.add(priceField);

        int result = JOptionPane.showConfirmDialog(this, form,
                current == null ? "Agregar salon" : "Editar salon",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return null;
        }

        String name = nameField.getText().trim();
        String type = typeField.getText().trim();
        if (name.isEmpty()) {
            throw new ValidationException("El nombre es obligatorio");
        }
        if (type.isEmpty()) {
            throw new ValidationException("El tipo es obligatorio");
        }
        int capacity;
        double price;
        try {
            capacity = Integer.parseInt(capacityField.getText().trim());
        } catch (NumberFormatException e) {
            throw new ValidationException("La capacidad debe ser un numero entero");
        }
        try {
            price = Double.parseDouble(priceField.getText().trim());
        } catch (NumberFormatException e) {
            throw new ValidationException("El precio debe ser numerico");
        }
        if (capacity <= 0) {
            throw new ValidationException("La capacidad debe ser mayor a 0");
        }
        if (price < 0) {
            throw new ValidationException("El precio no puede ser negativo");
        }

        HallFormData data = new HallFormData();
        data.name = name;
        data.hallType = type;
        data.maxCapacity = capacity;
        data.price = price;
        return data;
    }

    private static class HallFormData {
        String name;
        String hallType;
        int maxCapacity;
        double price;
    }

    private static class HallRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                                                       int index, boolean isSelected,
                                                       boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Hall hall) {
                String estado = hall.isAvailable() ? "Activo" : "Inactivo";
                setText(hall.getName() + " - " + hall.getHallType()
                        + " - cap " + hall.getMaxCapacity()
                        + " - $ " + hall.getPrice() + " [" + estado + "]");
            }
            return this;
        }
    }
}
