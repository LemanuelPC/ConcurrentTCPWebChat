package io.codeforall.javatars.client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    private Socket socket;
    private BufferedReader inputBufferedReader;
    private BufferedReader inputServerChatReader;
    private BufferedWriter outputBufferedWriter;
    private ClientReader clientReader;

    public Client(String serverAddress, int serverPort) {

        System.out.println("Trying to establishing the connection, please wait...");

        try {

            // connect to the specified host name and port
            socket = new Socket(serverAddress, serverPort);
            System.out.println("Connected to: " + socket);

            // create the streams
            setupSocketStreams();

            clientReader = new ClientReader(inputServerChatReader);

        } catch (UnknownHostException ex) {

            System.out.println("Host unknown: " + ex.getMessage());
            System.exit(1);

        } catch (IOException ex) {

            System.out.println(ex.getMessage());
            System.exit(1);

        }

        writeMessage();



        // close the client socket and buffers
        stop();

    }

    public static void main(String[] args) {

        /*if (args.length != 2) {

            System.out.println("Usage: java ChatClient <host> <port>");
            System.exit(1);

        }*/

        try {

            //new Client(args[0], Integer.parseInt(args[1]));
            new Client("192.168.2.147", 8080);

        } catch (NumberFormatException ex) {

            System.out.println("Invalid port number " + args[0]);

        }

    }

    public void setupSocketStreams() throws IOException {

        inputBufferedReader = new BufferedReader(new InputStreamReader(System.in));

        inputServerChatReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        outputBufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

    }

    public void stop() {

        try {

            if (socket != null) {
                System.out.println("Closing the socket");
                socket.close();
            }

        } catch (IOException ex) {

            System.out.println("Error closing connection: " + ex.getMessage());

        }
    }

    private void writeMessage() {
        String line = "";

        // while the client doesn't signal to quit
        while (!line.equals("//quit")) {

            try {

                // read the pretended message from the console
                line = inputBufferedReader.readLine();

                // write the pretended message to the output buffer
                outputBufferedWriter.write(line);
                outputBufferedWriter.newLine();
                outputBufferedWriter.flush();



            } catch (IOException ex) {

                System.out.println("Sending error: " + ex.getMessage() + ", closing client...");
                break;

            }

            Thread thread = new Thread(clientReader);
            thread.start();

        }
    }

}
