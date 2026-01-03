package ci553.happyshop.client.auth;

/**
 * SessionManager manages the currently logged-in user session.
 * This is a singleton pattern to ensure only one instance manages the session.
 * 
 * This class is used to track which user is currently logged in so that
 * their username can be retrieved when needed (e.g., when placing an order).
 * 
 * 
 */
public class SessionManager {
    private static SessionManager instance;
    private String currentUsername;

    /**
     * Private constructor to enforce singleton pattern
     */
    private SessionManager() {
        currentUsername = null;
    }

    /**
     * Gets the singleton instance of SessionManager
     */
    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    /**
     * Sets the currently logged-in user
     * @param username The username of the logged-in user
     */
    public void setCurrentUser(String username) {
        this.currentUsername = username;
    }

    /**
     * Gets the currently logged-in user
     * @return The username of the logged-in user, or null if no user is logged in
     */
    public String getCurrentUser() {
        return currentUsername;
    }

    /**
     * Clears the current session (user logout)
     */
    public void clearSession() {
        currentUsername = null;
    }

    /**
     * Checks if a user is currently logged in
     * @return true if a user is logged in, false otherwise
     */
    public boolean isUserLoggedIn() {
        return currentUsername != null && !currentUsername.trim().isEmpty();
    }
}
