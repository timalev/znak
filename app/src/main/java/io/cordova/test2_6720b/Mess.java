package io.cordova.test2_6720b;


/**
 * Created by Тимофей on 31.08.2017.
 */

class Mess {

    public String mess;
    public String page_for_comment;
    public String user;
    public String name;
    public String avatar;
    public String type;
    public String currtime;

    public Mess() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Mess(String mess, String page_for_comment, String user, String name, String avatar, String type, String currtime) {
        this.mess = mess;
        this.page_for_comment = page_for_comment;
        this.user = user;
        this.name = name;
        this.avatar = avatar;
        this.type = type;
        this.currtime = currtime;

    }


}