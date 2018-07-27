package com.user.aditya.gradbud;

import android.app.ProgressDialog;
import android.content.Intent;
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

public class ChooseStudent extends AppCompatActivity {
    LinearLayout outer[];
    TextView tv[];
    LinearLayout parent1;
    LayoutInflater inflater;
    LinearLayout.LayoutParams params1;
    ProgressDialog pd;
    Spinner spinner;
    String classes[],subs[],marks[],rollnos[],names[],targsub,targsem,targclass;
    String URL= "https://talentyaari.000webhostapp.com/Android_Retrievals_GradBud/getMarksTeach.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_student);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Choose Student");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        spinner=(Spinner)findViewById(R.id.spinner);
        parent1=(LinearLayout)findViewById(R.id.lin);
        subs=getIntent().getStringArrayExtra("subjects");
        classes=getIntent().getStringArrayExtra("classes");
        classes=getModifiedArray(classes,classes.length);
        final StringRequest request=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try
                {
                    JSONObject result=new JSONObject(response);
                    if(result.getString("error").equalsIgnoreCase("false")) {
                        JSONArray ja = result.getJSONArray("result");
                        int n = ja.length();
                        System.out.print("Length"+n);
                        if(n==0){
                            pd.dismiss();
                            Toast.makeText(ChooseStudent.this, "No students found.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            tv = new TextView[n];
                            outer = new LinearLayout[n];
                            rollnos = new String[n];
                            marks = new String[n];
                            names = new String[n];
                            for (int i = 0; i < n; i++) {
                                JSONObject jo = ja.getJSONObject(i);
                                outer[i]=new LinearLayout(ChooseStudent.this);
                                tv[i]=new TextView(ChooseStudent.this);
                                rollnos[i] = jo.getString("RollNo");
                                marks[i] = jo.getString("Marks");
                                names[i] = jo.getString("Name");
                                outer[i].setTag(i);
                                createView(rollnos[i], names[i], i);
                            }
                            pd.dismiss();
                        }
                    }
                    else {
                        pd.dismiss();
                        Toast.makeText(ChooseStudent.this, "No students found.", Toast.LENGTH_SHORT).show();
                    }
                }
                catch(Exception e)
                {
                    pd.dismiss();
                    Toast.makeText(ChooseStudent.this, "Unknown error. Please try again", Toast.LENGTH_SHORT).show();
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
                params.put("class", targclass);
                params.put("subject", targsub);
                return params;
            }
        };
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,classes);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                parent1.removeAllViews();
                if(i!=0)
                {
                    inflater=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                    params1=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                    params1.setMargins(10,10,10,10);
                    targsem=String.valueOf(classes[i].charAt(0));
                    targsub=subs[i-1];
                    targclass=classes[i];
                    pd=new ProgressDialog(ChooseStudent.this);
                    pd.setMessage("Loading students...");
                    pd.setCancelable(false);
                    pd.show();
                    Volley.newRequestQueue(ChooseStudent.this).add(request);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    void createView(String name,String rollno,int i){
        System.out.print("DETAILS::"+name+" "+rollno+"\n");
        outer[i].setLayoutParams(params1);
        outer[i].setOrientation(LinearLayout.VERTICAL);
        tv[i]=new TextView(this);
        tv[i].setLayoutParams(params1);
        tv[i].setPadding(15,15,15,15);
        tv[i].setGravity(Gravity.CENTER_VERTICAL);
        tv[i].setTextColor(Color.BLACK);
        tv[i].setTextSize(15);
        tv[i].setText(rollno+"\n\n"+name);
        outer[i].addView(tv[i]);
        outer[i].setGravity(Gravity.CENTER_VERTICAL);
        outer[i].setPadding(15,15,15,15);
        outer[i].setBackgroundResource(R.drawable.rounded);
        outer[i].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                System.out.print("TAG::"+v.getTag().toString());
                int z=(Integer)v.getTag();
                bundle.putString("marks",marks[z]);
                bundle.putString("subject",targsub);
                bundle.putString("semester",targsem);
                bundle.putString("rollno", rollnos[z]);
                Intent intent=new Intent(ChooseStudent.this,UploadMarks.class).putExtras(bundle);
                startActivity(intent);
            }
        });
        parent1.addView(outer[i]);
    }

    String[] getModifiedArray(String[] arr,int n)
    {
        String array[]=new String[n+1];
        array[0]="Select";
        for(int i=1,j=0;j<n;i++,j++)
            array[i]=arr[j];
        return array;
    }
}
