package com.example.aleksejs.pangumobileapp;

import android.app.Activity;
import android.content.Context;
import javax.microedition.khronos.egl.EGLConfig;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.WindowManager;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Aleksejs on 18/02/2016.
 */
public class OpenGl extends Activity {

    private GLSurfaceView appView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // making it full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        appView = new GLSurfaceView(this);

        this.appView = new GLSurfaceView(this);
        this.appView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        this.appView.setRenderer(new GLRenderer(this));
        this.setContentView(this.appView);

    }

}

