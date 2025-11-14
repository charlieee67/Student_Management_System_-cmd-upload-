package com.sms.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import com.sms.db.Connect;

public class ResultReport extends JFrame {

    JTable table;
    DefaultTableModel model;
    JButton btnRefresh;

    public ResultReport() {
        setTitle("Student Result Report");
        setSize(700, 400);
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Column headers
        String[] columnNames = {"Student Name", "Subject", "Marks Obtained", "Total Marks", "Percentage", "Grade"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Refresh button
        btnRefresh = new JButton("Refresh");
        add(btnRefresh, BorderLayout.SOUTH);

        btnRefresh.addActionListener(e -> loadData());

        loadData();

        setVisible(true);
    }

    void loadData() {
        model.setRowCount(0); // clear previous data

        try (Connection con = Connect.getConnection()) {
            String query = "SELECT s.name, m.subject, m.marks_obtained, m.total_marks " +
                           "FROM marks m JOIN students s ON s.student_id = m.student_id";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                String subject = rs.getString("subject");
                int marks = rs.getInt("marks_obtained");
                int total = rs.getInt("total_marks");

                // calculate percentage safely
                double percentage = (total > 0) ? ((double) marks / total) * 100 : 0.0;
                String grade = getGrade(percentage);

                model.addRow(new Object[]{
                        name,
                        subject,
                        marks,
                        total,
                        String.format("%.2f%%", percentage),
                        grade
                });
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }

    String getGrade(double percentage) {
        if (percentage >= 90) return "A+";
        else if (percentage >= 75) return "A";
        else if (percentage >= 60) return "B";
        else if (percentage >= 45) return "C";
        else if (percentage >= 33) return "D";
        else return "F";
    }

    public static void main(String[] args) {
        new ResultReport();
    }
}
