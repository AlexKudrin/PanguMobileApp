package com.example.aleksejs.pangumobileapp;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    String address;

    String myJSON;

    private static final String TAG_RESULTS="result";
    private static final String TAG_ID = "id";
    private static final String TAG_MODEL_NAME = "model_name";
    private static final String TAG_MODEL_IMAGE ="model_image";

    JSONArray models = null;

    String result;

    ArrayList<HashMap<String, String>> modelList;

    ListView list;

    TextView text;

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
        setContentView(R.layout.activity_main);

        text = (TextView) findViewById(R.id.selected);
        list = (ListView) findViewById(R.id.listView);
        modelList = new ArrayList<HashMap<String,String>>();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            address = extras.getString("address");
            Log.v("address : ", address);

            TextView addressBox = (TextView)findViewById(R.id.address);
            addressBox.setText("server address : " + address);
        }

        try {
            String data = "?" +  URLEncoder.encode("address", "UTF-8")
                    + "=" +  URLEncoder.encode(address, "UTF-8");

            URL url = new URL("http://s613660186.websitehome.co.uk/api_models.php" + data);

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

            result = sb.toString();

        }
        catch(Exception e){

        }


        try {
            JSONObject jsonObj = new JSONObject(result);

            models = jsonObj.getJSONArray(TAG_RESULTS);

            for(int i=0;i<models.length();i++){
                JSONObject c = models.getJSONObject(i);
                String id = c.getString(TAG_ID);
                String model_name = c.getString(TAG_MODEL_NAME);
                String model_image = c.getString(TAG_MODEL_IMAGE);

                HashMap<String,String> models = new HashMap<String,String>();

                models.put(TAG_ID,id);
                models.put(TAG_MODEL_NAME,model_name);
                models.put(TAG_MODEL_IMAGE,model_image);

                modelList.add(models);
            }

            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, modelList, R.layout.list_item,
                    new String[]{TAG_ID,TAG_MODEL_NAME,TAG_MODEL_IMAGE},
                    new int[]{R.id.id, R.id.model_name, R.id.model_image}
            );

            list.setAdapter(adapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    TextView item = (TextView) view.findViewById(R.id.model_name);
                    Log.v("item : ", item.getText().toString());

                    text.setText(item.getText().toString());

                }
            });
            

        } catch (JSONException e) {
            e.printStackTrace();
        }


        Button pangu= (Button) findViewById(R.id.button2);
        pangu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getBaseContext(), GamePanel.class);
                i.putExtra("model", text.getText());
                startActivity(i);
            }
        });

    }
}
