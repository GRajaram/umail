import java.io.IOException;
import java.util.Scanner;

public class MainClass {

  // Main method to test the SMTP client functionality
    public static void main(String[] args) {
        String smtpServer = "localhost";
        int port = 25;

        // Initialize a scanner for user input
        Scanner scanner = new Scanner(System.in);

        // Collect the username for SMTP authentication
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        // Collect the password for SMTP authentication
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();
        
        scanner.close();  // Close the scanner

        try {
            // Create an instance of the SMTP client
            UmailSMTPClient smtp = new UmailSMTPClient(smtpServer, port);
            smtp.connect();  // Connect to the server
            
            // Use the client to send a test email
            smtp.sendEmail(username, password, "sender@example.com", "receiver@example.com", "Test Email",
                    "Hello this is a test email");
        } catch (IOException e) {
            // Handle potential IO errors
            System.out.println("Error: " + e.getMessage());
        }
    }

}
