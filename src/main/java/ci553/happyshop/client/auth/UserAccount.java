package ci553.happyshop.client.auth;

/**
 * UserAccount represents a user account with username, password, and role.
 * This is a simple data class for storing user information.
 * 
 * @author GitHub Copilot
 * @version 1.0
 */
public class UserAccount {
    private String username;
    private String password;
    private UserRole role;

    /**
     * Constructor for UserAccount
     * @param username The username
     * @param password The password (should be hashed in production)
     * @param role The user role (CUSTOMER or ADMIN)
     */
    public UserAccount(String username, String password, UserRole role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    /**
     * Gets the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the user role
     */
    public UserRole getRole() {
        return role;
    }

    /**
     * Returns a string representation of the account
     */
    @Override
    public String toString() {
        return "UserAccount{" +
                "username='" + username + '\'' +
                ", role=" + role +
                '}';
    }
}
