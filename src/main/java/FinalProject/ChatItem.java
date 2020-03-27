package FinalProject;

/*
Desc: Abstract class used to storing chat log instances
Author: John Wang
Date: 3/26/2020
*/

public abstract class ChatItem extends DataItem{
    //user who sent data
    private UserItem userParent;

    //constructor
    ChatItem(UserItem userParent){
        this.userParent = userParent;
    }

    //accessor
    public UserItem getUserParent() {
        return userParent;
    }

}
