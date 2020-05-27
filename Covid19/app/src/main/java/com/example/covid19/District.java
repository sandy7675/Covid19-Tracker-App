package com.example.covid19;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

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

public class District extends AppCompatActivity {

    TextView t1,con,rec,dethh;
    String position,state1;
    LinearLayout linearLayout;
    private RequestQueue requestQueue;
    List<Data> heroList;
    TextView t;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_district);
        heroList = new ArrayList<>();
        t = findViewById(R.id.t11);
        con = findViewById(R.id.cc);
        rec = findViewById(R.id.rr);
        dethh = findViewById(R.id.dd);
        //linearLayout=findViewById(R.id.ll);
        //linearLayout.animate().rotationXBy(360).setDuration(500);
        listView = (ListView) findViewById(R.id.listView2);
        //DownloadTask task=new DownloadTask();
        requestQueue = Volley.newRequestQueue(this);
        position = getIntent().getStringExtra("state");
        state1 = getIntent().getStringExtra("state_name");
        String cfrm = getIntent().getStringExtra("confirm");
        String reco = getIntent().getStringExtra("Recover");
        String deth = getIntent().getStringExtra("Deaths");
        con.setText(cfrm);
        rec.setText(reco);
        dethh.setText(deth);
        t.setText("Selected state : " + state1);
        if (state1.equals("Uttrakhand"))
            state1 = "Uttarakhand";
        if (state1.equals("Telengana"))
            state1 = "Telangana";

        //task.execute("https://api.covid19india.org/state_district_wise.json");
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "https://api.covid19india.org/v2/state_district_wise.json", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                //int i= Integer.parseInt(position);

                try {
                    //int i=Integer.valueOf(position);
                    for (int i = 0; i < 32; i++) {
                        JSONObject js = response.optJSONObject(i);
                        String namae = js.getString("state");
                        Log.i("selected", namae);
                        Log.i("selected state", state1);
                        if (namae.equals(state1)) {
                            JSONArray arr = js.getJSONArray("districtData");
                            for (int ii = 0; ii < arr.length(); ii++) {

                                JSONObject js1 = arr.optJSONObject(ii);
                                Log.i("distreic", js1.getString("district"));
                                Log.i("active", js1.getString("active"));
                                Log.i("confirmed", js1.getString("confirmed"));
                                Log.i("deceased", js1.getString("deceased"));
                                Log.i("recovered", js1.getString("recovered"));

                                String dis = js1.getString("district");
                                String con = js1.getString("confirmed");
                                String recovered = js1.getString("recovered");
                                String deth = js1.getString("deceased");


                                heroList.add(new Data(dis, con, recovered, deth));

                            }
                            Adapter2 adapter = new Adapter2(District.this, R.layout.customlayout, heroList);
                            listView.setAdapter(adapter);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(jsonArrayRequest);
    }
}
