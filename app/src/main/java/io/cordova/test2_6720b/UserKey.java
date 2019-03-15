package io.cordova.test2_6720b;

/**
 * Created by timofey on 05.08.17.
 */
public class UserKey {

    public String user;

    public UserKey() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public UserKey(String user) {
        this.user = user;

    }
}
