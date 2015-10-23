package com.example.jessejohnson.cardboardtext;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.opengl.GLES20;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MainActivity extends Activity {

    private CameraActivity camera;

    public TextView rText;
    public TextView lText;
    private int window_width;
    private int window_height;
    private float font_size;

    private AsciiActivity ascii;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ascii = new AsciiActivity();
        setContentView(R.layout.activity_main);

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        window_width = size.x;
        window_height = size.y;
        font_size = window_height/(480/ascii.getResY());


        lText = (TextView)findViewById(R.id.lText);
        lText.setTextSize(TypedValue.COMPLEX_UNIT_PX, font_size);
        lText.setTypeface(Typeface.MONOSPACE, 0);
        lText.setGravity(Gravity.FILL);

        rText = (TextView)findViewById(R.id.rText);
        rText.setTextSize(TypedValue.COMPLEX_UNIT_PX, font_size);
        rText.setTypeface(Typeface.MONOSPACE, 0);
        rText.setGravity(Gravity.FILL);

        lText.setBackgroundColor(Color.rgb(255, 255, 255));
        rText.setBackgroundColor(Color.rgb(255, 255, 255));

        lText.setTextColor(Color.rgb(10, 10, 10));
        rText.setTextColor(Color.rgb(10, 10, 10));

        camera = new CameraActivity(this, lText, rText) {};
        camera.startCamera();
    }
}