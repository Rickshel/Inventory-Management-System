import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTextField usernameField;
    private JPasswordField passwordField;

    private JButton loginButton;
    private JButton signUpButton;
    private JButton logoutButton; // New logout button

    private static final String DB_URL = "jdbc:mysql://localhost:3306/imsdb";
    private static final String DB_USER = "admin";
    private static final String DB_PASSWORD = "admin";

    public Login() {
        super("Login Form");
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

        loginButton = new JButton("Login");
        loginButton.setBounds(110, 90, 80, 25);
        loginButton.addActionListener(e -> loginUser());
        add(loginButton);

        signUpButton = new JButton("Sign Up");
        signUpButton.setBounds(200, 90, 80, 25);
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSignUpForm();
            }
            
        });
        add(signUpButton);

        logoutButton = new JButton("Logout"); // Initialize logout button
        logoutButton.setBounds(110, 120, 80, 25);
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logoutUser();
            }
        });
        logoutButton.setVisible(false); // Initially hide the logout button
        add(logoutButton);
    }

    private void loginUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM Users WHERE Username = ? AND Password = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                statement.setString(2, password);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        String userRole = resultSet.getString("UserType");
                        JOptionPane.showMessageDialog(this, "Login successful!");
            

                         // Check user role and display notification if admin
            if ("Administrator".equals(userRole)) {
                checkAdminControl(username);
            }

            openDashboard(userRole);
            setLogoutButtonVisibility(true); // Show the logout button after successful login
        } else {
                        JOptionPane.showMessageDialog(this, "Invalid username or password.");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to the database.");
        }
    }

    private void setLogoutButtonVisibility(boolean b) {
    }

    private void checkAdminControl(String username) {
        // Check if the admin has control over notifications
        // (You can add a column in the database to store this information)
        boolean adminHasControl = isAdminControlEnabled(username);

        if (adminHasControl) {
            // Display a notification to the administrator
            JOptionPane.showMessageDialog(this, "Notification: System maintenance or critical update.", "System Notification", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private boolean isAdminControlEnabled(String username) {
        // Implement the logic to check if admin has control over notifications
        // This might involve querying the database for admin settings
        // For simplicity, let's assume a method isAdmin in the DatabaseUtils class
        return DatabaseUtils.isAdmin(username);
    }

    private void openDashboard(String userRole) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT UserType FROM Users WHERE Username = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, usernameField.getText());
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        Dashboard dashboard = new Dashboard(userRole);

                        if (!"Administrator".equals(userRole)) {
                          //  dashboard.hideUsersButton();
                        }

                        dashboard.setVisible(true);
                        dispose(); // Close the current login frame
                    } else {
                        JOptionPane.showMessageDialog(this, "No role found for the user.");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void openSignUpForm() {
        Signup signup = new Signup();
        signup.setVisible(true);

        dispose();
    }

    private void logoutUser() {
        // Perform logout actions, for example, hide the logout button
        logoutButton.setVisible(false);

        // Optionally, perform other logout actions here
        // For example, you might want to close the dashboard if it's open
    }

    public static void main(String[] args) {
        // Set up your database driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Create and show the login frame
        Login login = new Login();
        login.setVisible(true);
    }
}
