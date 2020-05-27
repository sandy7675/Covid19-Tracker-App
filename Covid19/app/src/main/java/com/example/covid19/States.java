package com.example.covid19;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.icu.text.TimeZoneFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class States extends AppCompatActivity {

    List<Data> heroList;


    //the listview
    ListView listView;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_states);
        DownloadTask task=new DownloadTask();
        heroList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listView);
        task.execute("https://api.rootnet.in/covid19-in/stats/latest");
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
                String re=ob.optString("regional");
                Log.i("Regional content",re);
                JSONArray arr=new JSONArray(re);
                for(int i=0;i<arr.length();i++)
                {
                    //Log.i("dis "," "+i+" "+state);
                    if(i==29)
                    {
                        JSONObject jsonpart=  arr.getJSONObject(i);
                        //Log.i("Loc",jsonpart.getString("loc"));
                        //Log.i("cases",jsonpart.getString("confirmedCasesIndian"));
                        String state=jsonpart.getString("loc");
                        String confiremd=jsonpart.getString("confirmedCasesIndian");
                        String recovered=jsonpart.getString("discharged");
                        String death=jsonpart.getString("deaths");
                        Log.i("dis "," "+i+" "+state);
                        heroList.add(new Data("Uttar Pradesh",confiremd,recovered,death ));
                    }
                    else if(i==30)
                    {
                        JSONObject jsonpart=  arr.getJSONObject(i);
                        //Log.i("Loc",jsonpart.getString("loc"));
                        //Log.i("cases",jsonpart.getString("confirmedCasesIndian"));
                        String state=jsonpart.getString("loc");
                        String confiremd=jsonpart.getString("confirmedCasesIndian");
                        String recovered=jsonpart.getString("discharged");
                        String death=jsonpart.getString("deaths");
                        Log.i("dis "," "+i+" "+state);
                        heroList.add(new Data("Uttrakhand",confiremd,recovered,death ));
                    }
                    else {
                        JSONObject jsonpart = arr.getJSONObject(i);
                        //Log.i("Loc",jsonpart.getString("loc"));
                        //Log.i("cases",jsonpart.getString("confirmedCasesIndian"));
                        String state = jsonpart.getString("loc");
                        String confiremd = jsonpart.getString("confirmedCasesIndian");
                        String recovered = jsonpart.getString("discharged");
                        String death = jsonpart.getString("deaths");
                        Log.i("dis ", " " + i + " " + state);
                        heroList.add(new Data(state, confiremd, recovered, death));
                    }

                }
               Adapter adapter = new Adapter(States.this, R.layout.customlayout, heroList);

                //attaching adapter to the listview
                listView.setAdapter(adapter);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}