package info.androidhive.camerafileupload;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;


public class ActionActivity extends AppCompatActivity {

    private Button btnAction1;private Button btnAction5;
    private Button btnAction2;private Button btnAction6;
    private Button btnAction3;private Button btnAction7;
    private Button btnAction4;private Button btnAction8;

    private Switch which_hand;
    private String username, password, direction;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action);

        Intent i = getIntent();
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // image or video path that is captured in previous activity
        username = i.getStringExtra("username");
        password = i.getStringExtra("password");
        direction="Left";

        initView();
        initListener();

    }

    private void initView(){
        btnAction1 = (Button) findViewById(R.id.action1);btnAction5 = (Button) findViewById(R.id.action5);
        btnAction2 = (Button) findViewById(R.id.action2);btnAction6 = (Button) findViewById(R.id.action6);
        btnAction3 = (Button) findViewById(R.id.action3);btnAction7 = (Button) findViewById(R.id.action7);
        btnAction4 = (Button) findViewById(R.id.action4);btnAction8 = (Button) findViewById(R.id.action8);
        which_hand = (Switch) findViewById(R.id.which_hand);
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

        which_hand.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    which_hand.setText("右手");
                    direction="Right";
                }else {
                    which_hand.setText("左手");
                    direction="Left";
                }
            }
        });

    }

    private void prepareRecord(int act) {
        Intent i = new Intent(ActionActivity.this, ChooseActivity.class);
        i.putExtra("act", act);
        i.putExtra("username",username);
        i.putExtra("password",password);
        i.putExtra("direction",direction);
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(i);
    }
}
