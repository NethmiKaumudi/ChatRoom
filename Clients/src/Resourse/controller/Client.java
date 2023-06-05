package Resourse.controller;

import javafx.scene.layout.VBox;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class Client {
    public String ClientName;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public Client(Socket socket, String ClientName) {
        try {
            this.socket = socket;
            this.ClientName = ClientName;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            closeEverything(socket, bufferedWriter, bufferedReader);
        }
    }

    public void clientSendImage(byte[] imageArray, String format, String fileName) {
        String imageString = Arrays.toString(imageArray);
        try {
            bufferedWriter.write(imageString);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            closeEverything(socket, bufferedWriter, bufferedReader);
        }
    }

    public void clientSendMessage(String message) {
        try {
            bufferedWriter.write(ClientName + " : " + message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            closeEverything(socket, bufferedWriter, bufferedReader);
        }
    }

    public void listenForMessage(VBox vBox) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()) {
                    try {
                        String message = bufferedReader.readLine();
                        ClientFormController.receiveMessage(message, vBox);
                    } catch (IOException e) {
                        e.printStackTrace();
                        closeEverything(socket, bufferedWriter, bufferedReader);
                        break;
                    }
                }
            }
        }).start();
    }

    //in here Terminate socket, reader and writer
    public void closeEverything(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
