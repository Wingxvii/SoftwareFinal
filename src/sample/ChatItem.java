package sample;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;


//class used to storing chat log instances
public abstract class ChatItem extends DataItem{
    //user who sent data
    UserItem userParent;
    //node representation of data
    Node nodeItem;

    //constructor
    ChatItem(Node nodeItem, UserItem userParent){
        this.nodeItem = nodeItem;
        this.userParent = userParent;
    }

    //setup text nodes
    public abstract void Setup(boolean self);

    //accessors
    public Node getNodeItem() {
        return nodeItem;
    }

    public UserItem getUserParent() {
        return userParent;
    }

}
