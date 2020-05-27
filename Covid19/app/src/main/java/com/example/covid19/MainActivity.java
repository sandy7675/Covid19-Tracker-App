package com.example.covid19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    TextView t1,t2,t3,t4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t1=findViewById(R.id.con);
        t2=findViewById(R.id.rec);
        t3=findViewById(R.id.de);
        t4=findViewById(R.id.Ac);
        DownloadTask task=new DownloadTask();
        task.execute("https://api.rootnet.in/covid19-in/stats/latest");
    }

    public void state(View view) {
        Intent intent=new Intent(MainActivity.this,States.class);
        startActivity(intent);
    }

    public void speek(View view) {
        Intent intent=new Intent(MainActivity.this,SpeechToText_TextToSpeech.class);
        startActivity(intent);
    }

    public class DownloadTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls) {
            String result="";
            URL url;
            HttpsURLConnection urlConnectio=null;
            try {
                url=new URL(urls[0]);
                urlConnectio=(HttpsURLConnection)url.openConnection();
                InputStream in=urlConnectio.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data=reader.read();
                while(data!=-1)
                {
                    char current=(char)data;
                    result +=current;
                    data=reader.read();
                }
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result );
            try {
                JSONObject jsonObject=new JSONObject(result);
                String weatherInfo=jsonObject.getString("data");
                //Log.i()
                Log.i("Website content",weatherInfo);
                JSONObject ob=new JSONObject(weatherInfo);
                String re=ob.optString("unofficial-summary");
                Log.i("Regional content",re);
                JSONArray arr=new JSONArray(re);
                for(int i=0;i<arr.length();i++)
                {
                    JSONObject jsonpart=  arr.getJSONObject(i);

                     Log.i("Total",jsonpart.getString("total"));
                     String total=jsonpart.getString("total");
                     String recoveres=jsonpart.getString("recovered");
                     String deaths=jsonpart.getString("deaths");
                     String Active=jsonpart.getString("active");
                     t1.setText(total);
                     t2.setText(recoveres);
                     t3.setText(deaths);
                     t4.setText(Active);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
