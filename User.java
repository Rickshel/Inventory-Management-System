import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class User extends JFrame {

    private JTable userTable;
    private JButton logoutButton;

    public User() {
        // Set up the JFrame
        setTitle("User Page");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Create the table model
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("User ID");
        tableModel.addColumn("Username");
        tableModel.addColumn("Password");
        tableModel.addColumn("User Type");

        // Create the JTable
        userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);

        // Add "Edit" button
        JButton editButton = new JButton("Edit");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = userTable.getSelectedRow();
                if (selectedRow != -1) {
                    editUser(selectedRow);
                } else {
                    JOptionPane.showMessageDialog(User.this, "Please select a user to edit.");
                }
            }
        });
// Add "Logout" button*/
logoutButton = new JButton("Logout");
logoutButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        logout();
    }
});

// Add components to the JFrame
add(scrollPane, BorderLayout.CENTER);
add(editButton, BorderLayout.SOUTH);
add(logoutButton, BorderLayout.NORTH);


        // Fetch data from the database and populate the table
        fetchData();

        // Set JFrame visibility
        setVisible(true);
    }

    private void fetchData() {
        String url = "jdbc:mysql://localhost:3306/imsdb";
        String username = "admin";
        String password = "admin";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();

            String sql = "SELECT * FROM Users";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                Object[] rowData = {
                        resultSet.getInt("UserID"),
                        resultSet.getString("Username"),
                        resultSet.getString("Password"),
                        resultSet.getString("UserType")
                };

                ((DefaultTableModel) userTable.getModel()).addRow(rowData);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void editUser(int rowIndex) {
        int userId = (int) userTable.getValueAt(rowIndex, 0);
        String currentUsername = (String) userTable.getValueAt(rowIndex, 1);
        String currentPassword = (String) userTable.getValueAt(rowIndex, 2);
        String currentUserType = (String) userTable.getValueAt(rowIndex, 3);

        // Create a dialog for editing user information
        JPanel editPanel = new JPanel(new GridLayout(4, 2));
        JTextField usernameField = new JTextField(currentUsername);
        JTextField passwordField = new JTextField(currentPassword);
        JTextField userTypeField = new JTextField(currentUserType);

        editPanel.add(new JLabel("Username:"));
        editPanel.add(usernameField);
        editPanel.add(new JLabel("Password:"));
        editPanel.add(passwordField);
        editPanel.add(new JLabel("User Type:"));
        editPanel.add(userTypeField);

        int result = JOptionPane.showConfirmDialog(
                this,
                editPanel,
                "Edit User Information",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {
            String newUsername = usernameField.getText();
            String newPassword = passwordField.getText();
            String newUserType = userTypeField.getText();

            // Update the table
            userTable.setValueAt(newUsername, rowIndex, 1);
            userTable.setValueAt(newPassword, rowIndex, 2);
            userTable.setValueAt(newUserType, rowIndex, 3);

            // Update the database
            updateDatabase(userId, newUsername, newPassword, newUserType);

            // Notify users based on their roles
            notifyUsers(newUserType);

            JOptionPane.showMessageDialog(this, "User information updated successfully.");
        }
    }
     private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            // Perform logout actions here
            dispose(); // Close the current frame
            // You may want to open the login frame or perform other actions after logout
            new Login().setVisible(true);
        }
    }

    private void updateDatabase(int userId, String newUsername, String newPassword, String newUserType) {
        String url = "jdbc:mysql://localhost:3306/imsdb";
        String username = "admin";
        String password = "admin";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);

            String sql = "UPDATE Users SET Username=?, Password=?, UserType=? WHERE UserID=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, newUsername);
                preparedStatement.setString(2, newPassword);
                preparedStatement.setString(3, newUserType);
                preparedStatement.setInt(4, userId);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("User information updated in the database.");
                } else {
                    System.out.println("Failed to update user information in the database.");
                }
            }

            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void notifyUsers(String userType) {
        if ("Warehouse Staff".equals(userType) || "Inventory Manager".equals(userType)) {
            // Display a notification to Warehouse Staff and Inventory Managers
            JOptionPane.showMessageDialog(null, "Notification: System maintenance or critical update.", "System Notification", JOptionPane.INFORMATION_MESSAGE);
        }
        // You can add more conditions for other user types if needed
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new User());
    }

    public void refreshData() {
        // Clear existing rows from the table
        DefaultTableModel tableModel = (DefaultTableModel) userTable.getModel();
        tableModel.setRowCount(0);

        // Fetch new data from the database and populate the table
        fetchData();
    }
}
