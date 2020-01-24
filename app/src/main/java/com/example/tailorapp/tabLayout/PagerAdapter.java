package com.example.tailorapp.tabLayout;

import android.os.Bundle;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private int mNumOfTabs;
    private ArrayList<String> cat_ids;

    public PagerAdapter(FragmentManager fm, int NumOfTabs, ArrayList<String> cat_ids) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.cat_ids = cat_ids;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        Bundle b = new Bundle();
        b.putString("cat_id", cat_ids.get(position));
        Fragment frag = TabsFragment.newInstance();
        frag.setArguments(b);
        return frag;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    public CharSequence getPageTitle(int position){

        return super.getPageTitle(position);

    }
}
