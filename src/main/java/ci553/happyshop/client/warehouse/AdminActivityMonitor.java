package ci553.happyshop.client.warehouse;

import ci553.happyshop.client.customer.AdminObserver;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Admin mirror window showing real-time customer activity for troubleshooting.
 * Displays: product image, search result, trolley, and receipt in a small window.
 */
public class AdminActivityMonitor implements AdminObserver {
    private Stage window;
    private ImageView ivProduct;
    private Label laSearchResult;
    private TextArea taTrolley;
    private TextArea taReceipt;

    public AdminActivityMonitor() {
        createWindow();
    }

    private void createWindow() {
        // Product image
        ivProduct = new ImageView("WarehouseImageHolder.jpg");
        ivProduct.setFitWidth(100);
        ivProduct.setFitHeight(80);
        ivProduct.setPreserveRatio(true);

        // Search result label
        laSearchResult = new Label("Waiting for customer activity...");
        laSearchResult.setWrapText(true);
        laSearchResult.setStyle("-fx-font-size: 12px; -fx-padding: 5px;");

        // Trolley display
        taTrolley = new TextArea();
        taTrolley.setEditable(false);
        taTrolley.setPrefHeight(80);
        taTrolley.setStyle("-fx-font-size: 11px; -fx-control-inner-background: white;");

        // Receipt display
        taReceipt = new TextArea();
        taReceipt.setEditable(false);
        taReceipt.setPrefHeight(80);
        taReceipt.setStyle("-fx-font-size: 11px; -fx-control-inner-background: white;");

        // Root layout
        VBox root = new VBox(8);
        root.setStyle("-fx-padding: 10px; -fx-background-color: #f0f0f0; -fx-border-color: #bbb; -fx-border-width: 1;");
        root.setAlignment(Pos.TOP_CENTER);
        root.getChildren().addAll(
            ivProduct,
            new Label("Search Result:"),
            laSearchResult,
            new Label("Trolley:"),
            taTrolley,
            new Label("Receipt:"),
            taReceipt
        );

        Scene scene = new Scene(root, 350, 600);
        window = new Stage();
        window.setScene(scene);
        window.setTitle("👁️ Customer Activity Monitor");
        window.setWidth(350);
        window.setHeight(700);
        window.show();
    }

    @Override
    public void onCustomerActivity(String imageName, String searchResult, String trolley, String receipt) {
        // Update image
        try {
            ivProduct.setImage(new Image(imageName));
        } catch (Exception e) {
            // image load error, keep existing image
        }

        // Update text content
        laSearchResult.setText(searchResult == null ? "" : searchResult);
        taTrolley.setText(trolley == null ? "" : trolley);
        taReceipt.setText(receipt == null ? "" : receipt);

        // Auto-show window if hidden
        if (!window.isShowing()) {
            window.show();
            window.toFront();
        }
    }

    public void close() {
        if (window != null) {
            window.close();
        }
    }
}
