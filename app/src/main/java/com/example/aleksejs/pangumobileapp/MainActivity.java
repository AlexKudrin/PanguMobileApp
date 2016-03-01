package com.example.aleksejs.pangumobileapp;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

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

        ImageView sun = (ImageView)findViewById(R.id.sun);
        sun.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Button selected = (Button) findViewById(R.id.button2);
                selected.setText("SUN");
                return false;
            }
        });
        ImageView mercury = (ImageView)findViewById(R.id.mercury);
        mercury.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                Button selected = (Button)findViewById(R.id.button2);
                selected.setText("MERCURY");
                return false;
            }
        });
        ImageView venus = (ImageView)findViewById(R.id.venus);
        venus.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                Button selected = (Button)findViewById(R.id.button2);
                selected.setText("VENUS");
                return false;
            }
        });
        ImageView earth = (ImageView)findViewById(R.id.earth);
        earth.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                Button selected = (Button)findViewById(R.id.button2);
                selected.setText("EARTH");
                return false;
            }
        });
        ImageView mars = (ImageView)findViewById(R.id.mars);
        mars.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                Button selected = (Button)findViewById(R.id.button2);
                selected.setText("MARS");
                return false;
            }
        });
        ImageView jupiter = (ImageView)findViewById(R.id.jupiter);
        jupiter.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                Button selected = (Button)findViewById(R.id.button2);
                selected.setText("JUPITER");
                return false;
            }
        });

        Button pangu= (Button) findViewById(R.id.button2);
        pangu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button text = (Button) findViewById(R.id.button2);
                Intent i = new Intent(getBaseContext(), GamePanel.class);
                i.putExtra("planet", text.getText());
                startActivity(i);
            }
        });

    }
}
