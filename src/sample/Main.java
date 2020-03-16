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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;

public class Main extends Application {

    boolean connected = false;

    UserItem self;
    ArrayList<UserItem> allUsers = new ArrayList<>();
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
    //menu
    MenuBar menubar = new MenuBar();
    //user table
    TableView userTable = new TableView();

    //main scene
    Scene masterScene = new Scene(masterPane);
    //connect scene
    Stage newWindow = new Stage();


    @Override
    public void start(Stage primaryStage) throws Exception{

        //Menu
        Menu menuFile = new Menu("File");
        Menu statusMenu = new Menu("Status");

        MenuItem menuSave = new MenuItem("Save");
        MenuItem menuExit = new MenuItem("Exit");

        MenuItem statusActive = new MenuItem("Active");
        MenuItem statusBusy = new MenuItem("Busy");

        statusMenu.getItems().addAll(statusActive,statusBusy);
        menuFile.getItems().addAll(menuSave,menuExit );
        menubar.getMenus().addAll(menuFile, statusMenu);

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
                if(connected) {
                    ChatItem chat = new ChatItem(new Label(textInput.getText()), self);
                    chat.SetupText(true);
                    textInput.clear();
                    items.add(chat);

                    chatList.getChildren().add(chat.nodeItem);
                    //call send packet
                    SendMessage(textInput.getText(), self);
                }
            }
        });

        imageButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                if(connected) {
                    //TODO: setup file database here...
                }
            }
        });

        //menus
        menuSave.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("CSV Files", "*.csv");
                fileChooser.getExtensionFilters().add(filter);

                File selectedFile = fileChooser.showSaveDialog(null);
                try{
                    String fileName = selectedFile.getAbsolutePath();
                    FileWriter writer = null;

                    try {
                        writer = new FileWriter(fileName);

                        for (ChatItem chat : items) {
                            switch (chat.type){
                                case CHATTEXT:
                                    writer.append(chat.userParent.Username + ",");
                                    writer.append(chat.text);

                                    break;
                                case CHATFILE:
                                    //TODO: EXPORT FILE LINK
                                    break;
                            }

                            writer.append("\n");
                        }

                        writer.flush();
                        writer.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }catch (NullPointerException e){

                }

            }
        });
        menuExit.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                UserUpdate(self.getUsername(), "Offline");
                System.exit(0);
            }
        });
        statusActive.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                UserUpdate(self.getUsername(), "Active");
            }
        });
        statusBusy.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                UserUpdate(self.getUsername(), "Busy");
            }
        });

        //add everything
        messageInput.getChildren().addAll(textInput,sendButton,imageButton);

        masterPane.setBottom(messageInput);
        masterPane.setLeft(userTable);
        masterPane.setCenter(chatLog);
        masterPane.setTop(menubar);

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Chat");
        primaryStage.setScene(masterScene);
        primaryStage.show();


        //setup the login data
        SetupLogin();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public void SetupLogin(){


        Label usernameLabel = new Label("Username:");
        Label ipLabel = new Label("IP Address:");

        TextField usernameText = new TextField();
        TextField ipText = new TextField();

        Button confirm = new Button("Confirm");

        GridPane gridPane = new GridPane();
        gridPane.add(usernameLabel, 0,0);
        gridPane.add(ipLabel, 0,1);
        gridPane.add(usernameText, 1,0);
        gridPane.add(ipText, 1,1);
        gridPane.add(confirm,2,0);

        Label connectionFailed = new Label("Connection Failed");
        connectionFailed.setTextFill(Color.RED);

        confirm.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                boolean connection = true;
                //TODO: check connection


                if(connection){
                    //TODO: setup initial chatroom data

                    if(usernameText.getText().isEmpty()){
                        usernameText.setText("Nameless");

                    }

                    self = new UserItem(usernameText.getText(), "Active");
                    allUsers.add(self);
                    UpdateUserList();
                    connected = true;
                    newWindow.close();
                }else{
                    gridPane.add(connectionFailed,2,1 );

                }

            }
        });

        Scene secondScene = new Scene(gridPane, 350, 100);

        // New window (Stage)
        newWindow.setTitle("Connect");
        newWindow.setScene(secondScene);

        newWindow.show();


    }

    public void SendMessage(String text, UserItem user){
        //TODO: Send message data

    }

    //TODO: Call this for incomming messages
    public void RecieveMessage(String text, UserItem user){
        ChatItem chat = new ChatItem(new Label(text), user);
        chat.SetupText(false);
        items.add(chat);

        chatList.getChildren().add(chat.nodeItem);
    }

    //TODO: Recieve a file hyperlink
    public void RecieveFile() {}

    //TODO: Send a file hyperlink
    public void SendFile() {}

    //TODO: Call this for incomming connections
    public void UserUpdate(String username, String status){
        boolean found = false;

        for(UserItem user : allUsers){
            if(user.Username.matches(username)){
                user.Status = status;
                UpdateUserList();

                //call send packet
                if(user.Username.matches(self.getUsername())){
                    SendUserUpdate(self.getUsername(), self.getStatus());
                }
                found = true;
            }
        }

        if(!found){
            UserItem newUser = new UserItem(username, status);
            allUsers.add(newUser);
            UpdateUserList();

        }

    }
    //TODO: Send this for outgoing updates
    public void SendUserUpdate(String username, String status){


    }

    public void UpdateUserList(){
        userTable.getItems().clear();
        for(UserItem user : allUsers){
            userTable.getItems().add(user);
        }

    }

}
