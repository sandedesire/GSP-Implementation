package GSPImplementation;

import java.util.ArrayList;
import java.util.List;
import GSPImplementation.UserBasket;

public class Confidence {
    public List<String> prefix = new ArrayList<>();
    public List<String> postfix = new ArrayList<>();
    public List<String> prefAndPos = new ArrayList<>();
    public float confidenceValue;

    public List<UserBasket> prefAndPosUsers = new ArrayList<>();
    public List<String> prefTimeFrame = new ArrayList<>();
    public List<String> posTimeFrame = new ArrayList<>();
    public List<String> timeDifferences = new ArrayList<>();
    public long minTimeDifference;



    public Confidence(List<String> prefix, List<String> postfix) {
        this.postfix = postfix;
        this.prefix = prefix;
    }

    public List<String> getPrefix() {
        return prefix;
    }

    public List<String> getPostfix() {
        return postfix;
    }

    public List<String> getPrefAndPos() {
        return prefAndPos;
    }

    public double getConfidenceValue() {
        return confidenceValue;
    }

    public void setPrefix(List<String> prefix) {
        this.prefix = prefix;
    }

    public void setPrefAndPos(List<String> prefAndPos) {
        this.prefAndPos = prefAndPos;
    }

    public void setPostfix(List<String> postfix) {
        this.postfix = postfix;
    }

    public void setConfidenceValue(float confidenceValue) {
        this.confidenceValue = confidenceValue;
    }

    public List<GSPImplementation.UserBasket> getPrefAndPosUsers() {
        return prefAndPosUsers;
    }

    public void setPrefAndPosUsers(List<UserBasket> prefAndPosUsers) {
        this.prefAndPosUsers = prefAndPosUsers;
    }

    public List<String> getPrefTimeFrame() {
        return prefTimeFrame;
    }

    public void setPrefTimeFrame(List<String> prefTimeFrame) {
        this.prefTimeFrame = prefTimeFrame;
    }

    public List<String> getPosTimeFrame() {
        return posTimeFrame;
    }

    public void setPosTimeFrame(List<String> posTimeFrame) {
        this.posTimeFrame = posTimeFrame;
    }

    public List<String> getTimeDifferences() {
        return timeDifferences;
    }

    public void setTimeDifferences(List<String> timeDifferences) {
        this.timeDifferences = timeDifferences;
    }

    public long getMinTimeDifference() {
        return minTimeDifference;
    }

    public void setMinTimeDifference(long minTimeDifference) {
        this.minTimeDifference = minTimeDifference;
    }
}
