import java.io.*;
import java.net.*;
import java.util.Base64;

public class UmailEmailServer {

    private int port;  // Server's listening port
    private ServerSocket serverSocket;  // Socket to accept client connections

    // Constructor: Initializes the server with a given port
    public UmailEmailServer(int port) {
        this.port = port;
    }

    // Starts the server, listens for client connections, and handles each client in a new thread
    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("SMTP Server started on port " + port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            new ClientHandler(clientSocket).start();  // Handle each client in a separate thread
        }
    }

    // Inner class to handle client interactions
    private static class ClientHandler extends Thread {
        private Socket socket;  // Client socket
        private BufferedReader reader;  // For reading data from the client
        private PrintWriter writer;  // For sending data to the client

        // Constructor: Initializes the handler with the client's socket
        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {

            try {
                // Setup streams for communication
                InputStream input = socket.getInputStream();
                OutputStream output = socket.getOutputStream();

                reader = new BufferedReader(new InputStreamReader(input));
                writer = new PrintWriter(output, true);

                boolean isAuthenticated = false;  // Flag to check if a user is authenticated

                // Initial greeting to the client
                sendResponse("220 SimpleSMTPServer Ready");

                String line;
                // Continue reading commands from the client
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("HELO") || line.startsWith("HELO")) {
                        sendResponse("250 Hello");
                    } else if (line.startsWith("AUTH LOGIN") || line.startsWith("AUTH")) {
                        // Handle authentication (login)

                        // Asking for username
                        sendResponse("334 VXNlcm5hbWU6"); 
                        String encodedUsername = reader.readLine();
                        String username = new String(Base64.getDecoder().decode(encodedUsername));

                        // Asking for password
                        sendResponse("334 UGFzc3dvcmQ6");
                        String encodedPassword = reader.readLine();
                        String password = new String(Base64.getDecoder().decode(encodedPassword));

                        // Check credentials and respond accordingly
                        if (isValidCredentials(username, password)) {
                            sendResponse("235 Authentication succeeded");
                            isAuthenticated = true;
                        } else {
                            sendResponse("535 Authentication failed");
                            // isAuthenticated = false;
                        }
                    }
                    // Handling various SMTP commands after authentication
                    else if (isAuthenticated && line.startsWith("MAIL FROM:")) {
                        sendResponse("250 OK");
                    } else if (isAuthenticated && line.startsWith("RCPT TO:")) {
                        sendResponse("250 OK");
                    } else if (isAuthenticated && line.equals("DATA")) {
                        sendResponse("354 Start mail input; end with <CRLF>.<CRLF>");
                        readEmailData();  // Read email content
                        sendResponse("250 OK");
                    } else if (isAuthenticated && line.equals("QUIT")) {
                        // Client wants to terminate connection
                        sendResponse("221 Bye");
                        break;
                    } else {
                        // Command not recognized
                        sendResponse("500 Unrecognized command");
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Cleanup: close the socket
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Send a response to the client
        private void sendResponse(String response) {
            writer.println(response);
        }

        // Read email data from the client
        private void readEmailData() throws IOException {
            StringBuilder emailData = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals("<CRLF>.<CRLF>")) {
                    break;  // End of email data
                }
                emailData.append(line).append("\n");
            }
            System.out.println("\nReceived Email:\n" + emailData);
        }

        // Check if the provided credentials are valid
        private boolean isValidCredentials(String username, String password) {
                CredentialVerifier verifier = new CredentialVerifier();
                return verifier.isValidCredentials(username, password);
        }
    }
}
