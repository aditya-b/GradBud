package com.user.aditya.gradbud;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
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

public class Teachlogin extends AppCompatActivity {
    ProgressDialog pd;
    boolean status=false;
    String URL= "https://talentyaari.000webhostapp.com/Android_Retrievals_GradBud/Teach_Login.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachlogin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Login");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ((Button)findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Teachlogin.this,Report.class));
            }
        });
        final EditText em=(EditText)findViewById(R.id.em);
        final EditText pa=(EditText) findViewById(R.id.pa);
        final Bundle bundle=new Bundle();
        final StringRequest request=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                try {
                    JSONObject result=new JSONObject(response);
                    System.out.println(result.toString());
                    Bundle bundle=new Bundle();
                    status=Boolean.valueOf(result.getString("error"));
                    if(!status) {
                        bundle.putString("name",result.getString("name"));
                        bundle.putString("rollno",result.getString("id"));
                        bundle.putString("password",result.getString("password"));
                        bundle.putInt("person",2);
                        System.out.println(result.getString("name"));
                    }
                    if(!status)
                    {
                        Intent i=new Intent(Teachlogin.this,CreatePin.class).putExtras(bundle);
                        finish();
                        startActivity(i);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Invalid Credentials!",Toast.LENGTH_LONG).show();
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(Teachlogin.this, "Server error! Try again later.", Toast.LENGTH_SHORT).show();
                System.out.print(error.toString());
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", em.getText().toString());
                params.put("password", pa.getText().toString());
                return params;
            }
        };
        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectInfo connectInfo=new ConnectInfo(Teachlogin.this);
                if(connectInfo.isConnected()) {
                    String email=em.getText().toString();
                    String pass=pa.getText().toString();
                    if(email.isEmpty())
                        Toast.makeText(Teachlogin.this, "Faculty ID cannot be empty. ", Toast.LENGTH_SHORT).show();
                    else if(pass.isEmpty())
                        Toast.makeText(Teachlogin.this, "Password cannot be empty.", Toast.LENGTH_SHORT).show();
                    else {
                        pd = new ProgressDialog(Teachlogin.this);
                        pd.setTitle("Please wait");
                        pd.setMessage("Validating user...");
                        pd.setCancelable(false);
                        pd.show();
                        Volley.newRequestQueue(Teachlogin.this).add(request);
                    }
                }
                else
                    Toast.makeText(Teachlogin.this, "Please Connect to Internet!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
