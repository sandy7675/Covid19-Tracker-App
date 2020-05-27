package com.example.covid19;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class SpeechToText_TextToSpeech extends AppCompatActivity {

    TextView outputText,loc,conn,recc,dethh;
    TextToSpeech textToSpeech;
    Button b1;
    private RequestQueue requestQueue;
    int c=0;
    String text="null",out="Please say valid district name";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_to_text__text_to_speech);
        b1=findViewById(R.id.b1);
        btnSpeech();
        requestQueue = Volley.newRequestQueue(this);
        loc=findViewById(R.id.loc);
        conn=findViewById(R.id.confirm);
        recc=findViewById(R.id.Recovered1);
        dethh=findViewById(R.id.Death);
        //outputText=(TextView)findViewById(R.id.txt_output);
        textToSpeech=new TextToSpeech(getApplicationContext(), new
                TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int i) {
                        if(i==TextToSpeech.SUCCESS){
                            int lang=textToSpeech.setLanguage(Locale.ENGLISH);
                        }
                    }
                });
        clicked();
    }

    private void clicked() {
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSpeech();
            }
        });
    }

    public void btnSpeech() {
        Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"hi Speak Something");
        try {
            startActivityForResult(intent,1);
        }catch (ActivityNotFoundException e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK && null!=data){
                    ArrayList<String> result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    text=result.get(0);
                }
                break;

        }

        if(text.equals("null"))
        {
            Intent intent=new Intent(SpeechToText_TextToSpeech.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            jason();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                    speek();
                }
            }, 500);
        }

    }

    private void jason() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "https://api.covid19india.org/v2/state_district_wise.json", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                //int i= Integer.parseInt(position);

                try {
                    //int i=Integer.valueOf(position);
                    for (int i = 0; i < 32; i++) {
                        JSONObject js = response.optJSONObject(i);
                        String namae = js.getString("state");
                        //if (namae.equals(state1)) {
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
                            //Log.i(text,out);
                            Log.i("District",text);
                            if(dis.equals(text))
                            {
                                out="Total case= "+con+" \nRecovered= "+recovered+" \nDeath= "+deth;
                                //outputText.setText(out);
                                c++;
                                Log.i(text,out);
                                loc.setText(dis);
                                conn.setText(con);
                                recc.setText(recovered);
                                dethh.setText(deth);
                            }

                        }
                    }
                    //}
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

    private void speek() {

        if(c!=0) {
            int speech = textToSpeech.speak(out, TextToSpeech.QUEUE_FLUSH, null);
            out="Please say valid district";
            c=0;
            text="null";
        }
        //else if(text.equals("null"))
        //{
          //  Intent intent=new Intent(SpeechToText_TextToSpeech.this,MainActivity.class);
            //startActivity(intent);
        //}
        else {
            int speech = textToSpeech.speak(out, TextToSpeech.QUEUE_FLUSH, null);
            Intent intent=new Intent(SpeechToText_TextToSpeech.this,MainActivity.class);
            startActivity(intent);
        }
    }
}
