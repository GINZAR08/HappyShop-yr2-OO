package ci553.happyshop.client;

import ci553.happyshop.client.admin.CustomerTestPage;
import ci553.happyshop.client.picker.PickerController;
import ci553.happyshop.client.picker.PickerModel;
import ci553.happyshop.client.orderTracker.OrderTrackerObserver;
import ci553.happyshop.orderManagement.OrderState;
import ci553.happyshop.utility.UIStyle;
import ci553.happyshop.utility.WinPosManager;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class PickerTrackerView implements OrderTrackerObserver {
    private final int WIDTH = UIStyle.pickerWinWidth;
    private final int HEIGHT = UIStyle.pickerWinHeight;

    private PickerController pickerController;

    private TextArea taOrderMap = new TextArea();
    private TextArea taOrderDetail = new TextArea();
    private Label laDetailRootTitle;
    private TabPane tabPane;
    private Tab tabOrderMap;
    private Tab tabOrderDetail;

    public PickerTrackerView(PickerController pickerController, PickerModel pickerModel) {
        this.pickerController = pickerController;
    }    public void start(Stage window) {
        tabPane = new TabPane();
        tabOrderMap = new Tab("Order Map", createOrderMapRoot());
        tabOrderDetail = new Tab("Order Detail", createOrderDetailRoot());
        
        // Add customer test tab for admin testing
        CustomerTestPage customerTestPage = new CustomerTestPage(WIDTH, HEIGHT);
        Tab tabCustomerTest = new Tab("🧪 Customer Test", customerTestPage.createRoot());
        
        tabPane.getTabs().addAll(tabOrderMap, tabOrderDetail, tabCustomerTest);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Scene scene = new Scene(tabPane, WIDTH, HEIGHT);
        window.setScene(scene);
        window.setTitle("🛒 HappyShop Picker & Tracker");
        window.setResizable(true); // Make window resizable
        WinPosManager.registerWindow(window, WIDTH, HEIGHT);
        ci553.happyshop.utility.LogoutManager.getInstance().registerWindow(window);
        window.show();
    }    private VBox createOrderMapRoot() {
        Label laOrderMapRootTitle = new Label("📋 Orders Waiting for Processing");
        laOrderMapRootTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #2C3E50;");

        taOrderMap.setEditable(false);
        taOrderMap.setPrefSize(WIDTH, HEIGHT - 100);
        taOrderMap.setStyle("-fx-font-size: 14px; -fx-font-family: 'Courier New', monospace; -fx-padding: 10px; -fx-border-color: #BDC3C7; -fx-border-width: 1px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        VBox.setVgrow(taOrderMap, javafx.scene.layout.Priority.ALWAYS);

        Button btnProgressing = new Button("⏩ Progressing");
        btnProgressing.setOnAction(this::buttonClicked);
        btnProgressing.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10px 16px; -fx-background-radius: 5px; -fx-cursor: hand;");
        btnProgressing.setOnMouseEntered(e -> btnProgressing.setStyle("-fx-background-color: #2980B9; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10px 16px; -fx-background-radius: 5px; -fx-cursor: hand;"));
        btnProgressing.setOnMouseExited(e -> btnProgressing.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10px 16px; -fx-background-radius: 5px; -fx-cursor: hand;"));
        
        Button btnLogout = new Button("🚪 Logout");
        btnLogout.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10px 16px; -fx-background-radius: 5px; -fx-cursor: hand;");
        btnLogout.setOnAction(e -> ci553.happyshop.utility.LogoutManager.getInstance().logout());
        btnLogout.setOnMouseEntered(e -> btnLogout.setStyle("-fx-background-color: #C0392B; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10px 16px; -fx-background-radius: 5px; -fx-cursor: hand;"));
        btnLogout.setOnMouseExited(e -> btnLogout.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10px 16px; -fx-background-radius: 5px; -fx-cursor: hand;"));
        
        javafx.scene.layout.HBox hbButtons = new javafx.scene.layout.HBox(10, btnProgressing, btnLogout);
        hbButtons.setAlignment(javafx.geometry.Pos.CENTER);

        VBox vbOrdersListRoot = new VBox(15, laOrderMapRootTitle, taOrderMap, hbButtons);
        vbOrdersListRoot.setAlignment(Pos.TOP_CENTER);
        vbOrdersListRoot.setStyle("-fx-padding: 15px; -fx-background-color: #ECF0F1;");
        return vbOrdersListRoot;
    }    private VBox createOrderDetailRoot() {
        laDetailRootTitle = new Label("📦 Progressing Order Details");
        laDetailRootTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #2C3E50;");

        taOrderDetail.setEditable(false);
        taOrderDetail.setPrefSize(WIDTH, HEIGHT - 100);
        taOrderDetail.setText("Order details");
        taOrderDetail.setStyle("-fx-font-size: 14px; -fx-font-family: 'Courier New', monospace; -fx-padding: 10px; -fx-border-color: #BDC3C7; -fx-border-width: 1px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        VBox.setVgrow(taOrderDetail, javafx.scene.layout.Priority.ALWAYS);

        Button btnCollected = new Button("✅ Customer Collected");
        btnCollected.setOnAction(this::buttonClicked);
        btnCollected.setStyle("-fx-background-color: #27AE60; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10px 16px; -fx-background-radius: 5px; -fx-cursor: hand;");
        btnCollected.setOnMouseEntered(e -> btnCollected.setStyle("-fx-background-color: #229954; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10px 16px; -fx-background-radius: 5px; -fx-cursor: hand;"));
        btnCollected.setOnMouseExited(e -> btnCollected.setStyle("-fx-background-color: #27AE60; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10px 16px; -fx-background-radius: 5px; -fx-cursor: hand;"));

        VBox vbOrderDetailsRoot = new VBox(15, laDetailRootTitle, taOrderDetail, btnCollected);
        vbOrderDetailsRoot.setAlignment(Pos.TOP_CENTER);
        vbOrderDetailsRoot.setStyle("-fx-padding: 15px; -fx-background-color: #ECF0F1;");
        return vbOrderDetailsRoot;
    }    private void buttonClicked(ActionEvent event) {
        Button button = (Button) event.getSource();
        String btnText = button.getText();
        try {
            if (btnText.contains("Progressing")) {
                tabPane.getSelectionModel().select(tabOrderDetail);
                pickerController.doProgressing();
            } else if (btnText.contains("Customer Collected")) {
                pickerController.doCollected();
                tabPane.getSelectionModel().select(tabOrderMap);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to handle button action: " + btnText, e);
        }
    }

    public void update(String strOrderMap, String strOrderDetail) {
        taOrderMap.setText(strOrderMap);
        taOrderDetail.setText(strOrderDetail);
        laDetailRootTitle.setText("Progressing Order Details");
    }

    @Override
    public void setOrderMap(TreeMap<Integer, OrderState> om) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, OrderState> entry : om.entrySet()) {
            int orderId = entry.getKey();
            OrderState orderState = entry.getValue();
            sb.append(orderId).append("        ").append(orderState).append("\n");
        }
        taOrderMap.setText(sb.toString());
    }
}
