package io.cordova.test2_6720b;

/**
 * Created by Тимофей on 19.09.2017.
 */

class Online {

    public double lat;
    public double lng;
    public String time;
    public String name;
    public String avatar;

    public Online() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Online(double lat, double lng, String time, String name, String avatar) {
        this.lat = lat;
        this.lng = lng;
        this.time = time;
        this.name = name;
        this.avatar = avatar;

    }
}