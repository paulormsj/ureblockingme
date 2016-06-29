package apuri.com.br.ureblockingme.database;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.content.SharedPreferencesCompat;

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

import apuri.com.br.ureblockingme.UreBlockingMeApplication;
import apuri.com.br.ureblockingme.entities.User;
import apuri.com.br.ureblockingme.messaging.InstanceIdService;

/**
 * Created by paulo.junior on 26/06/2016.
 */
public class UserManager {


    public static final String USER_DATA = "user_data";
    public static final String HAS_USER = "has_user";
    private FirebaseAuth auth;
    private boolean hasUser = false;
    private User user;
    private static UserManager instance;
    private List<IUserManagerObserver> observers;


    private UserManager(){
        auth = FirebaseAuth.getInstance();
        auth.addAuthStateListener(new AuthListener());
    }

    public static UserManager getInstance() {
        if(instance == null)
            instance = new UserManager();
        instance.hasUser = UreBlockingMeApplication.getSharedPreferences(USER_DATA).getBoolean(HAS_USER,false);
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
                            setHasUser(true);
                        }
                        if(callback != null)
                            callback.onRegisterUser(task.isSuccessful(),user);
                    }
                });
    }

    private void setHasUser(boolean has) {
        SharedPreferences prefs = UreBlockingMeApplication.getSharedPreferences("user_data");
        prefs.edit().putBoolean("has_user",has).apply();
        this.hasUser = has;
    }

    public boolean hasUser() {
        return hasUser;
    }

    private void setUser(FirebaseAuth firebaseAuth) {
        if(firebaseAuth != null ){
            user = new User(firebaseAuth.getCurrentUser().getDisplayName(),
                    firebaseAuth.getCurrentUser().getEmail(),firebaseAuth.getCurrentUser().getUid());
        }else{
            user = null;
        }

    }

    public void addObserver(IUserManagerObserver observer) {
        if(!this.observers.contains(observer))
            this.observers.add(observer);
    }

    public User getUser() {
        return this.user;
    }

    public static interface IUserManagerCallback{
        void onRegisterUser(boolean success, User user);
    }

    private synchronized void notifyUserLogged(final User user){
        for(IUserManagerObserver observer : this.observers)
            observer.userLogged(user);
    }

    private synchronized  void notifyUserLeft(final User user){
        for(IUserManagerObserver observer :  this.observers)
            observer.userLeft(user);
    }

    public static interface IUserManagerObserver{
        void userLogged(User user);
        void userLeft(User user);
    }

    private class AuthListener implements FirebaseAuth.AuthStateListener {

        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            if(firebaseAuth.getCurrentUser() != null) {
                setHasUser(true);
                setUser(firebaseAuth);
            }
            else {
                setHasUser(false);
                setUser(null);
            }
        }
    }



}
