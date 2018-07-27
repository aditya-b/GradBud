package com.user.aditya.gradbud;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class TeachDashboard extends AppCompatActivity {
    int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teach_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Dashboard");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Logout", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(TeachDashboard.this, "Information", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        ImageButton att=(ImageButton)findViewById(R.id.att);
        att.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TeachDashboard.this,MarkAttendance.class));
            }
        });

        ImageButton gassn=(ImageButton)findViewById(R.id.gassn);
        gassn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TeachDashboard.this,give_assignment.class));
            }
        });

        ImageButton massn=(ImageButton)findViewById(R.id.massn);
        massn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TeachDashboard.this,MarkAssignment.class));
            }
        });

        ImageButton mrks=(ImageButton)findViewById(R.id.marks);
        mrks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TeachDashboard.this,ChooseStudent.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.teachmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if(id==R.id.logout)
        {
            AlertDialog.Builder alert=new AlertDialog.Builder(TeachDashboard.this);
            alert.setTitle("Confirm your action");
            alert.setMessage("Are you sure you want to logout of this device?");
            alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    DB_Controller dc=new DB_Controller(TeachDashboard.this,null,null,2);
                    dc.delete_login();
                    startActivity(new Intent(TeachDashboard.this,Main3Activity.class));
                    finish();
                    Toast.makeText(TeachDashboard.this, "Logged out Successfully!", Toast.LENGTH_SHORT).show();
                }
            });
            alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            alert.show();
            return true;
        }
        if(id==R.id.about)
            startActivity(new Intent(TeachDashboard.this,AboutDev.class));

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        count++;
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                count=0;
            }
        },2000);
        if(count==2)
            super.onBackPressed();
    }
}
