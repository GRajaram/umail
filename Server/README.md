# UmailEmailServer

**UmailEmailServer** is a basic SMTP server implementation in Java that handles basic email commands and user authentication.

## Requirements

- **Java Development Kit (JDK)**: You must have the JDK installed on your machine to compile and run Java programs. Ensure you have at least JDK 8 or newer.

## Compilation

To compile the `UmailEmailServer` program, follow the steps below:

1. The program is saved in a file named `UmailEmailServer.java`.
2. Navigate to the directory containing the file using your terminal or command prompt.
3. Use the Java compiler (`javac`) to compile the program:
javac UmailEmailServer.java

This will generate a `UmailEmailServer.class` file which can be executed using the Java runtime.

## Execution

To run the compiled program:
java UmailEmailServer

Once executed, the SMTP server will start and listen on port 25, indicating it's ready to accept client connections.

## Usage

After starting the server, you can use `UmailSMTPClient` to connect to it by pointing the client to `localhost` and port `25`.

