package info.androidhive.camerafileupload;



import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class ActionActivity extends AppCompatActivity {

    private Button btnAction1;private Button btnAction5;
    private Button btnAction2;private Button btnAction6;
    private Button btnAction3;private Button btnAction7;
    private Button btnAction4;private Button btnAction8;

    private String username, password;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action);

        Intent i = getIntent();
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // image or video path that is captured in previous activity
        username = i.getStringExtra("username");
        password = i.getStringExtra("password");

        initView();
        initListener();

    }

    private void initView(){
        btnAction1 = (Button) findViewById(R.id.action1);btnAction5 = (Button) findViewById(R.id.action5);
        btnAction2 = (Button) findViewById(R.id.action2);btnAction6 = (Button) findViewById(R.id.action6);
        btnAction3 = (Button) findViewById(R.id.action3);btnAction7 = (Button) findViewById(R.id.action7);
        btnAction4 = (Button) findViewById(R.id.action4);btnAction8 = (Button) findViewById(R.id.action8);
    }
    private void initListener(){

        btnAction1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // prepare record
                prepareRecord(1);
            }
        });
        btnAction2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // prepare record
                prepareRecord(2);
            }
        });
        btnAction3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // prepare record
                prepareRecord(3);
            }
        });
        btnAction4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // prepare record
                prepareRecord(4);
            }
        });
        btnAction5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // prepare record
                prepareRecord(5);
            }
        });
        btnAction6.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // prepare record
                prepareRecord(6);
            }
        });
        btnAction7.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // prepare record
                prepareRecord(7);
            }
        });
        btnAction8.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // prepare record
                prepareRecord(8);
            }
        });

    }

    private void prepareRecord(int act) {
        Intent i = new Intent(ActionActivity.this, MainActivity.class);
        i.putExtra("act", act);
        i.putExtra("username",username);
        i.putExtra("password",password);
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(i);
    }
}
