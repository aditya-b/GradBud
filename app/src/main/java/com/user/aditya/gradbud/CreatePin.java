package com.user.aditya.gradbud;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CreatePin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Create PIN");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        final EditText pin1=(EditText)findViewById(R.id.pin1);
        final EditText pin2=(EditText)findViewById(R.id.pin2);
        final DB_Controller db_controller=new DB_Controller(CreatePin.this,null,null,2);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String p1=pin1.getText().toString();
                String p2=pin2.getText().toString();
                if(p1.equals(p2)&&p1.length()==4)
                {
                    int s=getIntent().getExtras().getInt("person");
                    String name=getIntent().getExtras().getString("name");
                    String password=getIntent().getExtras().getString("password");
                    String rollno=getIntent().getExtras().getString("rollno");
                    db_controller.enter_login(p1,name,s,rollno,password);
                    System.out.println(name+" "+password+" "+s+" "+rollno);
                    startActivity(new Intent(CreatePin.this,CheckPIN.class));
                    finish();
                }
                else
                    Toast.makeText(CreatePin.this,"PINs donot match.",Toast.LENGTH_SHORT).show();
            }
        });



    }

}
