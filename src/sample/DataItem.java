package sample;

import java.io.Serializable;

public class DataItem implements Serializable {
    public enum ItemType{
        USERITEM,
        CHATTEXT,
        CHATFILE,
    }

    protected ItemType type;
    DataItem(){}

    public ItemType getType(){
        return type;
    }


}
