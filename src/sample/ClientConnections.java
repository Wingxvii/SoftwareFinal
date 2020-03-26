package sample;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

//class for storing users
public class ClientConnections {
    private Socket sock;
    private UserItem user;
    private ObjectOutputStream out = null;
    private ObjectInputStream in = null;

    public ClientConnections(Socket sock){
        this.sock = sock;
    }

    //accessors
    public Socket getSock(){return sock;}
    public UserItem getUser(){return user;}
    public ObjectOutputStream getOut(){return out;}
    public ObjectInputStream getIn(){return in;}

    //mutators
    public void setUser(UserItem user){this.user = user;}

    public void setOut(ObjectOutputStream out){this.out = out;}
    public void setIn(ObjectInputStream in){this.in = in;}


}
