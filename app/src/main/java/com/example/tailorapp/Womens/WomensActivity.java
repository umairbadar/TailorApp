package com.example.tailorapp.Womens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.tailorapp.Mens.MenPagerAdapter;
import com.example.tailorapp.R;
import com.google.android.material.tabs.TabLayout;

public class WomensActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private WomenPagerAdapter adapter;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_womens);

        Toolbar toolbar = findViewById(R.id.toolbar_womens);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Trending Women Styles");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initializing Views
        initViews();
    }

    private void initViews(){

        mViewPager = findViewById(R.id.womens_tabPager);
        adapter = new WomenPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(adapter);

        mTabLayout = findViewById(R.id.womens_tabs);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
