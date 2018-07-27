package com.user.aditya.gradbud;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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

public class Example extends AppCompatActivity {

    Toolbar toolbar;
    String id,name,designation;
    Fragment fragment1=new teach_home();
    Fragment fragment2=new Dashboard();
    Fragment fragment3=new ChangePassTeach();
    Fragment fragment4=new ReportIssue();
    Bundle e=new Bundle();
    String subs[],classes[];
    ProgressDialog pd;
    int req;
    String URL= "https://talentyaari.000webhostapp.com/Android_Retrievals_GradBud/getClassesAndSubjects.php";
    int count=0;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home_teach:
                    toolbar.setTitle("Home");
                    loadFragment(fragment1);
                    return true;
                case R.id.dashboard_teach:
                    toolbar.setTitle("Dashboard");
                    loadFragment(fragment2);
                    return true;
                case R.id.changepassword:
                    toolbar.setTitle("Change Password/PIN");
                    loadFragment(fragment3);
                    return true;
                case R.id.report_teach:
                    toolbar.setTitle("Report");
                    loadFragment(fragment4);
                    return true;
            }
            return false;
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        toolbar.setNavigationIcon(null);
        setSupportActionBar(toolbar);
        id=getIntent().getStringExtra("id");
        name=getIntent().getStringExtra("name");
        designation=getIntent().getStringExtra("designation");
        e.putString("id",id);
        e.putString("name",name);
        e.putString("designation",designation);
        e.putString("imgurl",getIntent().getStringExtra("imgurl"));
        pd=new ProgressDialog(Example.this);
        pd.setMessage("Setting up things...");
        pd.setCancelable(false);
        pd.show();
        final StringRequest request=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try
                {
                    JSONObject result=new JSONObject(response);
                    JSONArray ja = result.getJSONArray("result");
                    int n = ja.length();
                    if (n == 0) {
                        pd.dismiss();
                        Toast.makeText(Example.this, "No Classes Found!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        subs = new String[n];
                        classes = new String[n];
                        for (int i = 0; i < n; i++) {
                            JSONObject jo = ja.getJSONObject(i);
                            subs[i] = jo.getString("Subject_ID");
                            classes[i] = jo.getString("Class");
                        }
                        e.putStringArray("classes",classes);
                        e.putStringArray("subjects",subs);
                    }
                    fragment1.setArguments(e);
                    fragment2.setArguments(e);
                    fragment3.setArguments(e);
                    loadFragment(fragment1);
                    pd.dismiss();
                }
                catch(Exception e)
                {
                    pd.dismiss();
                    Toast.makeText(Example.this, "Unknown error occurred.This might be due to slow connection. Try again.", Toast.LENGTH_SHORT).show();
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
                params.put("id", id);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);
        BottomNavigationView navigation = (BottomNavigationView)findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        getSupportFragmentManager().popBackStack();
        transaction.replace(R.id.content, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed()
    {
        if(count==0)
        {
            count++;
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        else
            super.onBackPressed();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                count=0;
            }
        },2000);
    }

}
