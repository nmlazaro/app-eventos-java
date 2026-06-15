package org.uade.view;

import org.uade.controller.AttendeeController;
import org.uade.exception.ValidationException;
import org.uade.model.Attendee;

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

public class AttendeeManagementView extends AbstractManagementView<Attendee> {
    private final AttendeeController attendeeController;

    public AttendeeManagementView(JFrame parent, AttendeeController attendeeController) {
        super(parent, "Gestion de asistentes");
        this.attendeeController = attendeeController;
        setRenderer(new AttendeeRenderer());
        refresh();
    }

    @Override
    protected List<Attendee> getItems() {
        return attendeeController.getAttendees();
    }

    @Override
    protected void doAdd() throws Exception {
        AttendeeFormData data = promptForm(null);
        if (data == null) {
            return;
        }
        Attendee attendee = new Attendee(attendeeController.getNextId(),
                data.name, data.email, data.phone, true);
        attendeeController.addAttendee(attendee);
    }

    @Override
    protected void doEdit(Attendee attendee) throws Exception {
        AttendeeFormData data = promptForm(attendee);
        if (data == null) {
            return;
        }
        attendee.setName(data.name);
        attendee.setEmail(data.email);
        attendee.setPhone(data.phone);
        attendeeController.editAttendee(attendee);
    }

    @Override
    protected void toggleActive(Attendee attendee) throws Exception {
        if (attendee.isActive()) {
            attendeeController.disableAttendee(attendee);
        } else {
            attendeeController.enableAttendee(attendee);
        }
    }

    private AttendeeFormData promptForm(Attendee current) throws ValidationException {
        JTextField nameField = new JTextField(current != null ? current.getName() : "");
        JTextField emailField = new JTextField(current != null ? current.getEmail() : "");
        JTextField phoneField = new JTextField(current != null ? current.getPhone() : "");

        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));
        form.add(new JLabel("Nombre:"));
        form.add(nameField);
        form.add(new JLabel("Email:"));
        form.add(emailField);
        form.add(new JLabel("Telefono:"));
        form.add(phoneField);

        int result = JOptionPane.showConfirmDialog(this, form,
                current == null ? "Agregar asistente" : "Editar asistente",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return null;
        }

        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        if (name.isEmpty()) {
            throw new ValidationException("El nombre es obligatorio");
        }
        if (email.isEmpty() || !email.contains("@")) {
            throw new ValidationException("El email no es valido");
        }
        if (phone.isEmpty()) {
            throw new ValidationException("El telefono es obligatorio");
        }

        AttendeeFormData data = new AttendeeFormData();
        data.name = name;
        data.email = email;
        data.phone = phone;
        return data;
    }

    private static class AttendeeFormData {
        String name;
        String email;
        String phone;
    }

    private static class AttendeeRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                                                       int index, boolean isSelected,
                                                       boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Attendee attendee) {
                String estado = attendee.isActive() ? "Activo" : "Inactivo";
                setText(attendee.getName() + " - " + attendee.getEmail()
                        + " - " + attendee.getPhone() + " [" + estado + "]");
            }
            return this;
        }
    }
}
