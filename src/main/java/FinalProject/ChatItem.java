package FinalProject;

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

    //constructor
    ChatItem(UserItem userParent){
        this.userParent = userParent;
    }

    public UserItem getUserParent() {
        return userParent;
    }

}
