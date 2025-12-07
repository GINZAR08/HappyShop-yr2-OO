package ci553.happyshop.client.customer;

import ci553.happyshop.utility.UIStyle;
import ci553.happyshop.utility.WinPosManager;
import ci553.happyshop.utility.WindowBounds;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

/**
 * The CustomerView is separated into two sections by a line :
 *
 * 1. Search Page – Always visible, allowing customers to browse and search for products.
 * 2. the second page – display either the Trolley Page or the Receipt Page
 *    depending on the current context. Only one of these is shown at a time.
 */

public class CustomerView  {
    public CustomerController cusController;

    private final int WIDTH = UIStyle.customerWinWidth;
    private final int HEIGHT = UIStyle.customerWinHeight;
    private final int COLUMN_WIDTH = WIDTH / 2 - 10;

    private HBox hbRoot; // Top-level layout manager
    private VBox vbTrolleyPage;  //vbTrolleyPage and vbReceiptPage will swap with each other when need
    private VBox vbBillingPage;  //billing information page
    private VBox vbReceiptPage;

    TextField tfId; //for user input on the search page. Made accessible so it can be accessed or modified by CustomerModel
    TextField tfName; //for user input on the search page. Made accessible so it can be accessed by CustomerModel

    //four controllers needs updating when program going on
    private ImageView ivProduct; //image area in searchPage
    private Label lbProductInfo;//product text info in searchPage
    private TextArea taTrolley; //in trolley Page
    private TextArea taReceipt;//in receipt page
    
    // Billing information fields
    private TextField tfCustomerName;
    private TextField tfEmail;
    private TextField tfAddress;
    private TextField tfCity;
    private TextField tfPostalCode;
    private TextField tfCardNumber;

    // Holds a reference to this CustomerView window for future access and management
    // (e.g., positioning the removeProductNotifier when needed).
    private Stage viewWindow;

    public void start(Stage window) {
        VBox vbSearchPage = createSearchPage();
        vbTrolleyPage = CreateTrolleyPage();
        vbBillingPage = createBillingPage();
        vbReceiptPage = createReceiptPage();

        // Create a divider line
        Line line = new Line(0, 0, 0, HEIGHT);
        line.setStrokeWidth(4);
        line.setStroke(Color.PINK);
        VBox lineContainer = new VBox(line);
        lineContainer.setPrefWidth(4); // Give it some space
        lineContainer.setAlignment(Pos.CENTER);

        hbRoot = new HBox(10, vbSearchPage, lineContainer, vbTrolleyPage); //initialize to show trolleyPage
        hbRoot.setAlignment(Pos.CENTER);
        hbRoot.setStyle(UIStyle.rootStyle);

        Scene scene = new Scene(hbRoot, WIDTH, HEIGHT);
        
        // Bind line height to scene height
        line.endYProperty().bind(scene.heightProperty());
        
        window.setScene(scene);
        window.setTitle("🛒 HappyShop Customer Client");
        window.setResizable(true); // Make window resizable
        WinPosManager.registerWindow(window,WIDTH,HEIGHT); //calculate position x and y for this window
        ci553.happyshop.utility.LogoutManager.getInstance().registerWindow(window);
        window.show();
        viewWindow=window;// Sets viewWindow to this window for future reference and management.
    }

