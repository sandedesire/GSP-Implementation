public class UserData {
    private String userID;
    private String itemDescription;
    private String timeStamp;

    public UserData(String userID, String itemDescription, String timeStamp) {
        this.userID = userID;
        this.itemDescription = itemDescription;
        this.timeStamp = timeStamp;
    }

    public String getUserID() {
        return userID;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
