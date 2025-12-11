package ci553.happyshop.client.auth;

import ci553.happyshop.utility.UIStyle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.function.Consumer;

/**
 * LoginView provides a login interface for users to authenticate as either
 * a Customer or an Admin. Upon successful login, it invokes a callback
 * with the selected user role.
 *
 * @author GitHub Copilot
 * @version 1.0
 */
public class LoginView {
    
    private Stage stage;
    private Consumer<UserRole> onLoginSuccess;
    
    // Hardcoded credentials (in a real system, these would be in a database)
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";
    private static final String CUSTOMER_USERNAME = "customer";
    private static final String CUSTOMER_PASSWORD = "customer123";
    
    /**
     * Constructor for LoginView
     * @param onLoginSuccess Callback function to be invoked when login succeeds, passing the UserRole
     */
    public LoginView(Consumer<UserRole> onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;
    }
    
    /**
     * Starts the login window
     * @param stage The JavaFX stage to display the login view
     */
    public void start(Stage stage) {
        this.stage = stage;
        stage.setTitle("HappyShop - Login");
        
        // Main container
        HBox root = new HBox();
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #f5f5f5;");
        
        // Login form container - wider and simpler
        VBox formContainer = new VBox(15);
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setPadding(new Insets(40, 60, 40, 60));
        formContainer.setStyle("-fx-background-color: white;");
        
        // Title
        Label titleLabel = new Label("HappyShop Login");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333;");
        
        // Username and Password in horizontal layout
        HBox credentialsBox = new HBox(20);
        credentialsBox.setAlignment(Pos.CENTER);
        
        VBox usernameBox = new VBox(5);
        Label usernameLabel = new Label("Username");
        usernameLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        usernameField.setStyle(UIStyle.textFiledStyle);
        usernameField.setPrefWidth(200);
        usernameBox.getChildren().addAll(usernameLabel, usernameField);
        
        VBox passwordBox = new VBox(5);
        Label passwordLabel = new Label("Password");
        passwordLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        passwordField.setStyle(UIStyle.textFiledStyle);
        passwordField.setPrefWidth(200);
        passwordBox.getChildren().addAll(passwordLabel, passwordField);
        
        credentialsBox.getChildren().addAll(usernameBox, passwordBox);
        
        // Role selection - horizontal
        ToggleGroup roleGroup = new ToggleGroup();
        RadioButton customerRadio = new RadioButton("Customer");
        customerRadio.setToggleGroup(roleGroup);
        customerRadio.setSelected(true);
        customerRadio.setStyle("-fx-font-size: 13px;");
        
        RadioButton adminRadio = new RadioButton("Admin");
        adminRadio.setToggleGroup(roleGroup);
        adminRadio.setStyle("-fx-font-size: 13px;");
        
        HBox roleBox = new HBox(30, customerRadio, adminRadio);
        roleBox.setAlignment(Pos.CENTER);
        
        // Error message label
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #d32f2f; -fx-font-size: 12px;");
        errorLabel.setVisible(false);
        
        // Login button
        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #4A90E2; -fx-text-fill: white; " +
                            "-fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 50; -fx-cursor: hand;");
        
        loginButton.setOnMouseEntered(e -> 
            loginButton.setStyle("-fx-background-color: #357ABD; -fx-text-fill: white; " +
                                "-fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 50; -fx-cursor: hand;"));
        loginButton.setOnMouseExited(e -> 
            loginButton.setStyle("-fx-background-color: #4A90E2; -fx-text-fill: white; " +
                                "-fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 50; -fx-cursor: hand;"));
        
        // Login action
        loginButton.setOnAction(e -> handleLogin(
            usernameField.getText(),
            passwordField.getText(),
            customerRadio.isSelected() ? UserRole.CUSTOMER : UserRole.ADMIN,
            errorLabel
        ));
        
        // Allow Enter key to login
        passwordField.setOnAction(e -> loginButton.fire());
        
        // Help text
        Label helpLabel = new Label("Customer: customer/customer123  |  Admin: admin/admin123");
        helpLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #999;");
        
        // Add all components to form container
        formContainer.getChildren().addAll(
            titleLabel,
            credentialsBox,
            roleBox,
            errorLabel,
            loginButton,
            helpLabel
        );
        
        // Add form to root
        root.getChildren().add(formContainer);
        
        Scene scene = new Scene(root, 700, 400);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        
        // Focus on username field
        usernameField.requestFocus();
    }
    
    /**
     * Handles the login logic
     */
    private void handleLogin(String username, String password, UserRole selectedRole, Label errorLabel) {
        // Clear previous error
        errorLabel.setVisible(false);
        
        // Validate input
        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            showError(errorLabel, "Please enter both username and password");
            return;
        }
        
        // Check credentials based on selected role
        boolean isValid = false;
        
        if (selectedRole == UserRole.CUSTOMER) {
            isValid = username.equals(CUSTOMER_USERNAME) && password.equals(CUSTOMER_PASSWORD);
        } else if (selectedRole == UserRole.ADMIN) {
            isValid = username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD);
        }
        
        if (isValid) {
            // Close login window
            stage.close();
            // Invoke callback with the user role
            onLoginSuccess.accept(selectedRole);
        } else {
            showError(errorLabel, "Invalid username or password for selected role");
        }
    }
    
    /**
     * Displays an error message
     */
    private void showError(Label errorLabel, String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}
