package io.cordova.test2_6720b;


public class Profile3 {

    private String profile_name;
    private String profile_photo;
    private String profile_age;
    private String profile_country;
    private String profile_likes;
    private String device_token;
    private String profile_gender;
    private String key;
    private int key2;
    private int key3;


    public Profile3() {
    }

    public Profile3(String profile_name, String profile_photo, String profile_age, String profile_country, String profile_likes, String device_token, String profile_gender, String key, int key2, int key3) {

        this.profile_name = profile_name;
        this.profile_likes = profile_likes;
        this.profile_photo = profile_photo;
        this.profile_age = profile_age;
        this.profile_country = profile_country;
        this.device_token = device_token;
        this.profile_gender = profile_gender;
        this.key = key;
        this.key2 = key2;
        this.key3 = key3;
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

    public String getLikes() {
        return profile_likes;
    }

    public String getDevice_token() {return  device_token;}

    public String getProfile_gender() {return profile_gender;}

    public String getKey() {
        return key;
    }

    public int getKey2() {
        return key2;
    }

    public int getKey3() {
        return key3;
    }

}
