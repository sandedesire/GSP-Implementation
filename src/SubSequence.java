import java.util.ArrayList;
import java.util.List;

public class SubSequence {
    public List<String> itemsJoined = new ArrayList<>();
    public float support;
    public boolean pruned;

    public SubSequence(List<String> itemsJoined) {
        this.itemsJoined = itemsJoined;
    }

    public List<String> getItemsJoined() {
        return itemsJoined;
    }

    public float getSupport() {
        return support;
    }

    public void setItemsJoined(List<String> itemsJoined) {
        this.itemsJoined = itemsJoined;
    }

    public void setSupport(float support) {
        this.support = support;
    }
}
