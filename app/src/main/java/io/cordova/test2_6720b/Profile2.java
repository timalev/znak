package io.cordova.test2_6720b;


public class Profile2 {
    private String mess;
    private String name;
    private String type;
    private String user;

    public Profile2() {
    }

    public Profile2(String mess, String name, String type, String user) {
        this.mess = mess;
        this.name = name;
        this.type = type;
        this.user = user;
    }

    public String getMess() { return mess; }

    public void setMess(String mess) {
        this.mess = mess;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

}
