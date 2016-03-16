package com.example.aleksejs.pangumobileapp;

import com.example.aleksejs.pangumobileapp.JoystickView;
import com.example.aleksejs.pangumobileapp.JoystickView.OnJoystickMoveListener;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.Socket;
import android.os.Handler;

import uk.ac.dundee.spacetech.pangu.ClientLibrary.ClientConnection;

/**
 * Created by Aleksejs on 05/02/2016.
 */
public class GamePanel extends AppCompatActivity {

    ClientConnection connectToPangu = connectToPangu();

    static float x=0, y=600, z=0, yw=0, pt=-180, rl=0;

    static float incpt=0, incyw=0, speed=0;

    static ImageView background;
    static Bitmap image;

    static private JoystickView joystick;

    static TextView distance, speedText;

    static boolean running = true;

    String model, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // making it full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.game);

        background = (ImageView)findViewById(R.id.background);
        distance = (TextView)findViewById(R.id.distance);
        speedText = (TextView)findViewById(R.id.speed);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            model = extras.getString("model");
            Log.v("model : ", model);

            address = extras.getString("address");
            Log.v("address : ", address);
        }


        Thread gameThread = new Thread()
        {
            @Override
            public void run() {
                while (running) {

                    if (speed>0) {
                        y = y - speed * ( (float) Math.cos(Math.toRadians(incpt)) * (float) Math.cos(Math.toRadians(incyw)));
                        z = z + speed * ( (float) Math.sin(Math.toRadians(incpt)) * (float) Math.cos(Math.toRadians(incyw)));
                        x = x + speed * ( (float) Math.sin(Math.toRadians(incpt)) );
                    }
                    else if (speed<0){
                        y = y + speed * ( (float) Math.cos(Math.toRadians(incpt)) * (float) Math.cos(Math.toRadians(incyw)) );
                        z = z - speed * (float) Math.sin(Math.toRadians(incpt));
                        x = x - speed * (float) Math.sin(Math.toRadians(incyw));
                    }

                    image = getImage(connectToPangu, x, y, z, yw, pt, rl);

                    runOnUiThread(new Runnable() //run on ui thread
                    {
                        public void run() {
                            distance.setText(String.valueOf(y-100));
                            speedText.setText(String.valueOf(speed));
                            background.setImageBitmap(image);
                            background.invalidate();
                        }
                    });
                }
            }
        };

        Button speedup = (Button) findViewById(R.id.speedup);
        speedup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                speed++;
            }
        });

        Button speeddown = (Button) findViewById(R.id.speeddown);
        speeddown.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                speed--;
            }
        });

        joystick = (JoystickView) findViewById(R.id.joystickView);

        joystick.setOnJoystickMoveListener(new OnJoystickMoveListener() {
            @Override
            public void onValueChanged(int angle, int power, int direction) {
                switch (direction) {
                    case JoystickView.FRONT:
                        incpt = incpt + 0.1f;
                        pt = pt + 0.1f;
                        break;

                    case JoystickView.FRONT_RIGHT:
                        incpt = incpt + 0.1f;
                        incyw = incyw + 0.1f;
                        pt = pt + 0.1f;
                        yw = yw - 0.1f;
                        break;

                    case JoystickView.RIGHT:
                        incyw = incyw + 0.1f;
                        yw = yw - 0.1f;
                        break;

                    case JoystickView.RIGHT_BOTTOM:
                        incpt = incpt - 0.1f;
                        incyw = incyw + 0.1f;
                        pt = pt - 0.1f;
                        yw = yw - 0.1f;
                        break;

                    case JoystickView.BOTTOM:
                        incpt = incpt - 0.1f;
                        pt = pt - 0.1f;
                        break;

                    case JoystickView.BOTTOM_LEFT:
                        incpt = incpt - 0.1f;
                        incyw = incyw - 0.1f;
                        pt = pt - 0.1f;
                        yw = yw + 0.1f;
                        break;

                    case JoystickView.LEFT:
                        incyw = incyw - 0.1f;
                        yw = yw + 0.1f;
                        break;

                    case JoystickView.LEFT_FRONT:
                        incpt = incpt + 0.1f;
                        incyw = incyw - 0.1f;
                        pt = pt + 0.1f;
                        yw = yw + 0.1f;
                        break;
                }
            }
        }, JoystickView.DEFAULT_LOOP_INTERVAL);

        gameThread.start();
    }

    static public ClientConnection connectToPangu(){
        ClientConnection connectionToPanguServer=null;
        try {
            Socket connectionSocket = new Socket("10.0.2.2", 10363);
            connectionToPanguServer = new ClientConnection(connectionSocket);
            Log.v("log : ", "COOOONNEEEECTED");
            return connectionToPanguServer;
        }
        catch (IOException ie) {
            ie.printStackTrace();
        }
        Log.v("log : ", "not COOOONNEEEECTED");
        return connectionToPanguServer;
    }

    static public Bitmap getImage(ClientConnection connectToPangu, float x, float y, float z,float yw,float pt,float rl){
        Bitmap bitmapImage = null;
        try {
            byte[] imagebyte = connectToPangu.getImageByDegrees(x, y, z, yw, pt, rl);
            bitmapImage = ReadBitmapFromPPM(imagebyte);
            return bitmapImage;
        }
        catch (IOException ie) {
            ie.printStackTrace();
        }
        Log.v("log : ", "image failed");
        return bitmapImage;
    }

    public static Bitmap ReadBitmapFromPPM(byte[] file) throws IOException {

        int filelength = file.length-3;
        int width = 512;
        int height = 512;
        int[] colors = new int[width * height];

        byte [] pixel = new byte[3];
        int cnt = 0;
        int total = 0;
        int[] rgb = new int[3];
        int j = 14;
        while (j < filelength) {
            pixel[0]=file[j];
            pixel[1]=file[j+1];
            pixel[2]=file[j+2];
            j = j+3;
            for (int i = 0; i < pixel.length; i ++) {
                rgb[cnt] = pixel[i]>=0?pixel[i]:(pixel[i] + 255);
                if ((++cnt) == 3) {
                    cnt = 0;
                    colors[total++] = Color.rgb(rgb[0], rgb[1], rgb[2]);
                }
            }
        }

        Bitmap bmp = Bitmap.createBitmap(colors, width, height, Bitmap.Config.ARGB_8888);
        return bmp;
    }

}
