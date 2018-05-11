package in.ac.iittp.solarx;

import android.app.ProgressDialog;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class SearchData extends AppCompatActivity {
    Button btnHit;
    TextView resultt;
    public String lat,lon;
    long lat1,lon1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_data);

        GPSTracker tracker = GPSTracker.getInstance();
        tracker.canGetLocation();
        tracker.getLocation(this);
        tracker.set_context(this);
         lat=""+Math.round(tracker.getLatitude());
         lon=""+Math.round(tracker.getLongitude());
//        tracker.showSettingsAlert("Location required","Turn on the GPS for the App to function currectly");

        btnHit = (Button) findViewById(R.id.btnHit);
        resultt = (TextView) findViewById(R.id.result);
        lat1= Math.round(tracker.getLatitude());
        lon1= Math.round(tracker.getLongitude());
//        String number = "10";
//        int result = Integer.parseInt(number);


        btnHit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lat1!=0||lon1!=0) {
                    String string = "http://10.21.1.99:3000/tasks/0/14/79/1";
//              String string = "http://10.21.1.99:3000/tasks/0/"+lat1+"/"+lon1+"/1";

                    new JsonTask().execute(string);
                }
            }
        });

//        CheckBox checkBox = (findViewById(R.id.chkbox));
//            boolean  checked =checkBox.isChecked();
//            if (checked){
//                tracker.showSettingsAlert("Enable GPS", "Access to location required");
//
//            }

    }

    public void chkbox2(View view) {
        boolean  checked = ((CheckBox) view).isChecked();

        if (checked){
            GPSTracker tracker = GPSTracker.getInstance();
            if(!tracker.canGetLocation()) {
                tracker.showSettingsAlert("Enable GPS", "Access to location required");
            }
            TextView textView = (findViewById(R.id.testLat));
            textView.setText(lat+"° N");
            TextView textView1 = (findViewById(R.id.testLon));
            textView1.setText(lon+"° E");
        }
    }

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
            resultt.append(result);
        }
    }
}
