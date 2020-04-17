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
    private static String username, password;
    private int DATA_COUNT = 5;
    private LineChart chart;

    List<Entry> chartData = new ArrayList<>();
    List<String> chartLabels = new ArrayList<>();
    List<LineDataSet> dataSets = new ArrayList<>();

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
        initchart();

    }

    private void initView(){

        listView = (ListView) findViewById(R.id.patient_data);
        patientList = new ArrayList<>();

        Log.d("SQL server","Before showlist");
        showList();

    }
    private void initListener(){

    }

    private void initchart()
    {
        chart=(LineChart)findViewById(R.id.chart);
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

                            Patient ppp = new Patient("名字","完成度","時間");
                            patientList.add(ppp);

                            for ( int i = 0; i<array.length();i++){
                                JSONObject patientObj = array.getJSONObject(i);
                                Log.d("SQL server",patientObj.getString("Name")+" "+patientObj.getString("Complete")+" "+patientObj.getString("Time"));

                                if(patientObj.getString("Name").equals(username))
                                {
                                    Patient p = new Patient(patientObj.getString("Name"),patientObj.getString("Complete"),patientObj.getString("Time"));
                                    patientList.add(p);
                                    //draw

                                    chartData.add(new Entry(Float.valueOf(patientObj.getString("Complete")), i));
                                    chartLabels.add(patientObj.getString("Time"));


                                    //draw
                                }

                            }
                            LineDataSet dataSetA = new LineDataSet(chartData, "Genius");

                            dataSets.add(dataSetA); // add the datasets
                            //chart.getDescription().setText("Description of my chart");
                            chart.setDescription(null);
                            chart.setData(new LineData(chartLabels, dataSets));
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


    private List<Entry> getChartData(){

        List<Entry> chartData = new ArrayList<>();
        for(int i=0;i<DATA_COUNT;i++){
            chartData.add(new Entry(i*2, i));
        }
        return chartData;
    }
    private List<String> getLabels(){
        List<String> chartLabels = new ArrayList<>();
        for(int i=0;i<DATA_COUNT;i++){
            chartLabels.add("X"+i);
        }
        return chartLabels;
    }

    private LineData getLineData(){
        LineDataSet dataSetA = new LineDataSet(getChartData(), "LabelA");

        List<LineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSetA); // add the datasets

        return new LineData(getLabels(), dataSets);
    }

}
