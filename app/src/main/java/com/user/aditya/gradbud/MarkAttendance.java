package com.user.aditya.gradbud;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
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

public class MarkAttendance extends AppCompatActivity {
    LinearLayout outer[];
    TextView tv[],att[];
    LinearLayout lin;
    LayoutInflater inflater;
    LinearLayout.LayoutParams params2,params1;
    SeekBar attendance[];
    EditText at;
    String names[],rollnos[],held[],attended[],maxheld,min[],classes[],subs[],target,tarclass;
    ProgressDialog pd;
    Spinner spinner;
    int last;
    RequestQueue queue;
    StringRequest requeststu,requestset;
    String URL= "https://talentyaari.000webhostapp.com/Android_Retrievals_GradBud/";
    int req=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_attendance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Mark Attendance");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        queue= Volley.newRequestQueue(this);
        pd=new ProgressDialog(MarkAttendance.this);
        pd.setCancelable(false);
        classes=getIntent().getStringArrayExtra("classes");
        subs=getIntent().getStringArrayExtra("subjects");
        classes=getModifiedArray(classes,classes.length);
        inflater=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        params2=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        params1=new LinearLayout.LayoutParams(100,100);
        lin=(LinearLayout)findViewById(R.id.lin);
        spinner=(Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> ad=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,classes);
        spinner.setAdapter(ad);
        at=(EditText)findViewById(R.id.editText2);
        final FloatingActionButton add=(FloatingActionButton) findViewById(R.id.inc);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maxheld= String.valueOf(Integer.parseInt(maxheld)+1);
                at.setText(maxheld);
                for(int i=0;i<attendance.length;i++)
                    attendance[i].setMax(Integer.parseInt(maxheld));
            }
        });
        final FloatingActionButton dec=(FloatingActionButton) findViewById(R.id.dec);
        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hc=at.getText().toString();
                if(!hc.equals(held[0]))
                {
                    maxheld=String.valueOf(Integer.parseInt(maxheld)-1);
                    at.setText(maxheld);
                }
                else
                    Toast.makeText(MarkAttendance.this, "Cannot decrease beyond "+held[0], Toast.LENGTH_SHORT).show();
                for(int i=0;i<attendance.length;i++)
                    attendance[i].setMax(Integer.parseInt(maxheld));
            }
        });

        requestset=new StringRequest(Request.Method.POST, URL+"setAttendance.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try
                {
                    pd.dismiss();
                    JSONObject result=new JSONObject(response);
                    if(result.getString("error").equalsIgnoreCase("false"))
                        Toast.makeText(MarkAttendance.this, "Attendance status updated", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(MarkAttendance.this, "Failed to update status.", Toast.LENGTH_SHORT).show();
                }
                catch(Exception e)
                {
                    pd.dismiss();
                    Toast.makeText(MarkAttendance.this, "Unknown error. Please try again", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(MarkAttendance.this, "Server error! Please try again", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String,String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                JSONArray rolls=new JSONArray();
                JSONArray stats2=new JSONArray();
                flushIntoJSON(rolls,rollnos);
                flushIntoJSON(stats2,attended);
                params.put("rollno",rolls.toString());
                params.put("held",maxheld);
                params.put("attended",stats2.toString());
                System.out.print("RollNos::"+rolls.toString());
                System.out.print("Status::"+stats2.toString());
                params.put("subject_id",target);
                params.put("semester",String.valueOf(tarclass.charAt(0)));
                return params;
            }
        };
        requeststu=new StringRequest(Request.Method.POST, URL+"getAttendanceTeach.php", new Response.Listener<String>() {
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
                            Toast.makeText(MarkAttendance.this, "No records found.", Toast.LENGTH_SHORT).show();
                        } else {
                            tv = new TextView[n];
                            att = new TextView[n];
                            attendance=new SeekBar[n];
                            outer = new LinearLayout[n];
                            rollnos = new String[n];
                            held = new String[n];
                            attended = new String[n];
                            min = new String[n];
                            names = new String[n];
                            for (int i = 0; i < n; i++) {
                                JSONObject jo = ja.getJSONObject(i);
                                rollnos[i] = jo.getString("RollNo");
                                held[i] = jo.getString("Held");
                                attendance[i]=new SeekBar(MarkAttendance.this);
                                attendance[i].setTag(i);
                                System.out.println("SEEK-"+String.valueOf(i)+"\tID::"+String.valueOf(attendance[i].getId()));
                                maxheld = held[i];
                                attended[i] = jo.getString("Attended");
                                min[i] = attended[i];
                                names[i] = jo.getString("Name");
                                createView(rollnos[i], names[i], attended[i], i);
                            }
                            at.setText(maxheld);
                            pd.dismiss();
                        }
                    }
                    else
                    {
                        pd.dismiss();
                        Toast.makeText(MarkAttendance.this, "No students found.", Toast.LENGTH_SHORT).show();
                    }
                }
                catch(Exception e)
                {
                    pd.dismiss();
                    Toast.makeText(MarkAttendance.this, "Unknown error. Please try again", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(MarkAttendance.this, "Server error! Please try again", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String,String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("class", tarclass);
                params.put("subject", target);
                return params;
            }
        };
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.setMessage("Updating attendance...");
                pd.show();
                queue.add(requestset);
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(last!=i) {
                    final int pos=i;
                    AlertDialog.Builder alert = new AlertDialog.Builder(MarkAttendance.this);
                    alert.setTitle("Are you sure?");
                    alert.setMessage("Any unsaved changes will be lost!");
                    alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (pos != 0) {
                                lin.removeAllViews();
                                last = pos;
                                target=subs[pos-1];
                                add.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_green_dark)));
                                dec.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_red_dark)));
                                add.setEnabled(true);
                                dec.setEnabled(true);
                                at.setTextColor(Color.BLACK);
                                tarclass=classes[pos];
                                pd.setMessage("Loading students...");
                                pd.show();
                                queue.add(requeststu);
                            }
                            else
                            {
                                add.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.darker_gray)));
                                dec.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.darker_gray)));
                                add.setEnabled(false);
                                dec.setEnabled(false);
                                at.setTextColor(Color.DKGRAY);
                                at.setText("Null");
                                lin.removeAllViews();
                            }
                        }
                    });
                    alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            spinner.setSelection(last);
                        }
                    });
                    alert.show();
                }
                if(i==0&&last==0)
                {
                    add.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.darker_gray)));
                    dec.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.darker_gray)));
                    add.setEnabled(false);
                    dec.setEnabled(false);
                    at.setTextColor(Color.DKGRAY);
                    at.setText("Null");
                    lin.removeAllViews();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    void createView(String roll,String n,String a,int i){
        outer[i]=new LinearLayout(this);
        outer[i].setLayoutParams(params2);
        outer[i].setOrientation(LinearLayout.VERTICAL);
        attendance[i].setProgress(Integer.parseInt(a));
        attendance[i].setMax(Integer.parseInt(held[i]));
        attendance[i].setLayoutParams(params2);
        att[i]=new TextView(this);
        att[i].setLayoutParams(params1);
        att[i].setText(a);
        att[i].setGravity(Gravity.RIGHT);
        att[i].setTextColor(Color.BLACK);
        att[i].setBackgroundResource(R.drawable.noborder);
        att[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        att[i].setPadding(8,8,8,8);
        attendance[i].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int z=(Integer)seekBar.getTag();
                System.out.print("Z="+String.valueOf(z));
                if(i>=Integer.parseInt(min[z]))
                    attended[z]=String.valueOf(i);
                else
                {
                    seekBar.setProgress(Integer.parseInt(min[z]));
                    Toast.makeText(MarkAttendance.this, "Can't be decreased beyond "+min[z], Toast.LENGTH_SHORT).show();
                }
                att[z].setText(String.valueOf(attended[z]));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        tv[i]=new TextView(this);
        tv[i].setLayoutParams(params2);
        tv[i].setPadding(10,10,10,10);
        tv[i].setGravity(Gravity.CENTER_VERTICAL);
        tv[i].setTextColor(Color.BLACK);
        tv[i].setTextSize(15);
        tv[i].setText(roll+"\n\n"+n);
        outer[i].addView(tv[i]);
        outer[i].addView(attendance[i]);
        outer[i].setGravity(Gravity.CENTER_VERTICAL);
        outer[i].setPadding(15,15,15,15);
        outer[i].addView(att[i]);
        lin.addView(outer[i]);
    }
    String[] getModifiedArray(String[] arr,int n)
    {
        String array[]=new String[n+1];
        array[0]="Select";
        for(int i=1,j=0;j<n;i++,j++)
            array[i]=arr[j];
        return array;
    }
    void flushIntoJSON(JSONArray arr, String[] arr2)
    {
        for(int i=0;i<arr2.length;i++)
            arr.put(arr2[i]);
    }
    void executeQuery()
    {

    }
}
