import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Products extends JFrame {
private JTable productTable;
private JTextField searchField;
private List<ProductChange> productChanges = new ArrayList<>();

public Products() {
// Set up the JFrame
setTitle("Product List");
setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Allow window close handling
setSize(800, 600);
setLayout(new BorderLayout());

// Create a panel for search functionality
JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
searchField = new JTextField(20);
JButton searchButton = new JButton("Search");
searchButton.addActionListener(e -> searchProducts());
searchPanel.add(new JLabel("Search by Name:"));
searchPanel.add(searchField);
searchPanel.add(searchButton);
add(searchPanel, BorderLayout.NORTH);

JButton printChangelogButton = new JButton("Print Changelog");
        printChangelogButton.addActionListener(e -> printChangelog());
        printChangelogButton.setPreferredSize(new Dimension(120, 30));
        add(printChangelogButton, BorderLayout.EAST);
// Create the table model
DefaultTableModel tableModel = new DefaultTableModel() {
@Override
public boolean isCellEditable(int row, int column) {
return true; // Allow all cells to be editable
}
};

tableModel.addColumn("Product ID");
tableModel.addColumn("Name");
tableModel.addColumn("Description");
tableModel.addColumn("Quantity");
tableModel.addColumn("Reorder Point");

// Create the JTable
productTable = new JTable(tableModel);
productTable.getModel().addTableModelListener(new MyTableModelListener());
JScrollPane scrollPane = new JScrollPane(productTable);

// Add "Edit" button
JButton editButton = new JButton("Edit");
editButton.addActionListener(e -> {
int selectedRow = productTable.getSelectedRow();
if (selectedRow != -1) {
editProduct(selectedRow);
} else {
JOptionPane.showMessageDialog(this, "Please select a product to edit.");
}
});

// Add components to the JFrame
add(scrollPane, BorderLayout.CENTER);
add(editButton, BorderLayout.SOUTH);

// Fetch data from the database and populate the table
fetchData();

// Set JFrame visibility
setVisible(true);

// Add a Window Listener to save changes on window close
addWindowListener(new WindowAdapter() {
@Override
public void windowClosing(WindowEvent e) {
saveProductChangesToDatabase();
dispose();
}
});
}

private void printChangelog() {
    // Define the file path where you want to save the changelog
    String filePath = "changelog.txt";

    try (FileWriter writer = new FileWriter(filePath, true)) {
        File file = new File(filePath);

        // Check if the file is empty, and if so, write the header
        if (file.length() == 0) {
            writer.write("Product Change Log:\n");
        }

        for (ProductChange change : productChanges) {
            String logEntry = String.format(
                "Product ID: %d, Column: %s, Old Value: %s, New Value: %s, Timestamp: %s%n",
                change.getProductId(),
                change.getColumnName(),
                change.getOldValue(),
                change.getNewValue(),
                change.getChangeTimestamp()
            );

            System.out.print(logEntry);
            writer.write(logEntry);
        }

        // Clear the productChanges list after printing the changelog
        productChanges.clear();

        System.out.println("Changelog saved to " + filePath);
    } catch (IOException ex) {
        ex.printStackTrace();
        System.err.println("Error writing changelog to file: " + ex.getMessage());
    }
}

private void searchProducts() {
DefaultTableModel tableModel = (DefaultTableModel) productTable.getModel();
tableModel.setRowCount(0); // Clear existing rows

String searchTerm = searchField.getText();

String url = "jdbc:mysql://localhost:3306/imsdb";
String username = "admin";
String password = "admin";

try {
Connection connection = DriverManager.getConnection(url, username, password);
String sql = "SELECT * FROM Products WHERE Name LIKE ?";
try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
preparedStatement.setString(1, "%" + searchTerm + "%"); // Add % around the searchTerm
ResultSet resultSet = preparedStatement.executeQuery();

while (resultSet.next()) {
Object[] rowData = {
resultSet.getInt("ProductID"),
resultSet.getString("Name"),
resultSet.getString("Description"),
resultSet.getInt("Quantity"),
resultSet.getInt("ReorderPoint")
};

tableModel.addRow(rowData);
}

resultSet.close();
}

connection.close();

} catch (Exception e) {
e.printStackTrace();
}
}

