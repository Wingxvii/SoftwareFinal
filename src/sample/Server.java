package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class Server  extends Application {
    private TextArea ta = new TextArea();
    private int clientCounter = 0;
    private Socket[] clients = new Socket[99];
    private DataOutputStream[] outputs = new DataOutputStream[99];

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(new ScrollPane(ta), 500, 200);
        stage.setScene(scene);
        stage.setTitle("Server");
        stage.show();

        new Thread( () -> {
            try {
                ServerSocket serverSocket = new ServerSocket(55555);
                ta.appendText("Server Started..." + '\n');

                while(true) {
                    Socket socket = serverSocket.accept();
                    clients[clientCounter] = socket;
                    outputs[clientCounter] = new DataOutputStream(clients[clientCounter].getOutputStream());
                    clientCounter++;

                    Platform.runLater(() -> {
                        ta.appendText(("Starting thread for client " + clientCounter + '\n'));

                        InetAddress inetAddress = socket.getInetAddress();
                        ta.appendText("Client " + clientCounter + "'s Name: " + inetAddress.getHostName() + " Address: " + inetAddress.getHostAddress() + '\n');

                    });
                    new Thread(new HandleClient(socket)).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    class HandleClient implements Runnable {
        private Socket socket;

        public HandleClient(Socket socket) {this.socket = socket;}

        @Override
        public void run() {
            try {
                DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());

                while(true) {

                    //int len = inputFromClient.readInt();
                    //ta.appendText("Len:" + len + '\n');
                    //byte[] data = new byte[len];
                    //inputFromClient.read(data);
                    //String dataOut = new String(data);

                    for (int i = 0; i < clientCounter; i++){
                        //if(clients[i] != socket) {
                        outputs[i].write(inputFromClient.read());
                            //outputs[i].writeInt(dataOut.length());
                            //outputs[i].writeBytes(dataOut);
                        ta.appendText("Sent to Client " + i + '\n');
                        //}
                    }

                    Platform.runLater(() -> {
                        ta.appendText("Data from " + socket.getInetAddress().getHostAddress() + '\n');
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
