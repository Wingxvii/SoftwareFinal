package FinalProject;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
/*
Desc: Class used to store a single text chat instance for transfer through network
Author: John Wang
Date: 3/26/2020
*/

public class TextChatItem extends ChatItem{
    //text representation of data
    String text;

    //constructor
    TextChatItem(String text, UserItem userParent) {
        super( userParent);
        this.type = ItemType.CHATTEXT;
        this.text = text;
    }

    //accessor
    public String getText(){
        return text;
    }


}