private void fetchData() {
String url = "jdbc:mysql://localhost:3306/imsdb";
String username = "admin";
String password = "admin";

try {
Connection connection = DriverManager.getConnection(url, username, password);
Statement statement = connection.createStatement();

String sql = "SELECT * FROM Products";
ResultSet resultSet = statement.executeQuery(sql);

while (resultSet.next()) {
Object[] rowData = {
resultSet.getInt("ProductID"),
resultSet.getString("Name"),
resultSet.getString("Description"),
resultSet.getInt("Quantity"),
resultSet.getInt("ReorderPoint")
};

((DefaultTableModel) productTable.getModel()).addRow(rowData);
}

resultSet.close();
statement.close();
connection.close();

} catch (Exception e) {
e.printStackTrace();
}
}

private void editProduct(int rowIndex) {
int productId = (int) productTable.getValueAt(rowIndex, 0);
String currentName = (String) productTable.getValueAt(rowIndex, 1);
String currentDescription = (String) productTable.getValueAt(rowIndex, 2);
int currentQuantity = (int) productTable.getValueAt(rowIndex, 3);
int currentReorderPoint = (int) productTable.getValueAt(rowIndex, 4);

// Create a dialog for editing product information
JPanel editPanel = new JPanel(new GridLayout(4, 2));
JTextField nameField = new JTextField(currentName);
JTextField descriptionField = new JTextField(currentDescription);
JTextField quantityField = new JTextField(String.valueOf(currentQuantity));
JTextField reorderPointField = new JTextField(String.valueOf(currentReorderPoint));

editPanel.add(new JLabel("Name:"));
editPanel.add(nameField);
editPanel.add(new JLabel("Description:"));
editPanel.add(descriptionField);
editPanel.add(new JLabel("Quantity:"));
editPanel.add(quantityField);
editPanel.add(new JLabel("Reorder Point:"));
editPanel.add(reorderPointField);

int result = JOptionPane.showConfirmDialog(
this,
editPanel,
"Edit Product Information",
JOptionPane.OK_CANCEL_OPTION
);

if (result == JOptionPane.OK_OPTION) {
String newName = nameField.getText();
String newDescription = descriptionField.getText();
int newQuantity = Integer.parseInt(quantityField.getText());
int newReorderPoint = Integer.parseInt(reorderPointField.getText());

// Update the table
productTable.setValueAt(newName, rowIndex, 1);
productTable.setValueAt(newDescription, rowIndex, 2);
productTable.setValueAt(newQuantity, rowIndex, 3);
productTable.setValueAt(newReorderPoint, rowIndex, 4);

// Update the database
updateDatabase(productId, newName, newDescription, newQuantity, newReorderPoint);

// Log the change
logProductChange(productId, "Name", currentName, newName);
logProductChange(productId, "Description", currentDescription, newDescription);
logProductChange(productId, "Quantity", String.valueOf(currentQuantity), String.valueOf(newQuantity));
logProductChange(productId, "ReorderPoint", String.valueOf(currentReorderPoint), String.valueOf(newReorderPoint));

JOptionPane.showMessageDialog(this, "Product information updated successfully.");
}
}

private void updateDatabase(int productId, String newName, String newDescription, int newQuantity, int newReorderPoint) {
String url = "jdbc:mysql://localhost:3306/imsdb";
String username = "admin";
String password = "admin";

try {
Connection connection = DriverManager.getConnection(url, username, password);

String sql = "UPDATE Products SET Name=?, Description=?, Quantity=?, ReorderPoint=? WHERE ProductID=?";
try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
preparedStatement.setString(1, newName);
preparedStatement.setString(2, newDescription);
preparedStatement.setInt(3, newQuantity);
preparedStatement.setInt(4, newReorderPoint);
preparedStatement.setInt(5, productId);

int rowsAffected = preparedStatement.executeUpdate();
if (rowsAffected > 0) {
System.out.println("Product information updated in the database.");
} else {
System.out.println("Failed to update product information in the database.");
}
}

connection.close();

} catch (Exception e) {
e.printStackTrace();
}
}

private void logProductChange(int productId, String columnName, String oldValue, String newValue) {
ProductChange change = new ProductChange(productId, columnName, oldValue, newValue);
productChanges.add(change);
}

