package FinalProject;

import java.io.Serializable;

public class DataItem implements Serializable {
    public enum ItemType{
        USERITEM,
        CHATTEXT,
        CHATIMAGE,
    }

    protected ItemType type;
    DataItem(){}
    DataItem(DataItem copy){
        type = copy.getType();
    }

    public ItemType getType(){
        return type;
    }


}
