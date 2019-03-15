package io.cordova.test2_6720b;


public class Profile3 {

    private String profile_name;
    private String profile_photo;
    private String profile_age;
    private String profile_country;
    private String key;


    public Profile3() {
    }

    public Profile3(String profile_name, String profile_photo, String profile_age, String profile_country, String key) {

        this.profile_name = profile_name;
        this.profile_photo = profile_photo;
        this.profile_age = profile_age;
        this.profile_country = profile_country;
        this.key = key;

    }

    public String getImageUrl() {
        return profile_photo;
    }

    public String getName() {
        return profile_name;
    }

    public String getAge() {
        return profile_age;
    }

    public String getCountry() {
        return profile_country;
    }

    public String getKey() {
        return key;
    }



}
