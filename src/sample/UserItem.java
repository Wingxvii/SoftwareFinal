package sample;


//class that holds data for each sent chat data
public class UserItem {
    //username
    public String Username = "Nameless";
    //status
    public String Status = "Offline";

    //constructor
    public UserItem(String Username, String Status) {
        this.Username = Username;
        this.Status = Status;
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
