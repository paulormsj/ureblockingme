package apuri.com.br.ureblockingme.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by paulo.junior on 26/06/2016.
 */
public class User {
    private String uid;
    private String name;
    private String email;
    private List<String> licensePlates;

    public User(){
        this.licensePlates = new ArrayList<>();
    }

    public User(String name,String email,String uid){
        this(name,email);
        this.uid = uid;
    }

    public User(String name,String email){
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getLicensePlates() {
        return licensePlates;
    }

    public void setLicensePlates(List<String> licensePlates) {
        this.licensePlates = licensePlates;
    }

    public void addLicensePlate(String licensePlate){
        this.licensePlates.add(licensePlate);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }
}
