package info.androidhive.camerafileupload;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class ChooseActivity extends AppCompatActivity {

    private Button btnRecord;
    private Button btnResult;
    private static int act;
    private static String username, password, direction;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_or_result);

        Intent i = getIntent();
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // image or video path that is captured in previous activity
        act = i.getIntExtra("act",0);
        username = i.getStringExtra("username");
        password = i.getStringExtra("password");
        direction= i.getStringExtra("direction");

        initView();
        initListener();

    }

    private void initView(){
        btnRecord = (Button) findViewById(R.id.go_record);btnResult = (Button) findViewById(R.id.go_result);
    }
    private void initListener(){


        btnRecord.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // prepare record
                prepareRecord(act);
            }
        });
        btnResult.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // prepare record
                prepareResult(act);
            }
        });


    }

    private void prepareRecord(int act) {
        Intent i = new Intent(ChooseActivity.this, MainActivity.class);
        i.putExtra("act", act);
        i.putExtra("username",username);
        i.putExtra("password",password);
        i.putExtra("direction",direction);
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(i);
    }
    private void prepareResult(int act) {
        Intent i = new Intent(ChooseActivity.this, ResultActivity.class);
        i.putExtra("act", act);
        i.putExtra("username",username);
        i.putExtra("password",password);
        i.putExtra("direction",direction);
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(i);
    }
}

