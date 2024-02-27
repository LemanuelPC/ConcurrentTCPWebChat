package io.codeforall.javatars.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private ArrayList<ClientHandler> clients;
    private ServerSocket serverSocket;

    public Server(int port){

        try {

            System.out.println("Binding to port " + port);
            serverSocket = new ServerSocket(port);
            System.out.println("Server started: " + serverSocket);

            clients = new ArrayList<>();
            ExecutorService maximumEffort = Executors.newCachedThreadPool();

            while (true) {

                System.out.println("Waiting for a client connection");
                clients.add(new ClientHandler(serverSocket.accept(), this));
                System.out.println("Client accepted: " + clients.getLast().getNick());

                maximumEffort.submit(clients.getLast());
            }


        } catch (IOException e) {
            System.out.println(e.getMessage());

        } finally {

            close();

        }
    }

    public static void main(String[] args) {

        /*if (args.length == 0) {
            System.out.println("Usage: java ChatServer [port]");
            System.exit(1);
        }*/

        try {
            // try to create an instance of the ChatServer at port specified at args[0]
            //new Server(Integer.parseInt(args[0]));
            new Server(8080);

        } catch (NumberFormatException ex) {
            // write an error message if an invalid port was specified by the user
            System.out.println("Invalid port number " + args[0]);
        }

    }



    public void close() {

        try {
            while (!clients.isEmpty()) {
                if (clients.getFirst().getClientSocket() != null) {
                    System.out.println("Closing client connection");
                    clients.removeFirst().getClientSocket().close();
                }
            }

            if (serverSocket != null) {
                System.out.println("Closing server socket");
                serverSocket.close();
            }


        } catch (IOException ex) {

            System.out.println("Error closing connection: " + ex.getMessage());

        }

    }

    public void broadcast(String message) throws IOException {
        PrintWriter out;

        for (ClientHandler client : clients){
            out = new PrintWriter(client.getClientSocket().getOutputStream());
            out.println(message);
            out.flush();
            //out.close();
        }
    }

    public void broadcast(String message, Socket clientSocket) throws IOException {

        PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
        if (message.equals("//list")){
            StringBuilder messageBuilder = new StringBuilder("List of current active chat clients:\n");
            for (ClientHandler client : clients){
                messageBuilder.append(client.getNick()).append("\n");
            }
            message = messageBuilder.toString();
        }

        out.println(message);
        out.flush();
        //out.close();

    }

    public void broadcast(String message, String destination) throws IOException {

        Socket clientSocket = null;

        for (ClientHandler client : clients){
            if (client.getNick().equals(destination)){
                clientSocket = client.getClientSocket();
                break;
            }
        }

        if(clientSocket == null){
            return;
        }

        PrintWriter out = new PrintWriter(clientSocket.getOutputStream());

        out.println(message);
        out.flush();
        //out.close();

    }
}
