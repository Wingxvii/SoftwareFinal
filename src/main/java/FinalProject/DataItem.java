package FinalProject;

import java.io.Serializable;
/*
Desc: Serializable Superclass for all packet objects, sent across network
Author: John Wang
Date: 3/26/2020

*/

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
