package apuri.com.br.ureblockingme;

import android.app.Application;

import apuri.com.br.ureblockingme.database.UserManager;

/**
 * Created by paulo.junior on 27/06/2016.
 */
public class UreBlockingMeApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        UserManager.getInstance();
    }
}
