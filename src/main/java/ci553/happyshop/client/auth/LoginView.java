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
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #4A90E2, #87CEEB);");
        
        // Title
        Label titleLabel = new Label("Welcome to HappyShop");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white;");
        
        Label subtitleLabel = new Label("Please login to continue");
        subtitleLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");
        
        // Login form container
        VBox formContainer = new VBox(15);
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setPadding(new Insets(30));
        formContainer.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                               "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 2);");
        formContainer.setMaxWidth(400);
        
        // Username field
        Label usernameLabel = new Label("Username:");
        usernameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        usernameField.setStyle(UIStyle.textFiledStyle);
        usernameField.setPrefWidth(300);
        
        // Password field
        Label passwordLabel = new Label("Password:");
        passwordLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        passwordField.setStyle(UIStyle.textFiledStyle);
        passwordField.setPrefWidth(300);
        
        // Role selection
        Label roleLabel = new Label("Login as:");
        roleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        
        ToggleGroup roleGroup = new ToggleGroup();
        RadioButton customerRadio = new RadioButton("Customer");
        customerRadio.setToggleGroup(roleGroup);
        customerRadio.setSelected(true);
        customerRadio.setStyle("-fx-font-size: 14px;");
        
        RadioButton adminRadio = new RadioButton("Admin");
        adminRadio.setToggleGroup(roleGroup);
        adminRadio.setStyle("-fx-font-size: 14px;");
        
        HBox roleBox = new HBox(20, customerRadio, adminRadio);
        roleBox.setAlignment(Pos.CENTER);
        
        // Error message label
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px; -fx-font-weight: bold;");
        errorLabel.setVisible(false);
        
        // Login button
        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #4A90E2; -fx-text-fill: white; " +
                            "-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 10 40;");
        loginButton.setPrefWidth(150);
        
        // Hover effects
        loginButton.setOnMouseEntered(e -> 
            loginButton.setStyle("-fx-background-color: #357ABD; -fx-text-fill: white; " +
                                "-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 10 40;"));
        loginButton.setOnMouseExited(e -> 
            loginButton.setStyle("-fx-background-color: #4A90E2; -fx-text-fill: white; " +
                                "-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 10 40;"));
        
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
        Label helpLabel = new Label("Default credentials:\nCustomer: customer/customer123\nAdmin: admin/admin123");
        helpLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #666; -fx-text-alignment: center;");
        helpLabel.setAlignment(Pos.CENTER);
        
        // Add all components to form container
        formContainer.getChildren().addAll(
            usernameLabel, usernameField,
            passwordLabel, passwordField,
            roleLabel, roleBox,
            errorLabel,
            loginButton,
            helpLabel
        );
        
        // Add everything to root
        root.getChildren().addAll(titleLabel, subtitleLabel, formContainer);
        
        Scene scene = new Scene(root, 500, 600);
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
