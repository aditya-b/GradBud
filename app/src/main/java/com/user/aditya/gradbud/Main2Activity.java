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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class Main2Activity extends AppCompatActivity {
    LinearLayout outer[];
    TextView tv[];
    LinearLayout parent1,parent2;
    LayoutInflater inflater;
    LinearLayout.LayoutParams params2;
    ProgressDialog pd;
    String URL= "https://talentyaari.000webhostapp.com/Android_Retrievals_GradBud/getTimeTable.php";
    String targday;
    String times[]={"9:00 to 9:50","9:50 to 10:40","10:50 to 11:40","11:40 to 12:30","13:30 to 14:20","14:20 to 15:10","15:10 to 16:00","16:00 to 16:50"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Time Table");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        String days[]={"Select Day","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
        parent1=(LinearLayout)findViewById(R.id.fn);
        parent2=(LinearLayout)findViewById(R.id.an);
        inflater=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        params2=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        final StringRequest request=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try
                {
                    JSONObject result=new JSONObject(response);
                    if(result.getString("error").equalsIgnoreCase("false"))
                    {
                        String periods=result.getString("result");
                        StringTokenizer st=new StringTokenizer(periods,",");
                        int i=0;
                        while(st.hasMoreTokens())
                            createView(st.nextToken(),i++);
                        pd.dismiss();
                    }
                    else
                    {
                        pd.dismiss();
                        Toast.makeText(Main2Activity.this, "Hurray! It is a holiday.", Toast.LENGTH_SHORT).show();
                    }
                }
                catch(Exception e)
                {
                    pd.dismiss();
                    Toast.makeText(Main2Activity.this, "Unknown error. Please try again", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(Main2Activity.this, "Server error! Please try again", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String,String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("class", getIntent().getStringExtra("class"));
                params.put("day", targday);
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
                tv = new TextView[8];
                outer = new LinearLayout[8];
                if(i!=0)
                {
                    targday=String.valueOf(i);
                    pd=new ProgressDialog(Main2Activity.this);
                    pd.setMessage("Loading time table...");
                    pd.setCancelable(false);
                    pd.show();
                    Volley.newRequestQueue(Main2Activity.this).add(request);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    void createView(String name,int i){
        outer[i]=new LinearLayout(this);
        outer[i].setLayoutParams(params2);
        outer[i].setOrientation(LinearLayout.HORIZONTAL);
        tv[i]=new TextView(this);
        tv[i].setLayoutParams(params2);
        tv[i].setPadding(10,10,10,10);
        tv[i].setGravity(Gravity.CENTER_VERTICAL);
        tv[i].setTextColor(Color.BLACK);
        tv[i].setTextSize(15);
        tv[i].setText(name.isEmpty()?"-":name+"\n\n"+times[i]);
        outer[i].addView(tv[i]);
        outer[i].setGravity(Gravity.CENTER_VERTICAL);
        outer[i].setPadding(15,15,15,15);
        if(i<4)
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
}
