package org.uade.view;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import java.awt.BorderLayout;
import java.util.List;

public abstract class AbstractManagementView<T> extends JDialog {
    protected final DefaultListModel<T> listModel = new DefaultListModel<>();
    protected final JList<T> list = new JList<>(listModel);

    public AbstractManagementView(JFrame parent, String title) {
        super(parent, true);
        setTitle(title);
        setSize(560, 480);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        add(new JScrollPane(list), BorderLayout.CENTER);
        add(buildButtons(), BorderLayout.SOUTH);
    }

    private JPanel buildButtons() {
        JPanel panel = new JPanel();
        JButton addButton = new JButton("Agregar");
        JButton editButton = new JButton("Editar");
        JButton toggleButton = new JButton("Activar / Desactivar");
        JButton closeButton = new JButton("Cerrar");

        addButton.addActionListener(e -> handleAdd());
        editButton.addActionListener(e -> handleEdit());
        toggleButton.addActionListener(e -> handleToggle());
        closeButton.addActionListener(e -> dispose());

        panel.add(addButton);
        panel.add(editButton);
        panel.add(toggleButton);
        panel.add(closeButton);
        return panel;
    }

    protected void setRenderer(ListCellRenderer<? super T> renderer) {
        list.setCellRenderer(renderer);
    }

    protected void refresh() {
        listModel.clear();
        for (T item : getItems()) {
            listModel.addElement(item);
        }
    }

    private void handleAdd() {
        try {
            doAdd();
            refresh();
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void handleEdit() {
        T item = list.getSelectedValue();
        if (item == null) {
            showWarning("Seleccionar un elemento");
            return;
        }
        try {
            doEdit(item);
            refresh();
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void handleToggle() {
        T item = list.getSelectedValue();
        if (item == null) {
            showWarning("Seleccionar un elemento");
            return;
        }
        try {
            toggleActive(item);
            refresh();
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    protected void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    protected void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Atencion", JOptionPane.WARNING_MESSAGE);
    }

    protected abstract List<T> getItems();
    protected abstract void doAdd() throws Exception;
    protected abstract void doEdit(T item) throws Exception;
    protected abstract void toggleActive(T item) throws Exception;
}
