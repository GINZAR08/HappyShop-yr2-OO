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
    private Stage viewWindow;    public void start(Stage window) {
        VBox vbSearchPage = createSearchPage();
        vbTrolleyPage = CreateTrolleyPage();
        vbBillingPage = createBillingPage();
        vbReceiptPage = createReceiptPage();

        // Create a divider line with gradient effect
        Line line = new Line(0, 0, 0, HEIGHT);
        line.setStrokeWidth(2);
        line.setStroke(Color.web("#BDC3C7"));
        VBox lineContainer = new VBox(line);
        lineContainer.setPrefWidth(2);
        lineContainer.setAlignment(Pos.CENTER);

        hbRoot = new HBox(0, vbSearchPage, lineContainer, vbTrolleyPage); //initialize to show trolleyPage
        hbRoot.setAlignment(Pos.CENTER);
        hbRoot.setStyle("-fx-background-color: #ECF0F1;");

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
    }private VBox createSearchPage() {
        Label laPageTitle = new Label("🔍 Search by Product ID/Name");
        laPageTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #2C3E50;");

        Label laId = new Label("ID:      ");
        laId.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #34495E;");
        tfId = new TextField();
        tfId.setPromptText("eg. 0001");
        tfId.setStyle("-fx-font-size: 14px; -fx-padding: 8px; -fx-border-color: #BDC3C7; -fx-border-width: 1px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        HBox hbId = new HBox(10, laId, tfId);
        hbId.setAlignment(Pos.CENTER_LEFT);

        Label laName = new Label("Name:");
        laName.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #34495E;");
        tfName = new TextField();
        tfName.setPromptText("implement it if you want");
        tfName.setStyle("-fx-font-size: 14px; -fx-padding: 8px; -fx-border-color: #BDC3C7; -fx-border-width: 1px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        HBox hbName = new HBox(10, laName, tfName);
        hbName.setAlignment(Pos.CENTER_LEFT);

        Button btnLogout = new Button("Logout");
        btnLogout.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px 16px; -fx-background-radius: 5px; -fx-cursor: hand;");
        btnLogout.setPrefWidth(80);
        btnLogout.setOnAction(e -> ci553.happyshop.utility.LogoutManager.getInstance().logout());
        btnLogout.setOnMouseEntered(e -> btnLogout.setStyle("-fx-background-color: #C0392B; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px 16px; -fx-background-radius: 5px; -fx-cursor: hand;"));
        btnLogout.setOnMouseExited(e -> btnLogout.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px 16px; -fx-background-radius: 5px; -fx-cursor: hand;"));
        
        Label laPlaceHolder = new Label(  " ".repeat(15)); //create left-side spacing so that this HBox aligns with others in the layout.
        Button btnSearch = new Button("🔍 Search");
        btnSearch.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10px 16px; -fx-background-radius: 5px; -fx-cursor: hand;");
        btnSearch.setOnAction(this::buttonClicked);
        btnSearch.setOnMouseEntered(e -> btnSearch.setStyle("-fx-background-color: #2980B9; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10px 16px; -fx-background-radius: 5px; -fx-cursor: hand;"));
        btnSearch.setOnMouseExited(e -> btnSearch.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10px 16px; -fx-background-radius: 5px; -fx-cursor: hand;"));
        
        Button btnAddToTrolley = new Button("🛒 Add to Trolley");
        btnAddToTrolley.setStyle("-fx-background-color: #27AE60; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10px 16px; -fx-background-radius: 5px; -fx-cursor: hand;");
        btnAddToTrolley.setPrefWidth(150);
        btnAddToTrolley.setOnAction(this::buttonClicked);
        btnAddToTrolley.setOnMouseEntered(e -> btnAddToTrolley.setStyle("-fx-background-color: #229954; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10px 16px; -fx-background-radius: 5px; -fx-cursor: hand;"));
        btnAddToTrolley.setOnMouseExited(e -> btnAddToTrolley.setStyle("-fx-background-color: #27AE60; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10px 16px; -fx-background-radius: 5px; -fx-cursor: hand;"));
        
        HBox hbBtns = new HBox(10, laPlaceHolder, btnSearch, btnAddToTrolley, btnLogout);

        // Product display card with shadow and rounded corners
        ivProduct = new ImageView("imageHolder.jpg");
        ivProduct.setFitHeight(100);
        ivProduct.setFitWidth(100);
        ivProduct.setPreserveRatio(true); // Image keeps its original shape and fits inside 100×100
        ivProduct.setSmooth(true); //make it smooth and nice-looking
        ivProduct.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);");

        lbProductInfo = new Label("Thank you for shopping with us.\n\n✨ Start by searching for a product using the ID field above!");
        lbProductInfo.setWrapText(true);
        lbProductInfo.setMinHeight(Label.USE_PREF_SIZE);  // Allow auto-resize
        lbProductInfo.setStyle("-fx-font-size: 14px; -fx-text-fill: #2C3E50; -fx-padding: 10px;");
        
        VBox productInfoBox = new VBox(5, lbProductInfo);
        productInfoBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(productInfoBox, javafx.scene.layout.Priority.ALWAYS);
        
        HBox hbSearchResult = new HBox(15, ivProduct, productInfoBox);
        hbSearchResult.setAlignment(Pos.CENTER_LEFT);
        hbSearchResult.setStyle("-fx-background-color: white; -fx-padding: 15px; -fx-border-color: #E0E0E0; -fx-border-width: 1px; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");

        VBox vbSearchPage = new VBox(15, laPageTitle, hbId, hbName, hbBtns, hbSearchResult);
        vbSearchPage.setPrefWidth(COLUMN_WIDTH);
        vbSearchPage.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(vbSearchPage, javafx.scene.layout.Priority.ALWAYS);
        vbSearchPage.setAlignment(Pos.TOP_CENTER);
        vbSearchPage.setStyle("-fx-padding: 20px; -fx-background-color: #ECF0F1;");

        return vbSearchPage;
    }    private VBox CreateTrolleyPage() {
        Label laPageTitle = new Label("🛒 Your Shopping Trolley");
        laPageTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #2C3E50;");

        taTrolley = new TextArea();
        taTrolley.setEditable(false);
        taTrolley.setPrefSize(WIDTH/2, HEIGHT-50);
        taTrolley.setStyle("-fx-font-size: 13px; -fx-font-family: 'Consolas', monospace; -fx-control-inner-background: white; -fx-border-color: #E0E0E0; -fx-border-width: 1px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        VBox.setVgrow(taTrolley, javafx.scene.layout.Priority.ALWAYS);

        Button btnCancel = new Button("❌ Cancel");
        btnCancel.setOnAction(this::buttonClicked);
        btnCancel.setStyle("-fx-background-color: #95A5A6; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10px 16px; -fx-background-radius: 5px; -fx-cursor: hand;");
        btnCancel.setOnMouseEntered(e -> btnCancel.setStyle("-fx-background-color: #7F8C8D; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10px 16px; -fx-background-radius: 5px; -fx-cursor: hand;"));
        btnCancel.setOnMouseExited(e -> btnCancel.setStyle("-fx-background-color: #95A5A6; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10px 16px; -fx-background-radius: 5px; -fx-cursor: hand;"));

        Button btnCheckout = new Button("✓ Check Out");
        btnCheckout.setOnAction(this::buttonClicked);
        btnCheckout.setStyle("-fx-background-color: #27AE60; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10px 16px; -fx-background-radius: 5px; -fx-cursor: hand;");
        btnCheckout.setPrefWidth(120);
        btnCheckout.setOnMouseEntered(e -> btnCheckout.setStyle("-fx-background-color: #229954; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10px 16px; -fx-background-radius: 5px; -fx-cursor: hand;"));
        btnCheckout.setOnMouseExited(e -> btnCheckout.setStyle("-fx-background-color: #27AE60; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10px 16px; -fx-background-radius: 5px; -fx-cursor: hand;"));

        HBox hbBtns = new HBox(10, btnCancel, btnCheckout);
        hbBtns.setStyle("-fx-padding: 15px;");
        hbBtns.setAlignment(Pos.CENTER);

        vbTrolleyPage = new VBox(15, laPageTitle, taTrolley, hbBtns);
        vbTrolleyPage.setPrefWidth(COLUMN_WIDTH);
        vbTrolleyPage.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(vbTrolleyPage, javafx.scene.layout.Priority.ALWAYS);
        vbTrolleyPage.setAlignment(Pos.TOP_CENTER);
        vbTrolleyPage.setStyle("-fx-padding: 20px; -fx-background-color: #ECF0F1;");
        return vbTrolleyPage;
    }    private VBox createBillingPage() {
        Label laPageTitle = new Label("💳 Billing Information");
        laPageTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #2C3E50;");

        // Customer Name
        Label laCustomerName = new Label("Full Name:");
        laCustomerName.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #34495E;");
        tfCustomerName = new TextField();
        tfCustomerName.setPromptText("Enter your full name");
        tfCustomerName.setStyle("-fx-font-size: 14px; -fx-padding: 8px; -fx-border-color: #BDC3C7; -fx-border-width: 1px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        HBox hbCustomerName = new HBox(10, laCustomerName, tfCustomerName);
        hbCustomerName.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(tfCustomerName, javafx.scene.layout.Priority.ALWAYS);

        // Email
        Label laEmail = new Label("Email:");
        laEmail.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #34495E;");
        tfEmail = new TextField();
        tfEmail.setPromptText("Enter your email");
        tfEmail.setStyle("-fx-font-size: 14px; -fx-padding: 8px; -fx-border-color: #BDC3C7; -fx-border-width: 1px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        HBox hbEmail = new HBox(10, laEmail, tfEmail);
        hbEmail.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(tfEmail, javafx.scene.layout.Priority.ALWAYS);

        // Address
        Label laAddress = new Label("Address:");
        laAddress.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #34495E;");
        tfAddress = new TextField();
        tfAddress.setPromptText("Street address");
        tfAddress.setStyle("-fx-font-size: 14px; -fx-padding: 8px; -fx-border-color: #BDC3C7; -fx-border-width: 1px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        HBox hbAddress = new HBox(10, laAddress, tfAddress);
        hbAddress.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(tfAddress, javafx.scene.layout.Priority.ALWAYS);

        // City
        Label laCity = new Label("City:");
        laCity.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #34495E;");
        tfCity = new TextField();
        tfCity.setPromptText("City");
        tfCity.setStyle("-fx-font-size: 14px; -fx-padding: 8px; -fx-border-color: #BDC3C7; -fx-border-width: 1px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        HBox hbCity = new HBox(10, laCity, tfCity);
        hbCity.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(tfCity, javafx.scene.layout.Priority.ALWAYS);

        // Postal Code
        Label laPostalCode = new Label("Postal Code:");
        laPostalCode.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #34495E;");
        tfPostalCode = new TextField();
        tfPostalCode.setPromptText("Postal code");
        tfPostalCode.setStyle("-fx-font-size: 14px; -fx-padding: 8px; -fx-border-color: #BDC3C7; -fx-border-width: 1px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        HBox hbPostalCode = new HBox(10, laPostalCode, tfPostalCode);
        hbPostalCode.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(tfPostalCode, javafx.scene.layout.Priority.ALWAYS);

        // Card Number
        Label laCardNumber = new Label("Card Number:");
        laCardNumber.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #34495E;");
        tfCardNumber = new TextField();
        tfCardNumber.setPromptText("XXXX-XXXX-XXXX-XXXX");
        tfCardNumber.setStyle("-fx-font-size: 14px; -fx-padding: 8px; -fx-border-color: #BDC3C7; -fx-border-width: 1px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        HBox hbCardNumber = new HBox(10, laCardNumber, tfCardNumber);
        hbCardNumber.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(tfCardNumber, javafx.scene.layout.Priority.ALWAYS);// Buttons
        Button btnBack = new Button("⬅ Back to Trolley");
        btnBack.setOnAction(this::buttonClicked);
        btnBack.setStyle("-fx-background-color: #95A5A6; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10px 16px; -fx-background-radius: 5px; -fx-cursor: hand;");
        btnBack.setPrefWidth(150);        btnBack.setOnMouseEntered(e -> btnBack.setStyle("-fx-background-color: #7F8C8D; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10px 16px; -fx-background-radius: 5px; -fx-cursor: hand;"));
        btnBack.setOnMouseExited(e -> btnBack.setStyle("-fx-background-color: #95A5A6; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10px 16px; -fx-background-radius: 5px; -fx-cursor: hand;"));

        Button btnConfirm = new Button("💳 Confirm & Pay");
        btnConfirm.setOnAction(this::buttonClicked);
        btnConfirm.setStyle("-fx-background-color: #27AE60; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10px 16px; -fx-background-radius: 5px; -fx-cursor: hand;");
        btnConfirm.setPrefWidth(150);
        btnConfirm.setOnMouseEntered(e -> btnConfirm.setStyle("-fx-background-color: #229954; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10px 16px; -fx-background-radius: 5px; -fx-cursor: hand;"));
        btnConfirm.setOnMouseExited(e -> btnConfirm.setStyle("-fx-background-color: #27AE60; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10px 16px; -fx-background-radius: 5px; -fx-cursor: hand;"));        HBox hbBtns = new HBox(10, btnBack, btnConfirm);
        hbBtns.setStyle("-fx-padding: 15px;");
        hbBtns.setAlignment(Pos.CENTER);

        ScrollPane scrollPane = new ScrollPane();
        VBox formContent = new VBox(15, laPageTitle, hbCustomerName, hbEmail, hbAddress, hbCity, hbPostalCode, hbCardNumber, hbBtns);
        formContent.setAlignment(Pos.TOP_CENTER);
        formContent.setStyle("-fx-padding: 20px;");
        scrollPane.setContent(formContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #ECF0F1; -fx-background-color: #ECF0F1;");

        vbBillingPage = new VBox(scrollPane);
        vbBillingPage.setPrefWidth(COLUMN_WIDTH);
        vbBillingPage.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(vbBillingPage, javafx.scene.layout.Priority.ALWAYS);
        VBox.setVgrow(scrollPane, javafx.scene.layout.Priority.ALWAYS);
        vbBillingPage.setAlignment(Pos.TOP_CENTER);
        return vbBillingPage;
    }private VBox createReceiptPage() {
        Label laPageTitle = new Label("📄 Receipt");
        laPageTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #2C3E50;");

        taReceipt = new TextArea();
        taReceipt.setEditable(false);
        taReceipt.setPrefSize(WIDTH/2, HEIGHT-50);
        taReceipt.setStyle("-fx-font-size: 13px; -fx-font-family: 'Consolas', monospace; -fx-control-inner-background: white; -fx-border-color: #E0E0E0; -fx-border-width: 1px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        VBox.setVgrow(taReceipt, javafx.scene.layout.Priority.ALWAYS);

        Button btnCloseReceipt = new Button("✓ OK & Close"); //btn for closing receipt and showing trolley page
        btnCloseReceipt.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10px 16px; -fx-background-radius: 5px; -fx-cursor: hand;");
        btnCloseReceipt.setOnMouseEntered(e -> btnCloseReceipt.setStyle("-fx-background-color: #2980B9; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10px 16px; -fx-background-radius: 5px; -fx-cursor: hand;"));
        btnCloseReceipt.setOnMouseExited(e -> btnCloseReceipt.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10px 16px; -fx-background-radius: 5px; -fx-cursor: hand;"));
        btnCloseReceipt.setOnAction(this::buttonClicked);

        vbReceiptPage = new VBox(15, laPageTitle, taReceipt, btnCloseReceipt);
        vbReceiptPage.setPrefWidth(COLUMN_WIDTH);
        vbReceiptPage.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(vbReceiptPage, javafx.scene.layout.Priority.ALWAYS);
        vbReceiptPage.setAlignment(Pos.TOP_CENTER);
        vbReceiptPage.setStyle("-fx-padding: 20px; -fx-background-color: #ECF0F1;");
        return vbReceiptPage;
    }    private void buttonClicked(ActionEvent event) {
        try{
            Button btn = (Button)event.getSource();
            String action = btn.getText();
            
            // Handle button actions with emojis - check if text contains the key action word
            if(action.contains("Add to Trolley")){
                showTrolleyOrReceiptPage(vbTrolleyPage); //ensure trolleyPage shows if the last customer did not close their receiptPage
                cusController.doAction("Add to Trolley");
                return;
            }
            if(action.contains("Check Out")){
                showTrolleyOrReceiptPage(vbBillingPage); //show billing page before checkout
                return; // Don't call controller yet
            }
            if(action.contains("Confirm & Pay")){
                // Validate billing information
                if(validateBillingInfo()){
                    cusController.doAction("Check Out"); // Process the actual checkout
                }
                return;
            }
            if(action.contains("Back to Trolley")){
                showTrolleyOrReceiptPage(vbTrolleyPage);
                return;
            }
            if(action.contains("OK & Close")){
                showTrolleyOrReceiptPage(vbTrolleyPage);
                cusController.doAction("OK & Close");
                return;
            }
            if(action.contains("Search")){
                cusController.doAction("Search");
                return;
            }
            if(action.contains("Cancel")){
                cusController.doAction("Cancel");
                return;
            }
            
            // Fallback for any other buttons
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
