package io.cordova.test2_6720b;

/**
 * Created by Тимофей on 16.09.2017.
 */

class Upload {

    public String video;
    public String type;


    public Upload() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Upload(String link, String type) {
        this.video = link;
        this.type = type;


    }

}
