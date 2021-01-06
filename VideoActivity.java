package com.example.mobile_computing535;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;

import static com.example.mobile_computing535.LoginActivity.EXTRA_URI;
import static com.example.mobile_computing535.LoginActivity.EXTRA_EMAIL;
import static com.example.mobile_computing535.LoginActivity.EXTRA_ID;
import static com.example.mobile_computing535.MainActivity.EXTRA_TEXT;
import static com.example.mobile_computing535.MainActivity.EXTRA_SERVER_IP;

public class VideoActivity extends AppCompatActivity {

    String old_text = "";
    String path;
    long time_started = 0;
    VideoView vv_learn;
    Spinner ges_list;
    String returnedURI;
    Button bt_practice;
    Button btn_upload;
    String ip;
    String id;

    //    This function sets OnClick Listener for 'Practice' button
    public void pratice_ges(View view) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        101);
            }
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        100);
            }

        } else {
            File f = new File(Environment.getExternalStorageDirectory(), "mobilecomputing535");
            if (!f.exists()) {
                f.mkdirs();
            }
            Intent startPracticeActivity = getIntent();
            String text = startPracticeActivity.getStringExtra(MainActivity.EXTRA_TEXT);
            String user = startPracticeActivity.getStringExtra(LoginActivity.EXTRA_EMAIL);
            String id = startPracticeActivity.getStringExtra(LoginActivity.EXTRA_ID);
            String text1 = startPracticeActivity.getStringExtra(MainActivity.EXTRA_SERVER_IP);

//            On-clicking the practice button Camera interface is opened
            Intent startCameraActivity = new Intent(this, CameraActivity.class);
            startCameraActivity.putExtra(EXTRA_TEXT, text);
            startCameraActivity.putExtra(EXTRA_EMAIL, user);
            startCameraActivity.putExtra(EXTRA_ID, id);
            startCameraActivity.putExtra(EXTRA_SERVER_IP, text1);
            startActivityForResult(startCameraActivity, 9999);
        }

    }

    //  Calling new Intent after recording activity is done
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 9999 && resultCode == 8888) {
            if (data.hasExtra(EXTRA_URI)) {
                returnedURI = data.getStringExtra(EXTRA_URI);
                ip = data.getStringExtra(EXTRA_SERVER_IP);
                id = data.getStringExtra(EXTRA_ID);
                Intent playIntent = new Intent(this, UploadActivity.class);
                playIntent.putExtra("videoUri", returnedURI);
                playIntent.putExtra(EXTRA_SERVER_IP, ip);
                playIntent.putExtra(EXTRA_ID, id);
                startActivity(playIntent);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        btn_upload = (Button) findViewById(R.id.btn_upload);
        btn_upload.setVisibility(View.GONE);
        ges_list = (Spinner) findViewById(R.id.ges_list);
        vv_learn = (VideoView) findViewById(R.id.vv_learn);
        TextView textView = (TextView) findViewById(R.id.textView);
        String head = textView.getText().toString();
        Intent startVideoActivity = getIntent();
        MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (mediaPlayer != null) {
                    mediaPlayer.start();
                }
            }
        };
        vv_learn.setOnCompletionListener(onCompletionListener);
        vv_learn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!vv_learn.isPlaying()) {
                    vv_learn.start();
                }
            }
        });
        String text = startVideoActivity.getStringExtra(MainActivity.EXTRA_TEXT);
        head = head + " -> " + text;
        textView.setText(head);
        if (!old_text.equals(text)) {
            path = "";
            time_started = System.currentTimeMillis();
            play_video(text);
        }
    }

    //  Function to retrieve the video-path from the folder named 'raw'
    public void play_video(String text) {
        old_text = text;
        if (text.equals("arrive")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.arrive;
        } else if (text.equals("buy")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.buy;
        } else if (text.equals("communicate")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.communicate;
        } else if (text.equals("create")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.create;
        } else if (text.equals("drive")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.drive;
        } else if (text.equals("fun")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.fun;
        } else if (text.equals("hope")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.hope;
        } else if (text.equals("house")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.house;
        } else if (text.equals("lip")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.lip;
        } else if (text.equals("man")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.man;
        } else if (text.equals("mother")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.mother;
        } else if (text.equals("mouth")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.mouth;
        } else if (text.equals("one")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.one;
        } else if (text.equals("perfect")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.perfect;
        } else if (text.equals("pretend")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.pretend;
        } else if (text.equals("read")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.read;
        } else if (text.equals("really")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.really;
        } else if (text.equals("sister")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.sister;
        } else if (text.equals("some")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.some;
        } else if (text.equals("write")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.write;
        }
        if (!path.isEmpty()) {
            Uri uri = Uri.parse(path);
            vv_learn.setVideoURI(uri);
            vv_learn.start();
        }

    }
}
