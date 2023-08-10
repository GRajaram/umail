import java.io.*;
import java.net.*;
import java.util.Base64;
import java.util.Scanner;

public class UmailSMTPClient {

    private String smtpServer;
    private int port;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public UmailSMTPClient(String smtpServer, int port) {
        this.smtpServer = smtpServer;
        this.port = port;
    }

    public void connect() throws IOException {
        socket = new Socket(smtpServer, port);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

        // Read the initial greeting from the SMTP server
        readResponse();
    }

    public void sendEmail(String username, String password, String from, String to, String subject, String body) throws IOException {
        sendCommand("HELO " + smtpServer);
        readResponse();

            // AUTH LOGIN command
            sendCommand("AUTH LOGIN");
            // readResponse();

            // Send username and password
            sendCommand(Base64.getEncoder().encodeToString(username.getBytes()));
            // readResponse();
            sendCommand(Base64.getEncoder().encodeToString(password.getBytes()));
            readResponse();


        sendCommand("MAIL FROM:<" + from + ">");
        readResponse();

        sendCommand("RCPT TO:<" + to + ">");
        readResponse();

        sendCommand("DATA");
        readResponse();

        writer.println("Subject: " + subject);
        writer.println("From: " + from);
        writer.println("To: " + to);
        writer.println();  // new line to separate headers from body
        writer.println(body);
        writer.println(".");
        readResponse();

        sendCommand("QUIT");
        readResponse();
    }

    private void sendCommand(String command) throws IOException {
        writer.println(command);
    }

    private void readResponse() throws IOException {
        String response = reader.readLine();
        System.out.println("S: " + response);

        if(response.startsWith("4") || response.startsWith("5")) {
            
            throw new IOException("SMTP server responded with an error: " + response);
        }
    }

    public void close() throws IOException {
        reader.close();
        writer.close();
        socket.close();
    }

    public static void main(String[] args) {
        String smtpServer = "localhost";
        int port = 25;

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        // System.out.print("Enter your age: ");
        // int age = scanner.nextInt();

        scanner.close();
        
        try  {
            UmailSMTPClient smtp = new UmailSMTPClient(smtpServer, port);
            smtp.connect();
            smtp.sendEmail(username, password, "sender@example.com", "receiver@example.com", "Test Email", "Hello from SMTPHandler!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
