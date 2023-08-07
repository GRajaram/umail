import java.io.*;
import java.net.*;

public class EmailServer {
    public static void main(String[] args) {
        try {
            ServerSocket smtpServerSocket = new ServerSocket(25);
            ServerSocket imapServerSocket = new ServerSocket(143);

            System.out.println("SMTP server is running and listening on port 25...");
            System.out.println("IMAP server is running and listening on port 143...");

            while (true) {
                Socket smtpClientSocket = smtpServerSocket.accept();
                Socket imapClientSocket = imapServerSocket.accept();

                System.out.println("Connected to SMTP client: " + smtpClientSocket.getInetAddress());
                System.out.println("Connected to IMAP client: " + imapClientSocket.getInetAddress());

                // Create threads to handle communication with SMTP and IMAP clients
                Thread smtpThread = new Thread(new SMTPHandler(smtpClientSocket));
                Thread imapThread = new Thread(new IMAPHandler(imapClientSocket));

                smtpThread.start();
                imapThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
