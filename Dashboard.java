import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Dashboard extends JFrame {

    private JButton productsButton;
    private JButton usersButton;
    private JButton addProductsButton;
    private JButton ordersButton;
    private JButton logoutButton;
    

    public Dashboard(String userRole) {
        super("Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width / 2 - getWidth() / 2, dim.height / 2 - getHeight() / 2);
        setLayout(null);

        // Common buttons for all users
        productsButton = new JButton("Products");
        productsButton.setBounds(150, 50, 150, 30);
        productsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openProductsPage();
            }
        });
        add(productsButton);

        addProductsButton = new JButton("Add New Products");
        addProductsButton.setBounds(150, 100, 150, 30);
        addProductsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InventoryManager.showAddProductDialog();
            }
        });
        add(addProductsButton);

        logoutButton = new JButton("Logout");
        logoutButton.setBounds(150, 300, 150, 30);
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
        add(logoutButton);

        // Role-specific buttons
        if ("Administrator".equals(userRole)) {
            usersButton = new JButton("Users");
            usersButton.setBounds(150, 150, 150, 30);
            usersButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    openUsersPage();
                }
            });
            add(usersButton);

            ordersButton = new JButton("Orders");
            ordersButton.setBounds(150, 200, 150, 30);
            ordersButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    openOrdersPage();
                }
            });
            add(ordersButton);
        } else if ("Warehouse Staff".equals(userRole) || "Inventory Manager".equals(userRole)) {
            ordersButton = new JButton("Orders");
            ordersButton.setBounds(150, 150, 150, 30);
            ordersButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    openOrdersPage();
                }
            });
            add(ordersButton);
        }
    }

    private void openProductsPage() {
        Products productsdetail = new Products();
    productsdetail.setVisible(true);
    }

    private void openOrdersPage() {
    int confirmResult = JOptionPane.showConfirmDialog(this, "Are you sure you want to place an order?", "Confirm Order", JOptionPane.YES_NO_OPTION);

    if (confirmResult == JOptionPane.YES_OPTION) {
        // Open the Order frame
        Order orderFrame = new Order();
        orderFrame.setVisible(true);
    }
}



    private void openUsersPage() {
        // Check if the user is an administrator, inventory manager, or warehouse staff before opening the Users page
        if ("Administrator".equals(getUserRole()) || "Inventory Manager".equals(getUserRole()) || "Warehouse Staff".equals(getUserRole())) {
            User Usergrame = new User();
        Usergrame.setVisible(true);
        } else {
            // Optionally, display a message or take other actions for non-admin users
            System.out.println("You do not have permission to access the Users page.");
        }
    }

    private String getUserRole() {
        // Replace this with your actual method to get the user role
        // For now, returning a placeholder value
        return "Administrator";
    }

    private void logout() {
        int confirm = javax.swing.JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", javax.swing.JOptionPane.YES_NO_OPTION);
        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            dispose();
            System.out.println("Logout");
        }
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Dashboard dashboard = new Dashboard("Administrator");
                dashboard.setVisible(true);
            }
        });
    }
}
