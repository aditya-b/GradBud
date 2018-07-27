package com.user.aditya.gradbud;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    LinearLayout outer[];
    TextView tv[],tv2[],tv3[],att,assn;
    LinearLayout parent1;
    LayoutInflater inflater;
    LinearLayout.LayoutParams params1,params2;
    String details[]=new String[10];
    int count=0;
    ProgressDialog pd;
    String URL= "https://talentyaari.000webhostapp.com/Android_Retrievals_GradBud/";
    String subjects[],tags[],depts[],links[],contacts[],contents[],tag="",hod,ct;
    RequestQueue queue;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("News Feed");
        setSupportActionBar(toolbar);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        queue= Volley.newRequestQueue(this);
        Bundle extras=getIntent().getExtras();
        details[0]=extras.getString("rollno");
        details[1]=extras.getString("password");
        details[2]=extras.getString("attendance");
        details[3]=extras.getString("assignments");
        details[4]=extras.getString("name");
        details[5]=extras.getString("email");
        details[6]=extras.getString("semester");
        details[7]=extras.getString("class");
        details[8]=extras.getString("subjects");
        details[9]=extras.getString("imgurl");
        hod=extras.getString("hod");
        ct=extras.getString("ct");
        System.out.println("CLASS::"+details[7]);
        pd=new ProgressDialog(MainActivity.this);
        pd.setCancelable(false);
        params2=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        final StringRequest requestnot=new StringRequest(Request.Method.POST, URL+"getSpecNotices.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try
                {
                    JSONObject result=new JSONObject(response);
                    JSONArray ja = result.getJSONArray("result");
                    int n = ja.length();
                    if (n == 0) {
                        pd.dismiss();
                        Toast.makeText(MainActivity.this, "No Notices Found!", Toast.LENGTH_SHORT).show();
                    } else {
                        subjects = new String[n];
                        tags = new String[n];
                        depts = new String[n];
                        links = new String[n];
                        contacts = new String[n];
                        contents = new String[n];
                        for (int i = 0; i < n; i++) {
                            JSONObject jo = ja.getJSONObject(i);
                            subjects[i] = jo.getString("subject");
                            tags[i] = jo.getString("tags");
                            depts[i] = jo.getString("department");
                            links[i] = jo.getString("links");
                            contacts[i] = jo.getString("contact");
                            contents[i] = jo.getString("content");
                        }
                        displayNotices(subjects, tags, depts, links, contacts, contents, n);
                        pd.dismiss();
                    }
                }
                catch(Exception e)
                {
                    pd.dismiss();
                    Toast.makeText(MainActivity.this, "Unknown error. Please try again", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(MainActivity.this, "Server error! Please try again", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String,String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("tag",tag);
                return params;
            }
        };
        final StringRequest requestassn=new StringRequest(Request.Method.POST, URL+"getAllAssignments.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try
                {
                    JSONObject result=new JSONObject(response);
                    if(result.getString("error").equalsIgnoreCase("false"))
                    {
                        details[3] = String.valueOf(result.get("result"));
                        setAssn();
                    }
                    pd.setMessage("Loading notices...");
                    queue.add(requestnot);
                }
                catch(Exception e)
                {
                    pd.dismiss();
                    Toast.makeText(MainActivity.this, "Unknown error. Please try again", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(MainActivity.this, "Server error! Please try again", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String,String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("rollno", getIntent().getStringExtra("rollno"));
                return params;
            }
        };
        final StringRequest requestatt=new StringRequest(Request.Method.POST, URL+"getAvgAttendance.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try
                {
                    JSONObject result=new JSONObject(response);
                    if(result.getString("error").equalsIgnoreCase("false"))
                    {
                        details[2] = String.valueOf(result.get("result"));
                        setAtt();
                    }
                    pd.setMessage("Loading your profile...");
                    queue.add(requestassn);
                }
                catch(Exception e)
                {
                    pd.dismiss();
                    Toast.makeText(MainActivity.this, "Unknown error. Please try again", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(MainActivity.this, "Server error! Please try again", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String,String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("rollno", details[0]);
                params.put("semester", details[6]);
                return params;
            }
        };

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.setMessage("Refreshing Notices...");
                pd.show();
                queue.add(requestnot);
            }
        });
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(MainActivity.this, "Refresh Feed", Toast.LENGTH_SHORT).show();
                return true;
            }
        });


        CircleImageView dp=(CircleImageView)navigationView.getHeaderView(0).findViewById(R.id.dp);
        Picasso.with(this).load(details[9]).placeholder(R.drawable.download).error(R.drawable.download).into(dp);
        ((TextView)navigationView.getHeaderView(0).findViewById(R.id.rno)).setText(details[0]);
        ((TextView)navigationView.getHeaderView(0).findViewById(R.id.name)).setText(details[4]);
        ((TextView)navigationView.getHeaderView(0).findViewById(R.id.email)).setText(details[5]);
        ((TextView)navigationView.getHeaderView(0).findViewById(R.id.cls)).setText(details[7]);

        final android.widget.SearchView sv=(android.widget.SearchView)findViewById(R.id.sv);
        sv.setQueryHint("Search by Tag");
        sv.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                tag=s;
                pd.setMessage("Loading notices...");
                pd.show();
                queue.add(requestnot);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                sv.setQueryHint("Search by Tag");
                if(s.isEmpty())
                {
                    tag=s;
                    queue.add(requestnot);
                }
                return true;
            }
        });
        pd.setMessage("Setting up things...");
        pd.show();
        queue.add(requestatt);
        //new getData().execute("rollno",details[0],"semester",details[6],"getAvgAttendance.php");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            AlertDialog.Builder alert=new AlertDialog.Builder(MainActivity.this);
            alert.setTitle("Confirm your action");
            alert.setMessage("Are you sure you want to logout of this device?");
            alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    DB_Controller dc=new DB_Controller(MainActivity.this,null,null,2);
                    dc.delete_login();
                    startActivity(new Intent(MainActivity.this,Main3Activity.class));
                    finish();
                    Toast.makeText(MainActivity.this, "Logged out Successfully!", Toast.LENGTH_SHORT).show();
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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_time_table) {
            startActivity(new Intent(MainActivity.this,Main2Activity.class).putExtra("class",details[7]));
        } else if (id == R.id.nav_assignments) {
            startActivity(new Intent(MainActivity.this,Assignments.class).putExtra("rollno",details[0]));
        } else if (id == R.id.nav_attendance) {
            startActivity(new Intent(MainActivity.this,Attendance.class).putExtra("rollno",details[0]));
        } else if (id == R.id.nav_marks) {
            startActivity(new Intent(MainActivity.this,Marks.class).putExtra("rollno",details[0]));
        } else if (id == R.id.nav_edit_profile) {
            startActivity(new Intent(MainActivity.this,ChangePassword.class).putExtra("rollno",details[0]));
        } else if (id == R.id.nav_report) {
            startActivity(new Intent(MainActivity.this,Report.class));
        } else if (id == R.id.nav_faq) {
            startActivity(new Intent(MainActivity.this,FAQ.class));
        } else if (id == R.id.nav_dev) {
            startActivity(new Intent(MainActivity.this,AboutDev.class));
        }
        else if (id == R.id.nav_share) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Ease up your attendance management, assignment management and scheduling easy? Here we are with the solution " +
                    ". Grad Bud is a college management app for both students and teachers to make the administration easy. Share it via: " +
                    "https://drive.google.com/open?id=1uBzdjSFEawcF-W6HkXTDQDjxdS4xI9Ui";
            String shareSub = "Grad Bud";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share using"));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
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
    void displayNotices(String[] subjects,String[] tags,String[] depts,String[] links,String[] contacts,String[] contents,int n){
        parent1=(LinearLayout)findViewById(R.id.lin);
        inflater=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        params1=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        params1.setMargins(10,10,10,10);
        parent1.removeAllViews();
        tv = new TextView[n];
        tv2 = new TextView[n];
        tv3 = new TextView[n];
        outer = new LinearLayout[n];
        for (int i = 0; i < n; i++)
            createView(subjects[i],tags[i],depts[i],links[i],contacts[i],contents[i],i);
    }
    @TargetApi(21)
    void createView(String subject,String tags,String dept,String links,String contacts,String content,int i){
        outer[i]=new LinearLayout(this);
        outer[i].setLayoutParams(params1);
        outer[i].setOrientation(LinearLayout.VERTICAL);
        tv[i]=new TextView(this);
        tv[i].setLayoutParams(params1);
        tv[i].setPadding(15,15,15,15);
        tv[i].setGravity(Gravity.CENTER_VERTICAL);
        tv[i].setTextColor(Color.BLACK);
        tv[i].setTextSize(15);
        tv[i].setText(subject);
        tv2[i]=new TextView(this);
        tv2[i].setLayoutParams(params2);
        tv2[i].setPadding(15,15,15,15);
        tv2[i].setGravity(Gravity.CENTER_VERTICAL);
        tv2[i].setTextColor(Color.WHITE);
        tv2[i].setBackgroundResource(R.drawable.tags);
        tv2[i].setPadding(15,15,15,15);
        //tv2[i].setTextSize(15);
        tv2[i].setText("Tags:");
        tv3[i]=new TextView(this);
        tv3[i].setLayoutParams(params1);
        tv3[i].setPadding(15,15,15,15);
        tv3[i].setGravity(Gravity.CENTER_VERTICAL);
        tv3[i].setTextColor(Color.BLACK);
        tv3[i].setTextSize(15);
        tags=tags.replace(","," ");
        tv3[i].setText(tags);
        outer[i].addView(tv[i]);
        outer[i].addView(tv2[i]);
        outer[i].addView(tv3[i]);
        outer[i].setGravity(Gravity.CENTER_VERTICAL);
        outer[i].setPadding(15,15,15,15);
        outer[i].setElevation(5);
        outer[i].setTranslationZ(10);
        final String sub=subject;
        final String dep=dept;
        final String lnk=links;
        final String con=contacts;
        final String cont=content;
        outer[i].setBackgroundResource(R.drawable.rounded);
        outer[i].getBackground().setAlpha(180);
        outer[i].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Notices.class);
                intent.putExtra("sub",sub);
                intent.putExtra("content",cont);
                intent.putExtra("dept",dep);
                intent.putExtra("contacts",con);
                intent.putExtra("links",lnk);
                startActivity(intent);
            }
        });
        parent1.addView(outer[i]);
    }
    void setAtt() {
        att = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_attendance));
        att.setGravity(Gravity.CENTER_VERTICAL);

        assn = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_assignments));
        assn.setGravity(Gravity.CENTER_VERTICAL);

        float attend = Float.parseFloat(details[2]);
        att.setText(details[2]);
        if (attend <= 65)
            att.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        else if (attend > 65 && attend <= 75)
            att.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
        else
            att.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
    }
    void setAssn(){
        int assgn=Integer.parseInt(details[3]);
        assn.setText(details[3]);
        if(assgn>=10)
            assn.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        else if(assgn<10&&assgn>=1)
            assn.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
        else
            assn.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
    }
}
