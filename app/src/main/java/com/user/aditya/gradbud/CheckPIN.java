package com.user.aditya.gradbud;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

public class CheckPIN extends AppCompatActivity {
    ProgressDialog pd;
    boolean status=false;
    String stuURL= "https://talentyaari.000webhostapp.com/Android_Retrievals_GradBud/Stu_Login.php";
    String teaURL="https://talentyaari.000webhostapp.com/Android_Retrievals_GradBud/Teach_Login.php";
    String details[],tag,URL;
    int person;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_pin);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Authorize User");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        final DB_Controller db_controller=new DB_Controller(CheckPIN.this,null,null,2);
        final EditText pin=((EditText) findViewById(R.id.pin1));
        pin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()==4)
                {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        details=db_controller.selectall();
        Bundle extras=new Bundle();
        extras.putString("id",details[3]);
        extras.putString("password",details[4]);

        ((Button)findViewById(R.id.lgn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectInfo connectInfo=new ConnectInfo(CheckPIN.this);
                if(connectInfo.isConnected()) {
                    status=false;
                    pd=new ProgressDialog(CheckPIN.this);
                    pd.setMessage("Validating credentials...");
                    pd.setCancelable(false);
                    String str = pin.getText().toString();
                    if (db_controller.check_PIN(str, 1)) {
                        URL=stuURL;
                        System.out.println(details[3]+" "+details[4]);
                        tag="rollno";
                        person=1;
                        pd.show();
                        getLogin();
                    } else if (db_controller.check_PIN(str, 2)) {
                        System.out.println(details[3]+" "+details[4]);
                        URL=teaURL;
                        tag="id";
                        person=2;
                        pd.show();
                        getLogin();
                    } else
                        Toast.makeText(CheckPIN.this, "Invalid PIN", Toast.LENGTH_SHORT).show();

                }
                else
                    Toast.makeText(CheckPIN.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
            }
        });
        ((Button)findViewById(R.id.rusr)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert=new AlertDialog.Builder(CheckPIN.this);
                alert.setTitle("Confirm your action");
                alert.setMessage("Are you sure you want to reset user?");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        db_controller.delete_login();
                        Toast.makeText(CheckPIN.this, "Logged out successfully!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(CheckPIN.this,Main3Activity.class));
                        finish();
                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alert.show();
            }
        });
    }
    void getLogin()
    {
        final StringRequest request=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try
                {
                    JSONObject result=new JSONObject(response);
                    System.out.print(response);
                    pd.dismiss();
                    Bundle bundle=new Bundle();
                    status=Boolean.valueOf(result.getString("error"));
                    if(!status) {
                        if(person==1) {
                            bundle.putString("name", result.getString("name"));
                            bundle.putString("rollno", result.getString("rollno"));
                            bundle.putString("password", result.getString("password"));
                            bundle.putString("attendance",result.getString("attendance"));
                            bundle.putString("assignments",result.getString("assignments"));
                            bundle.putString("email",result.getString("email"));
                            bundle.putString("semester",result.getString("semester"));
                            bundle.putString("class",result.getString("class"));
                            bundle.putString("subjects",result.getString("subjects"));
                            bundle.putString("imgurl",result.getString("imgurl"));
                            //System.out.println(result.getString("name"));
                        }
                        else
                        {
                            bundle.putString("name", result.getString("name"));
                            bundle.putString("id", result.getString("id"));
                            bundle.putString("password", result.getString("password"));
                            bundle.putString("designation",result.getString("designation"));
                            bundle.putString("imgurl",result.getString("imgurl"));
                        }
                    }
                    if(!status)
                    {
                        Intent i;
                        if(person==1)
                            i=new Intent(CheckPIN.this,MainActivity.class);
                        else
                            i=new Intent(CheckPIN.this,Example.class);
                        i.putExtras(bundle);
                        finish();
                        startActivity(i);
                    }
                    else
                    {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(),"Invalid Credentials!",Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e)
                {
                    pd.dismiss();
                    Toast.makeText(CheckPIN.this, "Unknown error. Please try again", Toast.LENGTH_SHORT).show();
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
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put(tag, details[3]);
                //Encrypt e=new Encrypt(details[4]);
                params.put("password",details[4]);
                return params;
            }
        };
        Volley.newRequestQueue(CheckPIN.this).add(request);
    }
}
