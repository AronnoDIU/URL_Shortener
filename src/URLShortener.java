import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class URLShortener {
    private static final Map<String, String> urlMap = new HashMap<>();

    public static void main(String[] args) {
        Scanner userInput = new Scanner(System.in);

        loadURLsFromFile(); // Load URLs from file if it exists

        while (true) {
            System.out.println("1. Shorten URL");
            System.out.println("2. Retrieve URL");
            System.out.println("3. List All URLs");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = userInput.nextInt();

            switch (choice) {
                case 1: // For Shorten URL
                    shortenURL(userInput);
                    break;
                case 2: // For Retrieve URL
                    retrieveURL(userInput);
                    break;
                case 3: // For List All URLs
                    listAllURLs();
                    break;
                case 4: // For Exit
                    saveURLsToFile();
                    System.out.println("Exiting URL Shortener. Goodbye!");
                    System.exit(0);
                    break;
                default: // For invalid choice
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    // For Shorten URL option
    private static void shortenURL(Scanner scanner) {
        System.out.print("Enter the long URL: ");
        String longURL = scanner.next();

        System.out.print("Enter a custom short URL (press Enter to generate one): ");
        String customShortURL = scanner.next();

        String shortURL; // The shortened URL to be generated

        // If the custom short URL is empty or already exists, generate a new one
        if (customShortURL.isEmpty() || urlMap.containsKey(customShortURL)) {
            shortURL = generateShortURL(longURL); // Generate a new short URL
        } else {
            shortURL = customShortURL; // Use the custom short URL
        }

        urlMap.put(shortURL, longURL); // Add the URL to the map

        System.out.println("Shortened URL: " + shortURL);
    }

    // For Retrieve URL option
    private static void retrieveURL(Scanner scanner) {
        System.out.print("Enter the short URL: ");
        String shortURL = scanner.next();

        String longURL = urlMap.get(shortURL); // Get the long URL from the map

        // If the long URL exists, print it. Otherwise, print an error message
        if (longURL != null) {
            System.out.println("Original URL: " + longURL);
        } else {
            System.out.println("URL not found. Please check the short URL.");
        }
    }

    // For List All URLs option
    private static void listAllURLs() {
        System.out.println("List of all shortened URLs:");

        // If the map is empty, print a message. Otherwise, print all the URLs
        if (urlMap.isEmpty()) {
            System.out.println("No URLs found.");
        } else {
            for (Map.Entry<String, String> entry : urlMap.entrySet()) {
                System.out.println(entry.getKey() + " -> " + entry.getValue());
            }
        }
    }

    // Generate a short URL from a long URL
    private static String generateShortURL(String longURL) {
        int hashCode = longURL.hashCode(); // Get the hash code of the long URL
        return Integer.toHexString(hashCode); // Convert the hash code to a hexadecimal string
    }

    // Save the URLs to a file so that they can be loaded later
    private static void saveURLsToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("urls.ser"))) {
            oos.writeObject(urlMap); // Write the map to the file as an object
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked") // Load the URLs from the file if it exists
    private static void loadURLsFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("urls.txt"))) {
            urlMap.putAll((Map<String, String>) ois.readObject());
        } catch (IOException | ClassNotFoundException e) {
            // Ignore if the file does not exist or cannot be read
        }
    }
}