package io.codeforall.javatars.client;

import java.io.BufferedReader;
import java.io.IOException;

public class ClientReader implements Runnable{

    BufferedReader inputServerChatReader;

    public ClientReader(BufferedReader inputServerChatReader){
        this.inputServerChatReader = inputServerChatReader;
    }

    public void readServerMessage(){

        try {
            String line = "";
            String message = "";
            while ((line = inputServerChatReader.readLine()) != "\n"){
                System.out.println(line);
                message += line;
            }
            System.out.println(message);


        } catch (IOException ex) {

            System.out.println("Sending error: " + ex.getMessage() + ", closing client...");

        }

    }

    @Override
    public void run() {
        readServerMessage();
    }
}
