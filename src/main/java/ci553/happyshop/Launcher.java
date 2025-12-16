package ci553.happyshop;

import ci553.happyshop.systemSetup.InitializeApplicationFolders;
import javafx.application.Application;
import java.io.IOException;

/**
 * The Launcher class serves as the main entry point of the system.
 * It calls the launch() method of the Main class to start the JavaFX application.
 * This class is intentionally kept simple to isolate the bootstrapping logic.
 *
 * It also ensures that all required application folders and files are initialized
 * before the JavaFX application starts, preventing file-not-found errors when the
 * application is run on a new computer.
 *
 * @author Shine Shan University of Brighton
 * @version 1.0
 */

public class Launcher  {
    /**
     * The main method to start the full system.
     * It initializes all required folders and files, then launches the Main JavaFX application.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        try {
            // Initialize all required application folders and files before starting the app
            InitializeApplicationFolders.initializeAllFolders();
        } catch (IOException e) {
            System.err.println("Failed to initialize application folders: " + e.getMessage());
            e.printStackTrace();
        }
        Application.launch(ci553.happyshop.Main.class, args);  // Starts the JavaFX application through Main
    }
}
