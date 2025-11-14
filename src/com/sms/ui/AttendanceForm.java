package com.sms.ui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

import com.sms.db.Connect;

public class AttendanceForm extends JFrame {
    JComboBox<String> cmbStudent;
    JTextField txtSubject, txtDate;
    JComboBox<String> cmbStatus;
    JButton btnSubmit;

    public AttendanceForm() {
        setTitle("Mark Attendance");
        setSize(800, 400);
        setLayout(new FlowLayout());
        setLocationRelativeTo(null);

        add(new JLabel("Student:"));
        cmbStudent = new JComboBox<>();
        loadStudents();
        add(cmbStudent);

        add(new JLabel("Subject:"));
        txtSubject = new JTextField(15);
        add(txtSubject);

        add(new JLabel("Date (YYYY-MM-DD):"));
        txtDate = new JTextField(10);
        add(txtDate);

        add(new JLabel("Status:"));
        cmbStatus = new JComboBox<>(new String[]{"Present", "Absent"});
        add(cmbStatus);

        btnSubmit = new JButton("Submit");
        add(btnSubmit);
        btnSubmit.addActionListener(e -> markAttendance());

        setVisible(true);
    }

    void loadStudents() {
        try {
            Connection con = Connect.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT name FROM students");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cmbStudent.addItem(rs.getString("name"));   //collecting data direct from table
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void markAttendance() {
        try {
            Connection con = Connect.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO attendance(student_id, subject, date, status) " +
                "VALUES ((SELECT student_id FROM students WHERE name=?), ?, ?, ?)");
            ps.setString(1, (String)cmbStudent.getSelectedItem());
            ps.setString(2, txtSubject.getText());
            ps.setString(3, txtDate.getText());
            ps.setString(4, (String)cmbStatus.getSelectedItem());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Attendance marked successfully!");
            dispose();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
