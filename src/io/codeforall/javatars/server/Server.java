package io.codeforall.javatars.server;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {

        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(8080);
            System.out.println("Server running at " + serverSocket.getInetAddress().getHostAddress() + ":" + serverSocket.getLocalPort());

            while (true){
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client accepted: " + clientSocket.getRemoteSocketAddress());
                Thread thread = new Thread(new ClientHandler(clientSocket));
                thread.start();
            }


        } catch (Exception e) {
            System.err.println("Could not create server Socket: " + e.getMessage());
        }

        finally {
            if (serverSocket != null && !serverSocket.isClosed()) {
                try { serverSocket.close(); } catch (Exception ignored) { }
            }
        }
    }
}
