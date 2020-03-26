package sample;

public class DataItem {
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
