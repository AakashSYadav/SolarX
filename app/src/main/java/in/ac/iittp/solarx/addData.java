package in.ac.iittp.solarx;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.app.AlertDialog.Builder;
import android.widget.Toast;


public class addData extends AppCompatActivity {

    TextView textLIGHT_available, textLIGHT_reading;
    long lat1,lon1;

    float a;
    private Button buttn;
    private TextView textViewLat;
    private TextView textViewLon;
//    private LocationManager locationManager;
//    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

         textViewLat = (findViewById(R.id.txtLat));
        textViewLon = (findViewById(R.id.txtLon));
//
//        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//        locationListener = new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//
//                textViewLat.setText(" " + location.getLatitude());
//                textViewLon.setText(" " + location.getLongitude());
//            }
//
//            @Override
//            public void onStatusChanged(String s, int i, Bundle bundle) {
//
//            }
//
//            @Override
//            public void onProviderEnabled(String s) {
//
//            }
//
//            @Override
//            public void onProviderDisabled(String s) {
//
//                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                startActivity(intent);
//            }
//        };
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                requestPermissions(new String[]{
//                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET
//                }, 10);
//                return;
//            }
//        }else {
//            configueButton();
//        }

        TextView textView3 = (TextView)findViewById(R.id.devid);
        textView3.setText(getDeviceName());
        Button button3 = (Button) findViewById(R.id.submit);
        button3.setEnabled(false);


        textLIGHT_available = (TextView)findViewById(R.id.LIGHT_available);
        textLIGHT_reading = (TextView)findViewById(R.id.LIGHT_reading);

        SensorManager mySensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        Sensor LightSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if(LightSensor != null){
            mySensorManager.registerListener(
                    LightSensorListener,
                    LightSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);

        }else{
            textLIGHT_available.setText("Sorry! Light Sensor Not available on your device");
        }

        GPSTracker tracker = GPSTracker.getInstance();
        tracker.canGetLocation();
        tracker.getLocation(this);
        tracker.set_context(this);
        textViewLat.setText(""+tracker.getLatitude());
        textViewLon.setText(""+tracker.getLongitude());
        if(!tracker.canGetLocation()) {
            tracker.showSettingsAlert("Enable GPS", "Access to location required");
        }

        lat1= Math.round(tracker.getLatitude());
        lon1= Math.round(tracker.getLongitude());
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lat1!=0||lon1!=0||a!=0) {
//                String string = "http://10.21.1.99:3000/tasks/1/14/79/1";
              String string = "http://10.21.1.99:3000/tasks/1/"+lat1+"/"+lon1+"/"+a;

                new JsonTask().execute(string);
                }
            }
        });
    }

    private final SensorEventListener LightSensorListener = new SensorEventListener(){

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if(event.sensor.getType() == Sensor.TYPE_LIGHT){
                textLIGHT_reading.setText(event.values[0] + " Lux");
                 a = event.values[0];
            }
        }

    };

    //get device model
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }
        return phrase.toString();
    }


    public void chkbox(View view) {
        boolean  checked = ((CheckBox) view).isChecked();

        if (checked){
            Button button = (findViewById(R.id.submit));
            button.setEnabled(true);
        }
        else{
            Button button = (findViewById(R.id.submit));
            button.setEnabled(false);
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        switch (requestCode){
//            case 10:
//                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
//                    configueButton();
//                return;
//        }
//    }
//
//    private void configueButton(){
//                locationManager.requestLocationUpdates("gps", 1000, 0, locationListener);
//
//    }
private class JsonTask extends AsyncTask<String, String, String> {

    protected void onPreExecute() {
        super.onPreExecute();

//            pd = new ProgressDialog(MainActivity.this);
//            pd.setMessage("Please wait");
//            pd.setCancelable(false);
//            pd.show();
    }

    protected String doInBackground(String... params) {

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line+"\n");
                Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

            }
            return buffer.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
//            if (pd.isShowing()){
//                pd.dismiss();
//            }
//        resultt.append(result);
        Toast.makeText(getApplication(), "Data Added!",
                Toast.LENGTH_LONG).show();
    }
}
}
