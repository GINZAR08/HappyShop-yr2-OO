package ci553.happyshop.client.admin;

import ci553.happyshop.client.customer.CustomerClient;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * CustomerTestPage provides an admin interface to simulate a customer and test
 * the customer-facing functionality without having to logout and login as a customer.
 * 
 * This is useful for QA testing and debugging customer features.
 * 
 * @author GitHub Copilot
 * @version 1.0
 */
public class CustomerTestPage {
    private final int WIDTH;

    public CustomerTestPage(int width, int height) {
        this.WIDTH = width;
    }

    /**
     * Creates the root VBox for the customer test page
     */
    public VBox createRoot() {
        // Title
        Label laTitle = new Label("🧪 Customer Simulation Test");
        laTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #2C3E50;");

        // Description
        Label laDescription = new Label(
            "Launch a customer interface to test customer-facing features.\n" +
            "This allows you to verify that the customer page works correctly\n" +
            "without needing to logout and login as a customer."
        );
        laDescription.setStyle("-fx-font-size: 12px; -fx-text-fill: #555; -fx-wrap-text: true;");
        laDescription.setMaxWidth(WIDTH - 80);
        laDescription.setWrapText(true);

        // Information text area
        TextArea taInfo = new TextArea();
        taInfo.setText(
            "CUSTOMER TEST INTERFACE\n\n" +
            "Click the button below to launch the customer interface in a new window.\n\n" +
            "Features to test:\n" +
            "  • Search for products by ID or name\n" +
            "  • View product information and images\n" +
            "  • Add products to trolley\n" +
            "  • View and manage trolley contents\n" +
            "  • Proceed to checkout\n" +
            "  • Enter billing information\n" +
            "  • Receive receipt after purchase\n\n" +
            "Notes:\n" +
            "  • The customer interface will use the same database\n" +
            "  • Any orders placed will be recorded in the system\n" +
            "  • You can keep this admin panel open while testing\n" +
            "  • Multiple customer windows can be opened simultaneously"
        );
        taInfo.setEditable(false);
        taInfo.setWrapText(true);
        taInfo.setPrefHeight(280);
        taInfo.setStyle("-fx-font-size: 11px; -fx-font-family: 'Courier New', monospace; " +
                       "-fx-padding: 10px; -fx-border-color: #BDC3C7; -fx-border-width: 1px; " +
                       "-fx-border-radius: 5px; -fx-background-radius: 5px;");

        // Launch button
        Button btnLaunchCustomer = new Button("🛍️ Launch Customer Interface");
        btnLaunchCustomer.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white; " +
                                   "-fx-font-weight: bold; -fx-font-size: 14px; " +
                                   "-fx-padding: 12px 24px; -fx-background-radius: 5px; " +
                                   "-fx-cursor: hand; -fx-min-width: 200px;");
        btnLaunchCustomer.setOnMouseEntered(e -> 
            btnLaunchCustomer.setStyle("-fx-background-color: #2980B9; -fx-text-fill: white; " +
                                       "-fx-font-weight: bold; -fx-font-size: 14px; " +
                                       "-fx-padding: 12px 24px; -fx-background-radius: 5px; " +
                                       "-fx-cursor: hand; -fx-min-width: 200px;")
        );
        btnLaunchCustomer.setOnMouseExited(e -> 
            btnLaunchCustomer.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white; " +
                                       "-fx-font-weight: bold; -fx-font-size: 14px; " +
                                       "-fx-padding: 12px 24px; -fx-background-radius: 5px; " +
                                       "-fx-cursor: hand; -fx-min-width: 200px;")
        );
        btnLaunchCustomer.setOnAction(e -> launchCustomerInterface());

        // Status label
        Label laStatus = new Label("Ready to launch customer interface");
        laStatus.setStyle("-fx-font-size: 11px; -fx-text-fill: #27AE60; -fx-font-weight: bold;");

        // Layout
        VBox vbContent = new VBox(15);
        vbContent.setAlignment(Pos.TOP_CENTER);
        vbContent.setStyle("-fx-padding: 15px;");
        vbContent.getChildren().addAll(
            laTitle,
            laDescription,
            taInfo,
            btnLaunchCustomer,
            laStatus
        );

        // Scroll pane to handle overflow
        ScrollPane scrollPane = new ScrollPane(vbContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #ECF0F1; -fx-background-color: #ECF0F1;");

        VBox vbRoot = new VBox(scrollPane);
        vbRoot.setStyle("-fx-background-color: #ECF0F1;");
        VBox.setVgrow(scrollPane, javafx.scene.layout.Priority.ALWAYS);

        return vbRoot;
    }

    /**
     * Launches the customer interface in a new window
     */
    private void launchCustomerInterface() {
        try {
            CustomerClient customerClient = new CustomerClient();
            Stage customerStage = new Stage();
            customerStage.setTitle("🛒 HappyShop - Customer Interface (Test Mode)");
            customerClient.start(customerStage);
        } catch (Exception e) {
            System.err.println("Failed to launch customer interface: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
