package com.example.aleksejs.pangumobileapp;

import com.example.aleksejs.pangumobileapp.JoystickView;
import com.example.aleksejs.pangumobileapp.JoystickView.OnJoystickMoveListener;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.Socket;

import java.lang.Math;
import android.os.Handler;

import uk.ac.dundee.spacetech.pangu.ClientLibrary.ClientConnection;

/**
 * Created by Aleksejs on 05/02/2016.
 */
public class StandartFlight extends AppCompatActivity {

    ClientConnection connectToPangu;

    static float x=0, y=0, z=0, yw=0, pt=-180, rl=0;

    static float incpt=0, incyw=0, speed=0, speedInc;

    static double q0t=0, q1t=0, q2t=0, q3t=0;

    static ImageView background;
    static Bitmap image;

    static private JoystickView joystick;

    static TextView distance, descriptionView;

    static boolean connected;

    static String model, address, port, description;

    static int object;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Log.v("connected : ", String.valueOf(connected));

        // making it full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.standard);

        background = (ImageView)findViewById(R.id.background);
        distance = (TextView)findViewById(R.id.distance);
        descriptionView = (TextView)findViewById(R.id.textView3);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            model = extras.getString("model");
            Log.v("model : ", model);

            y =  Float.parseFloat(extras.getString("distance"));
            speedInc = Float.parseFloat(extras.getString("speed"));

            object = Integer.parseInt(extras.getString("pangu_id"));

            address = extras.getString("address");
            port = extras.getString("port");

            address = "192.168.0.5";

            Log.v("address : ", address);
            Log.v("port : ", port);

            description = extras.getString("description");
        }

        connectToPangu = connectToPangu();

        try {
            connectToPangu.setObjectView(object, 1);
        }
        catch (IOException ie) {
            ie.printStackTrace();
        }

        descriptionView.setText(description);


        setImage(connectToPangu);


            Button speedup = (Button) findViewById(R.id.speedup);
            speedup.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    y = y - speedInc*10;
                    distance.setText(String.valueOf(y - speedInc*10));
                    setImage(connectToPangu);
                }
            });

            Button speeddown = (Button) findViewById(R.id.speeddown);
            speeddown.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    y = y + speedInc*10;
                    distance.setText(String.valueOf(y - speedInc*10));
                    setImage(connectToPangu);
                }
            });


        Button up = (Button) findViewById(R.id.up);
        up.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getImageStandartMode(connectToPangu, 0, 1, 0);
                setImage(connectToPangu);

            }
        });

        Button down = (Button) findViewById(R.id.down);
        down.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getImageStandartMode(connectToPangu, 0, -1, 0);
                setImage(connectToPangu);

            }
        });

        Button right = (Button) findViewById(R.id.right);
        right.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getImageStandartMode(connectToPangu, 0, 0, 1);
                setImage(connectToPangu);

            }
        });

        Button left = (Button) findViewById(R.id.left);
        left.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getImageStandartMode(connectToPangu, 0, 0, -1);
                setImage(connectToPangu);

            }
        });

        Button back = (Button) findViewById(R.id.button4);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    connectToPangu.setObjectView(object, 0);
                    connectToPangu.stop();
                } catch (IOException ie) {
                    ie.printStackTrace();
                }

                background.setImageResource(0);
                background.invalidate();

                finish();

                Intent i = new Intent(getBaseContext(), MainActivity.class);
                i.putExtra("address", "192.168.0.11");
                i.putExtra("port", port);
                startActivity(i);

            }
        });

    }

    static public void setImage(ClientConnection connectToPangu){
        image = getImage(connectToPangu, x, y, z, yw, pt, rl);
        background.setImageBitmap(image);
        background.invalidate();
    }

    static public ClientConnection connectToPangu(){
        ClientConnection connectionToPanguServer=null;
        try {
            Socket connectionSocket = new Socket(address, Integer.parseInt(port));
            connectionToPanguServer = new ClientConnection(connectionSocket);
            Log.v("log : ", "connected to server");
            return connectionToPanguServer;
        }
        catch (IOException ie) {
            ie.printStackTrace();
        }
        connected = true;
        Log.v("log : ", "not connected to server");
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

    static public void getImageStandartMode(ClientConnection connectToPangu, int xa, int ya, int za){
        double q0, q1, q2, q3;

        q0 = (double) Math.cos(1.5708 / 2);
        q1 = (double) xa*Math.sin(1.5708 / 2);
        q2 = (double) ya*Math.sin(1.5708 / 2);
        q3 = (double) za*Math.sin(1.5708 / 2);

        q0t = q0t + q0;
        q1t = q1t + q1;
        q2t = q2t + q2;
        q3t = q3t + q3;

        try {
            connectToPangu.setObjectPositionAttitude(object, 0, 0, 0, q0t, q1t, q2t, q3t);
        }
        catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    public static Bitmap ReadBitmapFromPPM(byte[] file) throws IOException {

        int filelength = file.length-3;
        int width = 1180;
        int height = 800;
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
