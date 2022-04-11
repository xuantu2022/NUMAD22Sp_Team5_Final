package edu.neu.madcourse.numad22sp_team5.Adapter;

public class ItemMessage {
    private final String babyId;
    private final String nickname;

    public ItemMessage(String babyId, String nickname) {
        this.babyId = babyId;
        this.nickname = nickname;
    }

    public String getBabyId() {
        return babyId;
    }

    public String getNickname() {
        return nickname;
    }
}
