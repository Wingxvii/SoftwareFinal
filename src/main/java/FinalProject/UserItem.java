package FinalProject;


//class that holds data for each sent chat data
public class UserItem extends DataItem{
    //username
    public String Username = "Nameless";
    //status
    public String Status = "Offline";

    //constructor
    public UserItem(String Username, String Status) {
        this.Username = Username;
        this.Status = Status;
        this.type = ItemType.USERITEM;

    }

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
