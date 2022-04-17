package edu.neu.madcourse.numad22sp_team5;

import android.app.Application;

public class GlobalStatus extends Application {
    private static boolean isInMessage;

    public GlobalStatus() {
        GlobalStatus.isInMessage = false;
    }


    public static boolean getIsInMessage() {
        return isInMessage;
    }

    public static void setIsInMessage(boolean isInMessage) {
        GlobalStatus.isInMessage = isInMessage;
    }
}
