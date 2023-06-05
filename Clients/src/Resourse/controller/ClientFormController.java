package Resourse.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientFormController implements Initializable {
    public static String ClientName;
    int[] emojiIcons = {0x1F606, 0x1F601, 0x1F602, 0x1F609, 0x1F618, 0x1F610, 0x1F914, 0x1F642, 0x1F635, 0x1F696, 0x1F636, 0x1F980, 0x1F625, 0x1F634, 0x1F641, 0x1F643};
    @FXML
    private ScrollPane content;
    @FXML
    private AnchorPane ClientPane;
    @FXML
    private VBox msgContent;
    @FXML
    private JFXTextField txtmessage;
    @FXML
    private Label lblClientName;
    private Client client;
    @FXML
    private ScrollPane spEmojiIcons;
    @FXML
    private GridPane gpEmojiIcons;

    public static void receiveMessage(String message, VBox vBox) {
        HBox hBox = new HBox();
        hBox.setStyle("-fx-alignment: center-left;-fx-fill-height: true;-fx-min-height: 50;-fx-pref-width: 520;-fx-max-width: 520;-fx-padding: 10");
        Label messageLbl = new Label(message);
        messageLbl.setStyle("-fx-background-color:#E325F4;-fx-background-radius:15;-fx-font-size: 18;-fx-font-weight: normal;-fx-text-fill: white;-fx-wrap-text: true;-fx-alignment: center-left;-fx-content-display: left;-fx-padding: 10;-fx-max-width: 350;");
        hBox.getChildren().add(messageLbl);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBox);
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setEmojisToPane();

        spEmojiIcons.setVisible(false);
        spEmojiIcons.setStyle("-fx-background-color: purple");

//        if (ClientName != null){
        lblClientName.setText(ClientName);
//        }else {
//            System.err.println("Your Username Is Null.........");
//        }
        try {
            client = new Client(new Socket("localhost", 2200), ClientName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ClientPane.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                content.setVvalue((Double) newValue);
            }
        });
        client.listenForMessage(msgContent);
        client.clientSendMessage("");
//        txtmessage.setOnAction(event -> {
//            String message="";
//            sendMessage(message);
//                txtmessage.clear();
//
//        });

    }

    private void sendMessage(String message) {
//        this.message=message;
        HBox hBox = new HBox();
        hBox.setStyle("-fx-alignment: center-right;-fx-fill-height: true;-fx-min-height: 50;-fx-pref-width: 520;-fx-max-width: 520;-fx-padding: 10");
        Label messageLbl = new Label(message);
        messageLbl.setStyle("-fx-background-color:#9308A0;-fx-background-radius:15;-fx-font-size: 18;-fx-font-weight: normal;-fx-text-fill: white;-fx-wrap-text: true;-fx-alignment: center-left;-fx-content-display: left;-fx-padding: 10;-fx-max-width: 350;");
        hBox.getChildren().add(messageLbl);
        msgContent.getChildren().add(hBox);
    }


    private void setEmojisToPane() {
        int EMOJI_INDEX = 0;
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 4; i++) {
                Label text = new Label(new String(Character.toChars(emojiIcons[EMOJI_INDEX++])));
                text.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        txtmessage.appendText(text.getText());
                    }
                });
                text.setStyle("-fx-font-size: 25px;" +
                        " -fx-font-family: 'Noto Emoji';" +
                        "-fx-text-alignment: center;");
                gpEmojiIcons.add(text, i, j);
            }
        }
    }

    @FXML
    void btnSendOnAction(MouseEvent event) {
        spEmojiIcons.setVisible(false);
        String message = txtmessage.getText();

        if (!message.isEmpty()) {
            sendMessage(message);
            txtmessage.clear();
            client.clientSendMessage(message);
        }
    }

    @FXML
    void btnimageOnAction(MouseEvent event) throws IOException {

        FileChooser fileChooser = new FileChooser();
//        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image","*.jpg;*.png;*.jpeg;*.gif;"));
        fileChooser.setTitle("Select image to send.");
        File file = fileChooser.showOpenDialog(new Stage());
        System.out.println(file.getParent());
        BufferedImage bufferedImage = ImageIO.read(new File(file.getPath()));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        String fileName = file.getName();
        String extension = "";
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1);
        }

        ImageIO.write(bufferedImage, extension, bos);
        byte[] data = bos.toByteArray();
        client.clientSendImage(data, extension, file.getName());
    }

    @FXML
    void emojiOnAction(MouseEvent event) {
        if (spEmojiIcons.isVisible()) {
            spEmojiIcons.setVisible(false);
        } else {
            spEmojiIcons.setVisible(true);
            Text text = new Text(new String(Character.toChars(emojiIcons[4])));
            text.setStyle("-fx-font-size: 25px; -fx-font-family: 'Noto Emoji';");
            gpEmojiIcons.add(text, 0, 0);
        }
    }

}
