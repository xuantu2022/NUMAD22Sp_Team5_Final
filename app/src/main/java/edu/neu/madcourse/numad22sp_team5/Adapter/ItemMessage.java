package edu.neu.madcourse.numad22sp_team5.Adapter;

import edu.neu.madcourse.numad22sp_team5.GlobalStatus;

public class ItemMessage {
    private final String babyId;
    private final String nickname;
    private final String headshot;
    GlobalStatus globalStatus;

    public ItemMessage(String babyId, String nickname, String headshot, GlobalStatus status) {
        this.babyId = babyId;
        this.nickname = nickname;
        this.headshot = headshot;
        globalStatus = status;
    }

    public GlobalStatus getStatus() {
        return globalStatus;
    }

    public String getBabyId() {
        return babyId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getHeadshot() {return headshot;}
}
