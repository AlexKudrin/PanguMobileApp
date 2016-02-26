package com.example.aleksejs.pangumobileapp;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.support.v4.view.VelocityTrackerCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Aleksejs on 18/02/2016.
 */
public class GLRenderer implements GLSurfaceView.Renderer {

    /**
     * Perspective setup, field of view component.
     */
    private static final float FIELD_OF_VIEW_Y = 45.0f;

    /**
     * Perspective setup, near component.
     */
    private static final float Z_NEAR = 0.1f;

    /**
     * Perspective setup, far component.
     */
    private static final float Z_FAR = 100.0f;

    /**
     * Object distance on the screen. move it back a bit so we can see it!
     */
    private float OBJECT_DISTANCE = -10.0f;

    /**
     * Planets
     */
    private final Sphere Sun;
    private final Sphere Mercury;
    private final Sphere Venus;
    private final Sphere Earth;
    private final Sphere Mars;
    private final Sphere Jupiter;
    private final Sphere Saturn;
    private final Sphere Uran;
    private final Sphere Neptune;

    /**
     * The context.
     */
    private final Context mContext;


    private VelocityTracker mVelocityTracker = null;


    /**
     * Constructor to set the handed over context.
     *
     * @param context The context.
     */
    public GLRenderer(final Context context) {
        this.mContext = context;
        this.Sun = new Sphere(5, 1.7f);
        this.Mercury = new Sphere(5, 0.5f);
        this.Venus = new Sphere(5, 0.9f);
        this.Earth = new Sphere(5, 1);
        this.Mars = new Sphere(5, 1.1f);
        this.Jupiter = new Sphere(5, 1.6f);
        this.Saturn = new Sphere(5, 1.5f);
        this.Uran = new Sphere(5, 1.3f);
        this.Neptune = new Sphere(5, 1.3f);
    }

    public void onDrawFrame(final GL10 gl) {
        gl.glClearColor(0f, 0f, 0f, 0.0f);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        gl.glTranslatef(-4.0f, 2.0f, OBJECT_DISTANCE);
        this.Sun.draw(gl);
        gl.glLoadIdentity();
        gl.glTranslatef(-1.2f, 2.0f, OBJECT_DISTANCE);
        this.Mercury.draw(gl);
        gl.glLoadIdentity();
        gl.glTranslatef(0.5f, 2.0f, OBJECT_DISTANCE);
        this.Venus.draw(gl);
        gl.glLoadIdentity();
        gl.glTranslatef(2.5f, 2.0f, OBJECT_DISTANCE);
        this.Earth.draw(gl);
        gl.glLoadIdentity();
        gl.glTranslatef(5f, 2.0f, OBJECT_DISTANCE);
        this.Mars.draw(gl);
        gl.glLoadIdentity();
        gl.glTranslatef(-4.5f, -1.6f, OBJECT_DISTANCE);
        this.Jupiter.draw(gl);
        gl.glLoadIdentity();
        gl.glTranslatef(-1.1f, -1.1f, OBJECT_DISTANCE);
        this.Saturn.draw(gl);
        gl.glLoadIdentity();
        gl.glTranslatef(1.8f, -1.1f, OBJECT_DISTANCE);
        this.Uran.draw(gl);
        gl.glLoadIdentity();
        gl.glTranslatef(4.6f, -1.1f, OBJECT_DISTANCE);
        this.Neptune.draw(gl);

    }

    public void onSurfaceChanged(final GL10 gl, final int width, final int height) {
        final float aspectRatio = (float) width / (float) (height == 0 ? 1 : height);

        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU.gluPerspective(gl, FIELD_OF_VIEW_Y, aspectRatio, Z_NEAR, Z_FAR);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void onSurfaceCreated(final GL10 gl, final EGLConfig config) {
        this.Sun.loadGLTexture(gl, this.mContext, R.drawable.sun);
        this.Mercury.loadGLTexture(gl, this.mContext, R.drawable.mercury);
        this.Venus.loadGLTexture(gl, this.mContext, R.drawable.venus);
        this.Earth.loadGLTexture(gl, this.mContext, R.drawable.earth);
        this.Mars.loadGLTexture(gl, this.mContext, R.drawable.mars);
        this.Jupiter.loadGLTexture(gl, this.mContext, R.drawable.jupiter);
        this.Saturn.loadGLTexture(gl, this.mContext, R.drawable.saturn);
        this.Uran.loadGLTexture(gl, this.mContext, R.drawable.uran);
        this.Neptune.loadGLTexture(gl, this.mContext, R.drawable.neptune);
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glClearColor(0f, 0f, 0f, 0.0f);
        gl.glClearDepthf(1.0f);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);

    }

    public String processTouchEvent(MotionEvent e)
    {
        float x = e.getX();
        float y = e.getY();

        int index = e.getActionIndex();
        int action = e.getActionMasked();
        int pointerId = e.getPointerId(index);


        switch (action) {
            case MotionEvent.ACTION_UP:
                if ((x>133 && x<336) && (y>82 && y<277))
                   return "sun";
                if ((x>462 && x<522) && (y>157 && y<211))
                    return "mercury";
                if ((x>579 && x<697) && (y>144 && y<233))
                    return "venus";
                if ((x>746 && x<882) && (y>120 && y<234))
                    return "earth";
                if ((x>954 && x<1112) && (y>115 && y<246))
                    return "mars";

                Log.v("x : ", Float.toString(x));
                Log.v("y : ", Float.toString(y));
                break;
            case MotionEvent.ACTION_MOVE:
                mVelocityTracker.addMovement(e);
                // When you want to determine the velocity, call
                // computeCurrentVelocity(). Then call getXVelocity()
                // and getYVelocity() to retrieve the velocity for each pointer ID.
                mVelocityTracker.computeCurrentVelocity(1000);
                // Log velocity of pixels per second
                // Best practice to use VelocityTrackerCompat where possible.
                Log.d("", "X velocity: " +
                        VelocityTrackerCompat.getXVelocity(mVelocityTracker,
                                pointerId));
                Log.d("", "Y velocity: " +
                        VelocityTrackerCompat.getYVelocity(mVelocityTracker,
                                pointerId));
                break;

        }
        return "select planet";
    }

}
