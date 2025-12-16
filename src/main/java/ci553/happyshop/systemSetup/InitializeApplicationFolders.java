package ci553.happyshop.systemSetup;

import ci553.happyshop.utility.StorageLocation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * This class is responsible for initializing all required application folders and files
 * during application startup.
 *
 * It ensures that the following directories and files are created if they don't exist:
 * 1. images/ - folder for product images
 * 2. images_resetDB/ - folder for backup product images
 * 3. orders/ - root folder for all orders
 * 4. orders/ordered/ - subfolder for "Ordered" state orders
 * 5. orders/progressing/ - subfolder for "Progressing" state orders
 * 6. orders/collected/ - subfolder for "Collected" state orders
 * 7. orders/orderCounter.txt - file to track order IDs
 *
 * This initialization happens automatically when the application launches,
 * ensuring that the application works correctly on new computers without manual setup.
 *
 * @author Shine Shan University of Brighton
 * @version 1.0
 */
public class InitializeApplicationFolders {

    /**
     * Initializes all required application folders and files.
     * This method is called during application startup to ensure all necessary
     * directories and files exist before the application needs them.
     *
     * @throws IOException if there's an error creating folders or files
     */
    public static void initializeAllFolders() throws IOException {
        // Create image folders
        createFolderIfNotExists(StorageLocation.imageFolderPath);
        createFolderIfNotExists(StorageLocation.imageResetFolderPath);

        // Create order folders
        createFolderIfNotExists(StorageLocation.ordersPath);
        createFolderIfNotExists(StorageLocation.orderedPath);
        createFolderIfNotExists(StorageLocation.progressingPath);
        createFolderIfNotExists(StorageLocation.collectedPath);

        // Create order counter file if it doesn't exist
        createOrderCounterFileIfNotExists(StorageLocation.orderCounterPath);
    }

    /**
     * Creates a folder if it doesn't already exist.
     *
     * @param folderPath the path to create
     * @throws IOException if there's an error creating the folder
     */
    private static void createFolderIfNotExists(Path folderPath) throws IOException {
        if (!Files.exists(folderPath)) {
            Files.createDirectories(folderPath);
            System.out.println("Created folder: " + folderPath);
        }
    }

    /**
     * Creates the order counter file with initial value "0" if it doesn't exist.
     *
     * @param filePath the path to the order counter file
     * @throws IOException if there's an error creating the file
     */
    private static void createOrderCounterFileIfNotExists(Path filePath) throws IOException {
        if (Files.notExists(filePath)) {
            Files.writeString(filePath, "0", StandardOpenOption.CREATE_NEW);
            System.out.println("Created order counter file: " + filePath);
        }
    }
}
