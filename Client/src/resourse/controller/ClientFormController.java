package resourse.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import resourse.model.Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientFormController {
    public static String ClientName;

    Socket socket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    String message = "";
    int[] emojiIcons = {0x1F606, 0x1F601, 0x1F602, 0x1F609, 0x1F618, 0x1F610, 0x1F914, 0x1F642, 0x1F635, 0x1F696, 0x1F636, 0x1F980, 0x1F625, 0x1F634, 0x1F641, 0x1F643,};
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
    private Client client;
    @FXML
    private ScrollPane spEmojiIcons;
    @FXML
    private GridPane gpEmojiIcons;

    public static void receiveMessage(String message, VBox vBox) {
        HBox hBox = new HBox();
        hBox.setStyle("-fx-alignment: center-left;-fx-fill-height: true;-fx-min-height: 50;-fx-pref-width: 520;-fx-max-width: 520;-fx-padding: 10");
        Label messageLbl = new Label(message);
        messageLbl.setStyle("-fx-background-color:   #E28AEA;-fx-background-radius:15;-fx-font-size: 18;-fx-font-weight: normal;-fx-text-fill: white;-fx-wrap-text: true;-fx-alignment: center-left;-fx-content-display: left;-fx-padding: 10;-fx-max-width: 350;");
        hBox.getChildren().add(messageLbl);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBox);
            }
        });
    }

    public void initialize() {
        setEmojisToPane();

        spEmojiIcons.setVisible(false);
        spEmojiIcons.setStyle("-fx-background-color: coral");

        lblClientName.setText(LoginFormController.ClientName);

        try {
            client = new Client(new Socket("localhost", 4500), ClientName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        pane.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                content.setVvalue((Double) newValue);
            }
        });
        client.listenForMessage(msgContent);
        client.clientSendMessage("");
        txtmessage.setOnAction(event -> {
            message = txtmessage.getText();
            sendMessage(message);
        });

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
                text.setStyle("-fx-font-size: 28px;" +
                        " -fx-font-family: 'Noto Emoji';" +
                        "-fx-text-alignment: center;");
                gpEmojiIcons.add(text, i, j);
            }
        }
    }

    @FXML
    void btnSendOnAction(MouseEvent event) throws IOException {
        spEmojiIcons.setVisible(false);
        message = txtmessage.getText();

        if (!message.isEmpty()) {
            sendMessage(message);
            txtmessage.clear();
            client.clientSendMessage(message);
        }
    }

    private void sendMessage(String message) {
        HBox hBox = new HBox();
        hBox.setStyle("-fx-alignment: center-right;-fx-fill-height: true;-fx-min-height: 50;-fx-pref-width: 520;-fx-max-width: 520;-fx-padding: 10");
        Label messageLbl = new Label(message);
        messageLbl.setStyle("-fx-background-color:#BF0ECE;-fx-background-radius:15;-fx-font-size: 18;-fx-font-weight: normal;-fx-text-fill: white;-fx-wrap-text: true;-fx-alignment: center-left;-fx-content-display: left;-fx-padding: 10;-fx-max-width: 350;");
        hBox.getChildren().add(messageLbl);
        msgContent.getChildren().add(hBox);
    }

    @FXML
    void btnimageOnAction(MouseEvent event) {

    }


    public void emojiOnAction(MouseEvent mouseEvent) {
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
