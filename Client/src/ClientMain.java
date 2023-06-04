import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientMain extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/resourse/view/loginForm.fxml"))));
        primaryStage.setTitle("Login");
        primaryStage.setResizable(false);
        primaryStage.setMaximized(false);
        primaryStage.show();


    }
}