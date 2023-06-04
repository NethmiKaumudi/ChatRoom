import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import resourse.controller.ServerFormController;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerMain {
    public static void main(String[] args) throws IOException {
//        launch(args);
        System.out.println("Server is start...........");
        ServerSocket serverSocket = new ServerSocket(4500);
//        System.out.println("Server is Running");
        ServerFormController server = new ServerFormController(serverSocket);
        server.runServer();
    }

//    @Override
//    public void start(Stage primaryStage) throws IOException {
//        primaryStage.setScene(new Scene(FXMLLoader
//                .load(this.getClass().getResource("/resourse/view/ServerForm1.fxml"))));
//        primaryStage.setTitle("PTCD CHAT ROOM");
//        primaryStage.centerOnScreen();
//        primaryStage.setResizable(false);
//        primaryStage.show();
//
//    }
}
