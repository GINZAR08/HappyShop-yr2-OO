package ci553.happyshop;

import ci553.happyshop.client.auth.LoginView;
import ci553.happyshop.client.auth.UserRole;
import ci553.happyshop.client.customer.*;
import ci553.happyshop.client.PickerTrackerView;

import ci553.happyshop.client.emergency.EmergencyExit;
import ci553.happyshop.client.warehouse.*;
import ci553.happyshop.orderManagement.OrderHub;
import ci553.happyshop.storageAccess.DatabaseRW;
import ci553.happyshop.storageAccess.DatabaseRWFactory;
import ci553.happyshop.utility.LogoutManager;
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * The Main JavaFX application class. The Main class is executable directly.
 * It serves as a foundation for UI logic and starts all the clients (UI) based on user role.
 *
 * This class now includes role-based authentication:
 * - CUSTOMER: Can only access the Customer client to browse and purchase products
 * - ADMIN: Has access to all management features (Picker, Warehouse, EmergencyExit)
 *
 * @version 2.0
 * @author  Shine Shan University of Brighton
 */

public class Main extends Application {

    public static void main(String[] args) {
        launch(args); // Launches the JavaFX application and calls the @Override start()
    }

    //starts the system with login screen
    @Override
    public void start(Stage window) throws IOException {
        // Configure logout callback to show login screen again
        LogoutManager.getInstance().setOnLogoutCallback(this::showLoginScreen);
        showLoginScreen();
    }
    
    private void showLoginScreen() {
        // Show login screen
        LoginView loginView = new LoginView(this::onLoginSuccess);
        loginView.start(new Stage());
    }
    
    /**
     * Callback method invoked after successful login.
     * Starts the appropriate clients based on the user's role.
     * 
     * @param role The role of the logged-in user
     */
    private void onLoginSuccess(UserRole role) {
        // Reset logout manager for new session
        LogoutManager.getInstance().reset();
        
        try {
            if (role == UserRole.CUSTOMER) {
                // Customer can only access the customer client
                startCustomerClient();
            } else if (role == UserRole.ADMIN) {
                // Admin has access to all management features
                startPickerTrackerClient();
                initializeOrderMap();
                startWarehouseClient();
                startEmergencyExit();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error starting client: " + e.getMessage());
        }
    }

    /** The customer GUI - search product, add to trolley, cancel/submit trolley, view receipt
     *
     * Creates the Model, View, and Controller objects, links them together so they can communicate with each other.
     * Also creates the DatabaseRW instance via the DatabaseRWFactory and injects it into the CustomerModel.
     * Starts the customer interface.
     *
     * Also creates the RemoveProductNotifier, which tracks the position of the Customer View
     * and is triggered by the Customer Model when needed.
     */
    private void startCustomerClient(){
        CustomerView cusView = new CustomerView();
        CustomerController cusController = new CustomerController();
        CustomerModel cusModel = new CustomerModel();
        DatabaseRW databaseRW = DatabaseRWFactory.createDatabaseRW();

        cusView.cusController = cusController;
        cusController.cusModel = cusModel;
        cusModel.cusView = cusView;
        cusModel.databaseRW = databaseRW;
        cusView.start(new Stage());

        //RemoveProductNotifier removeProductNotifier = new RemoveProductNotifier();
        //removeProductNotifier.cusView = cusView;
        //cusModel.removeProductNotifier = removeProductNotifier;
    }

    /** The picker GUI, - for staff to pack customer's order,
     *
     * Creates the Model, View, and Controller objects for the Picker client.
     * Links them together so they can communicate with each other.
     * Starts the Picker interface.
     *
     * Also registers the PickerModel with the OrderHub to receive order notifications.
     */
    private void startPickerTrackerClient(){
        ci553.happyshop.client.picker.PickerModel pickerModel = new ci553.happyshop.client.picker.PickerModel();
        ci553.happyshop.client.picker.PickerController pickerController = new ci553.happyshop.client.picker.PickerController();
        pickerController.pickerModel = pickerModel;
        PickerTrackerView pickerTrackerView = new PickerTrackerView(pickerController, pickerModel);
        // Ensure field exists and is public in PickerModel
        pickerModel.setPickerTrackerView(pickerTrackerView);
        // Register PickerModel with OrderHub to receive order map updates
        pickerModel.registerWithOrderHub();
        // Register PickerTrackerView as an order tracker observer
        OrderHub.getOrderHub().registerOrderTracker(pickerTrackerView);
        pickerTrackerView.start(new javafx.stage.Stage());
    }

    //initialize the orderMap<orderId, orderState> for OrderHub during system startup
    private void initializeOrderMap(){
        OrderHub orderHub = OrderHub.getOrderHub();
        orderHub.initializeOrderMap();
    }

    /** The Warehouse GUI- for warehouse staff to manage stock
     * Initializes the Warehouse client's Model, View, and Controller,and links them together for communication.
     * It also creates the DatabaseRW instance via the DatabaseRWFactory and injects it into the Model.
     * Once the components are linked, the warehouse interface (view) is started.
     *
     * Also creates the dependent HistoryWindow and AlertSimulator,
     * which track the position of the Warehouse window and are triggered by the Model when needed.
     * These components are linked after launching the Warehouse interface.
     */
    private void startWarehouseClient(){
        WarehouseView view = new WarehouseView();
        WarehouseController controller = new WarehouseController();
        WarehouseModel model = new WarehouseModel();
        DatabaseRW databaseRW = DatabaseRWFactory.createDatabaseRW();

        // Link controller, model, and view and start view
        view.controller = controller;
        controller.model = model;
        model.view = view;
        model.databaseRW = databaseRW;
        view.start(new Stage());

        //create dependent views that need window info
        HistoryWindow historyWindow = new HistoryWindow();
        AlertSimulator alertSimulator = new AlertSimulator();

        // Link after start
        model.historyWindow = historyWindow;
        model.alertSimulator = alertSimulator;
        historyWindow.warehouseView = view;
        alertSimulator.warehouseView = view;
    }

    //starts the EmergencyExit GUI, - used to close the entire application immediately
    private void startEmergencyExit(){
        EmergencyExit.getEmergencyExit();
    }
}