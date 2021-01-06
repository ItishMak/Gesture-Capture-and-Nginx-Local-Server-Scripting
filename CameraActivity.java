package com.example.mobile_computing535;
import java.io.File;
import java.io.IOException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import static com.example.mobile_computing535.LoginActivity.EXTRA_ID;
import static com.example.mobile_computing535.LoginActivity.EXTRA_EMAIL;
import static com.example.mobile_computing535.MainActivity.EXTRA_SERVER_IP;
import static com.example.mobile_computing535.LoginActivity.EXTRA_URI;
import static com.example.mobile_computing535.MainActivity.EXTRA_TEXT;

public class CameraActivity extends Activity implements SurfaceHolder.Callback {

    private MediaRecorder mMediaRecorder;
    private Camera mCamera;
    String returnedURI;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mHolder;
    private Button mToggleButton;
    private TextView tv_timer;
    private TextView tv_time;
    Intent returnIntent;
    String returnfile;
    CameraActivity activity;
    String word;
    String ip;
    String id;
    String user;
    private boolean mInitSuccesful;
    SharedPreferences sharedPreferences;
    CountDownTimer timer;
    CountDownTimer time;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        activity = this;
        returnIntent = new Intent();
        // we shall take the video in landscape orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);
        if(getIntent().hasExtra(EXTRA_TEXT)) {
            word = getIntent().getStringExtra(EXTRA_TEXT);
        }
        if(getIntent().hasExtra(EXTRA_SERVER_IP)) {
            ip = getIntent().getStringExtra(EXTRA_SERVER_IP);
        }
        if(getIntent().hasExtra(EXTRA_EMAIL)) {
            user = getIntent().getStringExtra(EXTRA_EMAIL);
        }
        if(getIntent().hasExtra(EXTRA_ID)) {
            id = getIntent().getStringExtra(EXTRA_ID);
        }
//        Log.i("Message11",id);
        mSurfaceView = (SurfaceView) findViewById(R.id.sv_camera);
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
        tv_timer = (TextView) findViewById(R.id.tv_timer);
        tv_time = (TextView) findViewById(R.id.tv_time);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        sharedPreferences =  this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        mToggleButton = (Button) findViewById(R.id.bt_start);
        time = new CountDownTimer(6000,1000) {
            @Override
            public void onTick(long l) {
                int a = (int) (l / 1000);
                tv_time.setText(a + " ");
            }

            @Override
            public void onFinish() {
                mMediaRecorder.stop();
                mMediaRecorder.reset();
                if(time!=null) {
                    time.cancel();
                }
                returnIntent.putExtra(EXTRA_URI,returnfile);
                returnIntent.putExtra(EXTRA_SERVER_IP,ip);
                returnIntent.putExtra(EXTRA_ID,id);
                activity.setResult(8888,returnIntent);
                activity.finish();
            }
        };

//        Adding toggle functionality and count-down timer to start and stop the recording
        mToggleButton.setOnClickListener( new OnClickListener() {
            @Override
            // toggle video recording
            public void onClick(final View v) {
                timer = new CountDownTimer(5000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        int a = (int) (millisUntilFinished / 1000);
                        tv_timer.setText(a + " ");
                        ((Button) v).setEnabled(false);
                    }
                    public void onFinish() {
                        tv_timer.setVisibility(View.GONE);
                        ((Button) v).setText("Stop Recording");
                        ((Button) v).setEnabled(true);
                        try {

                            mMediaRecorder.prepare();
                        }catch (IOException e) {
                            e.printStackTrace();
                        }
                        mMediaRecorder.start();
                        time.start();
                    }
                };
                if (((Button) v).getText().toString().equals("Start Recording")) {
                    timer.start();
                }
                else if (((Button) v).getText().toString().equals("Stop Recording")) {
                    mMediaRecorder.stop();
                    mMediaRecorder.reset();
                    ((Button) v).setText("Start Recording");
                    if(time!=null) {
                        time.cancel();
                    }
                    returnIntent.putExtra(EXTRA_URI,returnfile);
                    returnIntent.putExtra(EXTRA_SERVER_IP,ip);
                    returnIntent.putExtra(EXTRA_ID,id);
                    activity.setResult(8888,returnIntent);
                    activity.finish();

                }
            }
        });
    }

    boolean fileCreated = false;
    private void initRecorder(Surface surface) throws IOException {
        if(mCamera == null) {
            mCamera = Camera.open(1);
            mCamera.setDisplayOrientation(90);
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
            mCamera.unlock();

        }

        if(mMediaRecorder == null)
            mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setPreviewDisplay(surface);
        mMediaRecorder.setCamera(mCamera);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);

        int i=0;

//        Creating the directory to store the recorded-video files

        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/mobilecomputing535/"
                +word+"_PRACTICE_"+user+ ".mp4");
        //just to be safe
        while(file.exists()) {
            i++;
            file = new File(Environment.getExternalStorageDirectory().getPath() + "/mobilecomputing535/"
                    +word+"_PRACTICE_"+String.valueOf(i)+"_"+user+ ".mp4");
        }

        if(file.createNewFile()) {
            fileCreated = true;
            Log.e("file path",file.getPath());
            returnfile = file.getPath();
        }


        mMediaRecorder.setOutputFile(file.getPath());
        mMediaRecorder.setMaxDuration(5000);
        mMediaRecorder.setVideoSize(320,240);
        mMediaRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
            @Override
            public void onInfo(MediaRecorder mediaRecorder, int i, int i1) {
                if (i == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
                    mMediaRecorder.stop();
                    mMediaRecorder.reset();
                    if(time!=null) {
                        time.cancel();
                    }
                    returnIntent.putExtra(EXTRA_URI,returnfile);
                    returnIntent.putExtra(EXTRA_SERVER_IP,ip);
                    returnIntent.putExtra(EXTRA_ID,id);
                    activity.setResult(8888,returnIntent);
                    activity.finish();
                }

            }
        });


        mMediaRecorder.setOrientationHint(270);
        mMediaRecorder.setVideoFrameRate(50);
        mMediaRecorder.setVideoEncodingBitRate(3000000);
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
//        try {
//
//            mMediaRecorder.prepare();
//        } catch (IllegalStateException e) {
//            e.printStackTrace();
//        }

        mInitSuccesful = true;
    }


    @Override
    public void onBackPressed() {
        if(timer!=null)
            timer.cancel();
        if(time!=null)
            time.cancel();


        returnIntent.putExtra(EXTRA_URI,returnfile);
        returnIntent.putExtra(EXTRA_SERVER_IP,ip);
        returnIntent.putExtra(EXTRA_ID,id);
        activity.setResult(7777,returnIntent);
        activity.finish();

        super.onBackPressed();
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        try {
            if(!mInitSuccesful)
                initRecorder(mHolder.getSurface());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {}

    private void shutdown() {
        mMediaRecorder.reset();
        mMediaRecorder.release();
        mCamera.release();
        mMediaRecorder = null;
        returnIntent.putExtra(EXTRA_URI,returnfile);
        returnIntent.putExtra(EXTRA_SERVER_IP,ip);
        returnIntent.putExtra(EXTRA_ID,id);
        activity.setResult(7777,returnIntent);
        mCamera = null;
        timer.cancel();
        finish();
    }

}