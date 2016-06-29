package apuri.com.br.ureblockingme.messaging;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import apuri.com.br.ureblockingme.database.UserManager;
import apuri.com.br.ureblockingme.entities.User;

/**
 * Created by paulo.junior on 25/06/2016.
 */
public class InstanceIdService extends FirebaseInstanceIdService implements UserManager.IUserManagerObserver{

    @Override
    public void onCreate() {
        super.onCreate();
        UserManager.getInstance().addObserver(this);
    }

    private String currentToken = null;

    @Override
    public void onTokenRefresh() {
        currentToken = FirebaseInstanceId.getInstance().getToken();
        if(UserManager.getInstance().hasUser())
            sendRegistrationToServer(UserManager.getInstance().getUser(), currentToken);
    }

    private void sendRegistrationToServer(User user, String refreshedToken) {
        if (currentToken != null && user != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("users_info");
            myRef.child(user.getUid()).child("token").setValue(currentToken);
        }
    }


    @Override
    public void userLogged(User user) {
        sendRegistrationToServer(user,currentToken);
    }

    @Override
    public void userLeft(User user) {
        sendRegistrationToServer(user,"");
    }
}
