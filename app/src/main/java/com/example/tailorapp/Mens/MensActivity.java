package com.example.tailorapp.Mens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.tailorapp.R;
import com.google.android.material.tabs.TabLayout;

public class MensActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private MenPagerAdapter adapter;
    private TabLayout mTabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mens);

        Toolbar toolbar = findViewById(R.id.toolbar_mens);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Trending Men Styles");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initializing Views
        initViews();
    }

    private void initViews(){

        mViewPager = findViewById(R.id.mens_tabPager);
        adapter = new MenPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(adapter);

        mTabLayout = findViewById(R.id.mens_tabs);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
