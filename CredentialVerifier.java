    public class CredentialVerifier {

        // Create an array of User objects
        private User[] users = {
            new User("abc@yahoo.com", "1234"),
            new User("def@yahoo.com", "5678"),
            new User("ghi@yahoo.com", "4321"),
            // Add more users here if needed
            // new User("anotherUser@example.com", "anotherPassword"),
        };
    
        public boolean isValidCredentials(String username, String password) {
            // Iterate over the User array and check credentials
            for (User user : users) {
                if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                    return true;
                }
            }
            return false;
        }
    }
