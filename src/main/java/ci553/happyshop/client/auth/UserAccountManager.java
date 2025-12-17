package ci553.happyshop.client.auth;

import java.util.HashMap;
import java.util.Map;

/**
 * UserAccountManager manages all user accounts in the system.
 * Currently stores accounts in memory, but can be extended to use a database.
 * 
 * This is a singleton pattern to ensure only one instance manages all accounts.
 * 
 * @author GitHub Copilot
 * @version 1.0
 */
public class UserAccountManager {
    private static UserAccountManager instance;
    private Map<String, UserAccount> accounts;

    /**
     * Private constructor to enforce singleton pattern
     */
    private UserAccountManager() {
        accounts = new HashMap<>();
        initializeDefaultAccounts();
    }

    /**
     * Gets the singleton instance of UserAccountManager
     */
    public static synchronized UserAccountManager getInstance() {
        if (instance == null) {
            instance = new UserAccountManager();
        }
        return instance;
    }

    /**
     * Initializes default accounts (admin and customer)
     */
    private void initializeDefaultAccounts() {
        accounts.put("admin", new UserAccount("admin", "admin123", UserRole.ADMIN));
        accounts.put("customer", new UserAccount("customer", "customer123", UserRole.CUSTOMER));
    }

    /**
     * Creates a new user account
     * @param username The desired username
     * @param password The desired password
     * @param role The user role (CUSTOMER or ADMIN)
     * @return true if account was created successfully, false if username already exists
     */
    public synchronized boolean createAccount(String username, String password, UserRole role) {
        // Validate inputs
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        if (password == null || password.trim().isEmpty()) {
            return false;
        }
        if (username.length() < 3) {
            return false;
        }
        if (password.length() < 6) {
            return false;
        }
        
        // Check if username already exists
        if (accounts.containsKey(username.toLowerCase())) {
            return false;
        }

        // Create new account
        UserAccount newAccount = new UserAccount(username, password, role);
        accounts.put(username.toLowerCase(), newAccount);
        return true;
    }

    /**
     * Authenticates a user with username and password
     * @param username The username
     * @param password The password
     * @return The UserAccount if authentication is successful, null otherwise
     */
    public UserAccount authenticate(String username, String password) {
        if (username == null || password == null) {
            return null;
        }

        UserAccount account = accounts.get(username.toLowerCase());
        if (account != null && account.getPassword().equals(password)) {
            return account;
        }
        return null;
    }

    /**
     * Checks if a username already exists
     * @param username The username to check
     * @return true if username exists, false otherwise
     */
    public boolean usernameExists(String username) {
        return accounts.containsKey(username.toLowerCase());
    }

    /**
     * Gets an account by username
     * @param username The username
     * @return The UserAccount if found, null otherwise
     */
    public UserAccount getAccount(String username) {
        return accounts.get(username.toLowerCase());
    }

    /**
     * Gets the number of accounts
     */
    public int getAccountCount() {
        return accounts.size();
    }

    /**
     * Resets all accounts to default (useful for testing)
     */
    public synchronized void resetToDefaults() {
        accounts.clear();
        initializeDefaultAccounts();
    }
}
