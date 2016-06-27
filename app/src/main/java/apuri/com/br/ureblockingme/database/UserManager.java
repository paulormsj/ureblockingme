package apuri.com.br.ureblockingme.database;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import apuri.com.br.ureblockingme.entities.User;

/**
 * Created by paulo.junior on 26/06/2016.
 */
public class UserManager {


    private FirebaseAuth auth;
    private boolean hasUser = false;
  
    private static UserManager instance;


    private UserManager(){
        auth = FirebaseAuth.getInstance();
        auth.addAuthStateListener(new AuthListener());
    }

    public static UserManager getInstance() {
        if(instance == null)
            instance = new UserManager();
        return instance;
    }

    public void registerUser(final String name, final String email, final String password, final IUserManagerCallback callback){
        //TODO search if user exists
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        User user = null;
                        if(task.isSuccessful()) {
                            user = new User(name,email);
                            FirebaseUser authUser = task.getResult().getUser();
                            UserProfileChangeRequest update = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name).build();
                            authUser.updateProfile(update);
                        }
                        if(callback != null)
                            callback.onRegisterUser(task.isSuccessful(),user);
                    }
                });
    }

    public boolean hasUser() {
        return hasUser;
    }

    public static interface IUserManagerCallback{
        void onRegisterUser(boolean success, User user);
    }

    private class AuthListener implements FirebaseAuth.AuthStateListener {

        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            if(firebaseAuth.getCurrentUser() != null) {
                UserManager.this.hasUser = true;
            }
            else
                UserManager.this.hasUser = false;
        }
    }
}
