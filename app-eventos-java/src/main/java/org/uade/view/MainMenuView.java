package org.uade.view;

import org.uade.controller.AttendeeController;
import org.uade.controller.CateringController;
import org.uade.controller.EquipmentController;
import org.uade.controller.EventDetailController;
import org.uade.controller.EventFormController;
import org.uade.controller.EventMainMenuController;
import org.uade.controller.HallController;
import org.uade.model.Event;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;

public class MainMenuView extends JFrame {
    private final EventMainMenuController eventMainMenuController;
    private final EventFormController eventFormController;
    private final AttendeeController attendeeController;
    private final CateringController cateringController;
    private final EquipmentController equipmentController;
    private final HallController hallController;
    private final Runnable eventsOnChange;

    private final DefaultListModel<Event> listModel;
    private final JList<Event> eventList;

    public MainMenuView(EventMainMenuController eventMainMenuController,
                        EventFormController eventFormController,
                        AttendeeController attendeeController,
                        CateringController cateringController,
                        EquipmentController equipmentController,
                        HallController hallController,
                        Runnable eventsOnChange) {
        this.eventMainMenuController = eventMainMenuController;
        this.eventFormController = eventFormController;
        this.attendeeController = attendeeController;
        this.cateringController = cateringController;
        this.equipmentController = equipmentController;
        this.hallController = hallController;
        this.eventsOnChange = eventsOnChange != null ? eventsOnChange : () -> {};

        setTitle("Gestion de Eventos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setJMenuBar(buildMenuBar());

        JPanel mainPanel = new JPanel(new BorderLayout());
        add(mainPanel);

        JLabel titleLabel = new JLabel("Eventos proximos", SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 18f));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        this.listModel = new DefaultListModel<>();
        this.eventList = new JList<>(listModel);
        this.eventList.setCellRenderer(new EventListRenderer());
        JScrollPane scrollPane = new JScrollPane(eventList);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JButton addButton = new JButton("Agregar evento");
        JButton editButton = new JButton("Editar evento");
        JButton detailButton = new JButton("Ver detalle");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(detailButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> onAdd());
        editButton.addActionListener(e -> onEdit());
        detailButton.addActionListener(e -> onDetail());

        refreshList();
    }

    public void refreshList() {
        listModel.clear();
        for (Event event : eventMainMenuController.getActiveEventsSortedByDate()) {
            listModel.addElement(event);
        }
    }

    private Event getSelectedEvent() {
        return eventList.getSelectedValue();
    }

    private void onAdd() {
        EventFormView form = new EventFormView(this, eventFormController, hallController);
        form.setVisible(true);
        if (form.isSaved()) {
            refreshList();
        }
    }

    private void onEdit() {
        Event selected = getSelectedEvent();
        if (selected == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleccionar un evento para editar",
                    "Atencion",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        EventFormView form = new EventFormView(this, eventFormController, hallController, selected);
        form.setVisible(true);
        if (form.isSaved()) {
            refreshList();
        }
    }

    private JMenuBar buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu funcionesMenu = new JMenu("Funciones");
        JMenuItem calendarItem = new JMenuItem("Calendario");
        JMenuItem notificationsItem = new JMenuItem("Notificaciones");
        JMenuItem reportItem = new JMenuItem("Analisis de participacion");
        calendarItem.addActionListener(e -> new CalendarView(this, eventMainMenuController).setVisible(true));
        notificationsItem.addActionListener(e -> new NotificationsView(this, eventMainMenuController).setVisible(true));
        reportItem.addActionListener(e -> new ParticipationReportView(this, eventMainMenuController).setVisible(true));
        funcionesMenu.add(calendarItem);
        funcionesMenu.add(notificationsItem);
        funcionesMenu.add(reportItem);

        JMenu recursosMenu = new JMenu("Recursos");
        JMenuItem hallItem = new JMenuItem("Salones");
        JMenuItem cateringItem = new JMenuItem("Catering");
        JMenuItem equipmentItem = new JMenuItem("Equipamiento");
        JMenuItem attendeeItem = new JMenuItem("Asistentes");
        hallItem.addActionListener(e -> new HallManagementView(this, hallController).setVisible(true));
        cateringItem.addActionListener(e -> new CateringManagementView(this, cateringController).setVisible(true));
        equipmentItem.addActionListener(e -> new EquipmentManagementView(this, equipmentController).setVisible(true));
        attendeeItem.addActionListener(e -> new AttendeeManagementView(this, attendeeController).setVisible(true));
        recursosMenu.add(hallItem);
        recursosMenu.add(cateringItem);
        recursosMenu.add(equipmentItem);
        recursosMenu.add(attendeeItem);

        menuBar.add(funcionesMenu);
        menuBar.add(recursosMenu);
        return menuBar;
    }

    private void onDetail() {
        Event selected = getSelectedEvent();
        if (selected == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleccionar un evento para ver el detalle",
                    "Atencion",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        EventDetailController detailController = new EventDetailController(
                selected, attendeeController, cateringController, equipmentController);
        detailController.setOnChange(eventsOnChange);
        EventDetailView detail = new EventDetailView(
                this, selected, detailController,
                attendeeController, cateringController, equipmentController);
        detail.setVisible(true);
    }

    private static class EventListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                                                       int index, boolean isSelected,
                                                       boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Event event) {
                setText(event.getFormattedDate() + "  -  " + event.getName() + "  -  " + event.getFullLocation());
            }
            return this;
        }
    }
}
