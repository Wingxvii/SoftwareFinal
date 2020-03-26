package FinalProject;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MainProject extends Application {

    //reference to self user item
    UserItem self;

    //lists of all users
    ArrayList<UserItem> allUsers = new ArrayList<>();

    //list of all chat items
    ArrayList<ChatItem> items = new ArrayList<>();

    //region UI
    //master container pane
    BorderPane masterPane = new BorderPane();
    //bottom chat input
    HBox messageInput = new HBox();
    //text input
    TextArea textInput = new TextArea();
    //send button
    Button sendButton = new Button("Send");
    //chat pane
    VBox chatList = new VBox();
    ScrollPane chatLog = new ScrollPane(chatList);
    //file transfer button
    Button imageButton = new Button("Load Image");
    //menu
    MenuBar menubar = new MenuBar();
    //user table
    TableView userTable = new TableView();

    //endregion

    //region Scene
    //main scene
    Scene masterScene = new Scene(masterPane);
    //connect scene
    Stage newWindow = new Stage();
    //endregion

    //region Menu
    //Menu
    Menu menuFile = new Menu("File");
    Menu statusMenu = new Menu("Status");

    MenuItem menuSave = new MenuItem("Save");
    MenuItem menuExit = new MenuItem("Exit");

    MenuItem statusActive = new MenuItem("Active");
    MenuItem statusBusy = new MenuItem("Busy");
    //endregion

    //region Network
    //Network IO
    boolean isHost = false;
    ArrayList<ClientConnections> allUserConnections = new ArrayList<>();


    boolean connected = false;

    ObjectInputStream in = null;
    ObjectOutputStream out = null;
    //endregion

    //region Style
    // Styling presets
    String bgStyles =
            "-fx-background-color: rgba(240, 255, 240, 20);";

    String btStyles =
            "-fx-border-color: rgba(0, 0, 0, 0);" +
            "-fx-border-width: 1px;" +
            //"-fx-border-radius: 10;" +
            "-fx-background-color: #7777ff;" +
            "-fx-text-fill: rgba(230, 230, 230, 255);" +
            "-fx-font-family: 'Arial';" +
            "-fx-font-weight: BOLD;" +
            "-fx-font-size: 16px;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 10 10 10 10;"
            ;

    String labelStyles =
            "-fx-font-family: 'Arial';" +
                    "-fx-font-size: 12px;"
            ;

    String activeUserStyles =
            "-fx-background-color: #55ff55;"

            ;

    String promptStyles =
            "-fx-font-family: 'Arial';" +
                    "-fx-font-size: 16px;"
            ;

    String textStyles =
            "-fx-font-family: 'Arial';"
            ;

    //endregion

    @Override
    public void start(Stage primaryStage) throws Exception{

        //setup format
        textInput.setStyle(promptStyles);
        textInput.setMinSize(600,50);
        textInput.setPrefSize(600, 50);
        textInput.setPromptText("Send Message...");
        textInput.setWrapText(true);
        textInput.setDisable(true);
        textInput.setStyle("-fx-border-radius: 10 10 10 10;");

        sendButton.setStyle(btStyles);
        sendButton.setMinSize(100,50);
        sendButton.setDisable(true);
        Tooltip.install(sendButton, new Tooltip("Send Message"));

        imageButton.setStyle(btStyles);
        imageButton.setMinSize(120,50);
        imageButton.setDisable(true);
        Tooltip.install(imageButton, new Tooltip("Send Image File"));

        chatLog.setFitToHeight(true);
        chatLog.setDisable(true);
        //auto scroll to bottom
        chatList.heightProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                chatLog.setVvalue((Double)newValue);
            }
        });
        chatList.setSpacing(5);
        chatList.setPadding(new Insets(5));

        //user table
        TableColumn<String,String> usernameDisplay = new TableColumn<>("Username");
        usernameDisplay.setCellValueFactory(new PropertyValueFactory<>("Username"));
        usernameDisplay.setResizable(false);
        usernameDisplay.setReorderable(false);
        usernameDisplay.setSortable(false);
        usernameDisplay.setPrefWidth(200);
        TableColumn<String,String> statusDisplay = new TableColumn<>("Status");
        statusDisplay.setCellValueFactory(new PropertyValueFactory<>("Status"));
        statusDisplay.setResizable(false);
        statusDisplay.setReorderable(false);
        statusDisplay.setPrefWidth(50);
        userTable.getColumns().addAll(usernameDisplay, statusDisplay);
        userTable.getItems().add(self);
        userTable.setEditable(false);
        userTable.setDisable(true);
        userTable.setStyle(textStyles);

        //save option
        menuSave.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                //choose file to save to
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("CSV Files", "*.csv");
                fileChooser.getExtensionFilters().add(filter);

                File selectedFile = fileChooser.showSaveDialog(null);
                try{
                    //open file writer
                    String fileName = selectedFile.getAbsolutePath();
                    FileWriter writer = null;

                    try {
                        writer = new FileWriter(fileName);

                        //save each chat item
                        for (ChatItem chat : items) {
                            switch (chat.type){
                                case CHATTEXT:
                                    writer.append(chat.getUserParent().getUsername() + ",");
                                    writer.append(((TextChatItem)chat).getText());

                                    break;
                                case CHATIMAGE:
                                    writer.append(chat.getUserParent().getUsername() + ",");
                                    writer.append(((ImageChatItem)chat).getImageName());

                                    break;

                                default:
                                    break;
                            }

                            writer.append("\n");
                        }

                        //close writer
                        writer.flush();
                        writer.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }catch (NullPointerException e){
                    //if user cancels
                }
            }
        });

        //add everything
        statusMenu.getItems().addAll(statusActive,statusBusy);
        menuFile.getItems().addAll(menuSave,menuExit );
        menubar.getMenus().addAll(menuFile, statusMenu);
        menuFile.setDisable(true);
        statusMenu.setDisable(true);

        messageInput.getChildren().addAll(imageButton,textInput,sendButton);
        messageInput.setSpacing(5);
        messageInput.setPadding(new Insets(5));

        masterPane.setPadding(new Insets(0,5,5,5));

        masterPane.setTop(menubar);
        masterPane.setLeft(userTable);
        masterPane.setCenter(chatLog);
        masterPane.setBottom(messageInput);
        masterPane.setStyle(bgStyles);

        //Parent root = FXMLLoader.load(getClass().getResource(System.getProperty("user.dir") + "/src/main/resources/FinalProject.fxml"));
        primaryStage.setTitle("Chat");
        primaryStage.setScene(masterScene);
        primaryStage.show();

        //setup the login data
        SetupLogin();
    }

    public static void main(String[] args) {
        launch(args);
    }

    //setup login window
    public void SetupLogin(){
        // Setting up the field and text
        Label usernameLabel = new Label("Username:");
        usernameLabel.setStyle(labelStyles);

        Label ipLabel = new Label("IP Address:");
        ipLabel.setStyle(labelStyles);

        Label connectionFailed = new Label("Connection Failed");
        connectionFailed.setStyle(labelStyles);
        connectionFailed.setVisible(false);
        connectionFailed.setTextFill(Color.RED);

        CheckBox isHostButton = new CheckBox("Host");
        isHostButton.setStyle(labelStyles);
        Tooltip.install(isHostButton, new Tooltip("Host the chatroom"));

        TextField usernameText = new TextField();
        TextField ipText = new TextField();

        isHostButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    ipText.setDisable(true);
               }else{
                    ipText.setDisable(false);

                }
            }
        });

        GridPane connectionInputs = new GridPane();
        connectionInputs.setPadding(new Insets(10,10,10,10));
        connectionInputs.setHgap(5);
        connectionInputs.setVgap(5);
        connectionInputs.add(usernameLabel, 0, 0);
        connectionInputs.add(ipLabel, 0,1);
        connectionInputs.add(usernameText, 1,0);
        connectionInputs.add(ipText, 1,1);
        connectionInputs.add(isHostButton, 0,2);
        connectionInputs.add(connectionFailed,1,2 );

        Button connectButton = new Button("Connect");
        connectButton.setStyle(btStyles);
        connectButton.setMinSize(100, 55);
        connectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {

                boolean connection = false;

                //check connection
                try {
                    if(!isHostButton.isSelected()){
                        //connect to the server
                        Socket soc = new Socket(ipText.getText() , 6654);
                        //create input and output streams
                        in = new ObjectInputStream(soc.getInputStream());
                        out = new ObjectOutputStream(soc.getOutputStream());

                        //start a thread to process host data
                        new Thread(()-> {
                            while (true){
                                try {
                                    DataItem newItem = (DataItem)in.readObject();

                                    //schedule data process
                                    Platform.runLater(() -> {
                                        //process data
                                        switch (newItem.getType()) {
                                            case USERITEM:
                                                //process user item update
                                                UserUpdate((UserItem) newItem);
                                                break;
                                            case CHATTEXT:
                                                ReceiveMessage((TextChatItem) newItem);
                                                break;
                                            case CHATIMAGE:
                                                ReceiveImage((ImageChatItem) newItem);
                                                break;

                                            default:
                                                break;
                                        }
                                    });
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                } catch (ClassNotFoundException ex) {
                                    ex.printStackTrace();
                                }
                            }


                        }).start();
                    }
                    else{
                        ServerSocket soc = new ServerSocket(6654);
                        //thread connection handler
                        new Thread(()-> {
                        while(true) {
                            try {
                                Socket client = soc.accept();
                                HandleConnection(new ClientConnections(client));

                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }

                        }
                        }).start();
                    }
                    //set connection to be true
                    connection = true;

                }catch (IOException ex){
                    //set connection to be false
                    connection = false;
                    System.out.println("Connection Failed");
                }

                if(connection){
                    //TODO: setup initial chatroom data

                    if(usernameText.getText().isEmpty()){
                        usernameText.setText("Nameless");
                    }

                    self = new UserItem(usernameText.getText(), "Active");
                    allUsers.add(self);

                    isHost = isHostButton.isSelected();

                    if(!isHost){
                        SendData(self);
                    }

                    UpdateUserList();
                    connected = true;
                    enableFunctionality();
                    newWindow.close();
                }else{
                    connectionFailed.setVisible(true);
                }

            }
        });

        GridPane connectGrid = new GridPane();
        connectGrid.setPadding(new Insets(10,10,10,10));
        connectGrid.setHgap(5);
        connectGrid.setVgap(5);
        connectGrid.add(connectButton, 0, 0);

        HBox connectionGrid = new HBox();
        connectionGrid.setAlignment(Pos.CENTER);
        connectionGrid.getChildren().addAll(connectionInputs, connectGrid);

        //open connection window
        Scene secondScene = new Scene(connectionGrid, 400, 110);

        newWindow.setTitle("Connection");
        newWindow.setScene(secondScene);
        newWindow.show();
    }

    //updates user list node
    public void UpdateUserList(){
        userTable.getItems().clear();
        for(UserItem user : allUsers){
            userTable.getItems().add(user);
        }
    }

    //enable functionality for connection
    public void enableFunctionality(){
        sendButton.setDisable(false);
        imageButton.setDisable(false);
        menuFile.setDisable(false);
        statusMenu.setDisable(false);
        textInput.setDisable(false);
        userTable.setDisable(false);
        chatLog.setDisable(false);

        //setup functionality
        sendButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                if(connected) {
                    TextChatItem chat = new TextChatItem(textInput.getText(), self);
                    textInput.clear();
                    items.add(chat);

                    chatList.getChildren().add(SetupText(chat, true));
                    //call send packet
                    if(isHost){
                        Relay(chat);
                    }else {
                        SendData(chat);
                    }
                }
            }
        });

        //added enter submission
        textInput.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent ke)
            {
                if (ke.getCode().equals(KeyCode.ENTER))
                {
                    if(connected) {
                        TextChatItem chat = new TextChatItem(textInput.getText(), self);
                        textInput.clear();
                        items.add(chat);

                        chatList.getChildren().add(SetupText(chat, true));
                        //call send packet
                        if(isHost){
                            Relay(chat);
                        }else {
                            SendData(chat);
                        }
                    }
                }
            }
        });

        imageButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                if(connected) {
                    FileChooser fileChooser = new FileChooser();
                    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files (.png, .jpg)", "*.png", "*.jpg");
                    fileChooser.getExtensionFilters().add(extFilter);

                    File selectedFile = fileChooser.showOpenDialog(null);

                    try {
                            BufferedImage img = ImageIO.read(selectedFile);
                            ImageChatItem chat = new ImageChatItem(img,selectedFile.getName(),self);
                            items.add(chat);

                            chatList.getChildren().add(SetupImage(chat, true));
                            //call send packet
                            if(isHost){
                                Relay(chat);
                            }else {
                                SendData(chat);
                            }

                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                }
            }
        });

        menuExit.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                self.setStatus("Offline");
                UserUpdate(self);
                try {
                    if(isHost){
                        for(ClientConnections connection : allUserConnections){
                            connection.getIn().close();
                            connection.getOut().close();
                            connection.getSock().close();
                        }
                    }else {
                        in.close();
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.exit(0);
            }
        });
        statusActive.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                self.setStatus("Active");
                if(isHost){
                    UserUpdate(self,null);
                }else{
                    UserUpdate(self);
                }
            }
        });
        statusBusy.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                self.setStatus("Busy");
                if(isHost){
                    UserUpdate(self,null);
                }else{
                    UserUpdate(self);
                }
            }
        });

    }

    //process incoming messages
    public void ReceiveMessage(TextChatItem chat){
        items.add(chat);

        chatList.getChildren().add(SetupText(chat, false));
    }

    //process incoming images
    public void ReceiveImage(ImageChatItem chat){
        items.add(chat);

        chatList.getChildren().add(SetupImage(chat, false));
    }


    public HBox SetupText (TextChatItem item, boolean self){
        Label returnLabel = new Label(item.getText());
        returnLabel.setStyle(labelStyles);
        returnLabel.setAlignment(Pos.BASELINE_LEFT);
        returnLabel.setTextAlignment(TextAlignment.LEFT);
        returnLabel.setText(": " + (returnLabel).getText());

        Label returnLabel2 = new Label(item.getUserParent().getUsername());
        returnLabel2.setStyle(labelStyles);
        returnLabel2.setStyle("-fx-Font-Weight: BOLD;");

        if(self) {
            returnLabel.setTextFill(Color.GREEN);
            returnLabel2.setTextFill(Color.GREEN);
        }else{
            returnLabel.setTextFill(Color.BLUE);
            returnLabel2.setTextFill(Color.BLUE);
        }

        HBox newBox = new HBox();
        newBox.setMinWidth(560);
        newBox.setAlignment(Pos.BASELINE_LEFT);
        newBox.getChildren().addAll(returnLabel2,returnLabel);

        return newBox;
    }

    public BorderPane SetupImage(ImageChatItem item, boolean self){
        ImageView returnLabel = new ImageView();
        returnLabel.setImage(SwingFXUtils.toFXImage(item.getImage(), null));
        returnLabel.setPreserveRatio(true);
        returnLabel.setFitWidth(300);
        Tooltip.install(returnLabel, new Tooltip("Right Click to Save"));

        BorderPane alignment = new BorderPane();

        //context menu for image saving
        ContextMenu menu = new ContextMenu();

        MenuItem save = new MenuItem("Save Image");
        save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //save
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Image Files", "*.png");
                fileChooser.getExtensionFilters().add(filter);

                File selectedFile = fileChooser.showSaveDialog(null);
                try {
                    ImageIO.write(item.getImage(), "png", selectedFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        menu.getItems().addAll(save);
        returnLabel.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {

            @Override
            public void handle(ContextMenuEvent event) {

                menu.show(returnLabel, event.getScreenX(), event.getScreenY());
            }
        });

        Label returnLabel2 = new Label(item.getUserParent().getUsername() + ": " + item.getImageName());
        returnLabel2.setStyle("-fx-Font-Weight: BOLD;");

        if(self) {
            returnLabel2.setTextFill(Color.GREEN);
        }else {
            returnLabel2.setTextFill(Color.BLUE);
        }

        alignment.setTop(returnLabel2);
        alignment.setLeft(returnLabel);
        returnLabel2.setTextAlignment(TextAlignment.LEFT);

        return alignment;
    }

    //region ClientFunctionality
    //all functions called by clients
    //TODO: Send message data to host
    public void SendData(DataItem item){
        try {
            out.writeObject(item);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Sets up and updates incoming user updates and connections
    public void UserUpdate(UserItem user){
        boolean found = false;

        for(UserItem CurrUser : allUsers){
            if(CurrUser.getUsername().matches(user.getUsername())){
                CurrUser.setStatus(user.getStatus());
                UpdateUserList();

                //call send packet
                if(CurrUser.getUsername().matches(self.getUsername())){
                    SendData(new UserItem(self));
                }
                found = true;
            }
        }
        if(!found){
            allUsers.add(user);
            UpdateUserList();
        }
    }
    //endregion

    //region HostFunctionality
    //all functionality called by host

    //handles connection of new clients
    public void HandleConnection(ClientConnections connection){

        allUserConnections.add(connection);

        //start a listening thread
        new Thread(()->{
            try{
                //setup input and output
                connection.setOut(new ObjectOutputStream(connection.getSock().getOutputStream()));
                connection.setIn(new ObjectInputStream(connection.getSock().getInputStream()));

                //data reception
                while(true){
                    DataItem newItem = (DataItem) connection.getIn().readObject();

                    //schedule process
                    Platform.runLater(() -> {
                        //call relay
                        Relay(newItem, connection);

                        //process data
                        switch (newItem.getType()) {
                            case USERITEM:
                                //process user item update
                                connection.setUser((UserItem) newItem);
                                UserUpdate((UserItem) newItem, connection);
                                break;
                            case CHATTEXT:
                                ReceiveMessage((TextChatItem) newItem);
                                break;
                            case CHATIMAGE:
                                ReceiveImage((ImageChatItem) newItem);
                                break;
                            default:
                                break;

                        }
                    });
                }

            }catch(IOException ex) {
                ex.printStackTrace();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }

    //host targeted data send
    public void SendData(DataItem data, ClientConnections recipient){
        try {
            recipient.getOut().writeObject(data);
            recipient.getOut().flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Transfers packets to all connected users except 1 (ONLY CALLED IF HOST)
    public void Relay(DataItem item, ClientConnections sentClient){
        for(ClientConnections connection : allUserConnections){
            if(sentClient != connection){
                SendData(item, connection);
            }
        }
    }
    //Transfers packets to all connected users (ONLY CALLED IF HOST)
    public void Relay(DataItem item){
        for(ClientConnections connection : allUserConnections){
            SendData(item, connection);
        }
    }
    //host user update that checks for user initialization
    public void UserUpdate(UserItem user, ClientConnections connection){
        boolean found = false;

        for(UserItem CurrUser : allUsers){
            if(CurrUser.getUsername().matches(user.getUsername())){
                CurrUser.setStatus(user.getStatus());
                UpdateUserList();

                //call send packet
                if(CurrUser.getUsername().matches(self.getUsername())){
                    Relay(new UserItem(self));
                }
                found = true;
            }
        }
        if(!found && connection != null){
            allUsers.add(user);
            UpdateUserList();
            SendInitData(connection);
        }
    }
    //sends data logs to player
    public void SendInitData(ClientConnections connection){
        for(UserItem user :allUsers ){
            SendData(user, connection);
        }
        for(ChatItem chat : items){
            SendData(chat, connection);
        }
    }

    //endregion
}