    private VBox createSearchPage() {
        Label laPageTitle = new Label("Search by Product ID/Name");
        laPageTitle.setStyle(UIStyle.labelTitleStyle);

        Label laId = new Label("ID:      ");
        laId.setStyle(UIStyle.labelStyle);
        tfId = new TextField();
        tfId.setPromptText("eg. 0001");
        tfId.setStyle(UIStyle.textFiledStyle);
        HBox hbId = new HBox(10, laId, tfId);

        Label laName = new Label("Name:");
        laName.setStyle(UIStyle.labelStyle);
        tfName = new TextField();
        tfName.setPromptText("implement it if you want");
        tfName.setStyle(UIStyle.textFiledStyle);
        HBox hbName = new HBox(10, laName, tfName);

        Button btnLogout = new Button("Logout");
        btnLogout.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8px 16px; -fx-background-radius: 5px;");
        btnLogout.setPrefWidth(80);
        btnLogout.setOnAction(e -> ci553.happyshop.utility.LogoutManager.getInstance().logout());
        
        Label laPlaceHolder = new Label(  " ".repeat(15)); //create left-side spacing so that this HBox aligns with others in the layout.
        Button btnSearch = new Button("Search");
        btnSearch.setStyle(UIStyle.buttonStyle);
        btnSearch.setOnAction(this::buttonClicked);
        Button btnAddToTrolley = new Button("Add to Trolley");
        btnAddToTrolley.setStyle(UIStyle.buttonStyle);
        btnAddToTrolley.setPrefWidth(120);
        btnAddToTrolley.setOnAction(this::buttonClicked);
        HBox hbBtns = new HBox(10, laPlaceHolder,btnSearch, btnAddToTrolley, btnLogout);

        ivProduct = new ImageView("imageHolder.jpg");
        ivProduct.setFitHeight(60);
        ivProduct.setFitWidth(60);
        ivProduct.setPreserveRatio(true); // Image keeps its original shape and fits inside 60×60
        ivProduct.setSmooth(true); //make it smooth and nice-looking

        lbProductInfo = new Label("Thank you for shopping with us.");
        lbProductInfo.setWrapText(true);
        lbProductInfo.setMinHeight(Label.USE_PREF_SIZE);  // Allow auto-resize
        lbProductInfo.setStyle(UIStyle.labelMulLineStyle);
        HBox hbSearchResult = new HBox(5, ivProduct, lbProductInfo);
        hbSearchResult.setAlignment(Pos.CENTER_LEFT);

        VBox vbSearchPage = new VBox(15, laPageTitle, hbId, hbName, hbBtns, hbSearchResult);
        vbSearchPage.setPrefWidth(COLUMN_WIDTH);
        vbSearchPage.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(vbSearchPage, javafx.scene.layout.Priority.ALWAYS);
        vbSearchPage.setAlignment(Pos.TOP_CENTER);
        vbSearchPage.setStyle("-fx-padding: 15px;");

        return vbSearchPage;
    }

    private VBox CreateTrolleyPage() {
        Label laPageTitle = new Label("🛒🛒  Trolley 🛒🛒");
        laPageTitle.setStyle(UIStyle.labelTitleStyle);

        taTrolley = new TextArea();
        taTrolley.setEditable(false);
        taTrolley.setPrefSize(WIDTH/2, HEIGHT-50);
        VBox.setVgrow(taTrolley, javafx.scene.layout.Priority.ALWAYS);

        Button btnCancel = new Button("Cancel");
        btnCancel.setOnAction(this::buttonClicked);
        btnCancel.setStyle(UIStyle.buttonStyle);

        Button btnCheckout = new Button("Check Out");
        btnCheckout.setOnAction(this::buttonClicked);
        btnCheckout.setStyle(UIStyle.buttonStyle);

        HBox hbBtns = new HBox(10, btnCancel,btnCheckout);
        hbBtns.setStyle("-fx-padding: 15px;");
        hbBtns.setAlignment(Pos.CENTER);

        vbTrolleyPage = new VBox(15, laPageTitle, taTrolley, hbBtns);
        vbTrolleyPage.setPrefWidth(COLUMN_WIDTH);
        vbTrolleyPage.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(vbTrolleyPage, javafx.scene.layout.Priority.ALWAYS);
        vbTrolleyPage.setAlignment(Pos.TOP_CENTER);
        vbTrolleyPage.setStyle("-fx-padding: 15px;");
        return vbTrolleyPage;
    }

