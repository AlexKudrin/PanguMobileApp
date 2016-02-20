package com.example.aleksejs.pangumobileapp;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.view.MotionEvent;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Aleksejs on 18/02/2016.
 */
public class GLRenderer implements GLSurfaceView.Renderer {
    /**
     * Tilt the spheres a little.
     */
    private static final int AXIAL_TILT_DEGREES = 30;

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

    /**
     * The context.
     */
    private final Context mContext;

    /**
     * The rotation angle, just to give the screen some action.
     */
    private float mRotationAngle;

    /**
     * Constructor to set the handed over context.
     *
     * @param context The context.
     */
    public GLRenderer(final Context context) {
        this.mContext = context;
        this.Sun = new Sphere(5, 2);
        this.Mercury = new Sphere(5, 0.5f);
        this.Venus = new Sphere(5, 0.9f);
        this.Earth = new Sphere(5, 1);
        this.Mars = new Sphere(5, 1.1f);
        this.mRotationAngle = 0.0f;
    }

    public void onDrawFrame(final GL10 gl) {
        gl.glClearColor(0f, 0f, 0f, 0.0f);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        gl.glTranslatef(-4.0f, 0.0f, OBJECT_DISTANCE);
        gl.glRotatef(AXIAL_TILT_DEGREES, 1, 0, 0);
        gl.glRotatef(this.mRotationAngle++, 0, 1, 0);
        this.Sun.draw(gl);
        gl.glLoadIdentity();
        gl.glTranslatef(-1.2f, 0.0f, OBJECT_DISTANCE);
        gl.glRotatef(AXIAL_TILT_DEGREES, 1, 0, 0);
        gl.glRotatef(this.mRotationAngle++, 0, 1, 0);
        this.Mercury.draw(gl);
        gl.glLoadIdentity();
        gl.glTranslatef(0.5f, 0.0f, OBJECT_DISTANCE);
        gl.glRotatef(AXIAL_TILT_DEGREES, 1, 0, 0);
        gl.glRotatef(this.mRotationAngle++, 0, 1, 0);
        this.Venus.draw(gl);
        gl.glLoadIdentity();
        gl.glTranslatef(2.5f, 0.0f, OBJECT_DISTANCE);
        gl.glRotatef(AXIAL_TILT_DEGREES, 1, 0, 0);
        gl.glRotatef(this.mRotationAngle++, 0, 1, 0);
        this.Earth.draw(gl);
        gl.glLoadIdentity();
        gl.glTranslatef(5f, 0.0f, OBJECT_DISTANCE);
        gl.glRotatef(AXIAL_TILT_DEGREES, 1, 0, 0);
        gl.glRotatef(this.mRotationAngle++, 0, 1, 0);
        this.Mars.draw(gl);
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
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glClearColor(0f, 0f, 0f, 0.0f);
        gl.glClearDepthf(1.0f);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
    }

    float pointers, distance;

}
