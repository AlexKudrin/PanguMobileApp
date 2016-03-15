package com.example.aleksejs.pangumobileapp;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Aleksejs on 13/03/2016.
 */
public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // making it full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // set our MainGamePanel as the View
        setContentView(R.layout.login);

        Button connect = (Button)findViewById(R.id.button3);
        connect.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                EditText addressBox = (EditText)findViewById(R.id.address);
                EditText passwordBox = (EditText)findViewById(R.id.password);

                String address = addressBox.getText().toString();
                String password = passwordBox.getText().toString();
                try {
                    String data = "?" +  URLEncoder.encode("address", "UTF-8")
                            + "=" +  URLEncoder.encode(address, "UTF-8");
                    data += "&" + URLEncoder.encode("password", "UTF-8")
                            + "=" + URLEncoder.encode(password, "UTF-8");

                    URL url = new URL("http://s613660186.websitehome.co.uk/api_login.php" + data);

                    URLConnection conn = url.openConnection();

                    Log.v("data : ", data);

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write( data );
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while((line = reader.readLine()) != null)
                    {
                        sb.append(line);
                        break;
                    }


                    JSONObject jObject = new JSONObject(sb.toString());

                    String result = jObject.getString("success");

                    if (result=="1"){
                        Intent i = new Intent(getBaseContext(), MainActivity.class);
                        i.putExtra("address", address);
                        startActivity(i);
                    }
                    else{
                        TextView message = (TextView)findViewById(R.id.textView6);
                        message.setText("Wrong address or password");
                    }

                }
                catch(Exception e){

                }


                return false;
    }});
    }

}
