import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Signup extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> userTypeComboBox;
    private JButton signUpButton;
    private JButton backToLoginButton;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/imsdb";
    private static final String DB_USER = "admin";
    private static final String DB_PASSWORD = "admin";

    public Signup() {
        super("Sign Up Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width / 2 - getWidth() / 2, dim.height / 2 - getHeight() / 2);
        setLayout(null);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(20, 30, 80, 20);
        add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(110, 30, 150, 20);
        add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(20, 60, 80, 20);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(110, 60, 150, 20);
        add(passwordField);

        JLabel userTypeLabel = new JLabel("User Type:");
        userTypeLabel.setBounds(20, 90, 80, 20);
        add(userTypeLabel);

        String[] userTypes = {"Administrator", "Inventory Manager", "Warehouse Staff"};
        userTypeComboBox = new JComboBox<>(userTypes);
        userTypeComboBox.setBounds(110, 90, 150, 20);
        add(userTypeComboBox);

        signUpButton = new JButton("Sign Up");
        signUpButton.setBounds(110, 120, 80, 25);
        signUpButton.setEnabled(false);
        signUpButton.addActionListener(e -> signUpUser());
        add(signUpButton);

        backToLoginButton = new JButton("Back to Login");
        backToLoginButton.setBounds(200, 120, 120, 25);
        backToLoginButton.addActionListener(e -> backToLogin());
        add(backToLoginButton);

        usernameField.getDocument().addDocumentListener(new SignupDocumentListener());
        passwordField.getDocument().addDocumentListener(new SignupDocumentListener());
        userTypeComboBox.addActionListener(new SignupActionListener());
    }

    private void signUpUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String userType = (String) userTypeComboBox.getSelectedItem();

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO Users (Username, Password, UserType) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                statement.setString(2, password);
                statement.setString(3, userType);

                int affectedRows = statement.executeUpdate();
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, "User registered successfully!");
                    backToLogin(); // Go back to the login page after successful registration
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to register user.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to the database.");
        }
    }

    private void backToLogin() {
        Login login = new Login();
        login.setVisible(true);
        dispose(); // Close the current signup frame
    }

    private class SignupDocumentListener implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent e) {
            updateButtonState();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            updateButtonState();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            updateButtonState();
        }
    }

    private class SignupActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            updateButtonState();
        }
    }

    private void updateButtonState() {
        boolean fieldsFilled = !usernameField.getText().trim().isEmpty() &&
                passwordField.getPassword().length > 0 &&
                userTypeComboBox.getSelectedItem() != null;

        signUpButton.setEnabled(fieldsFilled);
    }

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Signup signup = new Signup();
        signup.setVisible(true);
    }
}
