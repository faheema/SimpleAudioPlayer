package sap.com.fahm.simpleaudioplayer;

import android.util.Log;

import sap.com.fahm.simpleaudioplayer.player.AudioPlayerCallBack;

/**
 * Created by Faheem on 21/12/17.
 */

public class LogHelper {
    public static final String TAG = "SIMPLE_AUDIO";

    public static String convertStateToString(@AudioPlayerCallBack.State int state) {
        String stateString;
        switch (state) {

            case AudioPlayerCallBack.State.INVALID:
                stateString = "INVALID";
                break;
            case AudioPlayerCallBack.State.PAUSED:
                stateString = "PAUSED";
                break;
            case AudioPlayerCallBack.State.PLAYING:
                stateString = "PLAYING";
                break;
            case AudioPlayerCallBack.State.STOP:
                stateString = "STOP";
                break;
            case AudioPlayerCallBack.State.RESET:
                stateString = "RESET";
                break;
            case AudioPlayerCallBack.State.COMPLETED:
                stateString = "COMPLETED";
                break;
            default:
                stateString = "N/A";
        }
        return stateString;
    }

    public static void Log(String message) {
        Log.d(TAG, message);
    }
}
