package mg.studio.weatherappdesign;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.net.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(isNetworkAvailable(this.getApplicationContext())){
        new DownloadUpdate().execute();
        Toast.makeText(this,"refreshed",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this,"failed to connect",Toast.LENGTH_LONG).show();
        }

    }


    public void btnClick(View view) {
        if(isNetworkAvailable(this.getApplicationContext())){
            new DownloadUpdate().execute();
            Toast.makeText(this,"refreshed",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this,"failed to connect",Toast.LENGTH_LONG).show();
        }
    }
    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager manger = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manger.getActiveNetworkInfo();
            if(info != null){
                return info.isConnected();
            }else{
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
    public int weather(String s){
        int temp=0;
        if(s.equals("小雨")){
          temp=R.drawable.rainy_small;
        }
        if(s.equals("阴")){
            temp=R.drawable.partly_sunny_small;
        }
        if(s.equals("多云")){
            temp=R.drawable.windy_small;
        }
        if(s.equals("晴")){
            temp=R.drawable.sunny_small;
        }
    return temp;
    }



    public String weekcntoenglish(String s){
        if(s.equals("星期一")){
            return "MON";
        }
        if(s.equals("星期二")){
            return  "TUE";
        }
        if(s.equals("星期三")){
            return  "WDN";
        }
        if(s.equals("星期四")){
            return "THU";
        }
        if(s.equals("星期五")){
            return "FRI";
        }
        if(s.equals("星期六")){
            return  "STA";
        }
        else{
            return "SUN";
        }


    }


    private class DownloadUpdate extends AsyncTask<String, Void, String> {



        @Override
        protected String doInBackground(String... strings) {
            String stringUrl = "http://t.weather.sojson.com/api/weather/city/101043700";
            HttpURLConnection urlConnection = null;
            BufferedReader reader;

            try {
                URL url = new URL(stringUrl);

                // Create the request to get the information from the server, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Mainly needed for debugging
                    Log.d("TAG", line);
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                //The temperature
                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String temperature) {
            //Update the temperature displayed

            try {

                JSONObject json = new JSONObject(temperature);
                String location=json.getJSONObject("cityInfo").get("parent").toString();
                if(location.equals("重庆市")){
                    location="Chongqing";
                }
                ((TextView)findViewById(R.id.tv_location)).setText(location);
                String date=json.getJSONObject("data").getJSONArray("forecast").getJSONObject(0).get("ymd").toString();
                ((TextView)findViewById(R.id.tv_date)).setText(date);
                String temprtod=json.getJSONObject("data").getJSONArray("forecast").getJSONObject(0).get("high").toString();
                String temptod=temprtod.substring(3,temprtod.indexOf("."));
                ((TextView)findViewById(R.id.temperature_of_the_day)).setText(temptod);
                String temprtom=json.getJSONObject("data").getJSONArray("forecast").getJSONObject(1).get("high").toString();
                String temptom=temprtom.substring(3,temprtod.indexOf("."))+"℃";
                ((TextView)findViewById(R.id.temptom)).setText(temptom);
                String temprth=json.getJSONObject("data").getJSONArray("forecast").getJSONObject(2).get("high").toString();
                String tempth=temprth.substring(3,temprtod.indexOf("."))+"℃";
                ((TextView)findViewById(R.id.tempth)).setText(tempth);
                String temprtfor=json.getJSONObject("data").getJSONArray("forecast").getJSONObject(3).get("high").toString();
                String tempfor=temprtfor.substring(3,temprtod.indexOf("."))+"℃";
                ((TextView)findViewById(R.id.tempfor)).setText(tempfor);
                String temprfif=json.getJSONObject("data").getJSONArray("forecast").getJSONObject(4).get("high").toString();
                String tempfif=temprfif.substring(3,temprtod.indexOf("."))+"℃";
                ((TextView)findViewById(R.id.tempfif)).setText(tempfif);
                String weektod=json.getJSONObject("data").getJSONArray("forecast").getJSONObject(0).get("week").toString();
                ((TextView)findViewById(R.id.weekid)).setText(weekcntoenglish(weektod));
                String weektom=json.getJSONObject("data").getJSONArray("forecast").getJSONObject(1).get("week").toString();
                ((TextView)findViewById(R.id.weektom)).setText(weekcntoenglish(weektom));
                String weekth=json.getJSONObject("data").getJSONArray("forecast").getJSONObject(2).get("week").toString();
                ((TextView)findViewById(R.id.weekth)).setText(weekcntoenglish(weekth));
                String weekfor=json.getJSONObject("data").getJSONArray("forecast").getJSONObject(3).get("week").toString();
                ((TextView)findViewById(R.id.weektfor)).setText(weekcntoenglish(weekfor));
                String weekfif=json.getJSONObject("data").getJSONArray("forecast").getJSONObject(4).get("week").toString();
                ((TextView)findViewById(R.id.weekfif)).setText(weekcntoenglish(weekfif));
                String toicon=json.getJSONObject("data").getJSONArray("forecast").getJSONObject(0).get("type").toString();
                ((ImageView)findViewById(R.id.img_weather_condition)).setImageResource(weather(toicon));
                String tomicon=json.getJSONObject("data").getJSONArray("forecast").getJSONObject(1).get("type").toString();
                ((ImageView)findViewById(R.id.tomorrow)).setImageResource(weather(tomicon));
                String thicon=json.getJSONObject("data").getJSONArray("forecast").getJSONObject(2).get("type").toString();
                ((ImageView)findViewById(R.id.thirday)).setImageResource(weather(thicon));
                String foricon=json.getJSONObject("data").getJSONArray("forecast").getJSONObject(3).get("type").toString();
                ((ImageView)findViewById(R.id.forthday)).setImageResource(weather(foricon));
                String fificon=json.getJSONObject("data").getJSONArray("forecast").getJSONObject(4).get("type").toString();
                ((ImageView)findViewById(R.id.fifthday)).setImageResource(weather(fificon));

            }
            catch (JSONException e){

            }
        }
    }
}
