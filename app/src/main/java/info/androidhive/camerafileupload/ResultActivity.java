package info.androidhive.camerafileupload;

import android.content.Intent;
import android.graphics.Color;
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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import info.androidhive.camerafileupload.Adapter.PatientAdapter;
import info.androidhive.camerafileupload.Model.Patient;

public class ResultActivity  extends AppCompatActivity {

    private ListView listView;
    List<Patient> patientList;

    private static int act;
    private static String username, password, direction;
    private int DATA_COUNT = 0;
    private LineChart complete_chart,fluency_chart;
    private TextView title;

    List<Entry> complete_chartData = new ArrayList<>();
    List<String> complete_chartLabels = new ArrayList<>();
    List<LineDataSet> complete_dataSets = new ArrayList<>();
    List<Entry> fluency_chartData = new ArrayList<>();
    List<String> fluency_chartLabels = new ArrayList<>();
    List<LineDataSet> fluency_dataSets = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);



        Intent i = getIntent();
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        act = i.getIntExtra("act",0);
        // image or video path that is captured in previous activity
        username = i.getStringExtra("username");
        password = i.getStringExtra("password");
        direction = i.getStringExtra("direction");


        initView();
        initListener();
        initchart();

    }

    private void initView(){

        listView = (ListView) findViewById(R.id.patient_data);
        title = (TextView)findViewById(R.id.resultTitle);
        patientList = new ArrayList<>();

        Log.d("SQL server","Before showlist");
        showList();

    }
    private void initListener(){

        if (direction.equals("Left"))
            title.setText("動作"+act+"  左手");
        else
            title.setText("動作"+act+"  右手");
    }

    private void initchart()
    {
        complete_chart=(LineChart)findViewById(R.id.complete_chart);
        fluency_chart=(LineChart)findViewById(R.id.fluency_chart);
        // enable touch gestures

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

                            Patient title = new Patient("名字","完成度","順暢度","時間");
                            patientList.add(title);

                            for ( int i = 0; i<array.length();i++){
                                JSONObject patientObj = array.getJSONObject(i);


                                if(patientObj.getString("Name").equals(username) && patientObj.getString("Direction").equals(direction) && patientObj.getInt("Action")==act)
                                {
                                    Log.d("SQL server",patientObj.getString("Name")+" "+patientObj.getString("Complete")+" "+patientObj.getString("Time"));
                                    Log.d("SQL server",patientObj.getString("Direction")+", "+patientObj.getString("Action"));
                                    Patient p = new Patient(patientObj.getString("Name"),patientObj.getString("Complete"),patientObj.getString("Fluency"),patientObj.getString("Time"));
                                    patientList.add(p);
                                    //draw

                                    complete_chartData.add(new Entry(Float.valueOf(patientObj.getString("Complete")), DATA_COUNT));
                                    complete_chartLabels.add(patientObj.getString("Time"));
                                    fluency_chartData.add(new Entry(Float.valueOf(patientObj.getString("Fluency")), DATA_COUNT));
                                    fluency_chartLabels.add(patientObj.getString("Time"));
                                    DATA_COUNT++;


                                    //draw
                                }

                            }
                            LineDataSet dataSetA = new LineDataSet(complete_chartData, "完成度折線圖");
                            complete_dataSets.add(dataSetA); // add the datasets
                            complete_chart.setDescription(null);
                            complete_chart.setData(new LineData(complete_chartLabels, complete_dataSets));
                            LineDataSet dataSetB = new LineDataSet(fluency_chartData, "流暢度折現圖");
                            fluency_dataSets.add(dataSetB); // add the datasets
                            fluency_chart.setDescription(null);
                            fluency_chart.setData(new LineData(fluency_chartLabels, fluency_dataSets));


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
