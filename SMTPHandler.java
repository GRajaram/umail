import java.io.*;
import java.net.Socket;

public class SMTPHandler implements Runnable {
    private Socket clientSocket;

    public SMTPHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            // Implement SMTP protocol handling here
            // You need to parse SMTP commands, read emails from the client, and store them
            // For simplicity, we will just close the connection for now
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Sample code: Send a response and close the connection
            out.println("220 Welcome to Java SMTP server");
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
