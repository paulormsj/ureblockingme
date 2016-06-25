package apuri.com.br.ureblockingme.messaging;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import apuri.com.br.ureblockingme.MainActivity;
import apuri.com.br.ureblockingme.R;

/**
 * Created by paulo.junior on 25/06/2016.
 */
public class MessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setAction(MainActivity.ACTION_NOTIFICATION);
        for(Map.Entry<String, String> entry : remoteMessage.getData().entrySet())
            intent.putExtra(entry.getKey(),entry.getValue());
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(getResources().getString(R.string.txt_ure_blocking_me))
                .setContentText(
                        String.format(
                                getResources().getString(R.string.txt_remove_you_car),
                        remoteMessage.getData().get(BlockingNotificationExtras.EXTRA_CAR_OWNER))
                ).addAction(android.R.drawable.ic_dialog_info,getResources().getString(R.string.txt_on_my_way)
                        ,null)
                .setContentIntent(pendingIntent)
                .setSmallIcon(android.R.drawable.sym_def_app_icon);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
