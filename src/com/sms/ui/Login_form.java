package com.sms.ui;

import javax.swing.*;

import com.sms.db.Connect;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Login_form extends JFrame {
    JLabel lblUser, lblPass;
    JTextField txtUser;
    JPasswordField txtPass;
    JButton btnLogin, btnExit;

    public Login_form() {
        setTitle("Login - Student Management System");
        setSize(800, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        

        lblUser = new JLabel("Username:");
        lblPass = new JLabel("Password:");
        txtUser = new JTextField(20);
        txtPass = new JPasswordField(20);
        btnLogin = new JButton("Login");
        btnExit = new JButton("Exit");

        add(lblUser); add(txtUser);
        add(lblPass); add(txtPass);
        add(btnLogin); add(btnExit);

        btnLogin.addActionListener(e -> login());
        btnExit.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    void login() {
        String user = txtUser.getText();
        String pass = new String(txtPass.getPassword());

        try {
            Connection con = Connect.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "SELECT role FROM users WHERE username=? AND password=?");
            ps.setString(1, user);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                JOptionPane.showMessageDialog(this, "Login successful as " + role);
                dispose();
                new Dashboard();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password");
            }
        } catch (Exception e) {
        	 JOptionPane.showMessageDialog(this, "Communication link failed ");
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
		new Login_form();
	}
}
