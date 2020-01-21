package com.example.tailorapp.Womens;

import com.example.tailorapp.Mens.AllStylesMenFragment;
import com.example.tailorapp.Mens.EthnicWearMenFragment;
import com.example.tailorapp.Mens.FormalWearMenFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class WomenPagerAdapter extends FragmentPagerAdapter {

    public WomenPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch(position) {
            case 0:
                AllStylesWomenFragment allStylesWomenFragment = new AllStylesWomenFragment();
                return allStylesWomenFragment;

            case 1:
                FormalWearWomenFragment formalWearWomenFragment = new FormalWearWomenFragment();
                return  formalWearWomenFragment;

            case 2:
                WesternWearWomenFragment westernWearWomenFragment = new WesternWearWomenFragment();
                return westernWearWomenFragment;

            case 3:
                EthnicWearWomenFragment ethnicWearWomenFragment = new EthnicWearWomenFragment();
                return ethnicWearWomenFragment;

            default:
                return  null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    public CharSequence getPageTitle(int position){

        switch (position) {
            case 0:
                return "ALL STYLES";

            case 1:
                return "FORMAL";

            case 2:
                return "WESTERN";

            case 3:
                return "ETHNIC";

            default:
                return null;
        }

    }
}
