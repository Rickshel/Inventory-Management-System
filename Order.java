import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Order extends JFrame {

    private JTextField productIdField;
    private JTextField quantityField;
    private JButton placeOrderButton;

    public Order() {
        setTitle("Place Order");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLayout(new GridLayout(3, 2));

        // Components
        add(new JLabel("Product ID:"));
        productIdField = new JTextField();
        add(productIdField);

        add(new JLabel("Quantity:"));
        quantityField = new JTextField();
        add(quantityField);

        placeOrderButton = new JButton("Place Order");
        placeOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeOrder();
            }
        });
        add(placeOrderButton);

        setVisible(true);
    }

    private void placeOrder() {
        String productIdText = productIdField.getText();
        String quantityText = quantityField.getText();
    
        if (productIdText.isEmpty() || quantityText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Product ID and Quantity.");
            return;
        }
    
        try {
            int productId = Integer.parseInt(productIdText);
            int quantity = Integer.parseInt(quantityText);
    
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be a positive integer.");
                return;
            }
    
            // Display a confirmation dialog
            int confirmResult = JOptionPane.showConfirmDialog(
                    this,
                    "Do you want to place an order for Product ID " + productId + " with a quantity of " + quantity + "?",
                    "Confirm Order",
                    JOptionPane.YES_NO_OPTION
            );
    
            if (confirmResult == JOptionPane.YES_OPTION) {
                // Deduct stock quantity
                boolean success = deductStock(productId, quantity);
    
                if (success) {
                    JOptionPane.showMessageDialog(this, "Order placed successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to place order. Insufficient stock or invalid product ID.");
                }
            }
    
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid numbers.");
        }
    }

    private boolean deductStock(int productId, int quantity) {
        String url = "jdbc:mysql://localhost:3306/imsdb";
        String username = "admin";
        String password = "admin";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Check if there is sufficient stock
            String checkStockSql = "SELECT Quantity FROM Products WHERE ProductID = ?";
            try (PreparedStatement checkStockStatement = connection.prepareStatement(checkStockSql)) {
                checkStockStatement.setInt(1, productId);
                ResultSet resultSet = checkStockStatement.executeQuery();

                if (resultSet.next()) {
                    int currentStock = resultSet.getInt("Quantity");

                    if (currentStock >= quantity) {
                        // Deduct stock
                        String deductStockSql = "UPDATE Products SET Quantity = Quantity - ? WHERE ProductID = ?";
                        try (PreparedStatement deductStockStatement = connection.prepareStatement(deductStockSql)) {
                            deductStockStatement.setInt(1, quantity);
                            deductStockStatement.setInt(2, productId);
                            deductStockStatement.executeUpdate();

                            return true; // Order placed successfully
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Insufficient stock or database error
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Order());
    }
}
