import java.util.*;

public class UserBasket {
    public String userID;
    public Set<String> allItemsEverBought = new HashSet<>();
    public Set<String> usersTimeStamp = new HashSet<>();
    public List<Map<String,List<String>>> timeStampToAllItems = new ArrayList<>();
    public List<Map<String,List<String>>> itemToAllTimeStamp = new ArrayList<>();




    public UserBasket(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }


    public Set<String> getAllItemsEverBought() {
        return allItemsEverBought;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }



    public void setAllItemsEverBought(Set<String> allItemsEverBought) {
        this.allItemsEverBought = allItemsEverBought;
    }

    public List<Map<String, List<String>>> getTimeStampToAllItems() {
        return timeStampToAllItems;
    }

    public List<Map<String, List<String>>> getItemToAllTimeStamp() {
        return itemToAllTimeStamp;
    }

    public void setTimeStampToAllItems(List<Map<String, List<String>>> timeStampToAllItems) {
        this.timeStampToAllItems = timeStampToAllItems;
    }

    public void setItemToAllTimeStamp(List<Map<String, List<String>>> itemToAllTimeStamp) {
        this.itemToAllTimeStamp = itemToAllTimeStamp;
    }

    public Set<String> getUsersTimeStamp() {
        return usersTimeStamp;
    }

    public void setUsersTimeStamp(Set<String> usersTimeStamp) {
        this.usersTimeStamp = usersTimeStamp;
    }
}
