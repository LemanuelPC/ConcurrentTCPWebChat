package io.codeforall.javatars.server;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable{

    private Socket clientSocket;
    private Server server;
    private BufferedReader inputBufferedReader;
    private String nick;

    public ClientHandler(Socket clientSocket, Server server){
        this.clientSocket = clientSocket;
        this.server = server;
        this.nick = clientSocket.getInetAddress().getHostAddress();
    }

    @Override
    public void run() {

        Thread.currentThread().setName(nick);

        while(true) {

            try {
                setupSocketStream();
                // read line from socket input reader
                String line = inputBufferedReader.readLine();

                // if received /quit close break out of the reading loop
                if (line == null || line.startsWith("//quit")) {

                    System.out.println("Client closed, exiting");
                    break;

                }

                if (line.startsWith("//nick")){
                    nick = line.substring(7).replace(" ", "");
                    server.broadcast(Thread.currentThread().getName() + " has changed nick to " + nick);
                    Thread.currentThread().setName(nick);
                    continue;
                }

                if(line.startsWith("//list")){
                    server.broadcast(line, clientSocket);
                    continue;
                }

                if(line.startsWith("//whisper")){
                    String destination = line.split(" ")[1];
                    String message = "-> " + nick + ": " + line.substring(line.split(" ")[0].length() + destination.length() + 2);
                    server.broadcast(message, destination);
                    continue;
                }

                server.broadcast(Thread.currentThread().getName() + ": " + line);

                // show the received line to the console
                System.out.println(Thread.currentThread().getName() + ": " + line);

            } catch (IOException ex) {

                System.out.println("Receiving error: " + ex.getMessage());

            }

        }

    }

    public void setupSocketStream() throws IOException {
        inputBufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public String getNick() {
        return nick;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }
}
