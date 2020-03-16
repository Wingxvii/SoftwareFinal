package sample;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Main extends Application {

    UserItem self;
    ArrayList<ChatItem> items = new ArrayList<>();

    //master container pane
    BorderPane masterPane = new BorderPane();
    //bottom chat input
    HBox messageInput = new HBox();
    //text input
    TextField textInput = new TextField();
    //send button
    Button sendButton = new Button("Send");
    //chat pane
    VBox chatList = new VBox();
    ScrollPane chatLog = new ScrollPane(chatList);
    //file transfer button
    Button imageButton = new Button("File..");


    Scene masterScene = new Scene(masterPane);

    @Override
    public void start(Stage primaryStage) throws Exception{

        self = new UserItem("SelfUserName", "Online");

        //setup format
        textInput.setMinSize(750,50);
        textInput.setFont(new Font(12));
        textInput.setPromptText("Send Message...");

        sendButton.setMinSize(100,50);
        sendButton.setFont(new Font(20));

        imageButton.setMinSize(70,50);
        imageButton.setFont(new Font(14));

        chatLog.setFitToHeight(true);
        //auto scroll to bottom
        chatList.heightProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldvalue, Object newValue) {
                chatLog.setVvalue((Double)newValue);
            }
        });

        //user table
        TableView userTable = new TableView();
        TableColumn<String,String> usernameDisplay = new TableColumn<>("Username");
        usernameDisplay.setCellValueFactory(new PropertyValueFactory<>("Username"));
        usernameDisplay.setResizable(false);
        usernameDisplay.setPrefWidth(250);
        TableColumn<String,String> statusDisplay = new TableColumn<>("Status");
        statusDisplay.setCellValueFactory(new PropertyValueFactory<>("Status"));
        statusDisplay.setResizable(false);
        statusDisplay.setPrefWidth(50);
        userTable.getColumns().addAll(usernameDisplay, statusDisplay);
        userTable.getItems().add(self);
        userTable.setEditable(false);

        //setup functionality
        sendButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                ChatItem chat = new ChatItem(new Label(textInput.getText()), self);
                chat.SetupText(true);
                textInput.clear();
                items.add(chat);

                chatList.getChildren().add(chat.nodeItem);
            }
        });

        imageButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                //TODO: setup file database here...


            }
        });

        //add everything
        messageInput.getChildren().addAll(textInput,sendButton,imageButton);

        masterPane.setBottom(messageInput);
        masterPane.setLeft(userTable);
        masterPane.setCenter(chatLog);

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Chat");
        primaryStage.setScene(masterScene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
