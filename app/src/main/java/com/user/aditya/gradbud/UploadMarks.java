package com.user.aditya.gradbud;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
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

public class UploadMarks extends AppCompatActivity {
    String marks,semester,sub,upmarks,rno;
    ProgressDialog pd;
    String URL= "https://talentyaari.000webhostapp.com/Android_Retrievals_GradBud/setMarks.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_marks);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Upload Marks");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Bundle extras=getIntent().getExtras();
        marks=extras.getString("marks");
        semester=extras.getString("semester");
        sub=extras.getString("subject");
        rno=extras.getString("rollno");
        StringTokenizer st=new StringTokenizer(marks,",");
        final EditText mid1=(EditText)findViewById(R.id.mid1);
        final EditText quiz=(EditText)findViewById(R.id.quiz);
        final EditText mid2=(EditText)findViewById(R.id.mid2);
        final EditText mid3=(EditText)findViewById(R.id.mid3);
        final EditText sem=(EditText)findViewById(R.id.sem);
        mid1.setText(st.nextToken());
        mid2.setText(st.nextToken());
        mid3.setText(st.nextToken());
        quiz.setText(st.nextToken());
        sem.setText(st.nextToken());
        pd=new ProgressDialog(UploadMarks.this);
        pd.setMessage("Uploading changes...");
        pd.setCancelable(false);
        final StringRequest request=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try
                {
                    JSONObject result=new JSONObject(response);
                    pd.dismiss();
                    if(result.getString("error").equalsIgnoreCase("false"))
                        Toast.makeText(UploadMarks.this, "Marks uploaded successfully.", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(UploadMarks.this, "Upload failed.", Toast.LENGTH_SHORT).show();
                }
                catch(Exception e)
                {
                    pd.dismiss();
                    Toast.makeText(UploadMarks.this, "Unknown error. Please try again", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(UploadMarks.this, "Server error! Please try again", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String,String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("semester",semester);
                params.put("marks",upmarks);
                params.put("subject",sub);
                params.put("rollno",rno);
                return params;
            }
        };
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upmarks=mid1.getText().toString()+","+mid2.getText().toString()+","+mid3.getText().toString()+
                        ","+quiz.getText().toString()+","+sem.getText().toString();
                pd.show();
                Volley.newRequestQueue(UploadMarks.this).add(request);
            }
        });
    }
}
