
import java.io.*;
import java.net.*;
import java.util.Base64;

public class UmailEmailServer {

    private int port;
    private ServerSocket serverSocket;

    public UmailEmailServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("SMTP Server started on port " + port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            new ClientHandler(clientSocket).start();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private BufferedReader reader;
        private PrintWriter writer;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                InputStream input = socket.getInputStream();
                OutputStream output = socket.getOutputStream();

                reader = new BufferedReader(new InputStreamReader(input));
                writer = new PrintWriter(output, true);

                // Initial greeting
                sendResponse("220 SimpleSMTPServer Ready");

                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("HELO") || line.startsWith("HELO")) {
                        sendResponse("250 Hello");
                    } else if (line.startsWith("AUTH LOGIN") || line.startsWith("AUTH")) {
                        sendResponse("334 VXNlcm5hbWU6"); // This is "Username:" encoded in base64
                        String encodedUsername = reader.readLine();
                        String username = new String(Base64.getDecoder().decode(encodedUsername));

                        sendResponse("334 UGFzc3dvcmQ6"); // This is "Password:" encoded in base64
                        String encodedPassword = reader.readLine();
                        String password = new String(Base64.getDecoder().decode(encodedPassword));

                        if (isValidCredentials(username, password)) {
                            sendResponse("235 Authentication succeeded");
                        } else {
                            sendResponse("535 Authentication failed");
                        }
                    } else if (line.startsWith("MAIL FROM:")) {
                        sendResponse("250 OK");
                    } else if (line.startsWith("RCPT TO:")) {
                        sendResponse("250 OK");
                    } else if (line.equals("DATA")) {
                        sendResponse("354 Start mail input; end with <CRLF>.<CRLF>");
                        readEmailData();
                        sendResponse("250 OK");
                    } else if (line.equals("QUIT")) {
                        sendResponse("221 Bye");
                        break;
                    } else {
                        sendResponse("500 Unrecognized command");
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void sendResponse(String response) {
            writer.println(response);
        }

        private void readEmailData() throws IOException {
            StringBuilder emailData = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals(".")) {
                    break;
                }
                emailData.append(line).append("\n");
            }
            System.out.println("\nReceived Email:\n" + emailData);
        }

        private boolean isValidCredentials(String username, String password) {
        // NOTE: This is just a hardcoded example.
        // In a real-world application, you'd check this data against a database or other secure storage.
        return "abc@yahoo.com".equals(username) && "12345678".equals(password);
    }
    }


    public static void main(String[] args) {
        UmailEmailServer server = new UmailEmailServer(25);
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
