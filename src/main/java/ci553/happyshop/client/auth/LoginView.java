package ci553.happyshop.client.auth;

import ci553.happyshop.utility.UIStyle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.function.Consumer;

/**
 * LoginView provides a login interface for users to authenticate as either
 * a Customer or an Admin. It also provides an interface to create new accounts.
 * Upon successful login, it invokes a callback with the selected user role.
 *
 * @author GitHub Copilot
 * @version 2.0
 */
public class LoginView {
    
    private Stage stage;
    private Consumer<UserRole> onLoginSuccess;
    private UserAccountManager accountManager;
    private StackPane stackPane;
    
    /**
     * Constructor for LoginView
     * @param onLoginSuccess Callback function to be invoked when login succeeds, passing the UserRole
     */
    public LoginView(Consumer<UserRole> onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;
        this.accountManager = UserAccountManager.getInstance();
    }
    
    /**
     * Starts the login window
     * @param stage The JavaFX stage to display the login view
     */
    public void start(Stage stage) {
        this.stage = stage;
        stage.setTitle("HappyShop - Login & Registration");
        
        // Main container with StackPane to switch between login and create account views
        HBox root = new HBox();
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #f5f5f5;");
        
        stackPane = new StackPane();
        stackPane.getChildren().addAll(
            createLoginPanel(),
            createAccountPanel()
        );
        
        // Initially show login panel
        stackPane.getChildren().get(0).setVisible(true);
        stackPane.getChildren().get(1).setVisible(false);
        
        root.getChildren().add(stackPane);
        
        Scene scene = new Scene(root, 700, 450);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    
    /**
     * Creates the login panel
     */
    private VBox createLoginPanel() {
        VBox formContainer = new VBox(15);
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setPadding(new Insets(40, 60, 40, 60));
        formContainer.setStyle("-fx-background-color: white;");
        
        // Title
        Label titleLabel = new Label("🛒 HappyShop Login");
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
        RadioButton customerRadio = new RadioButton("👤 Customer");
        customerRadio.setToggleGroup(roleGroup);
        customerRadio.setSelected(true);
        customerRadio.setStyle("-fx-font-size: 13px;");
        
        RadioButton adminRadio = new RadioButton("👨‍💼 Admin");
        adminRadio.setToggleGroup(roleGroup);
        adminRadio.setStyle("-fx-font-size: 13px;");
        
        HBox roleBox = new HBox(30, customerRadio, adminRadio);
        roleBox.setAlignment(Pos.CENTER);
        
        // Error message label
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #d32f2f; -fx-font-size: 12px;");
        errorLabel.setVisible(false);
        
        // Login button
        Button loginButton = new Button("🔓 Login");
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
        
        // Create account link
        Button createAccountLink = new Button("Don't have an account? Create one");
        createAccountLink.setStyle("-fx-background-color: transparent; -fx-text-fill: #4A90E2; " +
                                  "-fx-font-size: 12px; -fx-cursor: hand; -fx-underline: true;");
        createAccountLink.setOnAction(e -> switchToCreateAccount());
        
        // Help text
        Label helpLabel = new Label("Demo: customer/customer123  or  admin/admin123");
        helpLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #999;");
        
        // Add all components to form container
        formContainer.getChildren().addAll(
            titleLabel,
            credentialsBox,
            roleBox,
            errorLabel,
            loginButton,
            createAccountLink,
            helpLabel
        );
        
        return formContainer;
    }
    
    /**
     * Creates the create account panel
     */
    private VBox createAccountPanel() {
        VBox formContainer = new VBox(15);
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setPadding(new Insets(40, 60, 40, 60));
        formContainer.setStyle("-fx-background-color: white;");
        
        // Title
        Label titleLabel = new Label("✨ Create New Account");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333;");
        
        // Username
        VBox usernameBox = new VBox(5);
        Label usernameLabel = new Label("Username (minimum 3 characters)");
        usernameLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        usernameField.setStyle(UIStyle.textFiledStyle);
        usernameField.setPrefWidth(300);
        usernameBox.getChildren().addAll(usernameLabel, usernameField);
        
        // Password
        VBox passwordBox = new VBox(5);
        Label passwordLabel = new Label("Password (minimum 6 characters)");
        passwordLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        passwordField.setStyle(UIStyle.textFiledStyle);
        passwordField.setPrefWidth(300);
        passwordBox.getChildren().addAll(passwordLabel, passwordField);
        
        // Confirm Password
        VBox confirmPasswordBox = new VBox(5);
        Label confirmPasswordLabel = new Label("Confirm Password");
        confirmPasswordLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Re-enter password");
        confirmPasswordField.setStyle(UIStyle.textFiledStyle);
        confirmPasswordField.setPrefWidth(300);
        confirmPasswordBox.getChildren().addAll(confirmPasswordLabel, confirmPasswordField);
        
        // Role selection
        ToggleGroup roleGroup = new ToggleGroup();
        RadioButton customerRadio = new RadioButton("👤 Customer");
        customerRadio.setToggleGroup(roleGroup);
        customerRadio.setSelected(true);
        customerRadio.setStyle("-fx-font-size: 13px;");
        
        RadioButton adminRadio = new RadioButton("👨‍💼 Admin");
        adminRadio.setToggleGroup(roleGroup);
        adminRadio.setStyle("-fx-font-size: 13px;");
        
        HBox roleBox = new HBox(30, customerRadio, adminRadio);
        roleBox.setAlignment(Pos.CENTER);
        
        // Error/Success message label
        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: #d32f2f; -fx-font-size: 12px;");
        messageLabel.setVisible(false);
        
        // Create account button
        Button createButton = new Button("✅ Create Account");
        createButton.setStyle("-fx-background-color: #27AE60; -fx-text-fill: white; " +
                             "-fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 40; -fx-cursor: hand;");
        
        createButton.setOnMouseEntered(e -> 
            createButton.setStyle("-fx-background-color: #229954; -fx-text-fill: white; " +
                                 "-fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 40; -fx-cursor: hand;"));
        createButton.setOnMouseExited(e -> 
            createButton.setStyle("-fx-background-color: #27AE60; -fx-text-fill: white; " +
                                 "-fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 40; -fx-cursor: hand;"));
        
        createButton.setOnAction(e -> handleCreateAccount(
            usernameField.getText(),
            passwordField.getText(),
            confirmPasswordField.getText(),
            customerRadio.isSelected() ? UserRole.CUSTOMER : UserRole.ADMIN,
            messageLabel,
            usernameField,
            passwordField,
            confirmPasswordField
        ));
        
        // Back to login link
        Button backToLoginLink = new Button("↩️ Back to Login");
        backToLoginLink.setStyle("-fx-background-color: transparent; -fx-text-fill: #4A90E2; " +
                                "-fx-font-size: 12px; -fx-cursor: hand;");
        backToLoginLink.setOnAction(e -> switchToLogin());
        
        // Add all components to form container
        formContainer.getChildren().addAll(
            titleLabel,
            usernameBox,
            passwordBox,
            confirmPasswordBox,
            roleBox,
            messageLabel,
            createButton,
            backToLoginLink
        );
        
        return formContainer;
    }
    
    /**
     * Switches view to login panel
     */
    private void switchToLogin() {
        stackPane.getChildren().get(0).setVisible(true);
        stackPane.getChildren().get(1).setVisible(false);
    }
    
    /**
     * Switches view to create account panel
     */
    private void switchToCreateAccount() {
        stackPane.getChildren().get(0).setVisible(false);
        stackPane.getChildren().get(1).setVisible(true);
    }
      /**
     * Handles the login logic
     */
    private void handleLogin(String username, String password, UserRole selectedRole, Label errorLabel) {
        errorLabel.setVisible(false);
        
        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            showError(errorLabel, "Please enter both username and password");
            return;
        }
        
        UserAccount account = accountManager.authenticate(username, password);
        
        if (account != null && account.getRole() == selectedRole) {
            // Store the logged-in username in SessionManager
            SessionManager.getInstance().setCurrentUser(username);
            stage.close();
            onLoginSuccess.accept(selectedRole);
        } else {
            showError(errorLabel, "Invalid username, password, or role mismatch");
        }
    }
    
