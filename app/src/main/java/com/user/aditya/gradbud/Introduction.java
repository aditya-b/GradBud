package com.user.aditya.gradbud;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class Introduction extends AppCompatActivity {
    DB_Controller db_controller;
    @Override @TargetApi(24)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_introduction);
        db_controller=new DB_Controller(this,null,null,2);
        final TextView tv=(TextView)findViewById(R.id.textView13);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tv.setText("Loading Screens");
            }
        },2000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(db_controller.check_status())
                    startActivity(new Intent(Introduction.this,CheckPIN.class));
                else
                    startActivity(new Intent(Introduction.this,StartTabs.class));
                finish();
            }
        },4000);

    }
}
