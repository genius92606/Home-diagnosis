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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class LoginActivity extends AppCompatActivity{

    private Button btnCertify;
    private EditText username;
    private EditText password;
    private String getuser,getpass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        initListener();

    }

    private void initView(){
        btnCertify = (Button) findViewById(R.id.btn_certify);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
    }
    private void initListener(){

        btnCertify.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // prepare record
                getuser=username.getText().toString();
                getpass=password.getText().toString();
                Intent i = new Intent(LoginActivity.this, ActionActivity.class);
                i.putExtra("username", getuser);
                i.putExtra("password",getpass);
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(i);
            }
        });


    }
}
