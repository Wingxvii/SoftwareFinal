package sample;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;


//class used to storing chat log instances
public class ChatItem {
    //user who sent data
    UserItem userParent;
    //node representation of data
    Node nodeItem;
    //data type
    ItemType type;
    //text representation of data
    String text;

    public enum ItemType{
        CHATTEXT,
        CHATFILE,

    }

    //constructor
    ChatItem(Node nodeItem, UserItem userParent){
        this.nodeItem = nodeItem;
        this.userParent = userParent;
    }

    //setup text nodes
    public void SetupText(boolean self){
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
    //setup file nodes
    public void SetupFile(boolean self){
        //TODO: SETUP file download hyperlink
        type = ItemType.CHATFILE;

    }

    //accessors
    public Node getNodeItem() {
        return nodeItem;
    }

    public UserItem getUserParent() {
        return userParent;
    }

    public ItemType getType(){
        return type;
    }
    public String getText(){
        return text;
    }
}