    private VBox createBillingPage() {
        Label laPageTitle = new Label("💳 Billing Information");
        laPageTitle.setStyle(UIStyle.labelTitleStyle);

        // Customer Name
        Label laCustomerName = new Label("Full Name:");
        laCustomerName.setStyle(UIStyle.labelStyle);
        tfCustomerName = new TextField();
        tfCustomerName.setPromptText("Enter your full name");
        tfCustomerName.setStyle(UIStyle.textFiledStyle);
        HBox hbCustomerName = new HBox(10, laCustomerName, tfCustomerName);
        hbCustomerName.setAlignment(Pos.CENTER_LEFT);

        // Email
        Label laEmail = new Label("Email:");
        laEmail.setStyle(UIStyle.labelStyle);
        tfEmail = new TextField();
        tfEmail.setPromptText("Enter your email");
        tfEmail.setStyle(UIStyle.textFiledStyle);
        HBox hbEmail = new HBox(10, laEmail, tfEmail);
        hbEmail.setAlignment(Pos.CENTER_LEFT);

        // Address
        Label laAddress = new Label("Address:");
        laAddress.setStyle(UIStyle.labelStyle);
        tfAddress = new TextField();
        tfAddress.setPromptText("Street address");
        tfAddress.setStyle(UIStyle.textFiledStyle);
        HBox hbAddress = new HBox(10, laAddress, tfAddress);
        hbAddress.setAlignment(Pos.CENTER_LEFT);

        // City
        Label laCity = new Label("City:");
        laCity.setStyle(UIStyle.labelStyle);
        tfCity = new TextField();
        tfCity.setPromptText("City");
        tfCity.setStyle(UIStyle.textFiledStyle);
        HBox hbCity = new HBox(10, laCity, tfCity);
        hbCity.setAlignment(Pos.CENTER_LEFT);

        // Postal Code
        Label laPostalCode = new Label("Postal Code:");
        laPostalCode.setStyle(UIStyle.labelStyle);
        tfPostalCode = new TextField();
        tfPostalCode.setPromptText("Postal code");
        tfPostalCode.setStyle(UIStyle.textFiledStyle);
        HBox hbPostalCode = new HBox(10, laPostalCode, tfPostalCode);
        hbPostalCode.setAlignment(Pos.CENTER_LEFT);

        // Card Number
        Label laCardNumber = new Label("Card Number:");
        laCardNumber.setStyle(UIStyle.labelStyle);
        tfCardNumber = new TextField();
        tfCardNumber.setPromptText("XXXX-XXXX-XXXX-XXXX");
        tfCardNumber.setStyle(UIStyle.textFiledStyle);
        HBox hbCardNumber = new HBox(10, laCardNumber, tfCardNumber);
        hbCardNumber.setAlignment(Pos.CENTER_LEFT);

        // Buttons
        Button btnBack = new Button("Back to Trolley");
        btnBack.setOnAction(this::buttonClicked);
        btnBack.setStyle(UIStyle.buttonStyle);

        Button btnConfirm = new Button("Confirm & Pay");
        btnConfirm.setOnAction(this::buttonClicked);
        btnConfirm.setStyle("-fx-background-color: #27AE60; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8px 16px; -fx-background-radius: 5px;");
        btnConfirm.setPrefWidth(130);

        HBox hbBtns = new HBox(10, btnBack, btnConfirm);
        hbBtns.setStyle("-fx-padding: 15px;");
        hbBtns.setAlignment(Pos.CENTER);

        ScrollPane scrollPane = new ScrollPane();
        VBox formContent = new VBox(10, laPageTitle, hbCustomerName, hbEmail, hbAddress, hbCity, hbPostalCode, hbCardNumber, hbBtns);
        formContent.setAlignment(Pos.TOP_CENTER);
        formContent.setStyle("-fx-padding: 15px;");
        scrollPane.setContent(formContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: lightblue; -fx-background-color: lightblue;");

        vbBillingPage = new VBox(scrollPane);
        vbBillingPage.setPrefWidth(COLUMN_WIDTH);
        vbBillingPage.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(vbBillingPage, javafx.scene.layout.Priority.ALWAYS);
        VBox.setVgrow(scrollPane, javafx.scene.layout.Priority.ALWAYS);
        vbBillingPage.setAlignment(Pos.TOP_CENTER);
        return vbBillingPage;
    }

    private VBox createReceiptPage() {
        Label laPageTitle = new Label("Receipt");
        laPageTitle.setStyle(UIStyle.labelTitleStyle);

        taReceipt = new TextArea();
        taReceipt.setEditable(false);
        taReceipt.setPrefSize(WIDTH/2, HEIGHT-50);
        VBox.setVgrow(taReceipt, javafx.scene.layout.Priority.ALWAYS);

        Button btnCloseReceipt = new Button("OK & Close"); //btn for closing receipt and showing trolley page
        btnCloseReceipt.setStyle(UIStyle.buttonStyle);

        btnCloseReceipt.setOnAction(this::buttonClicked);

        vbReceiptPage = new VBox(15, laPageTitle, taReceipt, btnCloseReceipt);
        vbReceiptPage.setPrefWidth(COLUMN_WIDTH);
        vbReceiptPage.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(vbReceiptPage, javafx.scene.layout.Priority.ALWAYS);
        vbReceiptPage.setAlignment(Pos.TOP_CENTER);
        vbReceiptPage.setStyle(UIStyle.rootStyleYellow);
        return vbReceiptPage;
    }


    private void buttonClicked(ActionEvent event) {
        try{
            Button btn = (Button)event.getSource();
            String action = btn.getText();
            if(action.equals("Add to Trolley")){
                showTrolleyOrReceiptPage(vbTrolleyPage); //ensure trolleyPage shows if the last customer did not close their receiptPage
            }
            if(action.equals("Check Out")){
                showTrolleyOrReceiptPage(vbBillingPage); //show billing page before checkout
                return; // Don't call controller yet
            }
            if(action.equals("Confirm & Pay")){
                // Validate billing information
                if(validateBillingInfo()){
                    cusController.doAction("Check Out"); // Process the actual checkout
                }
                return;
            }
            if(action.equals("Back to Trolley")){
                showTrolleyOrReceiptPage(vbTrolleyPage);
                return;
            }
            if(action.equals("OK & Close")){
                showTrolleyOrReceiptPage(vbTrolleyPage);
            }
            cusController.doAction(action);
        }
        catch(SQLException e){
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void update(String imageName, String searchResult, String trolley, String receipt) {

        ivProduct.setImage(new Image(imageName));
        lbProductInfo.setText(searchResult);
        taTrolley.setText(trolley);
        if (!receipt.equals("")) {
            showTrolleyOrReceiptPage(vbReceiptPage);
            taReceipt.setText(receipt);
        }
    }

    // Replaces the last child of hbRoot with the specified page.
    // the last child is either vbTrolleyPage or vbReceiptPage.
    private void showTrolleyOrReceiptPage(Node pageToShow) {
        int lastIndex = hbRoot.getChildren().size() - 1;
        if (lastIndex >= 0) {
            hbRoot.getChildren().set(lastIndex, pageToShow);
        }
    }

    private boolean validateBillingInfo() {
        // Check if all fields are filled
        if (tfCustomerName.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Please enter your full name.");
            return false;
        }
        if (tfEmail.getText().trim().isEmpty() || !tfEmail.getText().contains("@")) {
            showAlert("Validation Error", "Please enter a valid email address.");
            return false;
        }
        if (tfAddress.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Please enter your address.");
            return false;
        }
        if (tfCity.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Please enter your city.");
            return false;
        }
        if (tfPostalCode.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Please enter your postal code.");
            return false;
        }
        if (tfCardNumber.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Please enter your card number.");
            return false;
        }
        return true;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    WindowBounds getWindowBounds() {
        return new WindowBounds(viewWindow.getX(), viewWindow.getY(),
                  viewWindow.getWidth(), viewWindow.getHeight());
    }
}
