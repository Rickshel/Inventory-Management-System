import javax.swing.*;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InventoryManager {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/imsdb";
    private static final String DB_USER = "admin";
    private static final String DB_PASSWORD = "admin";

    public static void main(String[] args) {
        // JDBC connection setup
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Inventory Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Commented out the "Add New Product" button since it's already in the dashboard
        // JButton addProductButton = new JButton("Add New Product");
        // addProductButton.addActionListener(new ActionListener() {
        //     @Override
        //     public void actionPerformed(ActionEvent e) {
        //         showAddProductDialog();
        //     }
        // });

        // frame.getContentPane().add(addProductButton);
        frame.setSize(300, 200);
        frame.setVisible(true);
    }

    static void showAddProductDialog() {
        JFrame addProductFrame = new JFrame("Add New Product");
        addProductFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
        JTextField nameField = new JTextField();
        JTextField descriptionField = new JTextField();
        JTextField quantityField = new JTextField();
        JTextField reorderPointField = new JTextField();
    
        JButton addButton = new JButton("Add Product");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String description = descriptionField.getText();
                int quantity = Integer.parseInt(quantityField.getText());
                int reorderPoint = Integer.parseInt(reorderPointField.getText());
    
                addProduct(name, description, quantity, reorderPoint);
    
                // Optionally, you can close the addProductFrame after adding the product
                addProductFrame.dispose();
            }
        });
    
        JPanel formPanel = new JPanel(new GridLayout(5, 2));
        formPanel.add(new JLabel("Name"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Description"));
        formPanel.add(descriptionField);
        formPanel.add(new JLabel("Quantity"));
        formPanel.add(quantityField);
        formPanel.add(new JLabel("Reorder Point"));
        formPanel.add(reorderPointField);
        formPanel.add(new JLabel()); // empty label
        formPanel.add(addButton);
    
        addProductFrame.getContentPane().add(formPanel);
        addProductFrame.setSize(300, 200);
        addProductFrame.setVisible(true);
    }

    static void addProduct(String name, String description, int quantity, int reorderPoint) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO Products (Name, Description, Quantity, ReorderPoint) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, name);
                statement.setString(2, description);
                statement.setInt(3, quantity);
                statement.setInt(4, reorderPoint);
                int affectedRows = statement.executeUpdate();
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(null, "Product added successfully.");
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to add product.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addNewProduct() {
    }

    public static void invokeChatGPT() {
    }
}
