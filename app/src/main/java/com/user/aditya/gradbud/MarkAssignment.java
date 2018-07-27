package com.user.aditya.gradbud;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MarkAssignment extends AppCompatActivity {
    LinearLayout outer[];
    TextView tv[],tv2[];
    LinearLayout parent1;
    LayoutInflater inflater;
    LinearLayout.LayoutParams params1,params2;
    Switch sub[];
    String names[],rollnos[],classes[],subs[],assnids[],targetassn,status[],targclass,targsub;
    ProgressDialog pd;
    Spinner assn;
    RequestQueue queue;
    StringRequest requeststu,requestassn,requestset;
    String URL= "https://talentyaari.000webhostapp.com/Android_Retrievals_GradBud/";
    int req=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_assignment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Mark Assignments");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        queue= Volley.newRequestQueue(this);
        pd=new ProgressDialog(MarkAssignment.this);
        pd.setCancelable(false);
        classes=getIntent().getStringArrayExtra("classes");
        subs=getIntent().getStringArrayExtra("subjects");
        classes=getModifiedArray(classes,classes.length);
        final Spinner clas=(Spinner)findViewById(R.id.classes);
        assn=(Spinner)findViewById(R.id.assns);
        ArrayAdapter<String> ad=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,classes);
        clas.setAdapter(ad);
        assn.setEnabled(false);
        parent1=(LinearLayout)findViewById(R.id.lin);
        Snackbar.make(parent1,"Please save your changes before navigating between assignments or classes!",Snackbar.LENGTH_LONG).show();

        requestset=new StringRequest(Request.Method.POST, URL+"setAssignments.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try
                {
                    JSONObject result=new JSONObject(response);
                    if(result.getString("error").equalsIgnoreCase("false"))
                        Toast.makeText(MarkAssignment.this, "Assignment status updated", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(MarkAssignment.this, "Failed to update status.", Toast.LENGTH_SHORT).show();
                }
                catch(Exception e)
                {
                    pd.dismiss();
                    Toast.makeText(MarkAssignment.this, "Unknown error. Please try again", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(MarkAssignment.this, "Server error! Please try again", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String,String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                JSONArray rolls=new JSONArray();
                JSONArray stats=new JSONArray();
                flushIntoJSON(rolls,rollnos);
                flushIntoJSON(stats,status);
                params.put("rollno",rolls.toString());
                params.put("status",stats.toString());
                System.out.print("RollNos::"+rolls.toString());
                System.out.print("Status::"+stats.toString());
                params.put("assn_id",targetassn);
                return params;
            }
        };
        requeststu=new StringRequest(Request.Method.POST, URL+"getAssignmentStatus.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try
                {
                    JSONObject result=new JSONObject(response);
                    if(result.getString("error").equalsIgnoreCase("false")) {
                        JSONArray ja = result.getJSONArray("result");
                        int n = ja.length();
                        if (n == 0) {
                            pd.dismiss();
                            Toast.makeText(MarkAssignment.this, "No records found.", Toast.LENGTH_SHORT).show();
                        } else {
                            tv = new TextView[n];
                            tv2 = new TextView[n];
                            sub = new Switch[n];
                            outer = new LinearLayout[n];
                            rollnos = new String[n];
                            status = new String[n];
                            names = new String[n];
                            for (int i = 0; i < n; i++) {
                                JSONObject jo = ja.getJSONObject(i);
                                rollnos[i] = jo.getString("RollNo");
                                status[i] = jo.getString("Status");
                                names[i] = jo.getString("Name");
                                createView(rollnos[i], status[i], names[i], i);
                            }
                            pd.dismiss();
                        }
                    }
                    else
                    {
                        pd.dismiss();
                        Toast.makeText(MarkAssignment.this, "No students found.", Toast.LENGTH_SHORT).show();
                    }
                }
                catch(Exception e)
                {
                    pd.dismiss();
                    Toast.makeText(MarkAssignment.this, "Unknown error. Please try again", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(MarkAssignment.this, "Server error! Please try again", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String,String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("id", targetassn);
                return params;
            }
        };


        requestassn=new StringRequest(Request.Method.POST, URL+"getAssignmentsForDisplay.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try
                {
                    JSONObject result=new JSONObject(response);
                    if(result.getString("error").equalsIgnoreCase("false")) {
                        JSONArray ja = result.getJSONArray("result");
                        int n = ja.length();
                        if (n == 0) {
                            pd.dismiss();
                            Toast.makeText(MarkAssignment.this, "No records found.", Toast.LENGTH_SHORT).show();
                        } else {
                            assnids = new String[n];
                            for (int i = 0; i < n; i++) {
                                JSONObject jo = ja.getJSONObject(i);
                                assnids[i] = jo.getString("ID");
                            }
                            assnids = getModifiedArray(assnids, n);
                            configureAssignments();
                            pd.dismiss();
                        }
                    }
                    else
                    {
                        pd.dismiss();
                        Toast.makeText(MarkAssignment.this, "No assignments found", Toast.LENGTH_SHORT).show();
                    }
                }
                catch(Exception e)
                {
                    pd.dismiss();
                    Toast.makeText(MarkAssignment.this, "Unknown error. Please try again", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(MarkAssignment.this, "Server error! Please try again", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String,String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("class", targclass);
                params.put("subject", targsub);
                return params;
            }
        };


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.setMessage("Updating assignment status...");
                pd.show();
                queue.add(requestset);
            }
        });
        clas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                assn.setEnabled(false);
                parent1.removeAllViews();
                if(i!=0)
                {
                    assn.setEnabled(false);
                    parent1.removeAllViews();
                    targsub=subs[i-1];
                    targclass=classes[i];
                    pd.setMessage("Loading assignments...");
                    pd.show();
                    queue.add(requestassn);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    void createView(String rollno,String stats,String name,int i){
        outer[i]=new LinearLayout(this);
        outer[i].setLayoutParams(params1);
        outer[i].setOrientation(LinearLayout.VERTICAL);
        tv[i]=new TextView(this);
        tv[i].setLayoutParams(params1);
        tv[i].setPadding(15,15,15,15);
        tv[i].setGravity(Gravity.CENTER_VERTICAL);
        tv[i].setTextColor(Color.BLACK);
        tv[i].setTextSize(15);
        tv[i].setText(rollno);
        tv2[i]=new TextView(this);
        tv2[i].setLayoutParams(params2);
        tv2[i].setPadding(15,15,15,15);
        tv2[i].setGravity(Gravity.CENTER_VERTICAL);
        tv2[i].setTextColor(Color.BLACK);
        tv2[i].setPadding(15,15,15,15);
        tv2[i].setTextSize(15);
        tv2[i].setText(name);
        boolean stat=stats.equals("1")?true:false;
        sub[i]=new Switch(this);
        sub[i].setText(stat?"Submitted":"Pending");
        sub[i].setChecked(stat);
        sub[i].setLayoutParams(params1);
        sub[i].setPadding(15,15,15,15);
        sub[i].setGravity(Gravity.RIGHT);
        final int x=i;
        sub[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sub[x].setText(b?"Submitted":"Pending");
                status[x]=b?String.valueOf(1):String.valueOf(0);
            }
        });
        outer[i].addView(tv[i]);
        outer[i].addView(tv2[i]);
        outer[i].addView(sub[i]);
        outer[i].setGravity(Gravity.CENTER_VERTICAL);
        outer[i].setPadding(15,15,15,15);
        outer[i].setBackgroundResource(R.drawable.rounded);
        parent1.addView(outer[i]);

    }
    void configureAssignments()
    {
        ArrayAdapter<String> ada=new ArrayAdapter<String>(MarkAssignment.this,android.R.layout.simple_spinner_dropdown_item,assnids);
        assn.setAdapter(ada);
        assn.setEnabled(true);
        assn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                parent1.removeAllViews();
                if(i!=0) {
                    params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                    params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params1.setMargins(10, 10, 10, 10);
                    params2.setMargins(10, 10, 10, 10);
                    req = 2;
                    targetassn = assnids[i];
                    pd.setMessage("Loading students...");
                    pd.show();
                    queue.add(requeststu);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    String[] getModifiedArray(String[] arr,int n)
    {
        String array[]=new String[n+1];
        array[0]="Select";
        for(int i=1,j=0;j<n;i++,j++)
            array[i]=arr[j];
        return array;
    }
    void flushIntoJSON(JSONArray arr,String[] arr2)
    {
        for(int i=0;i<arr2.length;i++)
            arr.put(arr2[i]);
    }
}
