package apuri.com.br.ureblockingme.messaging;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import apuri.com.br.ureblockingme.database.TokenManager;

/**
 * Created by paulo.junior on 25/06/2016.
 */
public class InstanceIdService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {
        TokenManager.getInstance().saveToken(refreshedToken);
    }


}
