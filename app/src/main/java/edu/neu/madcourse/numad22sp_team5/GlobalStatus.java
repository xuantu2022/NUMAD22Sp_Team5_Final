package edu.neu.madcourse.numad22sp_team5;

import android.app.Application;
import android.util.Log;

import java.util.HashMap;
import java.util.HashSet;

public class GlobalStatus extends Application {
    private static boolean isInMessage;
    private static HashSet<String> initialBabyNotify = new HashSet<>();
    private static HashMap<String, Boolean> muteNotify = new HashMap<String, Boolean>();
    private static HashSet<String> babyListNotify = new HashSet<>();
    private static int timesInMain = 0;

    public GlobalStatus() {
        GlobalStatus.isInMessage = false;
    }

    public static void setTimesInMain(int timesInMain){
        GlobalStatus.timesInMain = timesInMain;
    }

    public static int getTimesInMain(){
        return GlobalStatus.timesInMain;
    }

    public static void addBabyListNotify(String babyId) {
        babyListNotify.add(babyId);
    }

    public static boolean shouldBabyListNotify(String babyId) {
        return babyListNotify.contains(babyId);
    }
    public static void clearBabyListNotify() {
        babyListNotify.clear();
    }

    public static void addBabyNotify(String babyId) {
        initialBabyNotify.add(babyId);
    }

    public static void removeBabyNotify(String babyId) {
        initialBabyNotify.remove(babyId);
    }

    public static boolean shouldNotifyBaby(String babyId) {
        return initialBabyNotify.contains(babyId);
    }

    public static boolean isBabyMute(String baby) {
        if (muteNotify.containsKey(baby) && muteNotify.get(baby).equals(true)) {
            Log.d("info", "baby is muted " + baby);
            return true;
        }
        Log.d("info", "baby is not muted " + baby);
        return false;
    }

    public static void muteBaby(String baby) {
        Log.d("info", "mute baby" + baby);
        muteNotify.put(baby, true);
    }

    public static void unmuteBaby(String baby) {
        Log.d("info", "unmute baby " + baby);
        muteNotify.put(baby, false);
    }

    public static boolean getIsInMessage() {
        return isInMessage;
    }

    public static void setIsInMessage(boolean isInMessage) {
        GlobalStatus.isInMessage = isInMessage;
    }


}
