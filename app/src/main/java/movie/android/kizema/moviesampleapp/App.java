package movie.android.kizema.moviesampleapp;

import android.app.Application;
import android.content.Context;

import movie.android.kizema.moviesampleapp.network.controller.BaseController;
import movie.android.kizema.moviesampleapp.network.controller.NetworkController;

public class App extends Application {

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();

        appContext = getApplicationContext();
    }

    public static Context getAppContext(){
        return appContext;
    }


    public static BaseController getController(){
        //for test purposes we can inject here TestControlled
        return new NetworkController();
    }
}

