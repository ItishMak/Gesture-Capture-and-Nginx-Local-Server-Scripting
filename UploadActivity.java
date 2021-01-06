package com.example.mobile_computing535;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.EasyPermissions.PermissionCallbacks;


public class UploadActivity extends AppCompatActivity implements PermissionCallbacks {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String SERVER_PATH = "http://10.218.107.121/cse535/upload_video.php";

    Button uploadBtn;
    Button rejectBtn;
    VideoView vv_practice;
    public File file;
    public String group_id = "32";
    public String ip;
    public String toogle;
    public String asu_id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        vv_practice = findViewById(R.id.vv_practice);
        final Uri videoUri = Uri.parse(getIntent().getExtras().getString("videoUri"));
        Intent server = getIntent();
        ip = server.getStringExtra(MainActivity.EXTRA_SERVER_IP);
        asu_id = server.getStringExtra(LoginActivity.EXTRA_ID);
        vv_practice.setVideoURI(videoUri);
        vv_practice.start();
        MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (mediaPlayer!=null){
                    mediaPlayer.start();
                }
            }
        };
        uploadBtn = findViewById(R.id.bt_upload);
        rejectBtn = findViewById(R.id.bt_reject);
        vv_practice.setOnCompletionListener(onCompletionListener);
        vv_practice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!vv_practice.isPlaying()) {
                    vv_practice.start();
                }
            }
        });
        String filePath = getRealPathFromURIPath(videoUri, UploadActivity.this);
        file = new File(filePath);

//       Adding upload functionality to Upload button and setting toggle to 1
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (file != null) {
                    toogle ="1";
//                    Calling Function to upload the video
                    UploadAsyncTask uploadAsyncTask = new UploadAsyncTask(UploadActivity.this);
                    uploadAsyncTask.execute();

                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please select a file first", Toast.LENGTH_LONG).show();

                }

            }

        });
//      Adding reject functionality to reject button and setting toggle to 2
        rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (file != null) {
                    toogle ="2";
                    UploadAsyncTask uploadAsyncTask = new UploadAsyncTask(UploadActivity.this);
                    uploadAsyncTask.execute();

                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please select a file first", Toast.LENGTH_LONG).show();
                }

            }
        });

        uploadBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(UploadActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            }
        });


    }
//  Function to get the real path from the video-uri
    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        String realPath = "";
        if (cursor == null) {
            realPath = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            realPath = cursor.getString(idx);
        }
        if (cursor != null) {
            cursor.close();
        }

        return realPath;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, UploadActivity.this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
//        Log.d(TAG, "Permission has been denied");
    }


    private class UploadAsyncTask extends AsyncTask<Void, Integer, String> {


//        Adding httpClient to provide gateway to upload the videos
        HttpClient httpClient = new DefaultHttpClient();
        private Context context;
        private Exception exception;
        private ProgressDialog progressDialog;

        private UploadAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... params) {

            HttpResponse httpResponse = null;
            HttpEntity httpEntity = null;
            String responseString = null;

            try {
//              Setting the URL to the host server
                HttpPost httpPost = new HttpPost("http://"+ip+"/cse535/upload_video.php");
                MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
                // Add the file to be uploaded
                multipartEntityBuilder.addPart("uploaded_file", new FileBody(file));

//                Adding the Params to make file uploaded to the server
                multipartEntityBuilder.addTextBody("group_id",group_id);
                multipartEntityBuilder.addTextBody("id",asu_id);
                multipartEntityBuilder.addTextBody("accept",toogle);

                // Progress listener - updates task's progress
                MyHttpEntity.ProgressListener progressListener =
                        new MyHttpEntity.ProgressListener() {
                            @Override
                            public void transferred(float progress) {
                                publishProgress((int) progress);
                            }
                        };

                // POST
                httpPost.setEntity(new MyHttpEntity(multipartEntityBuilder.build(),
                        progressListener));


                httpResponse = httpClient.execute(httpPost);
                httpEntity = httpResponse.getEntity();

                int statusCode = httpResponse.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(httpEntity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }
            } catch (UnsupportedEncodingException | ClientProtocolException e) {
                e.printStackTrace();
                Log.e("UPLOAD", e.getMessage());
                this.exception = e;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return responseString;
        }

        @Override
        protected void onPreExecute() {
            this.progressDialog = new ProgressDialog(this.context);
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            this.progressDialog.dismiss();
            Toast.makeText(getApplicationContext(),
                    result, Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {

            this.progressDialog.setProgress((int) progress[0]);
        }
    }

}