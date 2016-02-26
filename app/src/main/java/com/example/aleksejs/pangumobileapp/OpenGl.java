package com.example.aleksejs.pangumobileapp;

import android.app.Activity;
import android.content.Context;
import javax.microedition.khronos.egl.EGLConfig;

import android.content.Intent;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Aleksejs on 18/02/2016.
 */
public class OpenGl extends Activity {

    private GLSurfaceView appView;
    GLRenderer mRenderer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        this.setContentView(R.layout.opengl);

        // making it full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        appView = new GLSurfaceView(this);

        mRenderer = new GLRenderer(this);

        this.appView = new GLSurfaceView(this);
        //this.appView.setGLWrapper(new GLSurfaceView.GLWrapper() {
            //public GL wrap(GL gl) {
               // return new MatrixTrackingGL(gl);
            //}
        //});
        this.appView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        this.appView.setRenderer(mRenderer);
        this.appView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        LinearLayout l = (LinearLayout) findViewById(R.id.Glayout);
        l.addView(appView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        Button pangu= (Button) findViewById(R.id.button);
        pangu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView text = (TextView) findViewById (R.id.textView4);
                Intent i = new Intent(getBaseContext(), GamePanel.class);
                i.putExtra("planet", text.getText());
                startActivity(i);
            }
        });
    }


    @Override
    public boolean onTouchEvent(MotionEvent e) {
        TextView text = (TextView) findViewById (R.id.textView4);
        text.setText(mRenderer.processTouchEvent(e));
        return true;
    }

}

