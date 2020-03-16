package sample;

public class UserItem {
    public String Username = "Nameless";
    public String Status = "Offline";

    public UserItem(String Username, String Status) {
        this.Username = Username;
        this.Status = Status;
    }

    public String getUsername() {
        return Username;
    }

    public String getStatus() {
        return Status;
    }

}
