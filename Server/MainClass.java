import java.io.IOException;

public class MainClass {

   // Main method to start the server
    public static void main(String[] args) {
        UmailEmailServer server = new UmailEmailServer(25);  // SMTP typically runs on port 25
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
