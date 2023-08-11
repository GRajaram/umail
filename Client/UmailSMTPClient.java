import java.io.*;
import java.net.*;
import java.util.Base64;
import java.util.Scanner;

// Define the SMTP client class
public class UmailSMTPClient {

    // Declare server details and IO stream-related instance variables
    private String smtpServer;
    private int port;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    // Constructor to initialize server address and port
    public UmailSMTPClient(String smtpServer, int port) {
        this.smtpServer = smtpServer;
        this.port = port;
    }

    // Establish a connection to the SMTP server
    public void connect() throws IOException {
        // Create a new socket for communication with the SMTP server
        socket = new Socket(smtpServer, port);
        
        // Initialize the reader to read data from the server
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
        // Initialize the writer to send data to the server
        writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

        // Read the server's initial greeting message
        readResponse();
    }

    // Handle the process of sending an email
    public void sendEmail(String username, String password, String from, String to, String subject, String body)
            throws IOException {
        // Send a HELO command to the server
        sendCommand("HELO " + smtpServer);
        // Expect a response from the server
        readResponse();

        try {
            // Begin the authentication process with the server
            sendCommand("AUTH LOGIN");
            
            // Send the username (Base64-encoded) for authentication
            sendCommand(Base64.getEncoder().encodeToString(username.getBytes()));
            
            // Send the password (Base64-encoded) for authentication
            sendCommand(Base64.getEncoder().encodeToString(password.getBytes()));
            
            // Expect a response after sending credentials
            readResponse();
        } catch (Exception e) {
            // Handle authentication errors
            System.out.println("Invalid Username or Password, Please try again");
            throw new IOException("SMTP server responded with an error: " + e);
        }
        
        // Indicate the sender's email address
        sendCommand("MAIL FROM:<" + from + ">");
        readResponse();

        // Indicate the recipient's email address
        sendCommand("RCPT TO:<" + to + ">");
        readResponse();

        // Begin sending the email content
        sendCommand("DATA");
        readResponse();

        // Specify email headers and the email body
        writer.println("Subject: " + subject);
        writer.println("From: " + from);
        writer.println("To: " + to);
        writer.println(); // Empty line to separate headers from body
        writer.println(body);
        writer.println("<CRLF>.<CRLF>");  // End of the email data
        readResponse();

        // End the SMTP session
        sendCommand("QUIT");
        readResponse();
    }

    // Utility method to send commands to the SMTP server
    private void sendCommand(String command) throws IOException {
        writer.println(command);
    }

    // Utility method to read and handle responses from the server
    private void readResponse() throws IOException {
        String response = reader.readLine();
        System.out.println("S: " + response);

        // Check for error codes in the server response
        if (response.startsWith("4") || response.startsWith("5")) {
            if (response.startsWith("535")) {
                System.out.println("Invalid Username or Password");
            } else {
                System.out.println("Error");
            }
            throw new IOException("Please try again");
        }
    }

    // Close all established connections and IO streams
    public void close() throws IOException {
        reader.close();
        writer.close();
        socket.close();
    }

}
