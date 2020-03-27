package FinalProject;

/*
Desc: Class that holds data locally for each connected user
Author: John Wang
Date: 3/26/2020
*/

public class UserItem extends DataItem{
    //username
    private String Username = "Nameless";
    //status
    private String Status = "Offline";

    //constructor
    public UserItem(String Username, String Status) {
        this.Username = Username;
        this.Status = Status;
        this.type = ItemType.USERITEM;

    }

    //copy constructor for simple re-seralization
    public UserItem(UserItem copy){
        super((DataItem)copy);
        this.Username = copy.getUsername();
        this.Status = copy.getStatus();
        this.type = ItemType.USERITEM;
    }

    //accessors
    public String getUsername() {
        return Username;
    }
    public String getStatus() {
        return Status;
    }

    //mutators
    public void setStatus(String status) {
        Status = status;
    }
}
