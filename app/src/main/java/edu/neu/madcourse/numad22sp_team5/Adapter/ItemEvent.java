package edu.neu.madcourse.numad22sp_team5.Adapter;

public class ItemEvent {
    private String time;
    private String descroption;

    public ItemEvent(String time, String descroption) {
        this.time = time;
        this.descroption = descroption;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return descroption;
    }
}
