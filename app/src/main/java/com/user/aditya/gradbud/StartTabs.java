package com.user.aditya.gradbud;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class StartTabs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start_tabs);
        final ViewPager vp=(ViewPager)findViewById(R.id.pager);
        final TabLayout tb=(TabLayout)findViewById(R.id.tl);
        final Button b=(Button)findViewById(R.id.button2);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(StartTabs.this,Main3Activity.class);
                finish();
                startActivity(i);
            }
        });
        tb.addTab(tb.newTab());
        tb.addTab(tb.newTab());
        tb.addTab(tb.newTab());
        tb.addTab(tb.newTab());
        final FirstScreen pa=new FirstScreen(getSupportFragmentManager(),tb.getTabCount());
        vp.setAdapter(pa);
        vp.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tb));
        tb.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int t=tab.getPosition();
                vp.setCurrentItem(t);
                if(t==3)
                    b.setText("Continue");
                else
                    b.setText("Skip");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    class FirstScreen extends FragmentStatePagerAdapter {
        int num;
        public FirstScreen(FragmentManager fm, int nu) {
            super(fm);
            this.num=nu;
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0: screen1 t=new screen1();
                    return t;
                case 1: screen2 t2=new screen2();
                    return t2;
                case 2: screen3 t3=new screen3();
                    return t3;
                case 3: screen4 t4=new screen4();
                    return t4;
                default:return null;
            }
        }

        @Override
        public int getCount() {
            return num;
        }
    }
}
