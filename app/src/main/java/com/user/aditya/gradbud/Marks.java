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
import java.util.StringTokenizer;

public class Marks extends AppCompatActivity {
    LinearLayout outer[];
    TextView tv[];
    LinearLayout parent1,parent2;
    LayoutInflater inflater;
    LinearLayout.LayoutParams params2;
    ProgressDialog pd;
    String URL= "https://talentyaari.000webhostapp.com/Android_Retrievals_GradBud/getMarks.php";
    String sems[]={"0","1","2","3","4","5","6","7","8"};
    String subs[],marks[],type[],tarsem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marks);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Marks");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_keyboard_arrow_left_white_24dp);
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
        final Spinner spinner=(Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> ad=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,days);
        spinner.setAdapter(ad);
        final StringRequest request=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try
                {
                    JSONObject result=new JSONObject(response);
                    if(result.getString("error").equalsIgnoreCase("false")) {
                        JSONArray ja = result.getJSONArray("result");
                        int n = ja.length();
                        subs = new String[n];
                        marks = new String[n];
                        type = new String[n];
                        for (int i = 0; i < n; i++) {
                            JSONObject jo = ja.getJSONObject(i);
                            subs[i] = jo.getString("SubjectName");
                            marks[i] = jo.getString("marks");
                            type[i] = jo.getString("Type");
                        }
                        createMarksList(subs, marks, type, n);
                        pd.dismiss();
                    }
                    else
                    {
                        pd.dismiss();
                        Toast.makeText(Marks.this, "No data for the selected semester.", Toast.LENGTH_SHORT).show();
                    }
                }
                catch(Exception e)
                {
                    pd.dismiss();
                    Toast.makeText(Marks.this, "Unknown error. Please try again", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(Marks.this, "Server error! Please try again", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String,String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("rollno", getIntent().getStringExtra("rollno"));
                params.put("semester", tarsem);
                return params;
            }
        };
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                parent1.removeAllViews();
                parent2.removeAllViews();
                if(i!=0) {
                    tarsem=sems[i];
                    pd=new ProgressDialog(Marks.this);
                    pd.setMessage("Loading marks...");
                    pd.setCancelable(false);
                    pd.show();
                    Volley.newRequestQueue(Marks.this).add(request);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    void createView(String sub,String marks,String typ,int i){
        outer[i]=new LinearLayout(this);
        outer[i].setLayoutParams(params2);
        outer[i].setOrientation(LinearLayout.HORIZONTAL);
        tv[i]=new TextView(this);
        tv[i].setLayoutParams(params2);
        tv[i].setPadding(10,10,10,10);
        tv[i].setGravity(Gravity.CENTER_VERTICAL);
        tv[i].setTextColor(Color.BLACK);
        tv[i].setTextSize(15);
        tv[i].setText(sub+"\n\n");
        StringTokenizer st=new StringTokenizer(marks,",");
        while(st.hasMoreTokens())
        {
            tv[i].append(st.nextToken()+"\t");
        }
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
    void createMarksList(String[] subs,String[] marks,String[] type,int n){
        tv = new TextView[n];
        outer = new LinearLayout[n];
        for (int j = 0; j < n; j++)
            createView(subs[j],marks[j],type[j],j);
    }
}
