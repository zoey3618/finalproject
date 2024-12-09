import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;


public class CheckoutWindow extends JFrame {

    public CheckoutWindow() {
        setTitle("Checkout");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Delivery Details Panel
        JPanel deliveryPanel = new JPanel(new GridLayout(3, 2));
        deliveryPanel.add(new JLabel("Name:"));
        JTextField nameField = new JTextField();
        deliveryPanel.add(nameField);
        deliveryPanel.add(new JLabel("Address:"));
        JTextField addressField = new JTextField();
        deliveryPanel.add(addressField);
        deliveryPanel.add(new JLabel("Phone:"));
        JTextField phoneField = new JTextField();
        deliveryPanel.add(phoneField);

        // Payment Options
        JPanel paymentPanel = new JPanel(new FlowLayout());
        String[] paymentMethods = {"Credit Card", "PayPal", "Cash on Delivery"};
        JComboBox<String> paymentComboBox = new JComboBox<>(paymentMethods);
        paymentPanel.add(new JLabel("Select Payment Method:"));
        paymentPanel.add(paymentComboBox);

        // Confirm Order Button
        JButton confirmOrderButton = new JButton("Confirm Order");
        confirmOrderButton.addActionListener(e -> confirmOrder(nameField, addressField, phoneField, paymentComboBox));

        // Add components to the layout
        add(deliveryPanel, BorderLayout.CENTER);
        add(paymentPanel, BorderLayout.NORTH);
        add(confirmOrderButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void confirmOrder(JTextField nameField, JTextField addressField, JTextField phoneField, JComboBox<String> paymentComboBox) {
        String customerName = nameField.getText().trim();
        String customerAddress = addressField.getText().trim();
        String customerPhone = phoneField.getText().trim();
        String paymentMethod = (String) paymentComboBox.getSelectedItem();

        if (customerName.isEmpty() || customerAddress.isEmpty() || customerPhone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill out all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create order summary
        String orderSummary = "Order Confirmed:\n" +
                              "Name: " + customerName + "\n" +
                              "Address: " + customerAddress + "\n" +
                              "Phone: " + customerPhone + "\n" +
                              "Payment Method: " + paymentMethod;

        JOptionPane.showMessageDialog(this, orderSummary, "Order Confirmation", JOptionPane.INFORMATION_MESSAGE);
        this.dispose();  // Close the checkout window
    }
}
