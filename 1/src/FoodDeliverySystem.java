import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class FoodDeliverySystem {

    public static final Order sharedOrder = new Order(); // Shared order state

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainWindow::new);
    }
}

// Shared Order Class
class Order {
    private List<String> items = new ArrayList<>();
    private String status = "No Order Placed";

    public void setItems(List<String> items) {
        this.items = new ArrayList<>(items);
        this.status = "Order Received";
    }

    public List<String> getItems() {
        return items;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

// Main Window
class MainWindow {
    public MainWindow() {
        JFrame frame = new JFrame("Food Delivery Management System");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome to Food Delivery Management System", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(welcomeLabel, BorderLayout.NORTH);

        JPanel rolePanel = new JPanel(new GridLayout(3, 1, 10, 10));

        JButton customerButton = new JButton("Customer");
        JButton restaurantButton = new JButton("Restaurant Staff");
        JButton deliveryButton = new JButton("Delivery Person");

        customerButton.addActionListener(e -> new CustomerInfo());

        restaurantButton.addActionListener(e -> new RestaurantWindow());
        deliveryButton.addActionListener(e -> new DeliveryWindow());

        rolePanel.add(customerButton);
        rolePanel.add(restaurantButton);
        rolePanel.add(deliveryButton);

        frame.add(rolePanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}

class CustomerInfo {
    public CustomerInfo() {
        JFrame frame = new JFrame("Customer Information");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new GridLayout(5, 1, 10, 10));

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();

        JLabel phoneLabel = new JLabel("Telephone:");
        JTextField phoneField = new JTextField();

        JLabel addressLabel = new JLabel("Address:");
        JTextField addressField = new JTextField();

        JButton saveButton = new JButton("Save");

        frame.add(nameLabel);
        frame.add(nameField);
        frame.add(phoneLabel);
        frame.add(phoneField);
        frame.add(addressLabel);
        frame.add(addressField);
        frame.add(saveButton);

        saveButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String address = addressField.getText().trim();

            if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Information saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();

                // Pass the user info to CustomerWindow
                CustomerWindow customerWindow = new CustomerWindow();
                customerWindow.setCustomerInfo(name, phone, address);
            }
        });

        frame.setVisible(true);
    }
}



// Customer Window
class CustomerWindow {
    private final List<String> cart = new ArrayList<>();
    private String customerName;
    private String customerPhone;
    private String customerAddress;

    public CustomerWindow() {
        JFrame frame = new JFrame("Customer Portal");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new GridLayout(4, 1, 10, 10)); // Updated to 4 rows

        JLabel label = new JLabel("Customer Features", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(label);

        JButton browseMenuButton = new JButton("Browse Menu");
        JButton viewCartButton = new JButton("View Cart");
        JButton viewOrderStatusButton = new JButton("View Order's Status");
        JButton viewProfileButton = new JButton("View Profile"); // New button

        browseMenuButton.addActionListener(e -> new MenuGUI(cart));
        viewCartButton.addActionListener(e -> new CartGUI(cart, FoodDeliverySystem.sharedOrder));
        viewOrderStatusButton.addActionListener(e -> JOptionPane.showMessageDialog(frame,
                "Order Status: " + FoodDeliverySystem.sharedOrder.getStatus()));

        // New action listener for "View Profile"
        viewProfileButton.addActionListener(e -> {
            String profileInfo = String.format("Name: %s%nPhone: %s%nAddress: %s",
                    customerName != null ? customerName : "Not set",
                    customerPhone != null ? customerPhone : "Not set",
                    customerAddress != null ? customerAddress : "Not set");

            JOptionPane.showMessageDialog(frame, profileInfo, "Profile Information", JOptionPane.INFORMATION_MESSAGE);
        });

        frame.add(browseMenuButton);
        frame.add(viewCartButton);
        frame.add(viewOrderStatusButton);
        frame.add(viewProfileButton); // Add the new button

        frame.setVisible(true);
    }

    // New setters for customer information
    public void setCustomerInfo(String name, String phone, String address) {
        this.customerName = name;
        this.customerPhone = phone;
        this.customerAddress = address;
    }
}


