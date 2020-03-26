package FinalProject;


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