    /**
     * Handles the create account logic
     */
    private void handleCreateAccount(String username, String password, String confirmPassword, 
                                    UserRole role, Label messageLabel, TextField usernameField,
                                    PasswordField passwordField, PasswordField confirmPasswordField) {
        messageLabel.setVisible(false);
        messageLabel.setStyle("-fx-text-fill: #d32f2f; -fx-font-size: 12px;");
        
        // Validate inputs
        if (username.trim().isEmpty()) {
            showMessage(messageLabel, "Username cannot be empty");
            return;
        }
        
        if (username.length() < 3) {
            showMessage(messageLabel, "Username must be at least 3 characters long");
            return;
        }
        
        if (password.isEmpty()) {
            showMessage(messageLabel, "Password cannot be empty");
            return;
        }
        
        if (password.length() < 6) {
            showMessage(messageLabel, "Password must be at least 6 characters long");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            showMessage(messageLabel, "Passwords do not match");
            return;
        }
        
        if (accountManager.usernameExists(username)) {
            showMessage(messageLabel, "Username already exists. Please choose a different one");
            return;
        }
        
        // Create account
        if (accountManager.createAccount(username, password, role)) {
            messageLabel.setText("✅ Account created successfully! Switching to login...");
            messageLabel.setStyle("-fx-text-fill: #27AE60; -fx-font-size: 12px; -fx-font-weight: bold;");
            messageLabel.setVisible(true);
            
            // Clear fields
            usernameField.clear();
            passwordField.clear();
            confirmPasswordField.clear();
            
            // Switch to login after a short delay
            javafx.application.Platform.runLater(() -> {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException ignored) {}
                switchToLogin();
            });
        } else {
            showMessage(messageLabel, "Failed to create account. Please try again");
        }
    }
    
    /**
     * Displays an error or info message
     */
    private void showError(Label label, String message) {
        label.setText(message);
        label.setStyle("-fx-text-fill: #d32f2f; -fx-font-size: 12px;");
        label.setVisible(true);
    }
    
    /**
     * Displays a message
     */
    private void showMessage(Label label, String message) {
        label.setText(message);
        label.setStyle("-fx-text-fill: #d32f2f; -fx-font-size: 12px;");
        label.setVisible(true);
    }
}
