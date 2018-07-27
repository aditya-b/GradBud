package com.user.aditya.gradbud;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

public class Main3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Login as");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        final DB_Controller db_controller=new DB_Controller(this,null,null,2);
        CircleImageView stu=(CircleImageView)findViewById(R.id.stu);
        stu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(db_controller.check_status())
                {
                    Toast.makeText(Main3Activity.this, "Login Exists!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Main3Activity.this,CheckPIN.class));
                }
                else
                    startActivity(new Intent(Main3Activity.this,Login.class));
            }
        });
        CircleImageView tea=(CircleImageView)findViewById(R.id.tea);
        tea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(db_controller.check_status())
                {
                    Toast.makeText(Main3Activity.this, "Login Exists!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Main3Activity.this,CheckPIN.class));
                }
                else
                    startActivity(new Intent(Main3Activity.this,Teachlogin.class));
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Snackbar.make(view, "More Information", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert=new AlertDialog.Builder(Main3Activity.this);
                alert.setMessage("Student:\n Login as student to view any notices,attendance and academic profile.\n\nTeacher:\n Login as teacher to mark" +
                        "attendance,issue notices or assignments.");
                alert.setTitle("Information");
                alert.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alert.show();

            }
        });
    }
}
