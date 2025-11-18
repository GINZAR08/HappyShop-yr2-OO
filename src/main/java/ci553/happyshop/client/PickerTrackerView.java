package ci553.happyshop.client;

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
    }

    public void start(Stage window) {
        tabPane = new TabPane();
        tabOrderMap = new Tab("Order Map", createOrderMapRoot());
        tabOrderDetail = new Tab("Order Detail", createOrderDetailRoot());
        tabPane.getTabs().addAll(tabOrderMap, tabOrderDetail);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Scene scene = new Scene(tabPane, WIDTH, HEIGHT);
        window.setScene(scene);
        window.setTitle("🛒 HappyShop Picker & Tracker");
        WinPosManager.registerWindow(window, WIDTH, HEIGHT);
        window.show();
    }

    private VBox createOrderMapRoot() {
        Label laOrderMapRootTitle = new Label("Orders Waiting for Processing");
        laOrderMapRootTitle.setStyle(UIStyle.labelTitleStyle);

        taOrderMap.setEditable(false);
        taOrderMap.setPrefSize(WIDTH, HEIGHT - 100);
        taOrderMap.setStyle(UIStyle.textFiledStyle);

        Button btnProgressing = new Button("Progressing");
        btnProgressing.setOnAction(this::buttonClicked);
        btnProgressing.setStyle(UIStyle.buttonStyle);

        VBox vbOrdersListRoot = new VBox(15, laOrderMapRootTitle, taOrderMap, btnProgressing);
        vbOrdersListRoot.setAlignment(Pos.TOP_CENTER);
        vbOrdersListRoot.setStyle(UIStyle.rootStyleYellow);
        return vbOrdersListRoot;
    }

    private VBox createOrderDetailRoot() {
        laDetailRootTitle = new Label("Progressing Order Details");
        laDetailRootTitle.setStyle(UIStyle.labelTitleStyle);

        taOrderDetail.setEditable(false);
        taOrderDetail.setPrefSize(WIDTH, HEIGHT - 100);
        taOrderDetail.setText("Order details");
        taOrderDetail.setStyle(UIStyle.textFiledStyle);

        Button btnCollected = new Button("Customer Collected");
        btnCollected.setOnAction(this::buttonClicked);
        btnCollected.setStyle(UIStyle.buttonStyle);

        VBox vbOrderDetailsRoot = new VBox(15, laDetailRootTitle, taOrderDetail, btnCollected);
        vbOrderDetailsRoot.setAlignment(Pos.TOP_CENTER);
        vbOrderDetailsRoot.setStyle(UIStyle.rootStyleBlue);
        return vbOrderDetailsRoot;
    }

    private void buttonClicked(ActionEvent event) {
        Button button = (Button) event.getSource();
        String btnText = button.getText();
        try {
            switch (btnText) {
                case "Progressing":
                    tabPane.getSelectionModel().select(tabOrderDetail);
                    pickerController.doProgressing();
                    break;
                case "Customer Collected":
                    pickerController.doCollected();
                    tabPane.getSelectionModel().select(tabOrderMap);
                    break;
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
