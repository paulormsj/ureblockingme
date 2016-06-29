package apuri.com.br.ureblockingme;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import apuri.com.br.ureblockingme.database.UserManager;

/**
 * Created by paulo.junior on 27/06/2016.
 */
public class UreBlockingMeApplication extends Application {

    private static Context ctx;

    @Override
    public void onCreate() {
        super.onCreate();
        ctx = this;
    }

    public static SharedPreferences getSharedPreferences(String pref){
        return ctx.getSharedPreferences(pref,MODE_APPEND);
    }
}
