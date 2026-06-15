package org.uade.view;

import org.uade.controller.EventMainMenuController;
import org.uade.model.Event;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CalendarView extends JDialog {
    private static final String[] DAY_HEADERS = {"Lun", "Mar", "Mie", "Jue", "Vie", "Sab", "Dom"};
    private static final String[] MONTH_NAMES = {
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    };
    private static final Color EVENT_DAY_COLOR = new Color(173, 216, 230);
    private static final Color TODAY_BORDER_COLOR = new Color(30, 144, 255);

    private final EventMainMenuController eventController;
    private YearMonth currentMonth;
    private JLabel monthLabel;
    private JPanel calendarGrid;

    public CalendarView(JFrame parent, EventMainMenuController eventController) {
        super(parent, true);
        this.eventController = eventController;
        this.currentMonth = YearMonth.now();

        setTitle("Calendario de Eventos");
        setSize(720, 560);
        setLocationRelativeTo(parent);

        initUI();
        renderMonth();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel navPanel = new JPanel();
        JButton prevButton = new JButton("<");
        JButton nextButton = new JButton(">");
        JButton todayButton = new JButton("Hoy");
        monthLabel = new JLabel("", SwingConstants.CENTER);
        monthLabel.setFont(monthLabel.getFont().deriveFont(Font.BOLD, 18f));

        prevButton.addActionListener(e -> {
            currentMonth = currentMonth.minusMonths(1);
            renderMonth();
        });
        nextButton.addActionListener(e -> {
            currentMonth = currentMonth.plusMonths(1);
            renderMonth();
        });
        todayButton.addActionListener(e -> {
            currentMonth = YearMonth.now();
            renderMonth();
        });

        navPanel.add(prevButton);
        navPanel.add(monthLabel);
        navPanel.add(nextButton);
        navPanel.add(todayButton);
        add(navPanel, BorderLayout.NORTH);

        calendarGrid = new JPanel(new GridLayout(7, 7, 2, 2));
        add(calendarGrid, BorderLayout.CENTER);
    }

    private void renderMonth() {
        calendarGrid.removeAll();

        for (String dayName : DAY_HEADERS) {
            JLabel headerLabel = new JLabel(dayName, SwingConstants.CENTER);
            headerLabel.setFont(headerLabel.getFont().deriveFont(Font.BOLD));
            calendarGrid.add(headerLabel);
        }

        Map<Integer, ArrayList<Event>> eventsByDay = getEventsByDay();

        LocalDate firstDay = currentMonth.atDay(1);
        int dayOfWeek = firstDay.getDayOfWeek().getValue();
        int emptyCells = dayOfWeek - 1;

        for (int i = 0; i < emptyCells; i++) {
            calendarGrid.add(buildEmptyCell());
        }

        int daysInMonth = currentMonth.lengthOfMonth();
        LocalDate today = LocalDate.now();
        for (int day = 1; day <= daysInMonth; day++) {
            ArrayList<Event> dayEvents = eventsByDay.get(day);
            boolean isToday = today.getYear() == currentMonth.getYear()
                    && today.getMonth() == currentMonth.getMonth()
                    && today.getDayOfMonth() == day;
            calendarGrid.add(buildDayCell(day, dayEvents, isToday));
        }

        int totalDataCells = 6 * 7;
        int filled = emptyCells + daysInMonth;
        for (int i = filled; i < totalDataCells; i++) {
            calendarGrid.add(buildEmptyCell());
        }

        monthLabel.setText(MONTH_NAMES[currentMonth.getMonthValue() - 1] + " " + currentMonth.getYear());
        calendarGrid.revalidate();
        calendarGrid.repaint();
    }

    private JPanel buildEmptyCell() {
        JPanel cell = new JPanel();
        cell.setOpaque(true);
        cell.setBackground(new Color(245, 245, 245));
        cell.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        return cell;
    }

    private JPanel buildDayCell(int day, ArrayList<Event> events, boolean isToday) {
        JPanel cell = new JPanel(new BorderLayout());
        cell.setOpaque(true);
        cell.setBackground(Color.WHITE);

        if (isToday) {
            cell.setBorder(BorderFactory.createLineBorder(TODAY_BORDER_COLOR, 2));
        } else {
            cell.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        }

        JLabel dayLabel = new JLabel(" " + day);
        dayLabel.setFont(dayLabel.getFont().deriveFont(Font.BOLD));
        cell.add(dayLabel, BorderLayout.NORTH);

        if (events != null && !events.isEmpty()) {
            cell.setBackground(EVENT_DAY_COLOR);
            JLabel countLabel = new JLabel(events.size() + " evento(s)", SwingConstants.CENTER);
            cell.add(countLabel, BorderLayout.CENTER);
            cell.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            cell.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    showEventsForDay(events);
                }
            });
        }

        return cell;
    }

    private Map<Integer, ArrayList<Event>> getEventsByDay() {
        Map<Integer, ArrayList<Event>> map = new HashMap<>();
        for (Event event : eventController.getActiveEvents()) {
            LocalDate eventDate = event.getDate();
            if (eventDate.getYear() == currentMonth.getYear()
                    && eventDate.getMonth() == currentMonth.getMonth()) {
                map.computeIfAbsent(eventDate.getDayOfMonth(), k -> new ArrayList<>()).add(event);
            }
        }
        return map;
    }

    private void showEventsForDay(ArrayList<Event> events) {
        StringBuilder sb = new StringBuilder();
        for (Event event : events) {
            sb.append(" - ").append(event.getName())
              .append(" (").append(event.getFullLocation()).append(")\n");
        }
        JOptionPane.showMessageDialog(this, sb.toString(),
                "Eventos del dia", JOptionPane.INFORMATION_MESSAGE);
    }
}
