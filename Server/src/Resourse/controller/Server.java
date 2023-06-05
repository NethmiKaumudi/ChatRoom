package Resourse.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    //    Server socket that accept the client and manage the data flow
    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void runServer() {
        while (!(serverSocket.isClosed())) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("New Client Connected");
                Thread thread = new Thread(new ClientHandler(socket));
                thread.start();
            } catch (IOException e) {
                e.printStackTrace();
                closeServer();
            }
        }
    }

    private void closeServer() {

        try {
            if (serverSocket != null) {
                serverSocket.close();
                System.out.println("Server Closed.........!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
