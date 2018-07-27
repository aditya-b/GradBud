package com.user.aditya.gradbud;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
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

public class Assignments extends AppCompatActivity {
    LinearLayout outer[];
    TextView tv[],tv2[],tv3[],tv4[];
    LinearLayout parent1;
    LayoutInflater inflater;
    LinearLayout.LayoutParams params1,params2;
    ProgressDialog pd;
    String URL= "https://talentyaari.000webhostapp.com/Android_Retrievals_GradBud/getAssignments.php";
    String subs[],contents[],dates[],status[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignments);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Assignments");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        parent1=(LinearLayout)findViewById(R.id.lin);
        inflater=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        params2=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        params1=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        params1.setMargins(10,10,10,10);
        params2.setMargins(10,10,10,10);
        parent1.removeAllViews();
        pd=new ProgressDialog(this);
        pd.setMessage("Loading assignments...");
        pd.setCancelable(false);
        pd.show();
        final StringRequest request= new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject result=new JSONObject(response);
                    if(result.getString("error").equalsIgnoreCase("false")) {
                        JSONArray ja = result.getJSONArray("result");
                        int n = ja.length();
                        subs = new String[n];
                        contents = new String[n];
                        dates = new String[n];
                        status = new String[n];
                        for (int i = 0; i < n; i++) {
                            JSONObject jo = ja.getJSONObject(i);
                            subs[i] = jo.getString("Subject");
                            contents[i] = jo.getString("Content");
                            dates[i] = jo.getString("DueDate");
                            status[i] = jo.getString("Status");
                        }
                        createAssnList(subs,contents,dates,status,n);
                    }
                    else
                        Toast.makeText(Assignments.this, "Hurray! No Assignments", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
                catch (Exception e)
                {
                    pd.dismiss();
                    Toast.makeText(Assignments.this, "Unknown error. Please try again", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(Assignments.this, "Server error! Please try again", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                HashMap<String,String> params=new HashMap<>();
                params.put("rollno",getIntent().getStringExtra("rollno"));
                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }
    void createView(String sub,String content,String date,String stat,int i){
        outer[i]=new LinearLayout(this);
        outer[i].setLayoutParams(params1);
        outer[i].setOrientation(LinearLayout.VERTICAL);
        tv[i]=new TextView(this);
        tv[i].setLayoutParams(params1);
        tv[i].setPadding(15,15,15,15);
        tv[i].setGravity(Gravity.CENTER_VERTICAL);
        tv[i].setTextColor(Color.BLACK);
        tv[i].setTextSize(15);
        tv[i].setText(date);
        tv2[i]=new TextView(this);
        tv2[i].setLayoutParams(params2);
        tv2[i].setPadding(15,15,15,15);
        tv2[i].setGravity(Gravity.CENTER_VERTICAL);
        tv2[i].setTextColor(Color.WHITE);
        tv2[i].setBackgroundResource(R.drawable.links);
        tv2[i].setTextSize(15);
        tv2[i].setText("Date:");
        tv3[i]=new TextView(this);
        tv3[i].setLayoutParams(params1);
        tv3[i].setPadding(15,15,15,15);
        tv3[i].setGravity(Gravity.CENTER_VERTICAL);
        tv3[i].setTextColor(Color.BLACK);
        tv3[i].setTextSize(15);
        tv3[i].setText(sub+"\n\n"+content);
        tv4[i]=new TextView(this);
        tv4[i].setLayoutParams(params1);
        tv4[i].setPadding(0,0,15,15);
        if(stat.equals("1"))
            tv4[i].setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.mipmap.ic_done_all_black_24dp),null);
        else
            tv4[i].setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.mipmap.ic_done_black_24dp),null);
        outer[i].addView(tv2[i]);
        outer[i].addView(tv[i]);
        outer[i].addView(tv3[i]);
        outer[i].addView(tv4[i]);
        outer[i].setGravity(Gravity.CENTER_VERTICAL);
        outer[i].setPadding(15,15,15,15);
        outer[i].setBackgroundResource(R.drawable.rounded);
        outer[i].getBackground().setAlpha(180);
        parent1.addView(outer[i]);

    }
    void createAssnList(String[] sub,String[] content,String[] date,String[] stat,int n){
        tv = new TextView[n];
        outer = new LinearLayout[n];
        tv = new TextView[n];
        tv2 = new TextView[n];
        tv3 = new TextView[n];
        tv4 = new TextView[n];
        outer = new LinearLayout[n];
        for (int j = 0; j < n; j++)
            createView(sub[j],content[j],date[j],stat[j],j);
    }
}
