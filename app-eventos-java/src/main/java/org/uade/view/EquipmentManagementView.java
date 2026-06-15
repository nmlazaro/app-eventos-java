package org.uade.view;

import org.uade.controller.EquipmentController;
import org.uade.exception.ValidationException;
import org.uade.model.Equipment;

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

public class EquipmentManagementView extends AbstractManagementView<Equipment> {
    private final EquipmentController equipmentController;

    public EquipmentManagementView(JFrame parent, EquipmentController equipmentController) {
        super(parent, "Gestion de equipamiento");
        this.equipmentController = equipmentController;
        setRenderer(new EquipmentRenderer());
        refresh();
    }

    @Override
    protected List<Equipment> getItems() {
        return equipmentController.getAll();
    }

    @Override
    protected void doAdd() throws Exception {
        EquipmentFormData data = promptForm(null);
        if (data == null) {
            return;
        }
        Equipment equipment = new Equipment(equipmentController.getNextId(),
                data.name, true, data.hourPrice);
        equipmentController.add(equipment);
    }

    @Override
    protected void doEdit(Equipment equipment) throws Exception {
        EquipmentFormData data = promptForm(equipment);
        if (data == null) {
            return;
        }
        equipment.setName(data.name);
        equipment.setHourPrice(data.hourPrice);
        equipmentController.edit(equipment);
    }

    @Override
    protected void toggleActive(Equipment equipment) throws Exception {
        if (equipment.isAvailable()) {
            equipmentController.disable(equipment);
        } else {
            equipmentController.enable(equipment);
        }
    }

    private EquipmentFormData promptForm(Equipment current) throws ValidationException {
        JTextField nameField = new JTextField(current != null ? current.getName() : "");
        JTextField priceField = new JTextField(current != null ? String.valueOf(current.getHourPrice()) : "");

        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));
        form.add(new JLabel("Nombre:"));
        form.add(nameField);
        form.add(new JLabel("Precio por hora:"));
        form.add(priceField);

        int result = JOptionPane.showConfirmDialog(this, form,
                current == null ? "Agregar equipamiento" : "Editar equipamiento",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return null;
        }

        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            throw new ValidationException("El nombre es obligatorio");
        }
        double price;
        try {
            price = Double.parseDouble(priceField.getText().trim());
        } catch (NumberFormatException e) {
            throw new ValidationException("El precio debe ser numerico");
        }
        if (price < 0) {
            throw new ValidationException("El precio no puede ser negativo");
        }

        EquipmentFormData data = new EquipmentFormData();
        data.name = name;
        data.hourPrice = price;
        return data;
    }

    private static class EquipmentFormData {
        String name;
        double hourPrice;
    }

    private static class EquipmentRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                                                       int index, boolean isSelected,
                                                       boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Equipment equipment) {
                String estado = equipment.isAvailable() ? "Activo" : "Inactivo";
                setText(equipment.getName() + " - $ " + equipment.getHourPrice() + "/hora [" + estado + "]");
            }
            return this;
        }
    }
}
