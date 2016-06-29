package apuri.com.br.ureblockingme.database;

import android.content.SharedPreferences;
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

import apuri.com.br.ureblockingme.UreBlockingMeApplication;
import apuri.com.br.ureblockingme.entities.User;

/**
 * Created by paulo.junior on 26/06/2016.
 */
public class UserManager {


    public static final String USER_DATA = "user_data";
    public static final String HAS_USER = "has_user";
    public static final String USERS_DATABASE = "users";
    private FirebaseAuth auth;
    private boolean hasUser = false;
    private User user;
    private static UserManager instance;
    private List<IUserManagerObserver> observers;


    private UserManager(){
        auth = FirebaseAuth.getInstance();
        auth.addAuthStateListener(new AuthListener());
        this.observers = new ArrayList<>();
    }

    public static UserManager getInstance() {
        if(instance == null)
            instance = new UserManager();
        instance.hasUser = UreBlockingMeApplication.getSharedPreferences(USER_DATA).getBoolean(HAS_USER,false);
        return instance;
    }

    public void registerUser(final User user, String password, final IUserManagerCallback callback){
        //TODO search if user exists
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(user.getEmail(),password).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()) {
                            createUserProfile(task.getResult(),user);
                            user.setUid(task.getResult().getUser().getUid());
                            setUser(user);
                            notifyUserLogged(user);
                        }
                        if(callback != null)
                            callback.onRegisterUser(task.isSuccessful(),user);
                    }
                });
    }

    private void createUserProfile(AuthResult authUser, User user) {
        UserProfileChangeRequest update = new UserProfileChangeRequest.Builder()
                .setDisplayName(user.getName()).build();
        authUser.getUser().updateProfile(update);
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");
        database.child(authUser.getUser().getUid()).setValue(user);
    }

    private void setHasUser(boolean has) {
        SharedPreferences prefs = UreBlockingMeApplication.getSharedPreferences("user_data");
        prefs.edit().putBoolean("has_user",has).apply();
        this.hasUser = has;
    }

    public boolean hasUser() {
        return hasUser;
    }

    private void setUser(User user) {
        this.user = user;
    }

    public void updateUserToken(String token){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(USERS_DATABASE);
        myRef.child(user.getUid()).child("token").setValue(token);
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
            }
            else {
                setHasUser(false);
                setUser(null);
            }
        }
    }



}