// Restaurant Window
class RestaurantWindow {
    public RestaurantWindow() {
        JFrame frame = new JFrame("Restaurant Staff Portal");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new GridLayout(2, 1, 10, 10));

        JLabel label = new JLabel("Restaurant Features", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(label);

        JButton viewIncomingOrdersButton = new JButton("View Incoming Orders");
        JButton updateOrderStatusButton = new JButton("Update Order Status");

        viewIncomingOrdersButton.addActionListener(e -> {
            List<String> items = FoodDeliverySystem.sharedOrder.getItems();
            if (items.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No orders received yet.");
            } else {
                JOptionPane.showMessageDialog(frame, "Incoming Orders: " + String.join(", ", items));
            }
        });

        updateOrderStatusButton.addActionListener(e -> {
            String[] statuses = {"Preparing", "Ready for Delivery"};
            String newStatus = (String) JOptionPane.showInputDialog(frame, "Select Order Status:",
                    "Update Order Status", JOptionPane.PLAIN_MESSAGE, null, statuses, statuses[0]);
            if (newStatus != null) {
                FoodDeliverySystem.sharedOrder.setStatus(newStatus);
                JOptionPane.showMessageDialog(frame, "Order status updated to: " + newStatus);
            }
        });

        frame.add(viewIncomingOrdersButton);
        frame.add(updateOrderStatusButton);

        frame.setVisible(true);
    }
}

// Delivery Window
class DeliveryWindow {
    public DeliveryWindow() {
        JFrame frame = new JFrame("Delivery Person Portal");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new GridLayout(3, 1, 10, 10));

        JLabel label = new JLabel("Delivery Features", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(label);

        JButton pickedUpButton = new JButton("Mark as Picked Up");
        JButton inTransitButton = new JButton("Mark as In Transit");
        JButton deliveredButton = new JButton("Mark as Delivered");

        pickedUpButton.addActionListener(e -> {
            FoodDeliverySystem.sharedOrder.setStatus("Picked Up");
            JOptionPane.showMessageDialog(frame, "Order marked as Picked Up.");
        });
        inTransitButton.addActionListener(e -> {
            FoodDeliverySystem.sharedOrder.setStatus("In Transit");
            JOptionPane.showMessageDialog(frame, "Order marked as In Transit.");
        });
        deliveredButton.addActionListener(e -> {
            FoodDeliverySystem.sharedOrder.setStatus("Delivered");
            JOptionPane.showMessageDialog(frame, "Order marked as Delivered.");
        });

        frame.add(pickedUpButton);
        frame.add(inTransitButton);
        frame.add(deliveredButton);

        frame.setVisible(true);
    }
}

// Menu GUI
class MenuGUI {
    public MenuGUI(List<String> cart) {
        JFrame frame = new JFrame("Browse Menu");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JLabel label = new JLabel("Menu", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(label, BorderLayout.NORTH);

        String[] menuItems = {"Pizza - $10", "Burger - $8", "Pasta - $12"};
        JList<String> menuList = new JList<>(menuItems);

        frame.add(new JScrollPane(menuList), BorderLayout.CENTER);

        JButton addToCartButton = new JButton("Add to Cart");
        addToCartButton.addActionListener(e -> {
            String selectedItem = menuList.getSelectedValue();
            if (selectedItem != null) {
                cart.add(selectedItem);
                JOptionPane.showMessageDialog(frame, selectedItem + " added to cart!");
            } else {
                JOptionPane.showMessageDialog(frame, "Please select an item to add to cart.");
            }
        });
        frame.add(addToCartButton, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
}

// Cart GUI
class CartGUI {
    public CartGUI(List<String> cart, Order order) {
        JFrame frame = new JFrame("View Cart");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JLabel label = new JLabel("Your Cart", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(label, BorderLayout.NORTH);

        JList<String> cartList = new JList<>(cart.toArray(new String[0]));
        frame.add(new JScrollPane(cartList), BorderLayout.CENTER);

        JButton placeOrderButton = new JButton("Place Order");
        placeOrderButton.addActionListener(e -> {
            if (!cart.isEmpty()) {
                order.setItems(cart);
                JOptionPane.showMessageDialog(frame, "Order placed successfully!");
                cart.clear();
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Your cart is empty!");
            }
        });
        frame.add(placeOrderButton, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
}
