package io.codeforall.javatars.server;

import java.net.Socket;

public class ClientHandler implements Runnable{

    Socket clientSocket = null;

    public ClientHandler(Socket clientSocket){
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {

    }
}
