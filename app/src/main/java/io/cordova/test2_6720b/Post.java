package io.cordova.test2_6720b;

/**
 * Created by timofey on 13.07.17.
 */
public class Post {

    public String name;
    public String status;
    public String photo;
    public String avatar;


    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Post(String name, String status, String photo, String avatar) {
        this.name = name;
        this.status = status;
        this.photo = photo;
        this.photo = avatar;

    }


}
