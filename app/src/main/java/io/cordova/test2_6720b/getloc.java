package io.cordova.test2_6720b;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class getloc extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getloc);



        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    // String lang = "en_US";

                    String lang = "ru_RU";

                    URL url2 = new URL("https://inpickup.ru/getloc2.php?lang=" + lang);

                    HttpURLConnection conn2 = (HttpURLConnection) url2.openConnection();
                    conn2.setRequestMethod("GET");
                    conn2.setRequestProperty("Content-Type", "application/json");


                    conn2.setDoOutput(true);
                    conn2.setDoInput(true);

                    // Log.i("JSON", json);


                    Log.i("STATUS88", String.valueOf(conn2.getResponseCode()));
                    Log.i("MSG88", String.valueOf(conn2.getResponseMessage()));

                    //  if (String.valueOf(conn2.getResponseCode()).equals("200")) {

                    BufferedReader br2 = new BufferedReader(new InputStreamReader(conn2.getInputStream()));
                    StringBuilder sb2 = new StringBuilder();
                    String line;
                    while ((line = br2.readLine()) != null) {
                        sb2.append(line + "\n");
                    }
                    br2.close();

                    Log.i("TEST88", String.valueOf(sb2.toString()));

                    final JSONObject obj = new JSONObject(sb2.toString());

                    String city = obj.getJSONObject("response").getJSONObject("GeoObjectCollection").getJSONArray("featureMember").getJSONObject(0).getJSONObject("GeoObject").getJSONObject("metaDataProperty").getJSONObject("GeocoderMetaData").getJSONObject("Address").getJSONArray("Components").getJSONObject(2).getString("name");
                    String country = obj.getJSONObject("response").getJSONObject("GeoObjectCollection").getJSONArray("featureMember").getJSONObject(0).getJSONObject("GeoObject").getJSONObject("metaDataProperty").getJSONObject("GeocoderMetaData").getJSONObject("Address").getJSONArray("Components").getJSONObject(0).getString("name");

                    String pos = obj.getJSONObject("response").getJSONObject("GeoObjectCollection").getJSONArray("featureMember").getJSONObject(0).getJSONObject("GeoObject").getJSONObject("Point").getString("pos");

                    Log.d("json_res:",pos + " / " + country + " / " + city);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread2.start();
    }
}