package com.user.aditya.gradbud;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Attendance extends AppCompatActivity {
    LinearLayout outer[];
    TextView tv[];
    LinearLayout parent1,parent2;
    LayoutInflater inflater;
    LinearLayout.LayoutParams params2;
    ProgressDialog pd;
    String URL= "https://talentyaari.000webhostapp.com/Android_Retrievals_GradBud/getAttendance.php";
    String sems[]={"0","1","2","3","4","5","6","7","8"};
    String subs[],held[],attended[],type[],targetsem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Attendance");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        String days[]={"Select Semester","1-1","1-2","2-1","2-2","3-1","3-2","4-1","4-2"};
        parent1=(LinearLayout)findViewById(R.id.fn);
        parent2=(LinearLayout)findViewById(R.id.an);
        inflater=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        params2=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        final StringRequest request=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject result = new JSONObject(response);
                    if(result.getString("error").equalsIgnoreCase("false")) {
                        JSONArray ja = result.getJSONArray("result");
                        int n = ja.length();
                        subs = new String[n];
                        held = new String[n];
                        attended = new String[n];
                        type = new String[n];
                        for (int i = 0; i < n; i++) {
                            JSONObject jo = ja.getJSONObject(i);
                            subs[i] = jo.getString("SubjectName");
                            held[i] = jo.getString("held");
                            attended[i] = jo.getString("attended");
                            type[i] = jo.getString("Type");
                        }
                        ((TextView) findViewById(R.id.avg)).setText(result.getString("avg"));
                        createAttendanceList(subs, held, attended, type, n);
                    }
                    else
                        Toast.makeText(Attendance.this, "No data for the selected semester.", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
                catch(Exception e)
                {
                    pd.dismiss();
                    Toast.makeText(Attendance.this, "Unknown error. Please try again", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Server error! Please try again", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }){
          @Override
            protected Map<String,String> getParams() {
              HashMap<String, String> params = new HashMap<>();
              params.put("rollno", getIntent().getStringExtra("rollno"));
              params.put("semester", targetsem);
              return params;
          }
        };
        final Spinner spinner=(Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> ad=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,days);
        spinner.setAdapter(ad);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                parent1.removeAllViews();
                parent2.removeAllViews();
                ((TextView) findViewById(R.id.avg)).setText("");
                if(i!=0) {
                    targetsem=sems[i];
                    pd=new ProgressDialog(Attendance.this);
                    pd.setMessage("Loading attendance...");
                    pd.setCancelable(false);
                    pd.show();
                    Volley.newRequestQueue(Attendance.this).add(request);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    void createView(String sub,String held,String att,String typ,int i){
        outer[i]=new LinearLayout(this);
        outer[i].setLayoutParams(params2);
        outer[i].setOrientation(LinearLayout.HORIZONTAL);
        tv[i]=new TextView(this);
        tv[i].setLayoutParams(params2);
        tv[i].setPadding(10,10,10,10);
        tv[i].setGravity(Gravity.CENTER_VERTICAL);
        tv[i].setTextColor(Color.BLACK);
        tv[i].setTextSize(15);
        tv[i].setText(sub+"\nAttended:"+att+"\tHeld:"+held);
        outer[i].addView(tv[i]);
        outer[i].setGravity(Gravity.CENTER_VERTICAL);
        outer[i].setPadding(15,15,15,15);
        if(typ.equals("1"))
        {
            parent1.addView(outer[i]);
            inflater.inflate(R.layout.divider,parent1);
        }
        else
        {
            parent2.addView(outer[i]);
            inflater.inflate(R.layout.divider,parent2);
        }
    }
    void createAttendanceList(String[] subs,String[] held,String[] attended,String[] type,int n){
        tv = new TextView[n];
        outer = new LinearLayout[n];
        for (int j = 0; j < n; j++)
            createView(subs[j],held[j],attended[j],type[j],j);
    }
}
