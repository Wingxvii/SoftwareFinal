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

    TextChatItem(Node nodeItem, UserItem userParent) {
        super(nodeItem, userParent);
        this.type = ItemType.CHATTEXT;
    }

    public void Setup(boolean self)
    {
        ((Label)nodeItem).setFont(new Font(18));
        ((Label) nodeItem).setMinWidth(600);
        type = ItemType.CHATTEXT;
        text = ((Label)nodeItem).getText();

        if(self) {
            ((Label)nodeItem).setText(((Label)nodeItem).getText() + " - " + userParent.Username);
            ((Label) nodeItem).setTextFill(Color.GREEN);

            ((Label) nodeItem).setAlignment(Pos.BASELINE_RIGHT);
            ((Label) nodeItem).setTextAlignment(TextAlignment.RIGHT);
        }else{
            ((Label)nodeItem).setText(userParent.Username + " - " + ((Label)nodeItem).getText());
            ((Label) nodeItem).setTextFill(Color.BLUE);

            ((Label) nodeItem).setAlignment(Pos.BASELINE_LEFT);
            ((Label) nodeItem).setTextAlignment(TextAlignment.LEFT);
        }
    }

    public String getText(){
        return text;
    }


}
