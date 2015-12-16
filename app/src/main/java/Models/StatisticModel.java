package Models;

/**
 * Created by kovacslev on 12/16/2015.
 */
public class StatisticModel {

    private String time;

    private int maxPoints;

    private int reachedPoints;

    private int minReachPoints;

    private int success;

    public int getMaxPoints() {
        return maxPoints;
    }

    public int getMinReachPoints() {
        return minReachPoints;
    }

    public int getReachedPoints() {
        return reachedPoints;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setMaxPoints(int maxPoints) {
        this.maxPoints = maxPoints;
    }

    public void setMinReachPoints(int minReachPoints) {
        this.minReachPoints = minReachPoints;
    }

    public void setReachedPoints(int reachedPoints) {
        this.reachedPoints = reachedPoints;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }
}
