package Resourse.controller;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
    private static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    public String ClientName;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.ClientName = bufferedReader.readLine();
            clientHandlers.add(this);
            broadcastMessage(ClientName + " has Joined Chat...!");
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String message;
        while (socket.isConnected()) {
            try {
                message = bufferedReader.readLine();
                broadcastMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    public void removeClientHandler() {
        clientHandlers.remove(this);
        broadcastMessage(ClientName + "Left From Chat...!");
    }

    private void broadcastMessage(String sendMessage) {
        System.out.println("Sending Message : " + sendMessage);
        try {
            for (ClientHandler clientHandler : clientHandlers) {
                if (!clientHandler.ClientName.equals(ClientName)) ;
                clientHandler.bufferedWriter.write(sendMessage);
                clientHandler.bufferedWriter.newLine();
                clientHandler.bufferedWriter.flush();
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
            e.printStackTrace();
        }
    }

    private void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandler();
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
                System.out.println("A client left from chat...............");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

