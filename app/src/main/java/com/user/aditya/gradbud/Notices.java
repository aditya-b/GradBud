package com.user.aditya.gradbud;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class Notices extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notices);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getStringExtra("sub"));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ((TextView)findViewById(R.id.dept)).setText(getIntent().getStringExtra("dept"));
        ((TextView)findViewById(R.id.cont)).setText(getIntent().getStringExtra("content"));
        final TextView links=((TextView)findViewById(R.id.links));
        links.setText(getIntent().getStringExtra("links"));
        links.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(Notices.this,links.getText().toString(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(links.getText().toString())));
            }
        });
        final TextView contacts=((TextView)findViewById(R.id.contacts));
        contacts.setText(getIntent().getStringExtra("contacts"));
        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(Notices.this,contacts.getText().toString(), Toast.LENGTH_SHORT).show();
                String num=contacts.getText().toString();
                if(num.matches("[0-9]{10}"))
                    startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse(num)));
            }
        });
    }
}
