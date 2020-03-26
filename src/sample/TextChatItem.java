package sample;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class TextChatItem extends ChatItem{
    //text representation of data
    String text;

    TextChatItem(String text, UserItem userParent) {
        super( userParent);
        this.type = ItemType.CHATTEXT;
        this.text = text;
    }

    public String getText(){
        return text;
    }


}