private void saveProductChangesToDatabase() {
String url = "jdbc:mysql://localhost:3306/imsdb";
String username = "admin";
String password = "admin";

try {
Connection connection = DriverManager.getConnection(url, username, password);

for (ProductChange change : productChanges) {
String sql = "INSERT INTO ProductChanges (ProductID, ColumnName, OldValue, NewValue) VALUES (?, ?, ?, ?)";
try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
preparedStatement.setInt(1, change.getProductId());
preparedStatement.setString(2, change.getColumnName());
preparedStatement.setString(3, change.getOldValue());
preparedStatement.setString(4, change.getNewValue());

preparedStatement.executeUpdate();
}
}

connection.close();

} catch (Exception e) {
e.printStackTrace();
}
}

public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new Products());
}

public void refreshData() {
// Clear existing rows from the table
DefaultTableModel tableModel = (DefaultTableModel) productTable.getModel();
tableModel.setRowCount(0);

// Fetch new data from the database and populate the table
fetchData();
}

// TableModelListener to capture changes
class MyTableModelListener implements TableModelListener {
    public MyTableModelListener() {
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        if (e.getType() == TableModelEvent.UPDATE) {
            int row = e.getFirstRow();
            int column = e.getColumn();
            DefaultTableModel model = (DefaultTableModel) e.getSource();
            Object updatedValue = model.getValueAt(row, column);

            // Update the database based on the edited cell and value
            updateDatabase(row, column, updatedValue);
        }
    }
    private void checkLowStockNotification(int row) {
        int currentStock = (int) productTable.getValueAt(row, 3); // Assuming Quantity column is at index 3

        // Define your low stock threshold (e.g., 2)
        int lowStockThreshold = 2;
    
        if (currentStock <= 0) {
            // Remove the row from the table
            removeRowFromTable(row);
            showLowStockNotification("Out of stock", row);
        } else if (currentStock <= lowStockThreshold) {
            showLowStockNotification("Low stock level", row);
        }
    }
    
    private void removeRowFromTable(int row) {
        DefaultTableModel model = (DefaultTableModel) productTable.getModel();
        model.removeRow(row);
    }

    private void updateDatabase(int row, int column, Object newValue) {
        String columnName = null;
    
        // Determine the column name based on the column index
        switch (column) {
            case 1: // Name
                columnName = "Name";
                break;
            case 2: // Description
                columnName = "Description";
                break;
            // Add cases for other columns as needed
            case 3:
                columnName = "Quantity";
                break;
            case 4:
                columnName = "ReorderPoint";
                break;
        }
    
        if (columnName != null) {
            int productId = (int) productTable.getValueAt(row, 0); // Assuming ProductID is in column 0
            String currentStringValue = String.valueOf(productTable.getValueAt(row, column));
            String updateSql = "UPDATE Products SET " + columnName + " = ? WHERE ProductID = ?";
    
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/imsdb", "admin", "admin")) {
                PreparedStatement statement = connection.prepareStatement(updateSql);
                statement.setObject(1, newValue);
                statement.setInt(2, productId);
                statement.executeUpdate();
    
                // Log the change
                logProductChange(productId, columnName, currentStringValue, String.valueOf(newValue));
    
                // Check for low stock levels after updating the Quantity column
                if (columnName.equals("Quantity")) {
                    checkLowStockNotification(row);
                }
    
            } catch (Exception e) {
                e.printStackTrace();
                // Handle errors appropriately
            }
        }
    }

    private void logProductChange(int productId, String columnName, String oldValue, String newValue) {
        String logSql = "INSERT INTO ProductChanges (ProductID, ColumnName, OldValue, NewValue, ChangeTimestamp) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/imsdb", "admin", "admin")) {
            PreparedStatement statement = connection.prepareStatement(logSql);
            statement.setInt(1, productId);
            statement.setString(2, columnName);
            statement.setString(3, oldValue);
            statement.setString(4, newValue);
            statement.executeUpdate();

            System.out.println("Change logged successfully.");

        } catch (Exception e) {
            e.printStackTrace();
            // Handle errors appropriately
        }
    }
}

public void showLowStockNotification(String string, int row) {
}
}
