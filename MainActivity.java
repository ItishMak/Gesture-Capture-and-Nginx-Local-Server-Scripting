package com.example.mobile_computing535;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Environment;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;
import static com.example.mobile_computing535.LoginActivity.EXTRA_EMAIL;
import static com.example.mobile_computing535.LoginActivity.EXTRA_ID;
import java.io.File;


public class MainActivity extends AppCompatActivity {


    public static String EXTRA_TEXT = "com.example.mobile_computing535.EXTRA_TEXT";
    public static String EXTRA_SERVER_IP = "com.example.mobile_computing535.EXTRA_SERVER_IP";
    Spinner ges_list;
    Spinner ser_ip_add;
    Activity mainActivity;

//  This Function adds on Click functionality to 'Learn' Button
    public void play_vid(View view) {
        ges_list = (Spinner) findViewById(R.id.ges_list);
        String text = ges_list.getSelectedItem().toString();
        ser_ip_add = (Spinner) findViewById(R.id.ser_ip_add);
        String text1 = ser_ip_add.getSelectedItem().toString();
        Intent startUser = getIntent();
        String user = startUser.getStringExtra(LoginActivity.EXTRA_EMAIL);
        String id = startUser.getStringExtra(LoginActivity.EXTRA_ID);
        Intent startVideoActivity = new Intent(this, VideoActivity.class);
        startVideoActivity.putExtra(EXTRA_TEXT, text);
        startVideoActivity.putExtra(EXTRA_EMAIL, user);
        startVideoActivity.putExtra(EXTRA_ID, id);
        startVideoActivity.putExtra(EXTRA_SERVER_IP, text1);
        startActivity(startVideoActivity);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

//    Implementing Logout functionality
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                mainActivity = this;
                final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("ALERT");
                alertDialog.setMessage("Logging out will delete all the data!");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
//                                Deleting all created videos from the directory once the user is logged-out.
                                File f = new File(Environment.getExternalStorageDirectory(), "mobilecomputing535");
                                if (f.isDirectory()) {
                                    String[] children = f.list();
                                    for (int i = 0; i < children.length; i++) {
                                        new File(f, children[i]).delete();
                                    }
                                }
                                f = new File(Environment.getExternalStorageDirectory(), "mobilecomputing535_logs");
                                if (f.isDirectory()) {
                                    String[] children = f.list();
                                    for (int i = 0; i < children.length; i++) {
                                        new File(f, children[i]).delete();
                                    }
                                }
                                startActivity(new Intent(mainActivity, LoginActivity.class));
                                mainActivity.finish();

                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alertDialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }


    }

}
