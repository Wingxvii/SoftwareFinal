package sample;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

public class ChatItem {
    UserItem userParent;
    Node nodeItem;

    ChatItem(Node nodeItem, UserItem userParent){
        this.nodeItem = nodeItem;
        this.userParent = userParent;
    }

    public void SetupText(){
        ((Label)nodeItem).setText(userParent.Username + ": " + ((Label)nodeItem).getText());
        ((Label)nodeItem).setFont(new Font(18));

    }

    public Node getNodeItem() {
        return nodeItem;
    }

    public UserItem getUserParent() {
        return userParent;
    }
}
