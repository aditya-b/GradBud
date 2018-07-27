package com.user.aditya.gradbud;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
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

public class give_assignment extends AppCompatActivity {
    ProgressDialog pd;
    String date,subs[],classes[],targetclass,targetsub,content;
    String URL= "https://talentyaari.000webhostapp.com/Android_Retrievals_GradBud/giveAssignments.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_assignment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Give Assignment");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        date=null;
        subs=getIntent().getStringArrayExtra("subjects");
        classes=getIntent().getStringArrayExtra("classes");
        final TextView disp=(TextView)findViewById(R.id.disp);
        final EditText cont=(EditText) findViewById(R.id.assn);
        Spinner spinner=(Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,classes);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                targetclass=classes[i];
                targetsub=subs[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        final StringRequest request=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try
                {
                    JSONObject result=new JSONObject(response);
                    if(result.getString("error").equalsIgnoreCase("false")) {
                        Toast.makeText(give_assignment.this, "Assignment uploaded successfully.", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                    else
                        Toast.makeText(give_assignment.this, "Upload error! Please try again later.", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
                catch(Exception e){
                    pd.dismiss();
                    Toast.makeText(give_assignment.this, "Unknown error. Please try again", Toast.LENGTH_SHORT).show();
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
                params.put("class", targetclass);
                params.put("subject", targetsub);
                params.put("duedate", date);
                params.put("content", content);
                return params;
            }
        };
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                content=cont.getText().toString();
                if(date==null||content.isEmpty())
                    Toast.makeText(give_assignment.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                else
                {
                    pd=new ProgressDialog(give_assignment.this);
                    pd.setMessage("Giving assignments...\nThis might take a while based on the number of students. Please do not exit...");
                    pd.setCancelable(false);
                    pd.show();
                    Volley.newRequestQueue(give_assignment.this).add(request);
                }
            }
        });
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(give_assignment.this, "Upload Assignment", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        final CalendarView cv=(CalendarView)findViewById(R.id.dates);
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                date=String.valueOf(i)+"/"+String.valueOf(i1+1)+"/"+String.valueOf(i2);
                disp.setText(date);
            }
        });
    }
}
