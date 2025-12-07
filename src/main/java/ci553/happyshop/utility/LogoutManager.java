package ci553.happyshop.utility;

import javafx.application.Platform;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * LogoutManager handles logout functionality across all client windows.
 * It tracks all open windows and provides a mechanism to close them all
 * and return to the login screen.
 * 
 * @author GitHub Copilot
 * @version 1.0
 */
public class LogoutManager {
    private static LogoutManager instance;
    private List<Stage> openWindows = new ArrayList<>();
    private Runnable onLogoutCallback;
    
    private LogoutManager() {}
    
    public static LogoutManager getInstance() {
        if (instance == null) {
            instance = new LogoutManager();
        }
        return instance;
    }
    
    /**
     * Registers a window to be tracked
     */
    public void registerWindow(Stage window) {
        if (!openWindows.contains(window)) {
            openWindows.add(window);
        }
    }
    
    /**
     * Sets the callback to be invoked after logout (typically to show login screen)
     */
    public void setOnLogoutCallback(Runnable callback) {
        this.onLogoutCallback = callback;
    }
    
    /**
     * Performs logout - closes all windows and shows login screen
     */
    public void logout() {
        // Close all registered windows
        for (Stage window : openWindows) {
            Platform.runLater(() -> window.close());
        }
        openWindows.clear();
        
        // Show login screen again
        if (onLogoutCallback != null) {
            Platform.runLater(onLogoutCallback);
        }
    }
    
    /**
     * Resets the manager (called when starting a new session)
     */
    public void reset() {
        openWindows.clear();
    }
}
