package movie.android.kizema.moviesampleapp.util;

import android.util.Log;

public class Logger {

    public static final String TAG = "TAG";

    public static void d(String msg){
        if (showLogs()){
            Log.d(TAG, msg);
        }
    }

    public static boolean showLogs(){
        return true;
    }
}
