package edu.neu.madcourse.numad22sp_team5;

import android.app.Application;

import java.util.HashMap;
import java.util.HashSet;

public class GlobalStatus extends Application {
    private static boolean isInMessage;
    private static HashSet<String> initialBabyNotify = new HashSet<>();

    public GlobalStatus() {
        GlobalStatus.isInMessage = false;
    }

    public static void addBabyNotify(String babyId) {
        initialBabyNotify.add(babyId);
    }

    public static boolean shouldNotifyBaby(String babyId) {
        return initialBabyNotify.contains(babyId);
    }

    public static boolean getIsInMessage() {
        return isInMessage;
    }

    public static void setIsInMessage(boolean isInMessage) {
        GlobalStatus.isInMessage = isInMessage;
    }


}
