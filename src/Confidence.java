import java.util.ArrayList;
import java.util.List;

public class Confidence {
    public List<String> prefix = new ArrayList<>();
    public List<String> postfix = new ArrayList<>();
    public List<String> prefAndPos = new ArrayList<>();
    public float confidenceValue;

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
}
