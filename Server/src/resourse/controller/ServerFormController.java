package resourse.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import resourse.model.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerFormController {
    @FXML
    private AnchorPane pane;
    @FXML
    private VBox msgContent;
    @FXML
    private JFXTextField txtmessage;

    //    Server socket that accept the client and manage the data flow
    private ServerSocket serverSocket;

    public ServerFormController(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void runServer() {
        while (!(serverSocket.isClosed())) {
            try {
                //New connection client socket
                Socket socket = serverSocket.accept();
                System.out.println("new Client Connected");
                ClientHandler clientHandler = new ClientHandler(socket);
                //start a new thread for new connected client from this
                Thread thread = new Thread(clientHandler);
                thread.start();
            } catch (IOException e) {
                e.printStackTrace();
                closeServer();
            }
        }
    }

    //close server socket is in here
    private void closeServer() {
        try {
            //if server socket is not null then it have to close
            if (serverSocket != null) {
                serverSocket.close();
                System.out.println("Server Closed!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
