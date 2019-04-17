package io.cordova.test2_6720b;


/**
 * Created by Тимофей on 31.08.2017.
 */

class Messpriv {

    public String mess;
    public String page_for_comment;
    public String currname;
    public String user;
    public String name;
    public String avatar;
    public String read;
    public String read2;
    public String type;
    public String profile_name;
    public String currtime;

    public Messpriv() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Messpriv(String mess, String page_for_comment, String currname, String user, String name, String avatar, String read, String read2, String type,String profile_name, String currtime) {
        this.mess = mess;
        this.page_for_comment = page_for_comment;
        this.currname = currname;
        this.user = user;
        this.name = name;
        this.avatar = avatar;
        this.read = read;
        this.read2 = read2;
        this.type = type;
        this.profile_name = profile_name;
        this.currtime = currtime;

    }


}