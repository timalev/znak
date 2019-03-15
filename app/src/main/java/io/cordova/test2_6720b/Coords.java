package io.cordova.test2_6720b;

/**
 * Created by timofey on 13.07.17.
 */
public class Coords {

    public Double lat;
    public Double lng;

    public Coords() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Coords(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }
}
