package com.sms.ui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

import com.sms.db.Connect;

public class MarksForm extends JFrame {

    JComboBox<String> cmbStudent;
    JTextField txtSubject, txtMarks, txtTotal;
    JLabel lblPercentage, lblGrade;
    JButton btnCalculate, btnSubmit;

    public MarksForm() {
        setTitle("Marks Entry Form");
        setSize(800, 400);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        add(new JLabel("Student:"));
        cmbStudent = new JComboBox<>();
        loadStudents();
        add(cmbStudent);

        add(new JLabel("Subject:"));
        txtSubject = new JTextField(20);
        add(txtSubject);

        add(new JLabel("Marks Obtained:"));
        txtMarks = new JTextField(20);
        add(txtMarks);

        add(new JLabel("Total Marks:"));
        txtTotal = new JTextField(20);
        add(txtTotal);

        add(new JLabel("Percentage:"));
        lblPercentage = new JLabel("-");
        add(lblPercentage);

        add(new JLabel("Grade:"));
        lblGrade = new JLabel("-");
        add(lblGrade);

        btnCalculate = new JButton("Calculate");
        btnSubmit = new JButton("Submit");

        add(btnCalculate);
        add(btnSubmit);

        btnCalculate.addActionListener(e -> calculateGrade());
        btnSubmit.addActionListener(e -> saveMarks());

        setVisible(true);
    }

    void loadStudents() {
        try {
            Connection con = Connect.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT name FROM students");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cmbStudent.addItem(rs.getString("name"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading students: " + e.getMessage());
        }
    }

    void calculateGrade() {
        try {
            double marks = Double.parseDouble(txtMarks.getText());
            double total = Double.parseDouble(txtTotal.getText());

            if (total == 0) {
                JOptionPane.showMessageDialog(this, "Total marks cannot be 0");
                return;
            }

            double percentage = (marks / total) * 100;
            lblPercentage.setText(String.format("%.2f%%", percentage));

            String grade;
            if (percentage >= 90) grade = "A+";
            else if (percentage >= 75) grade = "A";
            else if (percentage >= 60) grade = "B";
            else if (percentage >= 45) grade = "C";
            else if (percentage >= 33) grade = "D";
            else grade = "F";

            lblGrade.setText(grade);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for marks.");
        }
    }

    void saveMarks() {
        String name = (String) cmbStudent.getSelectedItem();
        String subject = txtSubject.getText();
        String marks = txtMarks.getText();
        String total = txtTotal.getText();

        if (name.isEmpty() || subject.isEmpty() || marks.isEmpty() || total.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!");
            return;
        }

        try {
            double marksVal = Double.parseDouble(marks);
            double totalVal = Double.parseDouble(total);
            double percentage = (marksVal / totalVal) * 100;

            String grade = lblGrade.getText();

            Connection con = Connect.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO marks (student_id, subject, marks_obtained, total_marks, percentage, grade) " +
                "VALUES ((SELECT student_id FROM students WHERE name=?), ?, ?, ?, ?, ?)"
            );

            ps.setString(1, name);
            ps.setString(2, subject);
            ps.setDouble(3, marksVal);
            ps.setDouble(4, totalVal);
            ps.setDouble(5, percentage);
            ps.setString(6, grade);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Marks saved successfully!");
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving marks: " + e.getMessage());
        }
    }
}
