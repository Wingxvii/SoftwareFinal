package FinalProject;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
/*
Desc:  Class for storing connected users
Author: John Wang
Date: 3/26/2020
*/

public class ClientConnections {

    //socket for client
    private Socket sock;

    //user item linked
    private UserItem user;

    //socket streams
    private ObjectOutputStream out = null;
    private ObjectInputStream in = null;

    //constructor
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
