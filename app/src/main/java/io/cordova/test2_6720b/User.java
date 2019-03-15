package io.cordova.test2_6720b;

/**
 * Created by timofey on 02.08.17.
 */
public class User {
    public String video;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String video) {
        this.video = video;

    }

}
