package apuri.com.br.ureblockingme.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by paulo.junior on 25/06/2016.
 */
public class TokenManager {

    private TokenManager() {

    }

    public static TokenManager getInstance() {
        return new TokenManager();
    }

    public void saveToken(String token){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("tokens");
        myRef.setValue(token);
    }
}
