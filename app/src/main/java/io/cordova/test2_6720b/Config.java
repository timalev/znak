package io.cordova.test2_6720b;

/**
 * Created by timofey on 04.10.17.
 */

public class Config {

    public String tab(String tab)
    {
        String result;

        switch (tab)
        {
            case "users":
                result = "users5";
                break;

            case "notific":
                result = "notific";
                break;

            case "online":
                result = "online";
                break;

            case "messages":
                result = "messages";
                break;


            default:result = "null";
        }

        return result;
    }

}
