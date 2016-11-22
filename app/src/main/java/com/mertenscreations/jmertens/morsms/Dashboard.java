package com.mertenscreations.jmertens.morsms;

import android.app.Activity;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.concurrent.Semaphore;


public class Dashboard extends AppCompatActivity {

    private static String cameraId;
    private static CameraDevice cameraDevice;
    private Semaphore mCameraOpenCloseLock = new Semaphore(1);
    private CameraDevice mCameraDevice;

    private final CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            // This method is called when the camera is opened.  We start camera preview here.
            mCameraOpenCloseLock.release();
            mCameraDevice = cameraDevice;
//            createCameraPreviewSession();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int error) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
//            Activity activity = getActivity();
//            if (null != activity) {
//                activity.finish();
//            }
        }

    };

    private static HashMap<Character, String> codes = new HashMap<Character, String>();
    static{
        codes.put('a', ".-");
        codes.put('b', "-...");
        codes.put('c', "-.-.");
        codes.put('d', "-..");
        codes.put('e', ".");
        codes.put('f', "..-.");
        codes.put('g', "--.");
        codes.put('h', "....");
        codes.put('i', "..");
        codes.put('j', ".---");
        codes.put('k', "-.-");
        codes.put('l', ".-..");
        codes.put('m', "--");
        codes.put('n', "-.");
        codes.put('o', "---");
        codes.put('p', ".--.");
        codes.put('q', "--.-");
        codes.put('r', ".-.");
        codes.put('s', "...");
        codes.put('t', "-");
        codes.put('u', "..-");
        codes.put('v', "...-");
        codes.put('w', ".--");
        codes.put('x', "-..-");
        codes.put('y', "-.--");
        codes.put('z', "--..");
        codes.put('0', "-----");
        codes.put('1', ".----");
        codes.put('2', "..---");
        codes.put('3', "...--");
        codes.put('4', "....-");
        codes.put('5', ".....");
        codes.put('6', "-....");
        codes.put('7', "--...");
        codes.put('8', "---..");
        codes.put('9', "----.");
        codes.put('.', ".-.-.-");
        codes.put(',', "--..--");
        codes.put('?', "..--..");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final EditText edit =  (EditText) findViewById(R.id.writeMessage);
        final TextView tempText = (TextView) findViewById(R.id.tempTextId);

        Button sendButton;
        sendButton = (Button) findViewById(R.id.sendButtonID);

        sendButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                setupCamera2();
                openCameraDevice();
                String unfiltered = edit.getText().toString();
                String filtered = textToMorse(unfiltered);
                tempText.setText(filtered);
//                openCamera2();
            }
        });

//        CameraManager cameraManager = CameraManager();
//        Camera.Parameters p = camera.getParameters();
//        p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
//        camera.setParameters(p);
//        camera.startPreview();
    }

    private String textToMorse(String unfiltered) {
        String filtered = "cameraId: " + this.cameraId;
        for(int i=0; i<unfiltered.length(); i++ ) {
            if(codes.get(unfiltered.toLowerCase().charAt(i))!=null) {
                filtered = filtered + codes.get(unfiltered.toLowerCase().charAt(i));
            }
        }
        return filtered;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupCamera2() {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {

            for (String cameraId : manager.getCameraIdList()) {
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);

                if (characteristics.get(CameraCharacteristics.LENS_FACING) != CameraCharacteristics.LENS_FACING_FRONT) {
                    continue;
                }

                this.cameraId = cameraId;



//                imageReader = ImageReader.newInstance(picWidth, picHeight, ImageFormat.JPEG, 2);
//                imageReader.setOnImageAvailableListener(onImageAvailableListener, backgroundHandler);
            }

        } catch (CameraAccessException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void openCameraDevice() {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            manager.setTorchMode("1", true);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

}
