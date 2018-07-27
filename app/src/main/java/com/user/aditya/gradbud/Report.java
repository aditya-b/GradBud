package com.user.aditya.gradbud;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
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

public class Report extends AppCompatActivity {
    ProgressDialog pd;
    String URL= "https://talentyaari.000webhostapp.com/Android_Retrievals_GradBud/report.php",subject,content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Report");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        final EditText sub=(EditText)findViewById(R.id.sub);
        final EditText cont=(EditText)findViewById(R.id.content);
        final TextView subchar=(TextView)findViewById(R.id.subchar);
        final TextView conchar=(TextView)findViewById(R.id.conchar);
        sub.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                subchar.setText(charSequence.length()+"/50");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        cont.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                conchar.setText(charSequence.length()+"/255");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        final StringRequest request=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                try
                {
                    JSONObject result=new JSONObject(response);
                    if(result.getString("error").equalsIgnoreCase("false"))
                    {
                        Toast.makeText(Report.this, "Issue recorded by id "+result.getString("id")+". We will get back to you shortly.", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                    else
                        Toast.makeText(Report.this, "Couldn't report. Please try again later.", Toast.LENGTH_SHORT).show();
                }
                catch(Exception e)
                {
                    pd.dismiss();
                    Toast.makeText(Report.this, "Unknown error. Please try again", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(Report.this, "Server error! Please try again", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String,String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("subject", subject);
                params.put("content", content);
                return params;
            }
        };
        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subject=sub.getText().toString();
                content=cont.getText().toString();
                if(subject.isEmpty())
                    Toast.makeText(Report.this, "Please enter subject.", Toast.LENGTH_SHORT).show();
                else if(content.isEmpty())
                    Toast.makeText(Report.this, "Please describe your issue.", Toast.LENGTH_SHORT).show();
                else
                {
                    pd=new ProgressDialog(Report.this);
                    pd.setMessage("Sending your concern...");
                    pd.setCancelable(false);
                    pd.show();
                    Volley.newRequestQueue(Report.this).add(request);
                }
            }
        });
    }
}
