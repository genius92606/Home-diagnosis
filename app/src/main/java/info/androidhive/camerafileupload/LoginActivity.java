package info.androidhive.camerafileupload;


import android.Manifest;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import info.androidhive.camerafileupload.Adapter.PatientAdapter;
import info.androidhive.camerafileupload.Model.Patient;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class LoginActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{

    private Button btnCertify;
    private EditText username;
    private EditText password;
    private String getuser,getpass;

    private int haveName = 0;
    ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        initView();
        initListener();

        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO};
        if(EasyPermissions.hasPermissions(this,perms)){

            // Create an instance of Camera


        }else{
            EasyPermissions.requestPermissions(this, "我們需要這些權限來啟動錄影功能!",123,perms);
        }
       // listView = (ListView) findViewById(R.id.listView);


    }

    private void initView(){
        btnCertify = (Button) findViewById(R.id.btn_certify);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
    }

    private void initListener(){

        btnCertify.setOnTouchListener(new Button.OnTouchListener(){
            @Override
            public boolean onTouch(View arg0, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {  //按下的時候改變背景及顏色
                    btnCertify.setBackgroundResource(R.color.colorPrimaryDark);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {  //起來的時候恢復背景與顏色
                    getuser=username.getText().toString();
                    getpass=password.getText().toString();
                    btnCertify.setBackgroundResource(R.color.colorPrimary);
                    checkName();
                }
                return false;
            }
        });

/*
        btnCertify.setOnClickListener(new View.OnClickListener() {



            public void onClick(View v) {
                // prepare record
                getuser=username.getText().toString();
                getpass=password.getText().toString();
                checkName();


            }
        });

*/


    }

    private boolean checkName(){
        Log.d("SQL server","start checking name");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://140.116.70.173/AndroidFileUpload/login.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{

                            JSONObject obj = new JSONObject(response);
                            JSONArray array = obj.getJSONArray("users");

                            for ( int i = 0; i<array.length();i++){
                                JSONObject patientObj = array.getJSONObject(i);
                                Log.d("SQL server",patientObj.getString("Name")+" "+patientObj.getString("Gender")+" "+patientObj.getString("Age"));
                                if(patientObj.getString("Name").equals(getuser)&&patientObj.getString("Password").equals(getpass))
                                {
                                    Log.d("SQL server","Do have "+getuser);

                                    haveName=1;
                                }


                            }
                            if (haveName==0)
                                Toast.makeText(getApplicationContext(),
                                        "無此使用者或密碼錯誤", Toast.LENGTH_LONG)
                                        .show();
                            else
                            {
                                Intent a = new Intent(LoginActivity.this, ActionActivity.class);
                                a.putExtra("username", getuser);
                                a.putExtra("password", getpass);
                                a.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                startActivity(a);
                            }

                        } catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("SQL server", "Failed with error msg:\t" + error.getMessage());
                Log.d("SQL server", "Error StackTrace: \t" + error.getStackTrace());
            }
        }){

        };
        Handler.getInstance(getApplicationContext()).addToRequestQue(stringRequest);
        if (haveName==1)
            return true;
        else
            return false;
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this,perms)){
            new AppSettingsDialog.Builder(this).build().show();
        }
    }
}
