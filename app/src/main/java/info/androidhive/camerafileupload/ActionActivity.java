package info.androidhive.camerafileupload;



import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


public class ActionActivity extends AppCompatActivity {

    private ImageButton btnAction1;private ImageButton btnAction5;
    private ImageButton btnAction2;private ImageButton btnAction6;
    private ImageButton btnAction3;private ImageButton btnAction7;
    private ImageButton btnAction4;private ImageButton btnAction8;

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
        btnAction1 = (ImageButton) findViewById(R.id.action1);btnAction5 = (ImageButton) findViewById(R.id.action5);
        btnAction2 = (ImageButton) findViewById(R.id.action2);btnAction6 = (ImageButton) findViewById(R.id.action6);
        btnAction3 = (ImageButton) findViewById(R.id.action3);btnAction7 = (ImageButton) findViewById(R.id.action7);
        btnAction4 = (ImageButton) findViewById(R.id.action4);btnAction8 = (ImageButton) findViewById(R.id.action8);
        which_hand = (Switch) findViewById(R.id.which_hand);
        Glide.with(this).load(R.drawable.action1).fitCenter().into(btnAction1);
        Glide.with(this).load(R.drawable.action2).fitCenter().into(btnAction2);
        Glide.with(this).load(R.drawable.action3).fitCenter().into(btnAction3);
        Glide.with(this).load(R.drawable.action4).fitCenter().into(btnAction4);
        Glide.with(this).load(R.drawable.action5).fitCenter().into(btnAction5);
        Glide.with(this).load(R.drawable.action6).fitCenter().into(btnAction6);
        Glide.with(this).load(R.drawable.action7).fitCenter().into(btnAction7);
        Glide.with(this).load(R.drawable.action8).fitCenter().into(btnAction8);

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
