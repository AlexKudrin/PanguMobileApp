package com.example.aleksejs.pangumobileapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.util.Xml;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Handler;

import uk.ac.dundee.spacetech.pangu.ClientLibrary.ClientConnection;

/**
 * Created by Aleksejs on 05/02/2016.
 */
public class GamePanel extends AppCompatActivity {

    ClientConnection connectToPangu = connectToPangu();

    float x=0, y=0, z=400, yw=0, pt=-90, rl=0;

    float incx=0, incy=0;

    float speedFloat=0;
    float fuelFloat=100;

    ImageView background;
    Bitmap image;

    TextView speed, fuel, message, distance, x2, y2;

    boolean running = true;

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
        message = (TextView)findViewById(R.id.message);
        speed = (TextView)findViewById(R.id.speed);
        fuel = (TextView)findViewById(R.id.fuel);
        x2 = (TextView)findViewById(R.id.x);
        y2 = (TextView)findViewById(R.id.y);

        Thread gameThread = new Thread()
        {
            @Override
            public void run() {
                while (running) {

                    if (z==100) {
                        runOnUiThread(new Runnable() //run on ui thread
                        {
                            public void run() {
                                message.setText("You lost");
                            }
                        });
                        running = false;
                    }

                   // z = z - 1;

                    if (speedFloat!=0) {
                        x = x + (incx * speedFloat);
                        y = y + (incy * speedFloat);
                        z = z - 0.5f;
                        fuelFloat = fuelFloat - (speedFloat/10);
                    }

                    image = getImage(connectToPangu, x, y, z, yw, pt, rl);

                    runOnUiThread(new Runnable() //run on ui thread
                    {
                        public void run() {
                            distance.setText(String.valueOf(z-100));
                            speed.setText(String.valueOf(speedFloat));
                            fuel.setText(String.valueOf(fuelFloat));
                            x2.setText(String.valueOf(x));
                            y2.setText(String.valueOf(y));
                            background.setImageBitmap(image);
                            background.invalidate();
                        }
                    });
                }
            }
        };

        Button left=(Button)findViewById(R.id.left);
        left.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                   yw = yw + 0.5f;
                   incx = incx + 0.5f;
               }
           });

        Button right=(Button)findViewById(R.id.right);
        right.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                       yw = yw - 0.5f;
                       incx = incx + 0.5f;
                   }
               });

        Button up=(Button)findViewById(R.id.up);
        up.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                        pt = pt + 0.5f;
                        incy = incy + 0.5f;
                    }

                });

        Button down=(Button)findViewById(R.id.down);
        down.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                pt = pt - 0.5f;
                incy = incy - 0.5f;
            }

        });

        Button speedup=(Button)findViewById(R.id.speedup);
        speedup.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                speedFloat = speedFloat + 1;
                fuelFloat = fuelFloat - 0.1f;
            }
        });

        Button speeddown=(Button)findViewById(R.id.speeddown);
        speeddown.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                       speedFloat = speedFloat - 1;
                   }
               });

        gameThread.start();
    }

    public ClientConnection connectToPangu(){
        ClientConnection connectionToPanguServer=null;
        try {
            Socket connectionSocket = new Socket("192.168.0.11", 10363);
            connectionToPanguServer = new ClientConnection(connectionSocket);
            Log.v("log : ", "connected");
            return connectionToPanguServer;
        }
        catch (IOException ie) {
            ie.printStackTrace();
        }
        Log.v("log : ", "not connected");
        return connectionToPanguServer;
    }

    public Bitmap getImage(ClientConnection connectToPangu, float x, float y, float z,float yw,float pt,float rl){
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
