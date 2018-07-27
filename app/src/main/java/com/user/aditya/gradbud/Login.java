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

public class Login extends AppCompatActivity{
    ProgressDialog pd;
    boolean status=false;
    com.android.volley.RequestQueue queue;
    String URL= "https://talentyaari.000webhostapp.com/Android_Retrievals_GradBud/Stu_Login.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
                startActivity(new Intent(Login.this,Report.class));
            }
        });
        queue= Volley.newRequestQueue(this);
        final EditText em=(EditText)findViewById(R.id.em);
        final EditText pa=(EditText) findViewById(R.id.pa);
        final Bundle bundle=new Bundle();
        final StringRequest request=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                try {
                    JSONObject result=new JSONObject(response);
                    status = Boolean.valueOf(result.getString("error"));
                    System.out.print(result.toString());
                    if(!status) {
                        bundle.putString("name",result.getString("name"));
                        bundle.putString("rollno",result.getString("rollno"));
                        bundle.putString("password",result.getString("password"));
                        bundle.putInt("person",1);
                        System.out.println(result.getString("name"));
                    }
                    if(!status)
                    {
                        Intent i=new Intent(Login.this,CreatePin.class).putExtras(bundle);
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
                Toast.makeText(Login.this, "Server error! Try again later.", Toast.LENGTH_SHORT).show();
                System.out.print(error.toString());
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                Encrypt e=new Encrypt(pa.getText().toString());
                params.put("rollno", em.getText().toString());
                params.put("password", e.encrypt_pass());
                return params;
            }
        };
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.lwep);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectInfo connectInfo=new ConnectInfo(Login.this);
                if(connectInfo.isConnected()) {
                    String email=em.getText().toString();
                    String pass=pa.getText().toString();
                    if(email.isEmpty())
                        Toast.makeText(Login.this, "Roll No. cannot be empty. ", Toast.LENGTH_SHORT).show();
                    else if(pass.isEmpty())
                        Toast.makeText(Login.this, "Password cannot be empty.", Toast.LENGTH_SHORT).show();
                    else {
                        queue.add(request);
                        pd = new ProgressDialog(Login.this);
                        pd.setTitle("Please wait");
                        pd.setMessage("Validating user...");
                        pd.show();
                    }
                }
                else
                    Toast.makeText(Login.this, "Please Connect to Internet!", Toast.LENGTH_SHORT).show();
            }
        });


    }

}
