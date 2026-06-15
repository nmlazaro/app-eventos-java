package org.uade.view;

import org.uade.controller.CateringController;
import org.uade.exception.ValidationException;
import org.uade.model.Catering;

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

public class CateringManagementView extends AbstractManagementView<Catering> {
    private final CateringController cateringController;

    public CateringManagementView(JFrame parent, CateringController cateringController) {
        super(parent, "Gestion de catering");
        this.cateringController = cateringController;
        setRenderer(new CateringRenderer());
        refresh();
    }

    @Override
    protected List<Catering> getItems() {
        return cateringController.getAll();
    }

    @Override
    protected void doAdd() throws Exception {
        CateringFormData data = promptForm(null);
        if (data == null) {
            return;
        }
        Catering catering = new Catering(cateringController.getNextId(), data.name,
                data.description, data.price, true);
        cateringController.add(catering);
    }

    @Override
    protected void doEdit(Catering catering) throws Exception {
        CateringFormData data = promptForm(catering);
        if (data == null) {
            return;
        }
        catering.setName(data.name);
        catering.setDescription(data.description);
        catering.setPrice(data.price);
        cateringController.edit(catering);
    }

    @Override
    protected void toggleActive(Catering catering) throws Exception {
        if (catering.isAvailable()) {
            cateringController.disable(catering);
        } else {
            cateringController.enable(catering);
        }
    }

    private CateringFormData promptForm(Catering current) throws ValidationException {
        JTextField nameField = new JTextField(current != null ? current.getName() : "");
        JTextField descField = new JTextField(current != null ? current.getDescription() : "");
        JTextField priceField = new JTextField(current != null ? String.valueOf(current.getPrice()) : "");

        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));
        form.add(new JLabel("Nombre:"));
        form.add(nameField);
        form.add(new JLabel("Descripcion:"));
        form.add(descField);
        form.add(new JLabel("Precio:"));
        form.add(priceField);

        int result = JOptionPane.showConfirmDialog(this, form,
                current == null ? "Agregar catering" : "Editar catering",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return null;
        }

        String name = nameField.getText().trim();
        String desc = descField.getText().trim();
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

        CateringFormData data = new CateringFormData();
        data.name = name;
        data.description = desc;
        data.price = price;
        return data;
    }

    private static class CateringFormData {
        String name;
        String description;
        double price;
    }

    private static class CateringRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                                                       int index, boolean isSelected,
                                                       boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Catering catering) {
                String estado = catering.isAvailable() ? "Activo" : "Inactivo";
                setText(catering.getName() + " - $ " + catering.getPrice() + " [" + estado + "]");
            }
            return this;
        }
    }
}
