package com.sms.ui;

import javax.swing.*;

import com.sms.db.Connect;

import java.awt.*;
import java.sql.*;

public class AddStudentForm extends JFrame {
    JTextField txtName, txtRoll, txtClass, txtEmail, txtPhone;
    JButton btnSave;

    public AddStudentForm() {
        setTitle("Add Student");
        setSize(800, 400);
        setLayout(new FlowLayout());
        setLocationRelativeTo(null);

        add(new JLabel("Name:"));
        txtName = new JTextField(20);
        add(txtName);

        add(new JLabel("Roll No:"));
        txtRoll = new JTextField(10);
        add(txtRoll);

        add(new JLabel("Class:"));
        txtClass = new JTextField(10);
        add(txtClass);

        add(new JLabel("Email:"));
        txtEmail = new JTextField(10);
        add(txtEmail);

        add(new JLabel("Phone:"));
        txtPhone = new JTextField(10);
        add(txtPhone);

        btnSave = new JButton("Save");
        add(btnSave);
        btnSave.addActionListener(e -> saveStudent());

        setVisible(true);
    }

    void saveStudent() {
        try {
            Connection con = Connect.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO students(name, roll_no, class, email, phone) VALUES (?,?,?,?,?)");
            ps.setString(1, txtName.getText());
            ps.setString(2, txtRoll.getText());
            ps.setString(3, txtClass.getText());
            ps.setString(4, txtEmail.getText());
            ps.setString(5, txtPhone.getText());
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Student added successfully!");
            dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
