package com.user.aditya.gradbud;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

public class ChangePassword extends AppCompatActivity {
    ProgressDialog pd;
    String URL= "https://talentyaari.000webhostapp.com/Android_Retrievals_GradBud/changePass.php",p1,p2;
    DB_Controller db_controller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Change Password/PIN");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        final EditText op=(EditText)findViewById(R.id.op);
        final EditText np=(EditText)findViewById(R.id.np);
        final EditText cp=(EditText)findViewById(R.id.cp);
        final EditText opin=(EditText)findViewById(R.id.opin);
        final EditText npin=(EditText)findViewById(R.id.npin);
        final EditText cpin=(EditText)findViewById(R.id.cpin);
        op.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()!=0) {
                    opin.setEnabled(false);
                    npin.setEnabled(false);
                    cpin.setEnabled(false);
                }
                else
                {
                    opin.setEnabled(true);
                    npin.setEnabled(true);
                    cpin.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        np.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()!=0) {
                    opin.setEnabled(false);
                    npin.setEnabled(false);
                    cpin.setEnabled(false);
                }
                else
                {
                    opin.setEnabled(true);
                    npin.setEnabled(true);
                    cpin.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        cp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()!=0) {
                    opin.setEnabled(false);
                    npin.setEnabled(false);
                    cpin.setEnabled(false);
                }
                else
                {
                    opin.setEnabled(true);
                    npin.setEnabled(true);
                    cpin.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        opin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(opin.getText().length()!=0) {
                    op.setEnabled(false);
                    np.setEnabled(false);
                    cp.setEnabled(false);
                }
                else
                {
                    op.setEnabled(true);
                    np.setEnabled(true);
                    cp.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(opin.getText().length()==4){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
            }
        });
        npin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()!=0) {
                    op.setEnabled(false);
                    np.setEnabled(false);
                    cp.setEnabled(false);
                }
                else
                {
                    op.setEnabled(true);
                    np.setEnabled(true);
                    cp.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(npin.getText().length()==4){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
            }
        });
        cpin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()!=0) {
                    op.setEnabled(false);
                    np.setEnabled(false);
                    cp.setEnabled(false);
                }
                else
                {
                    op.setEnabled(true);
                    np.setEnabled(true);
                    cp.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(cpin.getText().length()==4){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
            }
        });
        final StringRequest request=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject result=new JSONObject(response);
                    pd.dismiss();
                    if (result.getString("error").equalsIgnoreCase("false")) {
                        Toast.makeText(ChangePassword.this, "Password Successfully Updated", Toast.LENGTH_SHORT).show();
                        db_controller.update_password(p2);
                    }
                    else
                        Toast.makeText(ChangePassword.this, "Password Updation Failed", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    pd.dismiss();
                    Toast.makeText(ChangePassword.this, "Unknown error. Please try again", Toast.LENGTH_SHORT).show();
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
              HashMap<String,String> params=new HashMap<>();
              params.put("rollno",getIntent().getStringExtra("rollno"));
              params.put("password",p1);
              params.put("npassword",p2);
              return params;
          }
        };
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db_controller=new DB_Controller(ChangePassword.this,"",null,2);
                if(cp.isEnabled())
                {
                    p1=op.getText().toString();
                    p2=np.getText().toString();
                    String p3=cp.getText().toString();
                    if(db_controller.check_password(p1)){
                        if(p2.equals(p3)&&p2.length()>=6)
                        {
                            if(test_validpassword(p2)) {
                                pd = new ProgressDialog(ChangePassword.this);
                                pd.setMessage("Updating password...");
                                pd.setCancelable(false);
                                pd.show();
                                Volley.newRequestQueue(ChangePassword.this).add(request);
                                db_controller.update_password(p2);
                            }
                            else
                                Toast.makeText(ChangePassword.this, "Password can only be alphanumeric.", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(ChangePassword.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(ChangePassword.this, "Wrong Password Entered", Toast.LENGTH_SHORT).show();
                }
                if(cpin.isEnabled())
                {
                    String pin1=opin.getText().toString();
                    String pin2=npin.getText().toString();
                    String pin3=cpin.getText().toString();
                    if(db_controller.check_PIN(pin1,1))
                    {
                        if(pin2.equals(pin3)&&pin2.length()==4) {
                            db_controller.update_pin(pin2);
                            Toast.makeText(ChangePassword.this, "PIN updated successfully", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                        else
                            Toast.makeText(ChangePassword.this, "PINs do not match", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(ChangePassword.this, "Wrong PIN entered", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    boolean test_validpassword(String pass)
    {
        String testexp="/^a-zA-Z0-9";
        if(pass.matches(testexp))
            return true;
        return false;
    }
}
