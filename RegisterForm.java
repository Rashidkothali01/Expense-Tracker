import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class RegisterForm extends JFrame {
    JTextField nameField, emailField, phoneField, collegeField;
    JComboBox<String> eventBox;
    JButton registerBtn, clearBtn, viewBtn;

    public RegisterForm() {
        setTitle("Event Registration");
        setSize(400, 400);
        setLayout(new GridLayout(8, 2, 5, 5));
        setLocationRelativeTo(null);

        add(new JLabel("Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Email:"));
        emailField = new JTextField();
        add(emailField);

        add(new JLabel("Phone:"));
        phoneField = new JTextField();
        add(phoneField);

        add(new JLabel("Event:"));
        eventBox = new JComboBox<>(new String[]{"Coding", "Robotics", "Quiz", "Hackathon"});
        add(eventBox);

        add(new JLabel("College:"));
        collegeField = new JTextField();
        add(collegeField);

        registerBtn = new JButton("Register");
        clearBtn = new JButton("Clear");
        viewBtn = new JButton("View Registrations");

        add(registerBtn);
        add(clearBtn);
        add(new JLabel(""));
        add(viewBtn);

        registerBtn.addActionListener(e -> registerParticipant());
        clearBtn.addActionListener(e -> clearFields());
        viewBtn.addActionListener(e -> new ViewRegistrations());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void registerParticipant() {
        try (Connection con = DBConnection.getConnection()) {
            String query = "INSERT INTO registrations (name, email, phone, event_name, college) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, nameField.getText());
            ps.setString(2, emailField.getText());
            ps.setString(3, phoneField.getText());
            ps.setString(4, eventBox.getSelectedItem().toString());
            ps.setString(5, collegeField.getText());
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "✅ Registration Successful!");
            clearFields();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage());
        }
    }

    private void clearFields() {
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        collegeField.setText("");
        eventBox.setSelectedIndex(0);
    }
}
