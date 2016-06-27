package apuri.com.br.ureblockingme;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import apuri.com.br.ureblockingme.database.UserManager;

/**
 * Created by paulo.junior on 26/06/2016.
 */
public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = null;
        if(UserManager.getInstance().hasUser()) {
            i = new Intent(this,MainActivity.class);
        }else {
            i = new Intent(this, WellcomeActivity.class);
        }
        startActivity(i);
    }
}
