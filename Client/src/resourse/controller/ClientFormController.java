package resourse.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class ClientFormController {
    public static boolean isImageChoose = false;
    final int PORT = 4001;
    Socket socket;
    //    Socket imgSocket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    String message = "";
    int i = 10;
    String path = "";
    File file;
    @FXML
    private ScrollPane content;
    @FXML
    private AnchorPane pane;
    @FXML
    private VBox msgContent;
    @FXML
    private JFXTextField txtmessage;
    @FXML
    private Label lblClientName;
//    OutputStream imgOutputStream;
//    InputStream imgInputStream;
//    boolean isUsed = false;

    public void initialize() {
        Platform.setImplicitExit(false);
        content.setContent(msgContent);
//        content.vvalueProperty().bind(msgContent.heightProperty());
//        content.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
//        content.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        lblClientName.setText(LoginFormController.ClientName);
        new Thread(() -> {
            try {
                socket = new Socket("localhost", PORT);
                while (true) {
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    message = dataInputStream.readUTF();
                    System.out.println(message);

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if (message.startsWith("/")) {
                                BufferedImage sendImage = null;
                                try {
                                    sendImage = ImageIO.read(new File(message));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Image img = SwingFXUtils.toFXImage(sendImage, null);
                                ImageView imageView = new ImageView(img);
                                imageView.setFitHeight(150);
                                imageView.setFitWidth(150);
                                imageView.setLayoutY(i);
                                msgContent.getChildren().add(imageView);
                                i += 150;
                            } else if (message.startsWith(LoginFormController.ClientName)) {
                                message = message.replace(LoginFormController.ClientName, "You");
                                Label label = new Label(message);
                                label.setStyle(" -fx-font-family: Ubuntu; -fx-font-size: 20px; -fx-background-color: #85b6ff; -fx-text-fill: #5c5c5c");
                                label.setLayoutY(i);
                                msgContent.getChildren().add(label);
                            } else {
                                Label label = new Label(message);
                                label.setStyle(" -fx-font-family: Ubuntu; -fx-font-size: 20px; -fx-background-color: #CDB4DB; -fx-text-fill: #5c5c5c");
                                label.setLayoutY(i);
                                msgContent.getChildren().add(label);
                            }
                            i += 30;
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        txtmessage.setOnAction(event -> {
            try {
                sendMessage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    @FXML
    void btnSendOnAction(MouseEvent event) throws IOException {
        sendMessage();
    }

    @FXML
    void btnimageOnAction(MouseEvent event) {
        FileChooser chooser = new FileChooser();
        Stage stage = new Stage();
        file = chooser.showOpenDialog(stage);

        if (file != null) {
//            dataOutputStream.writeUTF(file.getPath());
            path = file.getPath();
            System.out.println("selected");
            System.out.println(file.getPath());
            isImageChoose = true;
        }
    }

    private void sendMessage() throws IOException {
        if (isImageChoose) {
            dataOutputStream.writeUTF(path.trim());
            dataOutputStream.flush();
            isImageChoose = false;
        } else {
            dataOutputStream.writeUTF(lblClientName.getText() + " : " + txtmessage.getText().trim());
            dataOutputStream.flush();
            System.out.println(txtmessage.getText());
        }
        txtmessage.clear();
    }

}
