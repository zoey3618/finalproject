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

    public void clearItems() {
        this.items.clear(); // Clear the items
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
            // Always fetch the latest state
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



// Delivery Window
class DeliveryWindow {
    public DeliveryWindow() {
        JFrame frame = new JFrame("Delivery Person Portal");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new GridLayout(4, 1, 10, 10));

        JLabel label = new JLabel("Delivery Features", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(label);

        JButton waitingButton = new JButton("Waiting for Food");
        JButton pickedUpButton = new JButton("Mark as Picked Up");
        JButton inTransitButton = new JButton("Mark as In Transit");
        JButton deliveredButton = new JButton("Mark as Delivered");
        JButton sendMessageButton = new JButton("Send Message");

        // Disable status update buttons initially
        pickedUpButton.setEnabled(false);
        inTransitButton.setEnabled(false);
        deliveredButton.setEnabled(false);

        // Check if the order is ready for delivery
        if (FoodDeliverySystem.sharedOrder.getStatus().equals("Ready for Delivery")) {
            waitingButton.setText("Food Ready for Delivery");
            pickedUpButton.setEnabled(true);
            inTransitButton.setEnabled(true);
            deliveredButton.setEnabled(true);
        }

        pickedUpButton.addActionListener(e -> {
            FoodDeliverySystem.sharedOrder.setStatus("Picked Up");
            FoodDeliverySystem.sharedOrder.clearItems();
            JOptionPane.showMessageDialog(frame, "Order marked as Picked Up.");
        });
        inTransitButton.addActionListener(e -> {
            FoodDeliverySystem.sharedOrder.setStatus("In Transit");
            JOptionPane.showMessageDialog(frame, "Order marked as In Transit.");
        });
        deliveredButton.addActionListener(e -> {
            FoodDeliverySystem.sharedOrder.setStatus("Delivered");
            JOptionPane.showMessageDialog(frame, "Order marked as Delivered.");
            new FeedbackWindow(); // Open feedback window
        });
        sendMessageButton.addActionListener(e -> {
            String message = JOptionPane.showInputDialog(frame, "Enter your message to the customer:");
            if (message != null && !message.trim().isEmpty()) {
                FoodDeliverySystem.sharedOrder.setMessage(message);
                JOptionPane.showMessageDialog(frame, "Message sent to customer!");
            } else {
                JOptionPane.showMessageDialog(frame, "Message cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.add(waitingButton);
        frame.add(pickedUpButton);
        frame.add(inTransitButton);
        frame.add(deliveredButton);
        frame.add(sendMessageButton);

        frame.setVisible(true);
    }
}

// Menu GUI
class MenuGUI {
    private final HashMap<String, String> dishImageMap = new HashMap<>();


    public MenuGUI(List<String> cart) {
        // Mapping dishes to corrected image paths
        dishImageMap.put("Spicy and Sour Beef in Soup - $19.99", "1/src/images/2.jpeg");
        dishImageMap.put("Steamed Fish Head with Chopped Chili - $22.99", "1/src/images/4.jpg");
        dishImageMap.put("Minced Pork with Pickled Green Beans - $16.99", "1/src/images/5.jpeg");
        dishImageMap.put("Twice-Cooked Pork Belly - $17.99", "1/src/images/6.jpg");
        dishImageMap.put("Spicy and Sour Chicken Gizzards - $16.99", "1/src/images/7.jpg");
        dishImageMap.put("Kelp and Pork Rib Soup - $14.99", "1/src/images/8.jpeg");
        dishImageMap.put("Cold Cucumber Salad - $8.99", "1/src/images/9.jpg");
        dishImageMap.put("Yangzhou Fried Rice - $12.99", "1/src/images/10.jpeg");
        dishImageMap.put("Sichuan-style Boiled Beef in Chili Sauce - $19.99", "1/src/images/11.jpg");
        dishImageMap.put("Stir-fried Chicken with Old Ginger - $18.99", "1/src/images/12.jpg");
        dishImageMap.put("Chongqing-style Spicy Chicken - $19.99", "1/src/images/13.jpg");

        JFrame frame = new JFrame("Browse Menu");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JLabel label = new JLabel("Menu", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(label, BorderLayout.NORTH);

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(0, 2, 10, 10)); // Two columns for names and images

        DefaultListModel<String> menuModel = new DefaultListModel<>();
        JList<String> menuList = new JList<>(menuModel);
        menuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        menuList.setFont(new Font("Arial", Font.PLAIN, 16));

        // Add items to the list and their images to the panel
        for (String dish : dishImageMap.keySet()) {
            menuModel.addElement(dish);

            // Add corresponding dish image to the panel
            String imagePath = dishImageMap.get(dish);
            ImageIcon dishIcon = new ImageIcon(imagePath);
            Image scaledImage = dishIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));

            menuPanel.add(imageLabel);
        }

        // Scrollable list of menu items
        JScrollPane scrollPane = new JScrollPane(menuList);
        frame.add(scrollPane, BorderLayout.WEST);
        frame.add(new JScrollPane(menuPanel), BorderLayout.CENTER);

        JButton addToCartButton = new JButton("Add to Cart");
        addToCartButton.addActionListener(e -> {
            String selectedDish = menuList.getSelectedValue();
            if (selectedDish != null) {
                cart.add(selectedDish);
                JOptionPane.showMessageDialog(frame, selectedDish + " added to cart!");
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a dish from the list.");
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

        // Panel for Total Price and Place Order Button
        JPanel bottomPanel = new JPanel(new GridLayout(2, 1));

        JLabel totalPriceLabel = new JLabel("Total Price: $0.00", SwingConstants.CENTER); // Label for total price
        bottomPanel.add(totalPriceLabel);

        JButton placeOrderButton = new JButton("Place Order");
        placeOrderButton.addActionListener(e -> {
            if (!cart.isEmpty()) {
                order.setItems(cart); // Update the shared order
                JOptionPane.showMessageDialog(frame, "Proceeding to checkout...", "Place Order", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose(); // Close the cart window
                new CheckoutWindow(); // Open the CheckoutWindow
                cart.clear(); // Clear the cart
            } else {
                JOptionPane.showMessageDialog(frame, "Your cart is empty!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        bottomPanel.add(placeOrderButton);

        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Calculate and display the total price
        updateTotalPrice(cart, totalPriceLabel);

        frame.setVisible(true);
    }

    // Helper method to calculate the total price of the cart
    private void updateTotalPrice(List<String> cart, JLabel totalPriceLabel) {
        double totalPrice = 0.0;

        for (String item : cart) {
            // Extract the price from the cart item string (e.g., "Stir-fried Spicy Beef - $18.99")
            int dollarIndex = item.lastIndexOf('$');
            if (dollarIndex != -1) {
                try {
                    totalPrice += Double.parseDouble(item.substring(dollarIndex + 1));
                } catch (NumberFormatException ex) {
                    // Ignore parsing errors for invalid entries
                }
            }
        }

        totalPriceLabel.setText(String.format("Total Price: $%.2f", totalPrice));
    }
}


// FeedbackWindow Class
class FeedbackWindow extends JFrame {
    public FeedbackWindow() {
        setTitle("Feedback");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Please provide your feedback", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        add(label, BorderLayout.NORTH);

        JTextArea feedbackArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(feedbackArea);
        add(scrollPane, BorderLayout.CENTER);

        JButton submitButton = new JButton("Submit Feedback");
        submitButton.addActionListener(e -> {
            String feedback = feedbackArea.getText().trim();
            if (!feedback.isEmpty()) {
                FoodDeliverySystem.sharedOrder.setFeedback(feedback); // Store feedback in the shared order
                JOptionPane.showMessageDialog(this, "Thank you for your feedback!", "Feedback Submitted", JOptionPane.INFORMATION_MESSAGE);
                this.dispose(); // Close feedback window
            } else {
                JOptionPane.showMessageDialog(this, "Feedback cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(submitButton, BorderLayout.SOUTH);

        setVisible(true);
    }
}


