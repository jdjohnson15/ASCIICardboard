package com.example.jessejohnson.cardboardtext;

import android.content.Context;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES20;

import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Jesse Johnson on 10/19/2015.
 */
public abstract class CameraActivity extends SurfaceView implements SurfaceHolder.Callback{

    private Camera mCamera;
    private Camera.Parameters parameters;
    private static final int GL_TEXTURE_EXTERNAL_OES = 0x8D65;
    private int[] pixels;

    private int frameWidth, frameHeight, texture;

    private SurfaceTexture dummySurface;

    private TextView lText, rText;

    private AsciiActivity ascii;

    public CameraActivity(Context context, TextView l, TextView r) {
        super(context);
        lText = l;
        rText = r;
        ascii = new AsciiActivity();
    }

    public void startCamera() {
        dummySurface = new SurfaceTexture(texture);

        mCamera = Camera.open();
        parameters = mCamera.getParameters();
        List<Camera.Size> list = parameters.getSupportedPictureSizes();
        Camera.Size size = list.get(0);
        int minSize = 999999;
        for (int i = 0; i < list.size(); i++) {

            if (list.get(i).height + list.get(i).width < minSize) {
                size = list.get(i);
                minSize = list.get(i).height + list.get(i).width;
            }
        }
        parameters.setPreviewSize(size.width, size.height);
        parameters.setExposureCompensation(parameters.getMaxExposureCompensation());
        mCamera.setParameters(parameters);
        frameHeight = mCamera.getParameters().getPreviewSize().height;
        frameWidth = mCamera.getParameters().getPreviewSize().width;
        pixels = new int[frameWidth * frameHeight];

        mCamera.setPreviewCallback(
                new Camera.PreviewCallback() {
                    @Override
                    public void onPreviewFrame(byte[] data, Camera camera) {
                        decodeYUV420SP(pixels, data, frameWidth, frameHeight);
                    }
                });

        try {
            mCamera.setPreviewTexture(dummySurface);
            mCamera.startPreview();

        } catch (IOException ioe) {
            Log.v("MainActivity", "CAM LAUNCH FAILED");
        }
    }

    private void decodeYUV420SP(int[] l, byte[] yuv420sp, int width, int height) {

        int frameSize = width * height;
        int y;
        for (int i = 0; i < frameSize; i++) {
            y = (0xff & ((int) yuv420sp[i])) - 16;
            if (y < 0)
                y = 0;
            else if (y > 255)
                y = 255;
            l[i] = y;
        }
        lText.setText(ascii.convert(l, width, height));
        rText.setText(ascii.convert(l, width, height));

    }

    public int[] getPixels(){
        return pixels;
    }

    public int getFrameHeight(){
        return frameHeight;
    }
    public int getFrameWidth(){
        return frameWidth;
    }
    static private int createTexture()
    {
        int[] texture = new int[1];

        GLES20.glGenTextures(1, texture, 0);
        GLES20.glBindTexture(GL_TEXTURE_EXTERNAL_OES, texture[0]);
        GLES20.glTexParameterf(GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameterf(GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameteri(GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

        return texture[0];
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        texture = createTexture();
        startCamera();
    }


    public void surfaceDestroyed(SurfaceHolder holder) {

        if (mCamera != null) {
            synchronized (this) {
                mCamera.stopPreview();
                mCamera.setPreviewCallback(null);
                mCamera.release();
                mCamera = null;
            }
        }
        onPreviewStopped();
    }

    protected void onPreviewStopped() {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

    }
}
