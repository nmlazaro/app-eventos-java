package org.uade.view;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;

public abstract class TwoListPanel<T> extends JPanel {
    protected final DefaultListModel<T> availableModel = new DefaultListModel<>();
    protected final DefaultListModel<T> selectedModel = new DefaultListModel<>();
    protected final JList<T> availableList = new JList<>(availableModel);
    protected final JList<T> selectedList = new JList<>(selectedModel);

    public TwoListPanel(String availableTitle, String selectedTitle) {
        setLayout(new GridLayout(1, 3, 10, 10));

        add(buildSidePanel(availableTitle, availableList));
        add(buildButtonsPanel());
        add(buildSidePanel(selectedTitle, selectedList));
    }

    private JPanel buildSidePanel(String title, JList<T> list) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(title), BorderLayout.NORTH);
        panel.add(new JScrollPane(list), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildButtonsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JButton addButton = new JButton(">>");
        JButton removeButton = new JButton("<<");
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        removeButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        addButton.addActionListener(e -> handleAdd());
        removeButton.addActionListener(e -> handleRemove());

        panel.add(Box.createVerticalGlue());
        panel.add(addButton);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));
        panel.add(removeButton);
        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private void handleAdd() {
        T item = availableList.getSelectedValue();
        if (item == null) {
            return;
        }
        try {
            doAdd(item);
            refresh();
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void handleRemove() {
        T item = selectedList.getSelectedValue();
        if (item == null) {
            return;
        }
        try {
            doRemove(item);
            refresh();
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    protected void setRenderer(ListCellRenderer<? super T> renderer) {
        availableList.setCellRenderer(renderer);
        selectedList.setCellRenderer(renderer);
    }

    protected void setAvailable(List<T> items) {
        availableModel.clear();
        for (T item : items) {
            availableModel.addElement(item);
        }
    }

    protected void setSelected(List<T> items) {
        selectedModel.clear();
        for (T item : items) {
            selectedModel.addElement(item);
        }
    }

    protected void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    protected abstract void doAdd(T item) throws Exception;
    protected abstract void doRemove(T item) throws Exception;
    public abstract void refresh();
}
