import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

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
    private String feedback = "No feedback yet"; // New field for feedback
    private String message = "No messages yet";

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

    public String getFeedback() { // Getter for feedback
        return feedback;
    }

    public void setFeedback(String feedback) { // Setter for feedback
        this.feedback = feedback;
    }
    public String getMessage() { // Getter for message
        return message;
    }

    public void setMessage(String message) { // Setter for message
        this.message = message;
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

        customerButton.addActionListener(e -> new CustomerWindow() );

        restaurantButton.addActionListener(e -> new RestaurantWindow());
        deliveryButton.addActionListener(e -> new DeliveryWindow());

        rolePanel.add(customerButton);
        rolePanel.add(restaurantButton);
        rolePanel.add(deliveryButton);

        frame.add(rolePanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}



// CustomerWindow 
class CustomerWindow {
    private final List<String> cart = new ArrayList<>();
    private String customerName;
    private String customerPhone;
    private String customerAddress;

    public CustomerWindow() {
        JFrame frame = new JFrame("Customer Portal");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new GridLayout(5, 1, 10, 10)); // Updated to 5 rows

        JLabel label = new JLabel("Customer Features", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(label);

        JButton browseMenuButton = new JButton("Browse Menu");
        JButton viewCartButton = new JButton("View Cart");
        JButton viewOrderStatusButton = new JButton("View Order's Status");
        JButton viewMessageButton = new JButton("View Message");

        browseMenuButton.addActionListener(e -> new MenuGUI(cart));
        viewCartButton.addActionListener(e -> new CartGUI(cart, FoodDeliverySystem.sharedOrder));
        viewOrderStatusButton.addActionListener(e -> JOptionPane.showMessageDialog(frame,
                "Order Status: " + FoodDeliverySystem.sharedOrder.getStatus()));

        viewMessageButton.addActionListener(e -> {
            String message = FoodDeliverySystem.sharedOrder.getMessage();
            JOptionPane.showMessageDialog(frame, "Message from Delivery Person: " + message, "Message", JOptionPane.INFORMATION_MESSAGE);
        });       
        frame.add(browseMenuButton);
        frame.add(viewCartButton);
        frame.add(viewOrderStatusButton);
        frame.add(viewMessageButton);

        frame.setVisible(true);
    }

    public void setCustomerInfo(String name, String phone, String address) {
        this.customerName = name;
        this.customerPhone = phone;
        this.customerAddress = address;
    }
}



// Restaurant Window
// Updated RestaurantWindow
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
        JButton viewFeedbackButton = new JButton("viewFeedback");

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

        viewFeedbackButton.addActionListener(e -> {
            String feedback = FoodDeliverySystem.sharedOrder.getFeedback();
            JOptionPane.showMessageDialog(frame, "Customer Feedback: " + feedback);
        });

        frame.add(viewIncomingOrdersButton);
        frame.add(updateOrderStatusButton);
        frame.add(viewFeedbackButton);

        frame.setVisible(true);
    }
}