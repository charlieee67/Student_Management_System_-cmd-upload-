package com.sms.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Dashboard extends JFrame {

    JButton btnAddStudent, btnAttendance, btnMarks, btnReport, btnLogout;

    public Dashboard() {
        setTitle("Dashboard - Student Management System");
        setSize(800, 400);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        btnAddStudent = new JButton("Add Student");
        btnAttendance = new JButton("Mark Attendance");
        btnMarks = new JButton("Enter Marks");
        btnReport = new JButton("View Reports");
        btnLogout = new JButton("Logout");

        add(btnAddStudent);
        add(btnAttendance);
        add(btnMarks);
        add(btnReport);
        add(btnLogout);

        btnAddStudent.addActionListener(e -> new AddStudentForm());
        btnAttendance.addActionListener(e -> new AttendanceForm());
        btnMarks.addActionListener(e -> new MarksForm());
        btnReport.addActionListener(e -> new ResultReport());
        btnLogout.addActionListener(e -> System.exit(0));

        setVisible(true);
    }
}
