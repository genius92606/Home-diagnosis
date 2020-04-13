package info.androidhive.camerafileupload;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.camerafileupload.Adapter.PatientAdapter;
import info.androidhive.camerafileupload.Model.Patient;

public class ResultActivity  extends AppCompatActivity {

    private ListView listView;
    List<Patient> patientList;

    private static int act;
    private static String username, password;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);


        Intent i = getIntent();
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        act = i.getIntExtra("act",0);
        // image or video path that is captured in previous activity
        username = i.getStringExtra("username");
        password = i.getStringExtra("password");


        initView();
        initListener();

    }

    private void initView(){

        listView = (ListView) findViewById(R.id.patient_data);
        patientList = new ArrayList<>();

        Log.d("SQL server","Before showlist");
        showList();

    }
    private void initListener(){

    }


    private void showList(){
        Log.d("SQL server","start show list");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://140.116.67.148/AndroidFileUpload/sql.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONObject obj = new JSONObject(response);
                            JSONArray array = obj.getJSONArray("patient");
                            Log.d("SQL server",String.valueOf(array.length()));
                            for ( int i = 0; i<array.length();i++){
                                JSONObject patientObj = array.getJSONObject(i);

                                if(patientObj.getString("Name").equals("Genius"))
                                {
                                    Patient p = new Patient(patientObj.getString("Name"),patientObj.getString("Score"),patientObj.getString("Time"));
                                    patientList.add(p);
                                }

                            }
                            PatientAdapter adapter = new PatientAdapter(patientList,getApplication());
                            listView.setAdapter(adapter);
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
    }


}
